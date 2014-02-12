package musement



import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import musement.user.User;


@Transactional(readOnly = true)
class PostController {

    /** Services **/
    SpringSecurityService springSecurityService
    PostService postService

    /**
     * Redirect to user home page
     * @return
     */
    def index() {
        /* Send post using service */
        redirect ( controller: "userManagement", action: "home", params:[])
    }

    /**
     * Render a view with all post from a gived category
     * @param categoryId Id of the category desired
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def getPosts() {
        /* Params: currentCategory */
        User currentUser = springSecurityService.currentUser;
        Category currentCategory = Category.findById(params.categoryId);
        int catId = 1;
        if (currentCategory == null) {
            currentCategory = Category.findById(1)
        } else {
            catId = currentCategory.id
        }
        /* Return concerned posts */
        def posts = [];
        if (currentCategory != null) {
            posts = Post.findAllByCategory(currentCategory, params).reverse();
        }
        render(view:"/post/posts_show", model: [posts: posts, categoryId: catId, currentUser: currentUser])
    }

    /**
     * Render a view with a gived post
     * @param postId Id of the desired post
     * @return
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def renderAPost() {
        /* Params: postId */
        Post p = Post.findById(params.postId);
        if (p == null) {
            redirect ( controller: "userManagement", action: "home", params:[])
            return
        }
        User currentUser = springSecurityService.currentUser;
        /* Check if post is deletable */
        def deletable = false;
        if (SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')) {
            deletable = true;
        } else {
            deletable = p.getSender().equals(currentUser)
        }
        /* Check if new post: Present in notification of current user */
        def isNewPost = false;
        currentUser.notification.posts.each { np ->
            if (np.id == p.id) {
                isNewPost = true;
            }
        }
        /* Render post */
        render(view:"/post/post", model: [post: p, isNewPost: isNewPost, categoryId: p.getCategory().id, currentUser:currentUser, deletable:deletable])
    }


    /**
     * Send a post into a gived category, and redirect to user home page
     * @param categoryId Id of the desired category
     * @param content Content of the post
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def sendPost() {
        /* Params: currentCategory */
        User currentUser = springSecurityService.currentUser;
        Category currentCategory = Category.findById(params.categoryId);
        String content = params.content;
        def postSendedId = -1;
        if ((currentCategory != null) && (content != null)) {
            def p = postService.sendPost(currentUser, content, currentCategory);
            if (p != null) {
                postSendedId = p.id;
            }
        }
        /* Send post using service */
        redirect ( controller: "userManagement", action: "home", params:[categoryId: params.categoryId, postSended: postSendedId])
    }

    /**
     * Edit a post, and redirect to user home page
     * @param postId Id of the desired post
     * @param categoryId Id of the current category
     * @param newContent New content to set for the desired post
     * @return
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def editPost() {
        /* Params: post */
        User currentUser = springSecurityService.currentUser;
        Post post = Post.findById(params.postId);
        /* Check if can edit */
        def postEdited = false;
        def canEdit = false;
        if ((post != null) && (params.newContent != null)) {
            if (SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')) {
                canEdit = true;
            } else {
                canEdit = post.getSender().equals(currentUser)
            }
        }
        /* Edit post */
        if (canEdit) {
            postEdited = postService.editPost(post, params.newContent);
        }
        /* Redirect to home page */
        redirect ( controller: "userManagement", action: "home", params:[categoryId: params.categoryId, postEdited:postEdited])
    }

    /**
     * Delete a post, and redirect to user home page
     * @param postId Id of the post to delete
     * @param categoryId Id of the current category
     * @return
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def deletePost() {
        /* Params: post */
        User currentUser = springSecurityService.currentUser;
        Post post = Post.findById(params.postId);
        /* Check if can delete */
        def postDeleted = false;
        def canDelete = false;
        if (post != null) {
            if (SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')) {
                canDelete = true;
            } else {
                canDelete = post.getSender().equals(currentUser)
            }
        }
        /* Delete post using service */
        if (canDelete) {
            postDeleted = postService.deletePost(post)
        }
        /* Redirect to home page */
        redirect ( controller: "userManagement", action: "home", params:[categoryId: params.categoryId, postDeleted:postDeleted])
    }


}
