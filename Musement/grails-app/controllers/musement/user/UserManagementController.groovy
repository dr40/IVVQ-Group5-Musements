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
        User user = new User(params)
        render(view: '/userManagement/register', model: [user: user])
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
                selectedCategories.addAll(params.list("cats"))

            // Add default category
            selectedCategories.add("Musement")
            selectedCategories.each {
                Category cat = Category.findByName(it)

                if (cat)
                    user.addToCategories(cat)
            }

            user = userAccountService.addUser(user, normalRole, true)
        } else {
            user.errors.rejectValue('password', 'musement.user.password.match')
        }

        if (!user || user.hasErrors()) {
            render(view: '/userManagement/register', model: [user: user])
        } else {
            flash.info = message(code: 'musement.user.register.success')
            redirect(controller: "login", action: "auth")
        }
    }

    /**
     * Delete users own account
     * @return Logout page
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def unregister() {
        userAccountService.deleteUser(springSecurityService.getCurrentUser())
        redirect(controller: "logout")
    }

    /**
     * Method for loading the forms for updating current user's password or email
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def update() {
        flash.clear()
        render(view: '/userManagement/update', model: [user: springSecurityService.getCurrentUser()])
    }

    /**
     * Method for updating the current user's password
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def updatePassword() {
        User user = springSecurityService.getCurrentUser()

        // If the request is not POST, return
        if (!request.post) {
            return [user: user]
        }

        if (params.password2 == params.password3) {
            if (params.password && params.password2 && springSecurityService.passwordEncoder.isPasswordValid(user.password, params.password, null)) {
                // Change password only if old password was verified
                user = userAccountService.updatePasswordForUser(params.password2, user)
            } else {
                user.errors.rejectValue('password', 'musement.user.password.current.match')
            }
        } else {
            user.errors.rejectValue('password', 'musement.user.password.match')
        }

        if (!user || user.hasErrors()) {
            render(view: '/userManagement/update', model: [user: user])
        } else {
            springSecurityService.reauthenticate user.username
            flash.info = message(code: 'musement.user.update.password.success')
            render(view: '/userManagement/home', model: [user: user])
        }
    }

    /**
     * Method for updating the current user's email
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def updateEmail() {
        User user = springSecurityService.getCurrentUser()

        // If the request is not POST, return
        if (!request.post) {
            return [user: user]
        }

        // Verify the current password and then update
        if (params.password4 && springSecurityService.passwordEncoder.isPasswordValid(user.password, params.password4, null)) {
            user.email = params.email
            user = userAccountService.updateUser(user, user.getAuthorities().getAt(0))
        } else {
            user.errors.rejectValue('password', 'musement.user.password.current.match')
        }

        if (!user || user.hasErrors()) {
            render(view: '/userManagement/update', model: [user: user])
        } else {
            flash.info = message(code: 'musement.user.update.email.success')
            render(view: '/userManagement/home', model: [user: user])
        }
    }


    /**
     * Load the home view for the authenticated user
     * @return
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def home() {
        User user = springSecurityService.getCurrentUser()

        def catId = (params.containsKey('categoryId') ?  params.categoryId : Category.findByName("Musement").id)

        // Verify if the user clicked on a Notification
        if (params.containsKey('readPost')) {
            notificationService.readNotification(user, Category.findById(catId))
        }

        render(view: 'home', model: [user: user, categoryId: catId])
    }

    /**
     * Method for deleting an user. Should only be used by admin
     * @return Reloads the Control Panel controller
     */
    @Secured(['ROLE_ADMIN'])
    def deleteUser() {
        if (params.containsKey("userId")) {
            User user = User.findById(params.getLong('userId'))

            if (user && user.validate()) {
                User currentUser = springSecurityService.getCurrentUser()

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

        if (!user || !user.validate()) {
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

        // Do not allow if there is only one admin left
        if (UserRole.findAllByRole(Roles.ROLE_ADMIN.role).size() == 1) {
            flash.message = message(code: "musement.control.panel.users.oneadmin")
            redirect( controller: "controlPanel", action: "index", params:[editMode: "user"])
            return
        }

        User user = User.findById(params.getLong('userId'))

        if (!user || !user.validate()) {
            flash.message = message(code: "musement.control.panel.users.null")
            redirect( controller: "controlPanel", action: "index", params:[editMode: "user"])
            return
        }

        // Redirect in case of own demotion
        userAccountService.updateUser(user, Roles.ROLE_USER.role)
        User currentUser = springSecurityService.getCurrentUser()
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