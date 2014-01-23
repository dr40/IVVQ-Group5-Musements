package musement

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Notification)
class NotificationSpec extends Specification {

    def post_received
    def category1

    def setup() {
        post_received = new Post()
        category1 = new Category()
    }

    def cleanup() {
    }

    void "notification category cannot be empty"() {

        def notificationWithCategory = new Notification( concernedPost: post_received, category: category1)
        def notificationWithoutCategory = new Notification( concernedPost: post_received )

        expect:
        notificationWithCategory.validate()
        notificationWithCategory.category == category1
        !notificationWithoutCategory.validate()

    }

    void "notification post cannot be empty " () {
        def notificationWithPost = new Notification(concernedPost: post_received, category: category1 )
        def notificationWithoutPost = new Notification()

        expect:
        notificationWithPost.validate()
        notificationWithPost.concernedPost == post_received
        notificationWithPost.category == category1
        !notificationWithoutPost.validate()


    }
}
