package musement

import grails.transaction.Transactional
import musement.user.User
import musement.user.UserAccountService

@Transactional
class CategoryService {

    PostService postService;
    UserAccountService userAccountService

    List list() {
        return Category.list()
    }

    Category updateCategory(Category category ) {
        category.save()
        category
    }

    Category addCategory(Category category ) {
        category.save()

        // Admin automatically registered to all categories
        userAccountService.updateAdminCategories(category)

        category
    }
    void deleteCategory(Category category, boolean flush = true ) {
        /* Can't delete "Musement" category */
        if (category.id == 1) return;
        /* Delete category of all users */
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
            postService.deletePost(p, flush:false)
        }
        /* Delete category and flush if needed */
        if (flush) {
            category.delete(flush: true)
        } else {
            category.delete()
        }
    }
}