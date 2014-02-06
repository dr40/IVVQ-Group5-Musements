package musement



import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import musement.user.User;

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
        User u = springSecurityService.currentUser;
        Category c = Category.findById(params.get("categoryId"));
        /* Return concerned posts */
        def posts = Post.findAllByCategory(c, params).reverse();
        render(view:"/post/post_show", model: [posts: posts, categoryId: c.id, currentUser: u])
    }



    /**
     * @param categoryId Id of the current category
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def sendPost() {
        /* Params: currentCategory */
        User u = springSecurityService.currentUser;
        Category c = Category.findById(params.categoryId);
        String content = params.content;
        /* Send post using service */
        Post p = postService.sendPost(u, content, c);
        redirect ( controller: "userManagement", action: "home", params:[categoryId: params.categoryId])
    }


}
