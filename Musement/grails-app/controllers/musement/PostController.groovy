package musement



import static org.springframework.http.HttpStatus.*
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import musement.user.User;

@Transactional(readOnly = true)
class PostController {

    SpringSecurityService springSecurityService
    PostService postService
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * @param categoryId Id of the current category
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def getPosts() {
        /* Params: currentCategory */
        User u = springSecurityService.currentUser;
        Category c = Category.findById(params.categoryId);
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



    @Secured(['IS_AUTHENTICATED_FULLY'])
    def index() {
        User u = springSecurityService.currentUser;
        respond Post.findAllBySender(u, params), model:[postInstanceCount: Post.count()]
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def show(Post postInstance) {
        respond postInstance
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def create() {
        /* Get Current User and Category */
        respond new Post(params)
    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def save(Post postInstance) {
        if (postInstance == null) {
            notFound()
            return
        }

        if (postInstance.hasErrors()) {
            respond postInstance.errors, view:'create'
            return
        }

        postInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'postInstance.label', default: 'Post'), postInstance.id])
                redirect postInstance
            }
            '*' { respond postInstance, [status: CREATED] }
        }
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def edit(Post postInstance) {
        respond postInstance
    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def update(Post postInstance) {
        if (postInstance == null) {
            notFound()
            return
        }

        if (postInstance.hasErrors()) {
            respond postInstance.errors, view:'edit'
            return
        }

        postInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Post.content', default: 'Post'), postInstance.id])
                redirect postInstance
            }
            '*'{ respond postInstance, [status: OK] }
        }
    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def delete(Post postInstance) {

        if (postInstance == null) {
            notFound()
            return
        }

        postInstance.delete flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Post.content', default: 'Post'), postInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'postInstance.content', default: 'Post'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
