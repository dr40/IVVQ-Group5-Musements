package musement

import grails.transaction.Transactional

@Transactional
class CategoryService {

    List list() {
        return Category.list()
    }

    Category addCategory(Category category ) {
        category.save()
        category
    }

    Category updateCategory(Category category ) {
        category.save()
        category
    }

    void deleteCategory(Category category ) {
        category.delete()
    }
}
