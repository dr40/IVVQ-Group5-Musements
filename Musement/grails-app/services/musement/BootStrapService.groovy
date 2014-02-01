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

    def inializeDefaultUsers() {
        admin = User.findByUsername("admin")
        if (!admin) {
            admin = new User(username: 'admin', email: 'admin@musement.com', password: '12345678')
            admin = userAccountService.addUser(admin, adminRole, true)
        }

        normal = User.findByUsername('default');
        if (!normal) {
            normal = new User(username: 'default', email: 'default@musement.com', password: '87654321')
            normal = userAccountService.addUser(normal, normalRole, true)
        }
    }
}
