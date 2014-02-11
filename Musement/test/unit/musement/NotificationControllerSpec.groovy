package musement

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.authentication.encoding.BCryptPasswordEncoder
import grails.test.mixin.*
import spock.lang.*
import musement.user.User
@Mock([ User, Category, Notification, Post])
@TestFor(NotificationController)
class NotificationControllerSpec extends Specification {

    SpringSecurityService springSecurityService
    NotificationService notificationService
    User user
    Category musementCategory
    Notification notification
    Post myPost
    PostService postService

    def setup(){
        notification= Mock(Notification)
        myPost = Mock(Post)
        user = new User(username: "test", email: "test@musemnt.com", password: "\$2a\$10\$FDzV4Sw09Ua/z5dMP/FBWOyXFgo1pwTFG.KvIehsYycaL.ixUPnsi", notification: notification)
        musementCategory = new Category(name: "Musement", description: "test")

        // Notification Service
        notificationService = Mock(NotificationService)
        controller.notificationService = notificationService

        // SpringSecurityService
        springSecurityService = Mock(SpringSecurityService)
        springSecurityService.currentUser >> user
        controller.springSecurityService = springSecurityService

    }

    void "Test the notification number on null object"(){
      when:"the number of notif requested"
       def  x = controller.notificationsNumber()

      then:"the value should be 0 "
        0 == x.notificationsNumber

    }

    void "test the notification number for a list of posts"(){
        given:
        myPost.category = musementCategory
        myPost.id = 1

        and:"a notification"
        notification.user = user
        user.notification.posts = myPost

        when:" notification number requested"
        def x = controller.notificationsNumber()

        then: ""
        notification.posts.size() == x.notificationsNumber

    }


    void "Test the show notification for one post"(){
        given:" a mocked user"
        def notification = Mock(Notification)
        User user = new User(username: "test", email: "test@musement.com", password: "test", notification: notification)
        springSecurityService.currentUser >>user

        and:"set the category"
        Category category = new Category(name:"musement", description:"First")

        and:"another user"
        def notificationB = Mock(Notification)
        User userB = new User(username: "testB", email: "testB@musement.com", password: "testB", notification: notificationB)

        and:"userB posts"
        postService.sendPost(userB,"FirstPost", Mock(Category))

        when:"show"
        def model = controller.showNotifications()

        then:""
        model.notifications.size() == 1
    }

   /* void "Test the notificationNumber action returns the correct model"() {

        when: "The notificationsNumber action is executed"
        def user =  Mock(User)
        user.validate()
        controller.notificationsNumber()

        then: "The model is correct"
        model.notification
        model.notificationNumber == 0
    }*/

   /* void "Test the create action returns the correct model"() {
        when: "The create action is executed"
        controller.create()

        then: "The model is correctly created"
        model.notificationInstance != null
    }

    void "Test the save action correctly persists an instance"() {

        when: "The save action is executed with an invalid instance"
        def notification = new Notification()
        notification.validate()
        controller.save(notification)

        then: "The create view is rendered again with the correct model"
        model.notificationInstance != null
        view == 'create'

        when: "The save action is executed with a valid instance"
        response.reset()
        populateValidParams(params)
        notification = new Notification(params)

        controller.save(notification)

        then: "A redirect is issued to the show action"
        response.redirectedUrl == '/notification/show/1'
        controller.flash.message != null
        Notification.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
        controller.show(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the show action"
        populateValidParams(params)
        def notification = new Notification(params)
        controller.show(notification)

        then: "A model is populated containing the domain instance"
        model.notificationInstance == notification
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
        controller.edit(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the edit action"
        populateValidParams(params)
        def notification = new Notification(params)
        controller.edit(notification)

        then: "A model is populated containing the domain instance"
        model.notificationInstance == notification
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
        controller.update(null)

        then: "A 404 error is returned"
        response.redirectedUrl == '/notification/index'
        flash.message != null


        when: "An invalid domain instance is passed to the update action"
        response.reset()
        def notification = new Notification()
        notification.validate()
        controller.update(notification)

        then: "The edit view is rendered again with the invalid instance"
        view == 'edit'
        model.notificationInstance == notification

        when: "A valid domain instance is passed to the update action"
        response.reset()
        populateValidParams(params)
        notification = new Notification(params).save(flush: true)
        controller.update(notification)

        then: "A redirect is issues to the show action"
        response.redirectedUrl == "/notification/show/$notification.id"
        flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
        controller.delete(null)

        then: "A 404 is returned"
        response.redirectedUrl == '/notification/index'
        flash.message != null

        when: "A domain instance is created"
        response.reset()
        populateValidParams(params)
        def notification = new Notification(params).save(flush: true)

        then: "It exists"
        Notification.count() == 1

        when: "The domain instance is passed to the delete action"
        controller.delete(notification)

        then: "The instance is deleted"
        Notification.count() == 0
        response.redirectedUrl == '/notification/index'
        flash.message != null
    }*/
}
