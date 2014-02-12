package musement

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Category)
class CategorySpec extends Specification {

    @Unroll
    void "test constraints - name: #name"(String name, boolean expectedResult) {
        given: "a category"
        Category category = new Category(name: name, description: "Some valid description")


        expect: "validate method to return the expected result"
        category.validate() == expectedResult

        where:
        name                | expectedResult
        null                | false
        ""                  | false
        "Category"          | true
        "category"          | true
        "category1"         | true
        "category 1"        | false
        "1cat"              | false
        "1.cat"             | false
        "cat."              | false
    }

    @Unroll
    void "test constraints - description: #description"(String description, boolean expectedResult) {
        given: "a category"
        Category category = new Category(name: "ValidName", description: description)


        expect: "validate method to return the expected result"
        category.validate() == expectedResult

        where:
        description         | expectedResult
        null                | false
        ""                  | false
        "description"       | true
    }

    void "test for unique constraint"() {
        setup:
        // Valid category
        def cat1 = new Category(name: "Musement", description: "Best category ever")
        // Nullable fields
        def cat2 = new Category()
        // Not unique
        def cat3 = new Category(name: "Musement", description: "Best category ever")
        // Unique
        def cat4 = new Category(name: "Musement2", description: "Second best category ever")

        mockForConstraintsTests(Category, [cat1])

        expect:
        // Check all members not nullable
        assert !cat2.validate()
        assert "nullable" == cat2.errors["name"]
        assert "nullable" == cat2.errors["description"]

        // Check not unique
        assert !cat3.validate()
        assert "unique" == cat3.errors["name"]

        // Validation should pass!
        assert cat4.validate()
    }

}
