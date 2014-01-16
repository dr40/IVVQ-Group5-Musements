package musement

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Post)
class PostSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }


    void "post content cannot be empty"() {

        def postWithContent = new Post(content: "post content")
        def postWithoutContent = new Post()

        expect:
        postWithContent.validate()
        postWithContent.content == "post content"
        !postWithoutContent.validate()


    }
    void "post date are initialized to current date"() {

        def currentDate = new Date();
        def postTest = new Post(content: "post test")

        expect:
        postTest.postDate.compareTo(currentDate) >= 0

    }

}
