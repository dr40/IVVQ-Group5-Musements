package musement

import grails.test.mixin.TestFor
import musement.user.User
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

        def postWithContent = new Post(sender: Mock(User), category: Mock(Category), content: "post content")
        def postWithoutContent = new Post(sender: Mock(User), category: Mock(Category))

        expect:
        postWithContent.validate()
        postWithContent.content == "post content"
        !postWithoutContent.validate()

    }
    void "post content have user and category linked"() {

        def postWithUserAndCategory = new Post(sender: Mock(User), category: Mock(Category), content: "post content")
        def postWithoutUserAndCategory = new Post(content: "post content")
        def postWithoutUser = new Post(category: Mock(Category), content: "post content")
        def postWithoutCategory = new Post(sender: Mock(User), content: "post content")

        expect:
        postWithUserAndCategory.validate()
        !postWithoutUserAndCategory.validate()
        !postWithoutUser.validate()
        !postWithoutCategory.validate()

    }
    void "post date are initialized to current date"() {

        def currentDate = new Date();
        def postTest = new Post(content: "post test")

        expect:
        postTest.postDate.compareTo(currentDate) >= 0

    }

}
