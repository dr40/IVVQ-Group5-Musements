package musement



import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import musement.user.Roles
import musement.user.User;
import musement.user.UserRole;


@Transactional(readOnly = true)
class PostController {

    SpringSecurityService springSecurityService
    PostService postService

    /**
     * @param categoryId Id of the current category
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def getPosts() {
        /* Params: currentCategory */
        User currentUser = springSecurityService.currentUser;
        Category currentCategory = Category.findById(params.categoryId);
        /* Return concerned posts */
        def posts = Post.findAllByCategory(currentCategory, params).reverse();
        render(view:"/post/posts_show", model: [posts: posts, categoryId: currentCategory.id, currentUser: currentUser])
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def renderAPost() {
        /* Params: postId */
        Post p = Post.findById(params.postId);
        User currentUser = springSecurityService.currentUser;
        /* Check if post is deletable */
        def deletable = false;
        if (SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')) {
            deletable = true;
        } else {
            deletable = p.getSender().equals(currentUser)
        }
        /* Render post */
        render(view:"/post/post", model: [post: p, categoryId: p.getCategory().id, currentUser:currentUser, deletable:deletable])
    }

    /**
     * @param categoryId Id of the current category
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def sendPost() {
        /* Params: currentCategory */
        User currentUser = springSecurityService.currentUser;
        Category currentCategory = Category.findById(params.categoryId);
        String content = params.content;
        /* Send post using service */
        Post p = postService.sendPost(currentUser, content, currentCategory);
        redirect ( controller: "userManagement", action: "home", params:[categoryId: params.categoryId])
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def deletePost() {
        /* Params: post */
        User currentUser = springSecurityService.currentUser;
        Post post = Post.findById(params.postId);
        /* Check if can delete */
        def canDelete = false;
        if (SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')) {
            canDelete = true;
        } else {
            canDelete = post.getSender().equals(currentUser)
        }
        /* Delete post using service */
        if (canDelete) {
            postService.deletePost(post)
        }
        /* Redirect to home page */
        redirect ( controller: "userManagement", action: "home", params:[categoryId: params.categoryId])
    }


}
