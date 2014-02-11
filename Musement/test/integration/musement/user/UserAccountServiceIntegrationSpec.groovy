package musement.user

import grails.plugin.springsecurity.SpringSecurityService
import musement.BootStrapService
import musement.Notification
import spock.lang.*

/**
 *  Tests for the UserAccountService.
 *  They are meant to ensure that all functions for the User domain function accordingly.
 */
class UserAccountServiceIntegrationSpec extends Specification {

    BootStrapService bootStrapService
    UserAccountService userAccountService
    SpringSecurityService springSecurityService

    def "test add user"() {
        given: "the roles"
            bootStrapService.initializeRoles()

        when: "adding a user"
        userAccountService.addUser(new User(username: "test", email: "test@musement.com", password: "test", notification: new Notification()),
                Roles.ROLE_ADMIN.role)

        then: "the user is saved"
            User user = User.findByUsername("test")
            user != null
            user.enabled
            user.email == "test@musement.com"
            !user.authorities.empty
            user.authorities.first().authority == Roles.ROLE_ADMIN.name()
            springSecurityService.passwordEncoder.isPasswordValid(user.password, "test", null)

        when: "adding a disabled user "
        userAccountService.addUser(new User(username: "test2", email: "test2@musement.com", password: "test2", notification: new Notification()),
                Roles.ROLE_USER.role, false)

        then: "the user is disabled"
            User user2 = User.findByUsername("test2")
            user2 != null
            !user2.enabled
            user2.email == "test2@musement.com"
            !user2.authorities.empty
            user2.authorities.first().authority == Roles.ROLE_USER.name()
            springSecurityService.passwordEncoder.isPasswordValid(user2.password, "test2", null)
    }

    @Unroll
    def "test update user - email/role"() {
        given: "an user"
        bootStrapService.initializeRoles()
        def test = userAccountService.addUser(new User(username: "test", email: "test@musement.com", password: "test", notification: new Notification()),
                Roles.ROLE_USER.role, false)

        when: 'modify email and role, then update'
            test.email = newEmail
            Role mainRole = Roles.valueOf(newRoleName).role
            userAccountService.updateUser(test, mainRole)

        then: 'the user is modified properly'
            User user = User.findByUsername("test")
            user != null
            println user.errors
            (user.version != 0) == userHasChanged
            user.hasErrors() == userHasErrors
            if(userHasChanged) {
                UserRole.get(user.id, mainRole.id) != null
            }

        where:
            newEmail            |   newRoleName             | userHasChanged | userHasErrors
            "test2@musement.com"|   Roles.ROLE_USER.name()  | true           | false
            "test@musement.com" |   Roles.ROLE_ADMIN.name() | false          | false
            "test@@@@"          |   Roles.ROLE_USER.name()  | false          | true
    }

    def "test update user password"() {
        given: "an user"
            bootStrapService.initializeRoles()
            def test = userAccountService.addUser(new User(username: "test", email: "test@musement.com", password: "test", notification: new Notification()),
                    Roles.ROLE_USER.role, false)

        when: "password is updated"
            test.password = "newPassword"
            userAccountService.updatePasswordForUser("newPassword", test)

        then: 'the user is modified properly'
            User user = User.findByUsername("test")
            user != null
            springSecurityService.passwordEncoder.isPasswordValid(user.password, "newPassword", null)
            !springSecurityService.passwordEncoder.isPasswordValid(user.password, "test", null)
            user.password != "newPassword"
    }

    def "test delete user"() {
        given: "an user"
        bootStrapService.initializeRoles()
        def test = userAccountService.addUser(new User(username: "test", email: "test@musement.com", password: "test", notification: new Notification()),
                Roles.ROLE_USER.role, false)

        when: "user is deleted"
            userAccountService.deleteUser(test)

        then: "the user is no more"
            !User.findByUsername("test")
    }

    def "test all admins are subscribed to a category"() {
        given: "three users"
            bootStrapService.initializeRoles()
            userAccountService.addUser(new User(username: "test", email: "test@musement.com", password: "test", notification: new Notification()),
                Roles.ROLE_USER.role, false)
            userAccountService.addUser(new User(username: "admin1", email: "test2@musement.com", password: "test", notification: new Notification()),
                Roles.ROLE_ADMIN.role, false)
            userAccountService.addUser(new User(username: "admin2", email: "test3@musement.com", password: "test", notification: new Notification()),
                Roles.ROLE_ADMIN.role, false)

        and: "a category"
            bootStrapService.initializeDefaultCategory()
            def category = musement.Category.findByName("Musement")

        when: "the function is called (in the CategoryService"
            userAccountService.updateAdminCategories(category)

        then: "the correct users are updated"
            User user1 = User.findByUsername("test")
            User user2 = User.findByUsername("admin1")
            User user3 = User.findByUsername("admin2")
            user1 != null
            user2 != null
            user3 != null
            !user1.categories
            user2.categories.contains(category)
            user3.categories.contains(category)
    }

}
