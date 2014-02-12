package musement

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.*
import spock.lang.*
import musement.user.User
@Mock([ User, Category, Notification, Post])
@TestFor(NotificationController)
class NotificationControllerSpec extends Specification {

    SpringSecurityService springSecurityService
    NotificationService notificationService
    User user
    User user2
    Category musementCategory
    Category category2
    Post myPost
    Post myPost2
    Post myPost3
    PostService postService

    def setup(){
        user = new User(username: "test", email: "test@musemnt.com", password: "\$2a\$10\$FDzV4Sw09Ua/z5dMP/FBWOyXFgo1pwTFG.KvIehsYycaL.ixUPnsi")
        user.notification = new Notification(user: user).save()
        user2 = new User(username: "test2", email: "test@musemnt.com", password: "\$2a\$10\$FDzV4Sw09Ua/z5dMP/FBWOyXFgo1pwTFG.KvIehsYycaL.ixUPnsi")
        user2.notification = new Notification(user: user2).save()

        musementCategory = new Category(name: "Musement", description: "test").save()
        category2 = new Category(name: "TestCategory", description: "test").save()

        // Notification Service
        notificationService = Mock(NotificationService)
        controller.notificationService = notificationService

        // SpringSecurityService
        springSecurityService = Mock(SpringSecurityService)
        springSecurityService.currentUser >> user
        controller.springSecurityService = springSecurityService

        /* Mocked functions */
        User.metaClass.static.findByUsername = { def name ->
            if (name == user.username) {
                user
            } else if (name == user2.username) {
                user2
            } else {
                null
            }
        }
    }
    @Unroll
    void "Test the notification number on null object"(){
        when:"the number of notif requested"
        def  x = controller.notificationsNumber()

        then:"the value should be 0 "
        0 == x.notificationsNumber

    }

    @Unroll
    void "Test the notification number for a list of posts"(){
        given:"a post"
        myPost = new Post(sender: user2, category: musementCategory, content: "My first post").save()

        and:"a notification"
        user.notification.addToPosts(myPost)

        when:" notification number requested"
        def x = controller.notificationsNumber()

        then: "count of notification is correct"
        user.notification.posts.size() == x.notificationsNumber

    }
    @Unroll
    void "Test the notification number for many posts in the same category"(){
        given:"a post"
        myPost = new Post(sender: user2, category: musementCategory, content: "My first post").save()
        myPost2 = new Post(sender: user2, category: musementCategory, content: "My second post").save()

        and:"a notification"
        user.notification.addToPosts(myPost)
        user.notification.addToPosts(myPost2)

        when:" notification number requested"
        def x = controller.notificationsNumber()

        then: "count of notification is correct"
        1 == x.notificationsNumber

    }

    @Unroll
    void "Test the notification number for many posts in different categories"(){
        given:"a post"
        myPost = new Post(sender: user2, category: musementCategory, content: "My first post").save()
        myPost3 = new Post(sender: user2, category: category2, content: "Other post").save()
        myPost2 = new Post(sender: user2, category: musementCategory, content: "My second post").save()

        and:"a notification"
        user.notification.addToPosts(myPost)
        user.notification.addToPosts(myPost2)
        user.notification.addToPosts(myPost3)

        when:" notification number requested"
        def x = controller.notificationsNumber()

        then: "count of notification is correct"
        user.notification.posts.unique{it.category.name}.size() == x.notificationsNumber

    }
    @Unroll
    void "Test show notification for one post"(){
        given:" a post"
        myPost = new Post(sender: user2, category: musementCategory, content: "My first post").save()

        and:"a notification"
        user.notification.addToPosts(myPost)

        when:"showNotifications called"
        def model = controller.showNotifications()

        then:"only one notification will be returned with sender field specified and post count = 1"
        model.notifications.size() == 1
        model.notifications[0].post_count == 1
        model.notifications[0].category == musementCategory
        model.notifications[0].sender != null
    }

    @Unroll
    void "Test show notification for many post"() {
        given:" two post send into the same category"
        myPost = new Post(sender: user2, category: musementCategory, content: "My first post").save()
        myPost2 = new Post(sender: user2, category: musementCategory, content: "My second post").save()

        and:"two notification"
        user.notification.addToPosts(myPost)
        user.notification.addToPosts(myPost2)

        when:"showNotifications called"
        def model = controller.showNotifications()

        then:"only one notification will be returned without sender field and post count = 2"
        model.notifications.size() == 1
        model.notifications[0].post_count == 2
        model.notifications[0].category == musementCategory
        model.notifications[0].sender == null
    }

    @Unroll
    void "Test show notification for many notification"() {
        given:" two post send into two distinct category"
        myPost = new Post(sender: user2, category: musementCategory, content: "My first post").save()
        myPost2 = new Post(sender: user2, category: category2, content: "My second post").save()

        and:"two notification"
        user.notification.addToPosts(myPost)
        user.notification.addToPosts(myPost2)

        when:"showNotifications called"
        def model = controller.showNotifications()

        then:"two notifications will be returned"
        model.notifications.size() == user.notification.posts.size()

        and:"sender known"
        model.notifications[0].post_count == 1
        model.notifications[0].category == musementCategory
        model.notifications[0].sender == user2

        and:"sender known"
        model.notifications[1].post_count == 1
        model.notifications[1].category == category2
        model.notifications[1].sender == user2

    }

    @Unroll
    void "Test show notification for posts in different categories and many notification"() {
        given:" two post send into two distinct category"
        myPost = new Post(sender: user2, category: musementCategory, content: "My first post").save()
        myPost2 = new Post(sender: user2, category: category2, content: "My second post").save()
        myPost3 = new Post(sender: user2, category: category2, content: "My thirth post").save()

        and:"two notification"
        user.notification.addToPosts(myPost)
        user.notification.addToPosts(myPost2)
        user.notification.addToPosts(myPost3)

        when:"showNotifications called"
        def model = controller.showNotifications()

        then:"two notifications will be returned"
        model.notifications.size() == 2

        and:"first notification"
        model.notifications[0].post_count == 1
        model.notifications[0].category == musementCategory
        model.notifications[0].sender == user2

        and:"second notification, sender null"
        model.notifications[1].post_count == 2
        model.notifications[1].category == category2
        model.notifications[1].sender == null

    }

    @Unroll
    void "Test show notification on null post"() {

        when:"showNotifications called"
        def model = controller.showNotifications()

        then:"nothing returned"
        model.notifications == "None"
    }

}