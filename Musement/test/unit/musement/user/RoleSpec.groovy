package musement.user

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Role)
class RoleSpec extends Specification {

    @Unroll
    void "test constraints - authority: #authority"(String authority, boolean expectedResult) {
        given: "a user"
        Role role = new Role(
                authority: authority
        )

        expect: "validate method to return the expected result"
        role.validate() == expectedResult

        where:
        authority           | expectedResult
        Roles.ADMIN         | true
        Roles.USER          | true
        ""                  | false
        "someRole"          | false
    }
}
