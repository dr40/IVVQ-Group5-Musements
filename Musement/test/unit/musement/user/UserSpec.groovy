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
        null                | false
        ""                  | false
        "Username"          | true
        "User.name"         | true
        "username"          | true
        "1username"         | true
        "user name"         | false
        "User_Name"         | false
        ".username"         | false
        "username."         | false
        "user@name"         | false
        "!@#%^&*()-+,."     | false
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
        null                | false
        ""                  | false
        "user@email.com"    | true
        "email"             | false
        "email.com"         | false
        "1@2.3"             | false
        'a'*65              | false
    }

    @Unroll
    void "test constraints - password: #password"(String password, boolean expectedResult) {
        given: "a user"
        User user = new User(
                username: "username",
                email: "user@musement.com",
                password: password,
                notification: Mock(Notification)
        )

        expect: "validate method to return the expected result"
        user.validate() == expectedResult

        where:
        password            | expectedResult
        null                | false
        ""                  | false
        "bla"               | true
    }

    void "user has notification"() {
        def userWithNotification = new User(username: 'test', password: 'test', email: 'test@musement.com', notification: Mock(Notification))
        def userWithoutNotification = new User(username: 'test', password: 'test', email: 'test@musement.com')

        expect:
        userWithNotification.validate()
        !userWithoutNotification.validate()
    }

    void "user has role"() {
        setup:
        def userWithoutRole = Mock(User)

        expect:
        assertNull(userWithoutRole.getAuthorities())
    }

    void "test for unique constrains"() {
        setup:
        // Valid user
        def user1 = new User(username: "test", email: "test@musement.com", password: "test", notification: Mock(Notification))
        // Nullable fields
        def user2 = new User()
        // Not unique
        def user3 = new User(username: "test", email: "test@musement.com", password: "test", notification: Mock(Notification))
        // Unique
        def user4 = new User(username: "test2", email: "test2@musement.com", password: "test", notification: Mock(Notification))

        mockForConstraintsTests(User, [user1])

        expect:
        // Check all members not nullable
        assert !user2.validate()
        assert "nullable" == user2.errors["username"]
        assert "nullable" == user2.errors["email"]
        assert "nullable" == user2.errors["password"]
        assert "nullable" == user2.errors["notification"]

        // Check not unique
        assert !user3.validate()
        assert "unique" == user3.errors["username"]
        assert "unique" == user3.errors["email"]

        // Validation should pass!
        assert user4.validate()
    }

}
