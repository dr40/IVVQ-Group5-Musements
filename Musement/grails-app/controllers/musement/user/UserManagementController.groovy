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
        render(view: '/userManagement/register', model: [user: new User(params)])
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

            if (params.list("cats"))
                params.list("cats").each { selectedCategories.add(it) }

            // Add default category
            selectedCategories.add("Musement")
            selectedCategories.each {
                Category cat = Category.findByName(it.toString())
                if (cat.validate())
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
    }

    /**
     * Delete users own account
     * @return Logout page
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def unregister() {
        userAccountService.deleteUser(springSecurityService.currentUser)
        redirect controller: 'logout'
    }

    /**
     * Method for updating current user password
     * @return  The current user updated
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def update() {
        flash.clear()
        User user = (User) springSecurityService.getCurrentUser()

        // If the request is not POST, return
        if (!request.post) {
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
            flash.info = message(code: 'musement.user.update.success')

            render(view: '/userManagement/home', model: [user: user])
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
     * @return Reloads the Control Panel controller
     */
    @Secured(['ROLE_ADMIN'])
    def deleteUser() {
        if (params.containsKey("userId")) {
            User user = User.findById(params.userId)

            if (user.validate()) {
                User currentUser = springSecurityService.currentUser

                if (currentUser.equals(user)) {
                    flash.message = message(code: "musement.control.panel.users.cannot.delete")
                    redirect( controller: "controlPanel", action: "index", params:[editMode: "user"])
                    return;
                } else {
                    // Finally delete the user
                    userAccountService.deleteUser(user)

                    flash.info = message(code: "musement.control.panel.users.deleted")
                    redirect( controller: "controlPanel", action: "index", params:[editMode: "user"])
                    return;
                }
            }
        }

        // If we got this point, we encountered errors
        flash.message = message(code: "musement.control.panel.users.null")
        redirect( controller: "controlPanel", action: "index", params:[editMode: "user"])
    }

    /**
     * The default admin can give the ROLE_ADMIN to other users also
     * @param userId The ID of the user to be made admin
     * @return
     */
    @Secured(['ROLE_ADMIN'])
    def makeAdmin() {
        if (!params.containsKey('userId')) {
            flash.message = message(code: "musement.control.panel.users.null")
            redirect( controller: "controlPanel", action: "index", params:[editMode: "user"])
            return
        }

        User user = User.findById(params.getLong('userId'))

        if (!user.validate()) {
            flash.message = message(code: "musement.control.panel.users.null")
            redirect( controller: "controlPanel", action: "index", params:[editMode: "user"])
            return
        }

        userAccountService.updateUser(user, Roles.ROLE_ADMIN.role)

        flash.info = message(code: "musement.control.panel.users.admin.made", args: [user.username])
        redirect( controller: "controlPanel", action: "index", params:[editMode: "user", userId: user.id])
    }

    /**
     * Remove admin rights from a user
     * @param userId The ID of the user to be demoted
     */
    @Secured(['ROLE_ADMIN'])
    def removeAdmin() {
        if (!params.containsKey('userId')) {
            flash.message = message(code: "musement.control.panel.users.null")
            redirect( controller: "controlPanel", action: "index", params:[editMode: "user"])
            return
        }

        User user = User.findById(params.getLong('userId'))

        if (!user.validate()) {
            flash.message = message(code: "musement.control.panel.users.null")
            redirect( controller: "controlPanel", action: "index", params:[editMode: "user"])
        }

        // Redirect in case of own demotion
        userAccountService.updateUser(user, Roles.ROLE_USER.role)
        User currentUser = springSecurityService.currentUser
        if (currentUser.equals(user)) {
            springSecurityService.reauthenticate user.username

            flash.info = message(code: "musement.control.panel.users.admin.removed", args: [user.username])
            redirect(controller: 'userManagement', action: 'home')
            return
        }

        flash.info = message(code: "musement.control.panel.users.admin.removed", args: [user.username])
        redirect( controller: "controlPanel", action: "index", params:[editMode: "user", userId: user.id])
    }
}