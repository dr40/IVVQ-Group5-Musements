package musement.user

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import musement.Notification
import musement.Category
import musement.NotificationService

class UserManagementController {

    /** Services **/
    SpringSecurityService springSecurityService
    NotificationService notificationService

    UserAccountService userAccountService

    /**
     * Loads the register view. Necessary because the action cannot be done for the view also.
     * @return
     */
    @Secured(['ROLE_ANONYMOUS'])
    def register() {
        render(view: '/userManagement/register')
    }

    /**
     * Method for user registration into the system
     * @return  The user created, if successful
     */
    @Secured(['ROLE_ANONYMOUS'])
    def doRegister() {
        // Normal User Role
        Role normalRole = Roles.ROLE_USER.role

        // Each User has his own Notification object (one-to-one)
        Notification notification = new Notification()
        params.notification = notification

        // The user to register
        User user = new User(params)

        if (params.password == params.password2) {
            // Get selected categories
            List selectedCategories = new ArrayList()

            if (params.list("categories"))
                params.list("categories").each { selectedCategories.add(it) }

            // Add default category
            selectedCategories.add("Musement")
            selectedCategories.each {
                Category cat = Category.findByName(it.toString())
                if (cat)
                    user.addToCategories(cat)
            }

            user = userAccountService.addUser(user, normalRole, true)
        } else {
            user.errors.rejectValue('password', 'musement.user.password.match')
        }

        if (user.hasErrors()) {
            render(view: '/userManagement/register', model: [user: user])
        } else {
            redirect uri: '/login/auth'
            flash.info = message(code: 'musement.user.register.success')
        }

        println 'Registered user: ' + user.username + ' ' + params.categories
        if (user.categories) {
            println ' With categories: ' + user.categories.size()
            user.categories.each {println it.name}
        }
    }

    /**
     * Method for updating current user password
     * @return  The current user updated
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def update() {
        flash.clear()
        User user = (User) springSecurityService.getCurrentUser()

        if (!request.post) {
            // display default view
            return [user: user]
        }

        if (params.password3 != params.password2) {
            user.errors.rejectValue('password', 'musement.user.password.match')
        } else {
            // Verify the current password and then update
            if (springSecurityService.passwordEncoder.isPasswordValid(user.password, params.password, null)) {
                user = userAccountService.updatePasswordForUser(params.password2, user)
            } else {
                user.errors.rejectValue('password', 'musement.user.password.current.match')
            }
        }

        if (user.hasErrors()) {
            render(view: '/userManagement/update', model: [user: user])
        } else {
            springSecurityService.reauthenticate user.username
            render(view: '/userManagement/home', model: [user: user])
            flash.info = message(code: 'musement.user.update.success')
        }
    }

    /**
     * Load the home view for the authenticated user
     * @return
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def home() {
        def catId = (params.containsKey('categoryId') ?  params.categoryId : Category.findByName("Musement").id)

        // Verify if the user clicked on a Notification
        if (params.containsKey('readPost')) {
            notificationService.readCategory(springSecurityService.currentUser, Category.findById(catId))
        }

        render(view: 'home', model: [user: springSecurityService.currentUser, categoryId: catId])
    }

    /**
     * Method for deleting an user. Should only be used by admin
     * @param user  The user to be deleted
     */
    @Secured(['ROLE_ADMIN'])
    def deleteUser(User user) {
        userAccountService.deleteUser(user)
        redirect controller: 'logout'
    }
}
