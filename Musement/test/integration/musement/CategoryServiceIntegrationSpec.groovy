package musement

import grails.test.spock.IntegrationSpec
import musement.user.Roles
import musement.user.User
import musement.user.UserAccountService
import spock.lang.Unroll

/**
 * Tests for the CategoryService.
 * They are meant to ensure that all functions for the User domain function accordingly.
 */
class CategoryServiceIntegrationSpec extends IntegrationSpec {

    /** Services **/
    BootStrapService bootStrapService
    UserAccountService userAccountService
    PostService postService
    CategoryService categoryService

    void "test add category, without admins"() {
        when: "the service is called"
        categoryService.addCategory(new Category(name: "Test", description: "Description"))

        then: "the category is saved"
        Category category = Category.findByName("Test")
        category != null
        category.name == "Test"
        category.description == "Description"
    }

    void "test add category, with admins"() {
        given: "an admin"
        bootStrapService.initializeRoles()
        bootStrapService.initializeDefaultCategory()
        bootStrapService.initializeDefaultUsers()

        when: "the service is called"
        categoryService.addCategory(new Category(name: "Test", description: "Description"))

        then: "the category is saved"
        Category category = Category.findByName("Test")
        category != null
        category.name == "Test"
        category.description == "Description"
        User admin = User.findByUsername("admin")
        admin.categories.contains(category)
    }

    void "test add category, category errors with admins "() {
        given: "an admin"
        bootStrapService.initializeRoles()
        bootStrapService.initializeDefaultCategory()
        bootStrapService.initializeDefaultUsers()

        when: "the service is called"
        categoryService.addCategory(new Category(name: "12345", description: "Description"))

        then: "the category is saved"
        Category category = Category.findByName("12345")
        category == null
        User admin = User.findByUsername("admin")
        !admin.categories.contains(category)
    }

    @Unroll
    def "test update category - description"() {
        given: "a category"
        Category category = categoryService.addCategory(new Category(name: "Category", description: "Description"))

        when: 'modify description, then update'
        category.description = newDescription
        categoryService.updateCategory(category)

        then: 'the category is modified properly'
        Category cat = Category.findByName("Category")
        cat != null
        cat.hasErrors() == catHasErrors

        where:
        newDescription      | catHasErrors
        null                | true
        ""                  | true
        "someDescription"   | false
    }

    void "test delete category with users and posts"() {
        given: "a category, a user, and a post"
        bootStrapService.initializeRoles()
        User user = userAccountService.addUser(new User(username: "test", email: "test@musement.com", password: "test", notification: new Notification()),
                Roles.ROLE_USER.role)
        Category category = categoryService.addCategory(new Category(name: "Category", description: "Description"))
        user.addToCategories(category)
        Long postId = postService.sendPost(user, "Post", category).id

        when: "delete is called"
        categoryService.deleteCategory(category)

        then: "category deleted, post deleted, user un-subscribed"
        !Category.findByName("Category")
        !Post.findById(postId)
        user.categories.size() == 0
    }

    void "test delete category with users and posts without flush"() {
        given: "a category, a user, and a post"
        bootStrapService.initializeRoles()
        User user = userAccountService.addUser(new User(username: "test", email: "test@musement.com", password: "test", notification: new Notification()),
                Roles.ROLE_USER.role)
        Category category = categoryService.addCategory(new Category(name: "Category", description: "Description"))
        user.addToCategories(category)
        Long postId = postService.sendPost(user, "Post", category).id

        when: "delete is called"
        categoryService.deleteCategory(category, false)

        then: "category deleted, post deleted, user un-subscribed"
        !Category.findByName("Category")
        !Post.findById(postId)
        user.categories.size() == 0
    }

}
