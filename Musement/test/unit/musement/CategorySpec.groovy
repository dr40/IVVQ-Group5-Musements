package musement

import grails.test.mixin.TestFor
import spock.lang.Specification
import musement.Category

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Category)
class CategorySpec extends Specification {


    def setup() {
    }

    def cleanup() {
    }

    void "verify category name"() {

        def categoryWithName = new Category(name:"Dance", description:"A very enjoying group")
        def categoryWithoutName = new Category()
        def categoryWrongName = new Category(name:"01Dance", description:"A very enjoying group")
        def categoryEmptyName = new Category(name:"", description:"A very enjoying group")

        expect:
        categoryWithName.validate()
        categoryWithName.name == "Dance"
        !categoryWithoutName.validate()
        !categoryWrongName.validate()
        !categoryEmptyName.validate()
    }

    void "verify category has many users" (){


    }
}
