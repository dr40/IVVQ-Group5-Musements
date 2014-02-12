package musement

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.authentication.encoding.BCryptPasswordEncoder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import musement.user.Role
import musement.user.User
import musement.user.UserAccountService
import musement.user.UserRole
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@Mock([Role, User, UserRole, Category, Notification, Category, Notification, Post, NotificationService, PostService])
@TestFor(PostController)
class PostControllerSpec extends Specification {

    /** Services **/
    SpringSecurityService springSecurityService
    UserAccountService userAccountService
    PostService postService
    NotificationService notificationService

    User currentUser
    User admin
    User user
    User user2
    Category musementCategory
    Category testCategory
    Post post1
    Post post2
    Post post3


    BCryptPasswordEncoder passwordEncoder

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Init
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    def setup() {
        /* Data */
        admin = new User(username: "admin", email:"admin@musement.com", password: "\$2a\$10\$FDzV4Sw09Ua/z5dMP/FBWOyXFgo1pwTFG.KvIehsYycaL.ixUPnsi", notification: Mock(Notification))
        user = new User(username: "test", email: "test@musemnt.com", password: "\$2a\$10\$FDzV4Sw09Ua/z5dMP/FBWOyXFgo1pwTFG.KvIehsYycaL.ixUPnsi", notification: Mock(Notification))
        user2 = new User(username: "test2", email: "test@musemnt.com", password: "\$2a\$10\$FDzV4Sw09Ua/z5dMP/FBWOyXFgo1pwTFG.KvIehsYycaL.ixUPnsi", notification: Mock(Notification))
        musementCategory = new Category(name: "Musement", description: "test")
        musementCategory.save()
        testCategory = new Category(name: "MyCategory", description: "test")
        testCategory.save()

        // Some post
        post1 = new Post(sender: user2, category: testCategory, content: "My first post")
        post1.save()
        testCategory.addToPosts(post1).save()
        post2 = new Post(sender: user, category: testCategory, content: "My second post")
        post2.save()
        post3 = new Post(sender: user2, category: testCategory, content: "My second post")
        post3.save()
        admin.notification = new Notification(user: admin).save()
        user.notification = new Notification(user: user).save()
        user2.notification = new Notification(user: user2).save()
        user.notification.addToPosts(post1)
        user.notification.addToPosts(post3)
        user2.notification.addToPosts(post2)


        // SpringSecurityService
        passwordEncoder = new BCryptPasswordEncoder(10)
        springSecurityService = Mock(SpringSecurityService)
        springSecurityService.currentUser >> user
        springSecurityService.passwordEncoder >> passwordEncoder
        controller.springSecurityService = springSecurityService


        // Account Service
        userAccountService = Mock(UserAccountService)

        // Post Service
        postService = Mock(PostService)

        postService.notificationService = Mock(NotificationService)
        controller.postService = postService

        // Notification Service
        notificationService = Mock(NotificationService)
        controller.postService.notificationService = notificationService


        /* Generate methods required */
        Post.metaClass.static.findById = { def id ->
            if (id == null) {
                null
            } else if (id == post1.id) {
                post1
            } else if (id == post2.id) {
                post2
            } else if (id == post3.id) {
                post3
            } else {
                null
            }
        }
        Category.metaClass.static.findById = { def id ->
            if (id == null) {
                null
            } else if ((id == 1) || (id == musementCategory.id)) {
                musementCategory
            } else if (id == testCategory.id) {
                testCategory
            } else {
                null
            }
        }
        SpringSecurityUtils.metaClass.static.ifAllGranted = { String role ->
            if (role.contains("ROLE_ADMIN")) {
                return (currentUser == admin)
            } else {
                return true
            }
        }



    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Index page
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Unroll
    void "test index page"() {
        when: "the index page is called"
            controller.index()
        then: "redirect to home page with no parameters"
            response.redirectedUrl == '/userManagement/home'
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GetPosts page
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Unroll
    void "test getPosts action - without categoryId parameter gived"() {
        when: "the getPosts action is called without categoryId parameter"
            controller.getPosts()
        then: "show view of posts of the main category 'Musement'"
            view == "/post/posts_show"
            model.categoryId == 1
            model.currentUser == user
            model.posts != null
    }

    @Unroll
    void "test getPosts action - with unvalid categoryId parameter gived"() {
        given: "CategoryId parameter"
            params.categoryId = 48943216
        when: "the getPosts action is called with an invalid categoryId parameter"
            controller.getPosts()
        then: "show view of posts from the main category 'Musement'"
            view == "/post/posts_show"
            model.categoryId == 1
            model.currentUser == user
            model.posts != null
    }

    @Unroll
    void "test getPosts action - with valid categoryId parameter gived"() {
        given: "CategoryId parameter"
            params.categoryId = testCategory.id
        when: "the getPosts action is called with an valid categoryId parameter"
            controller.getPosts()
        then: "show view of posts from the desired category"
            view == "/post/posts_show"
            model.categoryId == testCategory.id
            model.currentUser == user
            model.posts == [post3,post2,post1]
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Render a post page
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Unroll
    void "test renderAPost action - without postId"() {
        when: "the renderAPost action is called without postId parameter"
            controller.renderAPost()
        then: "redirect to home page with no parameters"
        response.redirectedUrl == '/userManagement/home'
    }

    @Unroll
    void "test renderAPost action - with postId"() {
        given: "PostId parameter"
            params.postId = post1.id
        when: "the renderAPost action is called with postId parameter"
            controller.renderAPost();
        then: "show view of desired post"
            view == "/post/post"
            model.post.id == post1.id
            model.categoryId == post1.category.id
            model.currentUser == user
    }

    @Unroll
    void "test renderAPost action - Indicate new post"() {
        given: "PostId parameter of an other user"
            params.postId = post3.id
        when: "the renderAPost action is called with a post send by another user and still in notification pending state"
            controller.renderAPost()
        then: "show view of desired post with an indication that post is in notification"
            view == "/post/post"
            model.isNewPost
    }

    @Unroll
    void "test renderAPost action - Indicate new post only if the post are new"() {
        given: "PostId parameter of an other user"
            params.postId = post2.id
        when: "the renderAPost action is called with a post which aren't present in notification post list of current user"
            controller.renderAPost()
        then: "show view of desired post"
            view == "/post/post"
            !model.isNewPost
    }


    @Unroll
    void "test renderAPost action - Not admin user can only delete post that they have send"() {
        given: "User connected"
        when: "the renderAPost action is called with a post send by connected user"
            params.postId = post2.id
            controller.renderAPost()
        then: "post is deletable"
            view == "/post/post"
            model.deletable
        when: "the renderAPost action is called with a post not send by connected user"
            params.postId = post1.id
            controller.renderAPost()
        then: "post is deletable"
            view == "/post/post"
            !model.deletable
    }


    @Unroll
    void "test renderAPost action - All post are deletable by admin"() {
        given: "Admin connected"
            currentUser = admin
        when: "the renderAPost action is called with a post"
            params.postId = post1.id
            controller.renderAPost()
        then: "post is deletable"
            view == "/post/post"
            model.deletable
        when: "the renderAPost action is called with a post"
            params.postId = post2.id
            controller.renderAPost()
        then: "post is deletable"
            view == "/post/post"
            model.deletable
        when: "the renderAPost action is called with a post"
            params.postId = post3.id
            controller.renderAPost()
        then: "post is deletable"
            view == "/post/post"
            model.deletable
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Send post
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Unroll
    void "test sendPost action - without parameter"() {
        given: "User connected"

        when: "the sendPost action is called without parameter"
            controller.sendPost()
        then: "the service should not be called"
            0 * postService.sendPost((User) _, (String) _, (Category) _)
        and: "post not sended, and redirect to user home page"
            response.redirectedUrl == '/userManagement/home?categoryId=&postSended=-1'
    }
    @Unroll
    void "test sendPost action - with categoryId only"() {
        given: "User connected and an category id gived"
            def catId = testCategory.id
        when: "the sendPost action is called with categoryId only"
            params.categoryId = catId
            controller.sendPost()
        then: "the service should not be called"
            0 * postService.sendPost((User) _, (String) _, (Category) _)
        and: "post not sended, and redirect to user home page"
            response.redirectedUrl == '/userManagement/home?categoryId=' + catId + '&postSended=-1'
    }
    @Unroll
    void "test sendPost action - with categoryId and content"() {
        given: "User connected and an category id gived and a content gived"
            def catId = testCategory.id
            def content = "My first content"
            def retUrl = '/userManagement/home?categoryId=' + catId + '&postSended=';
        when: "the sendPost action is called with categoryId and content"
            params.categoryId = catId
            params.content = content
            controller.sendPost()
        then: "the service should be called"
            1 * postService.sendPost((User) _, (String) _, (Category) _)
        and: "and an redirect to user home page is done"
            response.redirectedUrl.substring(0, retUrl.length()) == retUrl
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Edit post
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Unroll
    void "test editPost action - without parameter"() {
        given: "User connected"
            def catId = testCategory.id
        when: "the editPost action is called without parameter"
            params.categoryId = catId
            controller.editPost()
        then: "the service should not be called"
            0 * postService.editPost((Post) _, (String) _)
        and: "post not edited, and redirect to user home page"
            response.redirectedUrl == '/userManagement/home?categoryId=' + catId + '&postEdited=false'
    }

    @Unroll
    void "test editPost action - with postId"() {
        given: "User connected and an post id gived"
            def postId = post2.id
            def catId = testCategory.id
        when: "the sendPost action is called with postId parameter"
            params.postId = postId
            params.categoryId = catId
            controller.editPost()
        then: "the service should not be called"
            0 * postService.editPost((Post) _, (String) _)
        and: "post not edited, and redirect to user home page"
            response.redirectedUrl == '/userManagement/home?categoryId=' + catId + '&postEdited=false'
    }
    @Unroll
    void "test editPost action - with postId and content"() {
        given: "User connected and an post id gived and a content gived"
            def postId = post2.id
            def catId = testCategory.id
            def content = "My first content"
            def retUrl = '/userManagement/home?categoryId=' + catId + '&postEdited=';

        when: "the sendPost action is called with postId and content parameters"
            params.postId = postId
            params.categoryId = catId
            params.newContent = content
            controller.editPost()

        then: "the service should be called"
            1 * postService.editPost((Post) _, (String) _)

        and: "redirect to user home page is done"
            response.redirectedUrl.substring(0, retUrl.length()) == retUrl

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Delete post
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Unroll
    void "test deletePost action - without parameter gived"() {
        given: "User connected"
            def catId = testCategory.id
        when: "the deletePost action is called without parameter"
            params.categoryId = catId
            controller.deletePost()
        then: "the service should not be called"
            0 * postService.deletePost((Post) _)
        and: "post not deleted, and redirect to user home page"
            response.redirectedUrl == '/userManagement/home?categoryId=' + catId + '&postDeleted=false'
    }

    @Unroll
    void "test deletePost action - with post id parameter gived"() {
        given: "User connected and post id parameter gived"
            def catId = testCategory.id
            def postId = post2.id
            def retUrl = '/userManagement/home?categoryId=' + catId + '&postDeleted=';
        when: "the deletePost action is called without parameter"
            params.categoryId = catId
            params.postId = postId
            controller.deletePost()
        then: "the service should be called"
            1 * postService.deletePost((Post) _)
        and: "redirect to user home page"
            response.redirectedUrl.substring(0, retUrl.length()) == retUrl
    }

}
