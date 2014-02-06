package musement



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import musement.user.*


@Transactional(readOnly = true)
class CategoryController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    SpringSecurityService springSecurityService
    CategoryService categoryService

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def index(Integer max) {

            params.max = Math.min(max ?: 10, 100)
            params.max = Math.min(max ?: 10, 100)
            respond Category.list(params), model: [categoryInstanceCount: Category.count()]

    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def show(Category categoryInstance) {
        respond categoryInstance
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        respond new Category(params)
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def save(Category categoryInstance) {
        if (categoryInstance == null) {
            notFound()
            return
        }

        if (categoryInstance.hasErrors()) {
            respond categoryInstance.errors, view: 'create'
            return
        }

        //categoryInstance.save flush: true
        categoryService.addCategory(categoryInstance)

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: categoryInstance.name, default: 'Category'), categoryInstance.id])
                redirect categoryInstance
            }
            '*' { respond categoryInstance, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit(Category categoryInstance) {
        respond categoryInstance
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def update(Category categoryInstance) {
        if (categoryInstance == null) {
            notFound()
            return
        }

        if (categoryInstance.hasErrors()) {
            respond categoryInstance.errors, view: 'edit'
            return
        }

        //categoryInstance.save flush: true
        categoryService.updateCategory(categoryInstance)

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: Category.name, default: 'Category'), categoryInstance.id])
                redirect categoryInstance
            }
            '*' { respond categoryInstance, [status: OK] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def delete(Category categoryInstance) {

        if (categoryInstance == null) {
            notFound()
            return
        }

        //categoryInstance.delete flush: true
        categoryService.deleteCategory(categoryInstance)

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: Category.name, default: 'Category'), categoryInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: categoryInstance.name, default: 'Category'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    protected boolean verifyUserPrivilege(){
        User user = (User) springSecurityService.currentUser
        if ( user.getUsername() == 'admin'){
            flash.message = 'admin'
            return true
        }
        false

    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def subscribe() {
        User user = springSecurityService.currentUser
        render(view: '/category/subscribe', model: [user: user])
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def doSubscribe() {
        User user = springSecurityService.currentUser

        user.categories.clear()
        user.addToCategories(Category.findAll().first())

        params.categories.each(){
            def Category category = Category.get(it)
            user.addToCategories(category)
        }

        user.save()

        println user.categories

        render(view: '/userManagement/home', model: [user: user])
    }
}
