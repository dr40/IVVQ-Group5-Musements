package musement

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import musement.user.Role
import musement.user.Roles
import musement.user.User
import musement.user.UserAccountService
import spock.lang.*

/**
 *
 */
class PostServiceIntegrationSpec extends Specification {

    PostService postService
    BootStrapService bootStrapService
    UserAccountService userAccountService

    Category category
    Role normalRole

    def setup() {
        // Given the default Musement category and roles
        bootStrapService.initializeRoles()
        bootStrapService.initializeDefaultCategory()

        category = Category.findByName("Musement")

        normalRole = Roles.ROLE_USER.role
    }

    def "test send post"() {
        given: "an user which are subscribed in the category"
            User user = new User( username: "user", email: "user@gmail.com", password: "userADZzad31234", notification: new Notification())
            user.addToCategories(category)
            userAccountService.addUser(user, normalRole, true)

        and: "an another user which are subscribed in the category too"
            User user2 = new User( username: "user2", email: "user2@gmail.com", password: "userADZzad3123sd4", notification: new Notification())
            user2.addToCategories(category)
            userAccountService.addUser(user2, normalRole, true)

        and: "another user which are not present in the category"
            User user3 = new User(username:"user3", email:"user3@gmail.com", password:"userADS943132", notification: new Notification())
            userAccountService.addUser(user3, normalRole, true)

        and: "a default content"
            def content = "My first post"

        when: "user which are present in the category send a post"
            postService.sendPost(user, content, category)

        then: "post are created"
            def p = Post.findBySenderAndCategory(user, category)
            p != null
            p.content == content
            user.posts.size() == 1

        and: "notifications have been sended correctly"
            user.notification.posts == null
            user2.notification.posts.size() == 1
            user3.notification.posts == null

        when: "user which are not present in the category send a post"
            postService.sendPost(user3, content, category)

        then: "post are not sended"
            Post.findBySenderAndCategory(user3, category) == null

        and: "no notifications have been sended"
            user.notification.posts == null
            user2.notification.posts.size() == 1
            user3.notification.posts == null

    }


    def "test edit post"() {
        given: "an user which are subscribed in a category"
            User editUser = new User( username: "editUser", email: "editUser@gmail.com", password: "userADZzad31234", notification: new Notification())
            editUser.addToCategories(category)
            userAccountService.addUser(editUser, normalRole, true)

        and: "a post"
            def content = "My post content"
            Post post = postService.sendPost(editUser, content, category)

        when: "edit post with an valid content"
            def newContent = "My new content"
            postService.editPost(post, newContent)

        then: "Post have been edited"
            Post.findBySenderAndCategory(editUser, category).content == newContent

        when: "edit post with an invalid content follow post content contraints"
            postService.editPost(post, null)

        then: "Post have not been edited"
            Post.findBySenderAndCategory(editUser, category).content == newContent

    }

    def "test delete post"() {
        given: "an user which are subscribed in a category"
            User delUser = new User( username: "delUser", email: "delUser@gmail.com", password: "userADZzad31234", notification: new Notification())
            delUser.addToCategories(category)
            userAccountService.addUser(delUser, normalRole, true)

        and: "a post"
            def content = "My post content"
            Post post = postService.sendPost(delUser, content, category)

        when: "delete an sended post"
            postService.deletePost(post)

        then: "Post have been deleted"
            Post.findBySenderAndCategory(delUser, category) == null

        when: "delete an unexisting post"
            def result = postService.deletePost(post)

        then: "No post deleted: and service return false"
            !result

    }

}
