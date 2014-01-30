package musement.user

import grails.test.mixin.TestFor
import musement.Notification
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
class UserSpec extends Specification {

    @Unroll
    void "test constraints - username: #username"(String username, boolean expectedResult) {
        given: "a user"
        User user = new User(
                username: username,
                email: "user@name.com",
                password: "password",
                notification: Mock(Notification)
        )

        expect: "validate method to return the expected result"
        user.validate() == expectedResult

        where:
        username            | expectedResult
        "Username"          | true
        "User.name"         | true
        "username"          | true
        "User_Name"         | false
        ".username"         | false
        "username."         | false
        "user@name"         | false
        "UsernameTooLong17" | false
        "Abc"               | false // Username too short
    }

    @Unroll
    void "test constraints - email: #email"(String email, boolean expectedResult) {
        given: "a user"
        User user = new User(
                username: "username",
                email: email,
                password: "password",
                notification: Mock(Notification)
        )

        expect: "validate method to return the expected result"
        user.validate() == expectedResult

        where:
        email               | expectedResult
        "user@email.com"    | true
        "email"             | false
        "email.com"         | false
        "1@2.3"             | false
    }

}
