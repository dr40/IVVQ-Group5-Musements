package musement

import grails.test.mixin.TestFor
import grails.test.spock.IntegrationSpec
import musement.user.User
import musement.Category
import spock.lang.*

/**
 *
 */
@TestFor(PostService)
class PostServiceIntegrationSpec extends IntegrationSpec {

    PostService postService = new PostService();

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
    }
    void "Send post will create a new post"() {
        def p = postService.sendPost(Mock(User), "Test content", Mock(Category));

        expect:
        p.validate()
    }

    void "Send post will notify all subscribed user of the category where post was send"() {
        // TODO: need to wait NotificationService
    }

}
