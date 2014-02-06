package musement

import grails.transaction.Transactional
import groovy.sql.Sql

import musement.user.*

@Transactional
class BootStrapService {

    def dataSource
    UserAccountService userAccountService

    Role normalRole
    Role adminRole

    User admin
    User normal

    Category musement

    Notification adminNotification
    Notification normalNotification

    def initializeRoles() {
        Sql sql = new Sql(dataSource)

        adminRole = Role.findByAuthority(Roles.ROLE_ADMIN.name())
        if (!adminRole) {
            sql.executeInsert("insert into role (id,authority) values (1,${Roles.ROLE_ADMIN.name()})")
            adminRole = Roles.ROLE_ADMIN.role
        }

        normalRole = Role.findByAuthority(Roles.ROLE_USER.name())
        if (!normalRole) {
            sql.executeInsert("insert into role (id,authority) values (2,${Roles.ROLE_USER.name()})")
            normalRole = Roles.ROLE_USER.role
        }
    }

    def initializeDefaultCategory() {
        /* Default category */
        musement = Category.findByName("Musement")
        if (!musement) {
            musement = new Category(name: 'Musement', description: 'Default Musement Category. Here you can see everything.')
            musement.save()
        }

        new Category(name: 'Test', description: 'Test').save()
    }

    def initializeDefaultUsers() {
        /* Default admin user */
        admin = User.findByUsername('admin')
        if (!admin) {
            admin = new User(username: 'admin', email: 'admin@musement.com', password: '12345678',
                        notification: new Notification())
            admin.addToCategories(musement)
            admin = userAccountService.addUser(admin, adminRole, true)
        }

        /* Default normal user */
        normal = User.findByUsername('default');
        if (!normal) {
            normal = new User(username: 'default', email: 'default@musement.com', password: '87654321',
                        notification: new Notification())
            normal.addToCategories(musement)
            normal = userAccountService.addUser(normal, normalRole, true)
        }
    }
}
