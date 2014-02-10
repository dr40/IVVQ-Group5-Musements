package musement.user

import musement.Notification
import spock.lang.Specification

/**
 * The purpose of these tests is to verify the <code>beforeInsert()</code> and
 * <code>beforeUpdate()</code> of the User domain class
 */
class UserIntegrationSpec extends Specification {

    def springSecurityService

    void testBeforeInsert() {
        given: "a valid user"
            User user = new User(username: "test", email: "test@musement.com", password: "test", notification: Mock(Notification))

        when: "user is save"
            user.save(failOnError: true)

        then: "password should be encrypted with bcrypt. Two encryptions of the same text are not the same"
            springSecurityService.passwordEncoder.isPasswordValid(user.password, "test", null)
            user.password != springSecurityService.encodePassword("test")
    }

    void testBeforeUpdate() {
        given: "a notification"
            Notification notification = new Notification()

        and: "a valid user"
            User user = new User(username: "test", email: "test@musement.com", password: "oldPassword", notification: notification).save(failOnError: true, flush: true)

        when: "updating the password and saving the user"
            user.password = "newPassword"
            user.save(failOnError: true, flush: true)

        then: "the password should be encoded"
            !springSecurityService.passwordEncoder.isPasswordValid(user.password, "oldPassword", null)
            springSecurityService.passwordEncoder.isPasswordValid(user.password, "newPassword", null)
            user.password != "newPassword"
    }
}
