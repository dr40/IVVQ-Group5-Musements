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
        Roles.ROLE_ADMIN    | true
        Roles.ROLE_USER     | true
        ""                  | false
        "someRole"          | false
    }

    void "test for unique constrains"() {
        setup:
        // Valid role
        def role1 = new Role(authority: Roles.ROLE_ADMIN)
        // Nullable fields
        def role2 = new Role()
        // Not unique
        def role3 = new Role(authority: Roles.ROLE_ADMIN)
        // Unique
        def role4 = new Role(authority: Roles.ROLE_USER)

        mockForConstraintsTests(Role, [role1])

        expect:
        // Check all members not nullable
        assert !role2.validate()
        assert "nullable" == role2.errors["authority"]

        // Check not unique
        assert !role3.validate()
        assert "unique" == role3.errors["authority"]

        // Validation should pass!
        assert role4.validate()
    }

    void "test for getRole"(){
        setup:
        def role = new Role(authority: Roles.ROLE_USER)

        expect:
        role
        assertNull(Roles.ROLE_USER.role)
    }
}
