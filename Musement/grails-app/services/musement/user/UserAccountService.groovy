package musement.user

import grails.transaction.Transactional
import musement.Category

@Transactional
class UserAccountService {
    /**
     * Add a new user, the user is not enabled
     * @param enabled flag that indicate if the account is enabled
     * @param mainRole the main role for the user
     * @param user the user to be added
     * @return the created user
     */
    User addUser(User user, Role mainRole, Boolean enabled = true) {
        user.enabled = enabled
        user.save()
        if (!user.hasErrors()) {
            UserRole.create(user, mainRole, enabled)
        }
        user
    }

    /**
     * Update the user
     * @param user the user
     * @param mainRole the main role
     * @return the updated user
     */
    User updateUser(User user, Role mainRole) {
        user.save()
        if (!user.hasErrors() && !UserRole.get(user.id, mainRole.id)) {
            UserRole.removeAll(user)
            UserRole.create(user, mainRole)
        }
        user
    }

    /**
     * Each time a category is created (by admin only), it will be added
     * as being subscribed to all admins
     * @param category
     */
    void updateAdminCategories(Category category) {
        def admins = UserRole.findAllByRole(Roles.ROLE_ADMIN.role)

        if (admins && admins.size() > 0) {
            admins.each { admin ->
                if (admin.user.validate()) {
                    admin.user.addToCategories(category)
                    admin.user.save(flush: true)
                }
            }
        }
    }

    /**
     *
     * @param newPassword the new password
     * @param user the user
     * @return the processed user
     */
    User updatePasswordForUser(String newPassword, User user) {
        user.password = newPassword
        user.save()
        user
    }

    /**
     * Deletes a user account
     * @param user
     */
    void deleteUser(User user) {
        UserRole.removeAll(user)
        user.delete()
    }
}