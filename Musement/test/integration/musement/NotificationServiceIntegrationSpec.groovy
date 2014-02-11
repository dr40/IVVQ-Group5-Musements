package musement

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import musement.user.User
import musement.Category
import spock.lang.*

@TestFor(NotificationService)
class NotificationServiceIntegrationSpec extends Specification {

    NotificationService notificationService
    PostService postService
    SpringSecurityService springSecurityService

    Category category

    def setup(){

        postService = new PostService()
        category = new Category(
                name: "musement",
                description: "First category"
        ).save(failOnError: true)

    }

    void "User is notified about a post"(){
        given:"User subscribed to the category Musement"
        def notification = new Notification()
        User user = new User(
                username: "First",
                email: "a@gmail.com",
                password: "Pass12",
                notification: notification
        )
        user.addToCategories(category)
        user.save(failOnError: true)

        and:"Another user that post in Musement"
        def notif = new Notification()
        Post post = new Post(
                content: "Visit",
                category: category
        )
        User sender = new User(
                username: "Sender",
                email: "s@gmail.com",
                password: "123456kkt",
                notification: notif
        )
        sender.addToCategories(category)
        sender.addToPosts(post)
        sender.save(failOnError: true)


        when:"The sender posts"
        notificationService.notifyUsers(post)

        then:"The user receives a notification"
        user.notification.posts.size() ==1

        and:"The sender is not notified about his own post"
        sender.notification.posts == null
    }

    void "Post saved with succes in Notification"(){
        given:"A user"
        def notificationB = new Notification()
        User.metaClass.static.encodePassword = { -> }
        User userB = new User(
                username: "First",
                email: "a@gmail.com",
                password: "Pass12",
                notification: notificationB,
                categories: category
        )
        Post postS = new Post(sender:userB, content:"First Post", category: category)
        userB.addToPosts(postS)
        userB.save(failOnError: true, flush:true)

        and:"A a user "
        def notifB = new Notification()
        User.metaClass.static.encodePassword = { -> }
        User senderB = new User(
                username: "Sender",
                email: "s@gmail.com",
                password: "123456kkt",
                notification: notifB,
                categories:category
        )

        when:"Sender sends post"
        postService.sendPost(userB,"First Post", category)

        then:"Succes-save notification "
        userB.posts.size() ==1
        senderB.notification.posts.size() == 1

    }

    void "Delete a post from notification after reading"(){

        given:"a user with posts in notification"
        def n_salsa = new Notification()
        User.metaClass.static.encodePassword = { -> }
        User salsa = new User(
                username: "Salsa",
                email: "s@gmail.com",
                password: "123456kkt",
                notification: n_salsa,
                categories: category
        )
        salsa.save(flush:true)

        and :"other user"
        Post postSend = new Post(content:"content")
        User sender = new User(
                username: "Salsa",
                email: "s@gmail.com",
                password: "123456kkt",
                notification: n_salsa,
                categories: category,
                posts: postSend
        ).save(flush:true)

        and:""
        salsa.notification.posts.add(postSend)
        salsa.save( failOnError: true, flush:true)

        when: ""
        notificationService.readNotification(salsa, category)

        then:""
        salsa.notification.posts == null




    }

    void "the sender should not receive a notification"(){
        given: "a post"
        def notification = new Notification()
        User.metaClass.encodePassword = { -> }
        User user = new User(
                username: "First",
                email: "a@gmail.com",
                password: "Pass12",
                notification: notification,
        ).save(failOnError: true)
        Post post = new Post(
                sender: user,
                content: "Visit",
                category: category
        ).save(failOnError: true)

        and:"add user to category"
        user.addToCategories(category)

        when:"notify called"
        notificationService.notifyUsers(post)

        then:"user notif empty"
        user.notification.posts == null
    }

    void "the users from a category should be notified about a post "(){
        given:" a sender"
        def notification = new Notification()
        User.metaClass.encodePassword = { -> }
        User sender = new User(
                username: "Sender",
                email: "s@gmail.com",
                password: "123456kkt",
                notification: notification,
                categories: category
        ).save(failOnError: true)

        and: "other user"
        def notif = new Notification()
        User user = new User(
                username: "kkt",
                email: "g@gmail.com",
                password: "123456HA",
                notification: notif,
                categories: category
        ).save(failOnError: true)

        and:"a post"
        Post post = new Post(
                sender: sender,
                content: "Visit",
                category: category
        ).save(failOnError: true)


        when:"notify called"
        assert notif.validate()
        assert notif.save(flush:true)



        then:"user notif empty"
        notif.user.username == "kkt"
        notif.posts.size() ==1


    }

}
