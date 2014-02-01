import grails.util.Environment
import groovy.sql.Sql
import musement.BootStrapService
import musement.user.Role
import musement.user.User
import musement.user.UserRole

import java.util.logging.Level

class BootStrap {

    BootStrapService bootStrapService

    def init = { servletContext ->
        bootStrapService.initializeRoles()
        bootStrapService.inializeDefaultUsers()

        assert User.count() >= 2
        assert Role.count() == 2
        assert UserRole.count() >= 2

        User.findAll().each { println it.username + ' ' + it.enabled + ' ' + it.password}
    }

    def destroy = {
    }
}
