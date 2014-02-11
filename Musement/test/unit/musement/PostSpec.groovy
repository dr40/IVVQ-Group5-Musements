package musement

import grails.test.mixin.TestFor
import musement.user.User
import spock.lang.Specification
import grails.test.mixin.Mock;
import grails.test.mixin.TestFor
import spock.lang.Unroll;

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Post)
@Mock([Category, User])
class PostSpec extends Specification {

    @Unroll
    void "post content cannot be empty"() {
        given: "a content"
        def content = "My first post #FirstPost #Welcome";

        /* Content empty */
        when: "post content is empty"
        def postWithoutContent = new Post(sender: Mock(User), category: Mock(Category))

        then: "post is not valid"
        !postWithoutContent.validate()

        /* The opposite */
        when: "post content is not empty"
        def postWithContent = new Post(sender: Mock(User), category: Mock(Category), content: content)

        then: "post is valid and content is well persisted"
        postWithContent.validate()
        postWithContent.content == content
    }

    @Unroll
    void "post have user and category linked"() {

        given: "an user and a category"
        def sender = Mock(User)
        def category = Mock(Category)

        /* User null */
        when: "post user not defined"
        def postWithoutUser = new Post(category: category, content: "post content")

        then: "post is not valid"
        !postWithoutUser.validate()

        /* Category null */
        when: "post category not defined"
        def postWithoutCategory = new Post(sender: sender, content: "post content")

        then: "post is not valid"
        !postWithoutCategory.validate()

        /* Post and category null */
        when: "post category and user not defined"
        def postWithoutUserAndCategory = new Post(content: "post content")

        then:
        !postWithoutUserAndCategory.validate()

        /* Finally: User/Category defined */
        when: "post category and user defined"
        def postWithUserAndCategory = new Post(sender: sender, category: category, content: "post content")

        then:
        postWithUserAndCategory.validate()

    }

    @Unroll
    void "post date are initialized to current date"() {
        given: "a post"
        def currentDate = new Date()
        def postTest = new Post(content: "A test message #test #ivvq #unit")

        when: "post created"
        then: "post send date are superior or equal to the current date"
        postTest.postDate.compareTo(currentDate) >= 0

    }

    @Unroll
    void "post cannot exceed 256 characters"() {
        given: "a post and a long content"
        def post = new Post(
                sender: Mock(User),
                category: Mock(Category)
        )
        def longContent = "Lorem ipsum...."
        def content256 = ""
        for(int i = 0; i < 256; i++) {
            longContent += "Lorem ipsum...."
            content256 += "a"
        }

        when: "post content length lower than 256 characters"
        post.content = "My short post"
        then: "post valid"
        post.validate()

        when: "post content length equals to 256 characters"
        post.content = content256
        then: "post valid"
        post.validate()

        when: "post content length exceed 256 characters"
        post.content = longContent
        then: "post not valid"
        !post.validate()

    }

}
