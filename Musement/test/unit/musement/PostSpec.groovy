package musement

import grails.test.mixin.TestFor
import musement.user.User
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Post)
class PostSpec extends Specification {

    def user1
    def category1

    def setup() {
        user1 = new User()
        category1 = new Category(name: "test", description: "myDesc")
    }

    def cleanup() {
    }


    void "post content cannot be empty"() {

        def postWithContent = new Post(sender: user1, postCategory: category1, content: "post content")
        def postWithoutContent = new Post(sender: user1, postCategory: category1)

        expect:
        postWithContent.validate()
        postWithContent.content == "post content"
        !postWithoutContent.validate()

    }
    void "post content have user and category linked"() {

        def postWithUserAndCategory = new Post(sender: user1, postCategory: category1, content: "post content")
        def postWithoutUserAndCategory = new Post(content: "post content")
        def postWithoutUser = new Post(postCategory: category1, content: "post content")
        def postWithoutCategory = new Post(sender: user1, content: "post content")

        expect:
        postWithUserAndCategory.validate()
        postWithUserAndCategory.sender == user1
        postWithUserAndCategory.postCategory == category1
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
