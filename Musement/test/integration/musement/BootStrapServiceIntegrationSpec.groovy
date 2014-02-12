package musement

import musement.user.Roles
import musement.user.Role
import musement.user.User
import musement.user.UserRole
import spock.lang.*

/**
 *  Tests written for the service called when the system starts.
 *  They are meant to assure that all functions can be done.
 */
class BootStrapServiceIntegrationSpec extends Specification {

    BootStrapService bootStrapService

    def "test initialize Admin and User roles"() {
        when: "the initializeRoles function is called"
            bootStrapService.initializeRoles()

        then: "the roles Admin and User are created with the proper ids"
            Role.findByAuthority(Roles.ROLE_ADMIN.name())
            Role.findByAuthority(Roles.ROLE_ADMIN.name()).id == 1
            Role.findByAuthority(Roles.ROLE_USER.name())
            Role.findByAuthority(Roles.ROLE_USER.name()).id == 2
    }

    def "test initialize musement category"() {
        when: "the function is called"
            bootStrapService.initializeDefaultCategory()

        then: "Musement category is created"
            Category.findByName("Musement")
    }

    def "test initialize default users"() {
        when: "the function is called"
            bootStrapService.initializeDefaultUsers()

        then: "all is good - users"
            UserRole.findAllByRole(Roles.ROLE_ADMIN.role).size() >= 1
            UserRole.findAllByRole(Roles.ROLE_USER.role).size() >= 1
            User.findAll().size() >= 2
    }
}
