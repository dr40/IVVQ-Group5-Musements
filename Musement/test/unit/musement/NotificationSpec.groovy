package musement

import grails.test.mixin.TestFor
import spock.lang.Specification
import musement.user.User
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Notification)
class NotificationSpec extends Specification {


    void "notification must have an user"() {
        def myPost = Mock(Post)
        def notificationWithUser = new Notification(user: Mock(User),posts:[myPost] )
        def notificationWithoutUser = new Notification(posts:[myPost])

        expect:
        notificationWithUser.validate()
        !notificationWithoutUser.validate()

    }

    void "notification post can have more than one post " () {
        def myPost = Mock(Post)
        def N = new Random().nextInt(100)+2
        def postList = [];
        for(int i = 0; i < N; i++) {
            postList.push(Mock(Post))
        }

        def notificationWithPost = new Notification(user:Mock(User), posts: [myPost])
        def notificationWithoutPost = new Notification(user:Mock(User))
        def notificationWithNPost = new Notification(user:Mock(User), posts:postList)

        expect:
        notificationWithNPost.validate()
        notificationWithNPost.posts.size() == N
        notificationWithPost.posts.size() == 1
        notificationWithPost.validate()
        notificationWithoutPost.validate()

    }
}
