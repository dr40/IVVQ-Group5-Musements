package musement

import grails.transaction.Transactional
import groovy.sql.Sql

import musement.user.*

@Transactional
class BootStrapService {

    def dataSource
    UserAccountService userAccountService
    CategoryService categoryService

    def initializeRoles() {
        Sql sql = new Sql(dataSource)

        if (!Role.findByAuthority(Roles.ROLE_ADMIN.name())) {
            sql.executeInsert("insert into role (id,authority) values (1,${Roles.ROLE_ADMIN.name()})")
        }

        if (!Role.findByAuthority(Roles.ROLE_USER.name())) {
            sql.executeInsert("insert into role (id,authority) values (2,${Roles.ROLE_USER.name()})")
        }
    }

    def initializeDefaultCategory() {
        /* Default category */
        if (!Category.findByName("Musement")) {
            Category musement = new Category(name: 'Musement', description: 'Default Musement Category. Here you can see everything.')
            categoryService.addCategory(musement)
        }
    }

    def initializeDefaultUsers() {
        Category musement = Category.findByName("Musement")
        Role adminRole = Roles.ROLE_ADMIN.role
        Role normalRole = Roles.ROLE_USER.role

        // Check if roles and category exist
        if (!musement || !musement.validate())
            return

        if (!Roles.ROLE_ADMIN.role || !Roles.ROLE_USER.role)
            return

        /* Default admin user */
        if (!User.findByUsername('admin')) {
            def admin = new User(username: 'admin', email: 'admin@musement.com', password: '12345678',
                        notification: new Notification())
            admin.addToCategories(musement)
            userAccountService.addUser(admin, adminRole, true)
        }

        /* Default normal user */
        if (!User.findByUsername('default')) {
            def normal = new User(username: 'default', email: 'default@musement.com', password: '87654321',
                        notification: new Notification())
            normal.addToCategories(musement)
            userAccountService.addUser(normal, normalRole, true)
        }
    }
}
