package musement

import grails.plugin.springsecurity.SpringSecurityService
import musement.user.Roles
import musement.user.User
import musement.user.UserAccountService
import spock.lang.*

class NotificationServiceIntegrationSpec extends Specification {

    NotificationService notificationService
    PostService postService
    SpringSecurityService springSecurityService
    BootStrapService bootStrapService
    UserAccountService userAccountService
    NotificationController notificationController

    Category category

    def setup(){
        // Given the default Musement category and roles
        bootStrapService.initializeRoles()
        bootStrapService.initializeDefaultCategory()

        category = Category.findByName("Musement")
    }

    void "User is notified about a post"(){
        given:"User subscribed to the category Musement"
        def notificationUser = new Notification()
        User user = new User( username: "user", email: "user@gmail.com", password: "user",
                              notification: notificationUser)
        user.addToCategories(category)
        user.save(failOnError: true)

        and:"A post"
        Post post = new Post(content: "Visit",category: category)

        and:"Another user that post in Musement"
        def notificationSender = new Notification()
        User sender = new User(username: "sender",email: "sender@gmail.com",password: "sender",
                notification: notificationSender)

        and:"Set sender with post and categories, like he did a post"
        sender.addToCategories(category)
        sender.addToPosts(post)
        sender.save(failOnError: true)

        when:"The sender posts, the notification service is called"
        notificationService.notifyUsers(post)

        then:" User notification updated with post"
        user.notification.posts.each {p -> equals(post)}

        and:"The sender is not notified about his own post"
        sender.notification.posts == null
    }

    void "Post saved with success in Notification"(){
        given: "An user"
        def notificationA = new Notification()
        User userA = new User(username: "userA", email: "userA@musement.com", password: "passwordA",
                notification: notificationA, categories: category)
        userAccountService.addUser(userA, Roles.ROLE_USER.role)

        and: "Another user"
        def notificationB = new Notification()
        User userB = new User(username: "userB", email: "userB@musement.com", password: "passwordB",
                notification: notificationB, categories:category)
        userAccountService.addUser(userB, Roles.ROLE_USER.role)

        when: "Sender sends Post"
        postService.sendPost(userA, "First Post", category)

        then: "Success save notification "
        userA.posts.size() == 1
        userB.notification.posts.size() == 1
    }

    void "Delete a post from notification after reading"(){
        given: "An user"
        def notificationA = new Notification()
        User userA = new User(username: "userA", email: "userA@musement.com", password: "passwordA",
                notification: notificationA, categories: category)
        userAccountService.addUser(userA, Roles.ROLE_USER.role)

        and :"Other user"
        def notificationB = new Notification()
        User userB = new User(username: "userB", email: "userB@musement.com", password: "passwordB",
                notification: notificationB, categories:category)
        userAccountService.addUser(userB, Roles.ROLE_USER.role)

        and:"Sender sends Post"
        postService.sendPost(userA, "First Post", category)

        when: "UserB read the notification"
        notificationService.readNotification(userB, category)

        then:"Delete post from notification for userB"
        userB.notification.posts.size() == 0

        and:"the post is saved for sender"
        userA.posts.each {p -> p.content =="First Post"}

    }

    void "All the post from a category should be deleted"(){
        given: "An user"
        def notificationA = new Notification()
        User userA = new User(username: "userA", email: "userA@musement.com", password: "passwordA",
                notification: notificationA, categories: category)
        userAccountService.addUser(userA, Roles.ROLE_USER.role)

        and :"Other user"
        def notificationB = new Notification()
        User userB = new User(username: "userB", email: "userB@musement.com", password: "passwordB",
                notification: notificationB, categories:category)
        userAccountService.addUser(userB, Roles.ROLE_USER.role)

        and:"The thirth user"
        def notificationC = new Notification()
        User userC = new User(username: "userC", email: "userC@musement.com", password: "passwordC",
                notification: notificationC, categories:category)
        userAccountService.addUser(userC, Roles.ROLE_USER.role)

        and:"Sender sends Post"
        postService.sendPost(userB, "B Post", category)
        postService.sendPost(userB,"B2 Post", category)
        postService.sendPost(userC, "C Post", category)

        and: "Notification number"
        userA.notification.posts.size() == 3
        userB.notification.posts.size() == 1
        userC.notification.posts.size() == 2

        when: "Read notifications"
        notificationService.readNotification(userA, category)

        then: "The posts for a category should be deleted"
        userA.notification.posts.size() == 0



    }




}
