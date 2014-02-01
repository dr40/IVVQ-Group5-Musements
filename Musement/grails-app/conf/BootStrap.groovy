import grails.util.Environment
import groovy.sql.Sql
import musement.BootStrapService
import musement.Notification
import musement.Category
import musement.user.Role
import musement.user.User
import musement.user.UserRole

import java.util.logging.Level

class BootStrap {

    BootStrapService bootStrapService

    def init = { servletContext ->
        bootStrapService.initializeRoles()
        bootStrapService.initializeDefaultCategory()
        bootStrapService.initializeDefaultUsers()

        assert Role.count() == 2
        assert Category.count() >= 1
        assert User.count() >= 2
        assert Notification.count() >= 2

        User.findAll().each { println 'Username: ' + it.username +
                             ' Notification: ' + it.notification.user.username +
                             ' Category: ' + it.categories.getAt(0).name}
    }

    def destroy = {
    }
}
