package musement.user

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.authentication.encoding.BCryptPasswordEncoder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import musement.Notification
import musement.NotificationService
import musement.Category
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@Mock([Role, User, UserRole, Category, Notification])
@TestFor(UserManagementController)
class UserManagementControllerSpec extends Specification {

    /** Services **/
    SpringSecurityService springSecurityService
    UserAccountService userAccountService
    NotificationService notificationService

    User user
    Category musementCategory
    BCryptPasswordEncoder passwordEncoder

    def setup() {
        user = new User(username: "test", email: "test@musemnt.com", password: "\$2a\$10\$FDzV4Sw09Ua/z5dMP/FBWOyXFgo1pwTFG.KvIehsYycaL.ixUPnsi", notification: Mock(Notification))
        musementCategory = new Category(name: "Musement", description: "test")
        passwordEncoder = new BCryptPasswordEncoder(10)

        // Account Service
        userAccountService = Mock(UserAccountService)
        controller.userAccountService = userAccountService
        
        // Notification Service
        notificationService = Mock(NotificationService)
        controller.notificationService = notificationService
        
        // SpringSecurityService
        springSecurityService = Mock(SpringSecurityService)
        springSecurityService.currentUser >> user
        springSecurityService.passwordEncoder >> passwordEncoder
        controller.springSecurityService = springSecurityService
    }
    
    void "test register page"() {
        when: "the register page is called"
            controller.register()

        then: "show register form/view"
            view == "/userManagement/register"
            model.user != null
    }

    void "test doRegister - empty params"() {
        given: "an user exactly how he would be created by the controller, and the result of the service"
            Notification notification = new Notification()
            params.notification = notification
            User replica = new User(params)
            User u = userAccountService.addUser(replica, null, true)

        when: "register form was submitted"
            controller.doRegister()

        then: "the following view should be rendered with the proper model"
            view == '/userManagement/register'
            model.user == u
    }

    void "test doRegister - different passwords"() {
        given: "mock params"
            Notification notification = new Notification()
            params.notification = notification
            params.username = "test"
            params.email    = "test@musement.com"
            params.password = "password1"
            params.password2 = "password2"

        when: "register form was submitted"
            controller.doRegister()

        then: "service should not be called"
            0 * userAccountService.addUser((User) _, Roles.ROLE_USER.role, true)

        then: "show the form with corresponding errors and valid params"
            view == '/userManagement/register'
            model.user.username == params.username
            model.user.email == params.email
    }

    void "test doRegister - service error"() {
        given: "a rejected user and a mock category"
            user.errors.rejectValue('username', null)
            def category = Mock(Category)
            def category1 = Mock(Category)
            musementCategory.save()

        and: "the rest of the params"
            params.notification = user.notification
            params.username = user.username
            params.email    = user.email
            params.password = user.password
            params.password2 = user.password
            params.cats     = [category.name, category1.name]

        when: "register form was submitted"
            controller.doRegister()

        then: "service should be called once"
            1 * userAccountService.addUser((User) _, Roles.ROLE_USER.role, true) >> user

        then: "show the form with corresponding errors and valid params"
            view == '/userManagement/register'
            model.user.username == params.username
            model.user.email == params.email
    }

    void "test doRegister - success"() {
        given: "a valid user and params; a mock category"
            def category = Mock(Category)
            musementCategory.save()
            params.notification = user.notification
            params.username = user.username
            params.email    = user.email
            params.password = user.password
            params.password2 = user.password
            params.cats      = category.name

        when: "register form was submitted"
            controller.doRegister()

        then: "service should be called once"
            1 * userAccountService.addUser((User) _, Roles.ROLE_USER.role, true) >> user

        then: "login page should be loaded and a info message created"
            response.redirectedUrl == '/login/auth'
            flash.info == 'musement.user.register.success'
    }

    void "test unregister - success"() {
        when: "the unregister method is called by the current method"
            controller.unregister()

        then: "the current user should be deleted and the logout controller called"
            1 * userAccountService.deleteUser(user)
            response.redirectedUrl == '/logout'
    }

    void "test update page"() {
        when: "the update page is called"
            controller.update()

        then: "show register form/view"
            view == "/userManagement/update"
            model.user == user
    }

    void "test updatePassword - post method only"() {
        when: "calling method with a type other than POST"
            Map map = (Map) controller.updatePassword()

        then: "it should return the default view with current user"
            map.user == user
            view == null
    }

    void "test updatePassword - empty params"() {
        given: "only the current user and method POST"
            request.method = "POST"

        when: "register form was submitted"
            controller.updatePassword()

        then: "the update service will not be called"
            0 * userAccountService.updatePasswordForUser((String) _, user)

        then: "the following view should be rendered with the proper model"
            view == '/userManagement/update'
            model.user == user
    }

    void "test updatePassword - different passwords"() {
        given: "mock params"
            params.password2 = "password2"
            params.password3 = "password3"

        and: "request method POST"
            request.method = "POST"

        when: "update password form was submitted"
            controller.updatePassword()

        then: "service should not be called"
            0 * userAccountService.updatePasswordForUser((String) _, user)

        then: "the following view should be rendered with the proper model"
            view == '/userManagement/update'
            model.user == user
    }

    void "test updatePassword - current password not valid"() {
        given: "mock params"
            params.password = "passwordNotValid"
            params.password2 = "password2"
            params.password3 = "password2"

        and: "request method POST"
            request.method = "POST"

        when: "update password form was submitted"
            controller.updatePassword()

        then: "service should not be called"
            0 * userAccountService.updatePasswordForUser((String) _, user)

        then: "the following view should be rendered with the proper model"
            view == '/userManagement/update'
            model.user == user
    }

    void "test updatePassword - service error"() {
        given: "a rejected user and a mock category"
            user.errors.rejectValue('password', null)

        and: "the rest of the params"
            params.password = "12345678"
            params.password2 = "newPassword"
            params.password3 = "newPassword"
            request.method = "POST"

        when: "register form was submitted"
            controller.updatePassword()

        then: "service should be called once"
            1 * userAccountService.updatePasswordForUser((String) _, user) >> user

        then: "show the form with corresponding errors and valid params"
            view == '/userManagement/update'
            model.user == user
    }

    void "test updatePassword - success"() {
        given: "a valid user and params"
            params.password = "12345678"
            params.password2 = "newPassword"
            params.password3 = "newPassword"
            request.method = "POST"

        when: "register form was submitted"
            controller.updatePassword()

        then: "service should be called once"
            1 * userAccountService.updatePasswordForUser((String) _, user) >> user
            1 * springSecurityService.reauthenticate(user.username)

        then: "home page should be loaded"
            view == '/userManagement/home'
            model.user == user
            flash.info != null
    }

    void "test updateEmail - post method only"() {
        when: "calling method with a type other than POST"
        Map map = (Map) controller.updateEmail()

        then: "it should return the default view with current user"
        map.user == user
        view == null
    }

    void "test updateEmail - empty params"() {
        given: "only the current user and method POST"
            request.method = "POST"

        when: "register form was submitted"
            controller.updateEmail()

        then: "the update service will not be called"
            0 * userAccountService.updateUser(user, (Role) _)

        then: "the following view should be rendered with the proper model"
            view == '/userManagement/update'
            model.user == user
    }

    void "test updateEmail - current password not valid"() {
        given: "mock params"
            params.password4 = "passwordNotValid"
            params.email = "user@musement.com"

        and: "request method POST"
            request.method = "POST"

        when: "update password form was submitted"
            controller.updateEmail()

        then: "service should not be called"
            0 * userAccountService.updateUser(user, (Role) _)

        then: "the following view should be rendered with the proper model"
            view == '/userManagement/update'
            model.user == user
    }

    void "test updateEmail - service error"() {
        given: "a rejected user and a mock category"
            user.errors.rejectValue('email', null)

        and: "the rest of the params"
            params.password4 = "12345678"
            params.email = "user@musement.com"
            request.method = "POST"

        when: "register form was submitted"
            controller.updateEmail()

        then: "service should be called once"
            1 * userAccountService.updateUser((User)_ , _) >> user

        then: "show the form with corresponding errors and valid params"
            view == '/userManagement/update'
            model.user == user
    }

    void "test updateEmail - success"() {
        given: "a valid user and params"
            params.password4 = "12345678"
            params.email = "user@musement.com"
            request.method = "POST"

        when: "register form was submitted"
            controller.updateEmail()

        then: "service should be called once"
            1 * userAccountService.updateUser((User)_ , _) >> user

        then: "home page should be loaded"
            view == '/userManagement/home'
            model.user == user
            flash.info != null
    }

    void "test home - empty params"() {
        given: "the musement category"
        musementCategory.save()

        when: "home page is requested"
        controller.home()

        then: "home page should be loaded with respective model"
        view == "/userManagement/home"
        model.user == user
        model.categoryId == musementCategory.id
    }

    void "test home - valid params"() {
        given: "the musement category"
            musementCategory.save()
            params.categoryId = musementCategory.id
            params.readPost = "true"

        when: "home page is requested"
            controller.home()

        then: "the notification service should be called"
            1 * notificationService.readNotification((User) _, (Category) _)

        then: "home page should be loaded with respective model"
            view == "/userManagement/home"
            model.user == user
            model.categoryId == musementCategory.id
    }

    void "test deleteUser - empty params"() {
        when: "someone wants to delete an user"
            controller.deleteUser()

        then: "the service should not be called"
            0 * userAccountService.deleteUser((User) _)

        then: "the corresponding controller, activity and params must be loaded"
            response.redirectUrl == "/controlPanel/index?editMode=user"
            flash.message != null
    }

    void "test deleteUser - valid params, wrong user"() {
        given: "the id of the user to be deleted, the same as the current user"
            user.validate()
            User.metaClass.static.findById = { Long id -> user }
            params.userId = 13

        when: "someone wants to delete an user"
            controller.deleteUser()

        then: "the service should not be called"
            0 * userAccountService.deleteUser((User) _)

        then: "the corresponding controller, activity and params must be loaded"
            response.redirectUrl == "/controlPanel/index?editMode=user"
            flash.message != null
    }

    void "test deleteUser - valid params, another user"() {
        given: "the id of the user to be deleted, different from the current user"
            User other = new User(username: "test2", email: "test2@musement.com", password: "test2", notification: Mock(Notification))
            other.validate()
            User.metaClass.static.findById = { Long id -> other }
            params.userId = 13

        when: "someone wants to delete an user"
            controller.deleteUser()

        then: "the service should be called"
            1 * userAccountService.deleteUser((User) _)

        then: "the corresponding controller, activity and params must be loaded"
            response.redirectUrl == "/controlPanel/index?editMode=user"
            flash.info != null
    }

    void "test makeAdmin - empty params"() {
        given: "the admin role"
            new Role(authority: Roles.ROLE_ADMIN.name()).save()

        when: "someone wants to make an user admin"
            controller.makeAdmin()

        then: "the service should not be called"
            0 * userAccountService.updateUser((User) _, (Role) _)

        then: "the corresponding controller, activity and params must be loaded"
            response.redirectUrl == "/controlPanel/index?editMode=user"
            flash.message != null
    }

    void "test makeAdmin - valid params, invalid user"() {
        given: "the id of the user to be made admin"
            new Role(authority: Roles.ROLE_ADMIN.name()).save()
            User other = Mock(User)
            User.metaClass.static.findById = { Long id -> other }
            params.userId = 13

        when: "someone wants to make an user admin"
            controller.makeAdmin()

        then: "the service should not be called"
            0 * userAccountService.updateUser((User) _, (Role) _)

        then: "the corresponding controller, activity and params must be loaded"
            response.redirectUrl == "/controlPanel/index?editMode=user"
            flash.message != null
    }

    void "test makeAdmin - valid params, valid user"() {
        given: "the id of the user to be made admin"
            new Role(authority: Roles.ROLE_ADMIN.name()).save()
            User other = new User(username: "test2", email: "test2@musement.com", password: "test2", notification: Mock(Notification))
            User.metaClass.static.findById = { Long id -> other }
            params.userId = 13

        when: "someone wants to make an user admin"
            controller.makeAdmin()

        then: "the service should be called"
            1 * userAccountService.updateUser((User) _, (Role) _)

        then: "the corresponding controller, activity and params must be loaded"
            response.redirectUrl == "/controlPanel/index?editMode=user&userId="
            flash.info != null
    }

    void "test removeAdmin - empty params"() {
        given: "normal user role"
            new Role(authority: Roles.ROLE_ADMIN.name()).save()
            new Role(authority: Roles.ROLE_USER.name()).save()

        when: "someone wants to demote an admin"
            controller.removeAdmin()

        then: "the service should not be called"
            0 * userAccountService.updateUser((User) _, (Role) _)

        then: "the corresponding controller, activity and params must be loaded"
            response.redirectUrl == "/controlPanel/index?editMode=user"
            flash.message != null
    }

    void "test removeAdmin - valid params, one admin left"() {
        given: "the id of the admin to be demoted"
            List<UserRole> admins = Mock(ArrayList)
            admins.size() >> 1
            UserRole.metaClass.static.findAllByRole = { Role role -> admins }
            params.userId = 13

        when: "someone wants to demote an admin"
            controller.removeAdmin()

        then: "the service should not be called"
            0 * userAccountService.updateUser((User) _, (Role) _)

        then: "the corresponding controller, activity and params must be loaded"
            response.redirectUrl == "/controlPanel/index?editMode=user"
            flash.message != null
    }

    void "test removeAdmin - valid params, invalid user"() {
        given: "the id of the admin to be demoted"
            new Role(authority: Roles.ROLE_ADMIN.name()).save()
            new Role(authority: Roles.ROLE_USER.name()).save()
            User other = Mock(User)
            User.metaClass.static.findById = { Long id -> other }
            params.userId = 13

        when: "someone wants to demote an admin"
            controller.removeAdmin()

        then: "the service should not be called"
            0 * userAccountService.updateUser((User) _, (Role) _)

        then: "the corresponding controller, activity and params must be loaded"
            response.redirectUrl == "/controlPanel/index?editMode=user"
            flash.message != null
    }

    void "test removeAdmin - valid params, valid other user"() {
        given: "the id of the admin to be made demoted"
            new Role(authority: Roles.ROLE_ADMIN.name()).save()
            new Role(authority: Roles.ROLE_USER.name()).save()
            User other = new User(username: "test2", email: "test2@musement.com", password: "test2", notification: Mock(Notification))
            User.metaClass.static.findById = { Long id -> other }
            params.userId = 13

        when: "someone wants to demote an admin"
            controller.removeAdmin()

        then: "the service should be called"
            1 * userAccountService.updateUser((User) _, (Role) _)

        then: "the corresponding controller, activity and params must be loaded"
            response.redirectUrl == "/controlPanel/index?editMode=user&userId="
            flash.info != null
    }

    void "test removeAdmin - valid params, valid current user"() {
        given: "the id of the admin to be made demoted"
            new Role(authority: Roles.ROLE_ADMIN.name()).save()
            new Role(authority: Roles.ROLE_USER.name()).save()
            User.metaClass.static.findById = { Long id -> user }
            params.userId = 13

        when: "someone wants to demote an admin"
            controller.removeAdmin()

        then: "the service should be called"
            1 * userAccountService.updateUser((User) _, (Role) _)

        then: "the corresponding controller, activity and params must be loaded"
            response.redirectUrl == "/userManagement/home"
            flash.info != null
    }
}