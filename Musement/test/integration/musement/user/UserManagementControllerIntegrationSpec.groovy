package musement.user

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.spock.IntegrationSpec
import musement.BootStrapService
import musement.Notification
import musement.NotificationService

/**
 *  Integrations tests for UserManagementController.
 *  Even though all the Unit test passed and cover the class 100%,
 *  it should also be tested with real objects
 */
class UserManagementControllerIntegrationSpec extends IntegrationSpec {

    /** Services **/
    SpringSecurityService springSecurityService
    BootStrapService bootStrapService
    UserAccountService userAccountService
    NotificationService notificationService

    /** The controller **/
    UserManagementController controller

    /** Current user **/
    User currentUser

    def setup() {
        bootStrapService.initializeRoles()
        bootStrapService.initializeDefaultCategory()

        controller = new UserManagementController()

        currentUser = new User(username: "current", email: "current@user.com", password: "currentPassword", notification: new Notification())
        userAccountService.addUser(currentUser, Roles.ROLE_USER.role)
    }

    def "test doRegister - empty params"() {
        given: "an user exactly how he would be created by the controller, and the result of the service"
            User user = new User()
            user = userAccountService.addUser(user, Roles.ROLE_USER.role, true)

        when: "register form was submitted"
            controller.doRegister()

        then: "the following view should be rendered with the proper model"
            controller.getModelAndView().viewName == '/userManagement/register'
            controller.getModelAndView().model.user != null
    }

    def "test doRegister - different passwords"() {
        given: "the params"
            controller.params.username = "test"
            controller.params.email = "test@musement.com"
            controller.params.password = "test"
            controller.params.password2 = "test2"

        when: "register form was submitted"
            controller.doRegister()

        then: "show the form with corresponding errors and valid params"
            controller.getModelAndView().viewName == '/userManagement/register'
            controller.getModelAndView().model.user != null
    }

    def "test doRegister - service error"() {
        given: "an invalid field"
            controller.params.username = "test"
            controller.params.email = "test@@@@"
            controller.params.password = "test"
            controller.params.password2 = "test"

        when: "register form was submitted"
            controller.doRegister()

        then: "show the form with corresponding errors and valid params"
            controller.getModelAndView().viewName == '/userManagement/register'
            controller.getModelAndView().model.user != null
            controller.getModelAndView().model.user.errors != null
            !User.findByUsername("test")
    }

    def "test doRegister - success"() {
            given: "a valid user and params; roles and musement category"
            controller.params.username = "test"
            controller.params.email = "test@musement.com"
            controller.params.password = "test"
            controller.params.password2 = "test"

        when: "register form was submitted"
            controller.doRegister()

        then: "login page should be loaded and a info message created"
            controller.response.redirectedUrl == '/login/auth'
            controller.flash.info != null
            User.findByUsername("test")
    }

}
