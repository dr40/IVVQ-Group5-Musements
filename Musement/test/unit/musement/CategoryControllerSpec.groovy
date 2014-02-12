package musement

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import musement.user.Role
import musement.user.User
import musement.user.UserRole
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@Mock([Role, User, UserRole, Category, Notification])
@TestFor(CategoryController)
class CategoryControllerSpec extends Specification {

    /** Services **/
    SpringSecurityService springSecurityService
    CategoryService categoryService

    User user
    Category category

    def setup() {
        user = new User(username: "test", email: "test@musemnt.com", password: "password", notification: Mock(Notification))
        category = new Category(name: "Musement", description: "test")

        // Category Service
        categoryService = Mock(CategoryService)
        controller.categoryService = categoryService

        // SpringSecurityService
        springSecurityService = Mock(SpringSecurityService)
        springSecurityService.currentUser >> user
        controller.springSecurityService = springSecurityService
    }

    void "test create page"() {
        when: "the create page is called"
        controller.create()

        then: "show create form/view"
        view == "/category/create"
        model.categoryInstance != null
    }

    void "test edit page"() {
        given: "a category instance"
        def categoryInstance = Mock(Category)

        when: "the edit page is called"
        controller.edit(categoryInstance)

        then: "show edit form/view"
        view == "/category/edit"
        model.categoryInstance == categoryInstance
    }

    void "test subscribe page"() {
        when: "the subscribe page is called"
        controller.subscribe()

        then: "show subscribe form/view"
        view == "/category/subscribe"
        model.user == user
    }

    void "test save - post method only"() {
        given: "a category and a different method"
        Category categoryInstance = Mock(Category)
        request.method = "PUT"

        when: "calling method with a type other than POST"
        Map map = (Map) controller.save(categoryInstance)

        then: "service should not be called"
        0 * categoryService.addCategory((Category) _)

        then: "it should return the category with no view"
        map.categoryInstance == categoryInstance
        view == null
    }

    void "test update - put method only"() {
        given: "a category and a different method"
        Category categoryInstance = Mock(Category)
        request.method = "POST"

        when: "calling method with a type other than PUT"
        Map map = (Map) controller.update(categoryInstance)

        then: "service should not be called"
        0 * categoryService.updateCategory((Category) _)

        then: "it should return the category with no view"
        map.categoryInstance == categoryInstance
        view == null
    }

    void "test save - null parameter"() {
        given: "the proper way of calling the method"
        request.method = "POST"

        when: "calling method with null parameter"
        controller.save(null)

        then: "service should not be called"
        0 * categoryService.addCategory((Category) _)

        then: "the proper view and model are loaded"
        flash.message != null
        response.redirectedUrl == '/userManagement/home'
    }

    void "test update - null parameter"() {
        given: "the proper way of calling the method"
        request.method = "PUT"

        when: "calling method with null parameter"
        controller.update(null)

        then: "service should not be called"
        0 * categoryService.updateCategory((Category) _)

        then: "the proper view and model are loaded"
        flash.message != null
        response.redirectedUrl == '/controlPanel/index?editMode=category'
    }

    void "test save - service error" () {
        given: "a rejected category"
        category.errors.rejectValue('description', null)

        and: "the right request method"
        request.method = "POST"

        when: "register form was submitted"
        controller.save(category)

        then: "service should be called once"
        1 * categoryService.addCategory((Category) _) >> category

        then: "show the form with corresponding errors and valid params"
        view == "/category/create"
        model.categoryInstance == category
    }

    void "test update - service error" () {
        given: "a rejected category"
        category.errors.rejectValue('description', null)

        and: "the right request method"
        request.method = "PUT"

        when: "register form was submitted"
        controller.update(category)

        then: "service should be called once"
        1 * categoryService.updateCategory((Category) _) >> category

        then: "show the form with corresponding errors and valid params"
        view == "/category/edit"
        model.categoryInstance == category
    }

    void "test save - success" () {
        given: "a valid category and the right request method"
        request.method = "POST"

        when: "register form was submitted"
        controller.save(category)

        then: "service should be called once"
        1 * categoryService.addCategory((Category) _) >> category

        then: "show the form with corresponding errors and valid params"
        response.redirectedUrl == '/userManagement/home?categoryId='
    }

    void "test update - success" () {
        given: "a valid category and the right request method"
        request.method = "PUT"

        when: "register form was submitted"
        controller.update(category)

        then: "service should be called once"
        1 * categoryService.updateCategory((Category) _) >> category

        then: "the proper view and model are loaded"
        flash.info != null
        response.redirectedUrl == '/controlPanel/index?editMode=category&categoryId='
    }

    void "test doSubscribe - user has no categories"() {
        when: "the subscribe form is submitted"
        Map map = (Map) controller.doSubscribe()

        then: "the proper view and model are loaded"
        map.user == user
        view == null
    }

    void "test doSubscribe - empty params, no Musement category"() {
        given: "the current user with a category"
        List cats = new ArrayList()
        Category cat = Mock(Category)
        cats.add(cat)
        user.categories = cats

        when: "the subscribe form is submitted"
        controller.doSubscribe()

        then: "the proper view and model are loaded"
        response.redirectedUrl == "/userManagement/home"
        user.categories.size() == 0
    }

    void "test doSubscribe - empty params, with Musement category"() {
        given: "the current user with a category"
        category.save()
        List cats = new ArrayList()
        cats.add(category)
        user.categories = cats

        when: "the subscribe form is submitted"
        controller.doSubscribe()

        then: "the proper view and model are loaded"
        response.redirectedUrl == "/userManagement/home"
        user.categories.size() == 1
    }

    void "test doSubscribe - success"() {
        given: "the current user with a category"
        category.save()
        List cats = new ArrayList()
        cats.add(category)
        user.categories = cats

        and: "the params"
        Category test = new Category(name: "Test", description: "Test").save()
        params.categories = ["Test", "InexistentCategory"]

        when: "the subscribe form is submitted"
        controller.doSubscribe()

        then: "the proper view and model are loaded"
        response.redirectedUrl == "/userManagement/home"
        user.categories.size() == 2
    }

    void "test deleteCategory - empty params"() {
        when: "admin wants to delete a category"
        controller.deleteCategory()

        then: "service should not be called"
        0 * categoryService.deleteCategory((Category) _)

        then: "the corresponding view and model are loaded"
        flash.message != null
        response.redirectedUrl == "/controlPanel/index?editMode=category"
    }

    void "test deleteCategory - invalid params"() {
        given: "an invalid category id"
        params.categoryId = 1

        when: "admin wants to delete a category"
        controller.deleteCategory()

        then: "service should not be called"
        0 * categoryService.deleteCategory((Category) _)

        then: "the corresponding view and model are loaded"
        flash.message != null
        response.redirectedUrl == "/controlPanel/index?editMode=category"
    }

    void "test deleteCategory - id of Musement category"() {
        given: "the id of Musement"
        Category.metaClass.static.findById = { Long id -> category }
        params.categoryId = 13

        when: "admin wants to delete a category"
        controller.deleteCategory()

        then: "service should not be called"
        0 * categoryService.deleteCategory((Category) _)

        then: "the corresponding view and model are loaded"
        flash.info != null
        response.redirectedUrl == "/controlPanel/index?editMode=category"
    }

    void "test deleteCategory - success"() {
        given: "the id of another category"
        Category cat = new Category(name: "Test", description: "Test")
        Category.metaClass.static.findById = { Long id -> cat }
        params.categoryId = 13

        when: "admin wants to delete a category"
        controller.deleteCategory()

        then: "service should be called once"
        1 * categoryService.deleteCategory((Category) _)

        then: "the corresponding view and model are loaded"
        flash.info != null
        response.redirectedUrl == "/controlPanel/index?editMode=category"
    }
}
