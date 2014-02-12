package musement

import grails.transaction.Transactional
import musement.user.User
import musement.user.UserAccountService

@Transactional
class CategoryService {

    PostService postService;
    UserAccountService userAccountService

    /**
     * Save category and subscribe admins to it
     * @param category  The category to save
     * @return  The category saved
     */
    Category addCategory(Category category ) {
        category.save()

        // Admins automatically registered to all valid categories
        if (!category.hasErrors())
            userAccountService.updateAdminCategories(category)

        category
    }

    /**
     * Update a category's description
     * @param category Instance to be updated
     * @return  Modified instance
     */
    Category updateCategory(Category category ) {
        category.save()
        category
    }

    /**
     * Delete a category, the posts and un-subscribe users
     * @param category  The instance to be deleted
     * @param flush
     */
    void deleteCategory(Category category, boolean flush = true) {
        /* Delete category from all users subscribed */
        User.list().each {u ->
            def catToRemove = [];
            u.categories.each {c ->
                if (c.id == category.id) {
                    catToRemove.add(c);
                }
            }
            if (catToRemove.size() > 0) {
                for(Category c : catToRemove) {
                    u.categories.remove (c);
                }
                u.save()
            }
        }

        /* Delete all post from the category */
        Post.findAllByCategory(category).each { p ->
            postService.deletePost(p, false)
        }

        /* Delete category and flush if needed */
        if (flush) {
            category.delete(flush: true)
        } else {
            category.delete()
        }
    }
}