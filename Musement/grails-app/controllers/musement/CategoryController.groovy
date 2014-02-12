package musement

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import musement.user.User

class CategoryController {

    static allowedMethods = [save: "POST", update: "PUT"]

    SpringSecurityService springSecurityService
    CategoryService categoryService

    /**
     * Method called when the admin wants to add a category
     * @return  The create view with the corresponding model
     */
    @Secured(['ROLE_ADMIN'])
    def create() {
        def categoryInstance = new Category(params)
        render(view: "/category/create", model: [categoryInstance: categoryInstance])
    }

    /**
     * Category creation form submit action
     * @param categoryInstance The model passed by the form
     */
    @Secured(['ROLE_ADMIN'])
    def save(Category categoryInstance) {
        // Check allowed request method
        if (!request.post) {
            return [categoryInstance: categoryInstance]
        }

        // If the passed instance is invalid redirect to source controller and action
        if (!categoryInstance) {
            flash.message = message(code: 'default.not.found.message')
            redirect(controller: 'userManagement', action: 'home')
            return
        }

        categoryInstance = categoryService.addCategory(categoryInstance)

        // If there are errors, show them in the view
        if (categoryInstance.hasErrors()) {
            respond(view: "/category/create", categoryInstance.errors)
            return
        }

        redirect(controller: 'userManagement', action: 'home', params: [categoryId: categoryInstance.id])
    }

    /**
     * Method called for loading the edit view
     * @param categoryInstance The category that will be edited
     * @return The edit view with the corresponding model
     */
    @Secured(['ROLE_ADMIN'])
    def edit(Category categoryInstance) {
        render(view: "/category/edit", model: [categoryInstance: categoryInstance])
    }

    /**
     * Edit form submit action
     * @param categoryInstance  The category modified
     */
    @Secured(['ROLE_ADMIN'])
    def update(Category categoryInstance) {
        // Check allowed method
        if (!request.method.equals("PUT")) {
            return [categoryInstance: categoryInstance]
        }

        // If the passed instance is invalid redirect to source controller and action
        if (!categoryInstance) {
            flash.message = message(code: 'default.not.found.message')
            redirect(controller: "controlPanel", action: "index", params: [editMode: "category"])
            return
        }

        categoryInstance = categoryService.updateCategory(categoryInstance)

        // If there are errors, show them in the view
        if (categoryInstance.hasErrors()) {
            respond(view: "/category/edit", categoryInstance.errors)
            return
        }

        flash.info = message(code: "musement.category.update.success")
        redirect(controller: "controlPanel", action: "index", params: [editMode: "category", categoryId: categoryInstance.id])
    }

    /**
     * Method used to show the subscribe page
     * @return The view with the corresponding model
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def subscribe() {
        User user = springSecurityService.getCurrentUser()
        render(view: "/category/subscribe", model: [user: user])
    }

    /**
     * Subscribe form submit action
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def doSubscribe() {
        User user = springSecurityService.getCurrentUser()

        List selectedCategories = new ArrayList()

        // Get the selected categories
        if (params.list("categories"))
            selectedCategories.addAll(params.list("categories"))

        // Add default category
        selectedCategories.add("Musement")

        // In case user un-subscribed from all categories
        if (user.categories) {
            user.categories.clear()
        } else {
            // This should never happen. All users should be subscribed to Musement
            return [user: user]
        }

        selectedCategories.each {
            Category cat = Category.findByName(it)

            if (cat)
                user.addToCategories(cat)
        }

        redirect(controller: 'userManagement', action: 'home')
    }

    /**
     * Method called when the admin wants to delete a category
     */
    @Secured(['ROLE_ADMIN'])
    def deleteCategory() {
        if (params.containsKey("categoryId")) {
            Category cat = Category.findById(params.getLong("categoryId"))

            if (cat) {
                if (cat.name.equals("Musement")) {
                    flash.info = message(code: "musement.control.panel.categories.cannot.delete", default: "Cannot delete Musement category")
                    redirect(controller: "controlPanel", action: "index", params: [editMode: "category"])
                } else {
                    categoryService.deleteCategory(cat)

                    flash.info = message(code: "musement.control.panel.categories.deleted", default: "Category deleted")
                    redirect(controller: "controlPanel", action: "index", params: [editMode: "category"])
                }
            } else {
                flash.message = message(code: 'default.not.found.message')
                redirect(controller: "controlPanel", action: "index", params: [editMode: "category"])
            }
        } else {
            flash.message = message(code: "musement.control.panel.categories.null", default: "Category not found or null")
            redirect(controller: "controlPanel", action: "index", params: [editMode: "category"])
        }
    }
}