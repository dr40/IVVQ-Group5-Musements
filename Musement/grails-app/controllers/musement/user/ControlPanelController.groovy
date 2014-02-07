package musement.user

import grails.plugin.springsecurity.annotation.Secured
import musement.Category
import musement.CategoryController
import musement.CategoryService

/**
 *  Class dedicated to the Admin Control Panel
 */
@Secured(['ROLE_ADMIN'])
class ControlPanelController {
    CategoryService categoryService
    /**
     * Load Control Panel main view. By default in category edit mode.
     */
    def index() {
        def catId = (params.containsKey('categoryId') ?  params.categoryId : 1)
        def userId = (params.containsKey('userId') ?  params.userId : 1)
        def editMode = (params.containsKey('editMode') ?  params.editMode : 'category')

        render(view: '/controlPanel/index', params:[editMode: editMode, categoryId: catId, userId: userId])
    }

    def manageCategories() {
        def catId = (params.containsKey('categoryId') ?  params.categoryId : 1)

        render(view: '/controlPanel/manageCategories', params:[categoryId: catId])
    }

    def manageUsers() {
        def userId = (params.containsKey('userId') ?  params.userId : 1)

        render(view: '/controlPanel/manageUsers', params:[ userId: userId])
    }

   def deleteCategories(){

       def catId = (params.containsKey('categoryId') ?  params.categoryId : 1)
       def userId = (params.containsKey('userId') ?  params.userId : 1)
       def editMode = (params.containsKey('editMode') ?  params.editMode : 'category')
       Category c = Category.findById(params.get("categoryId"))
       if(c.validate())
           categoryService.deleteCategory(c)
       render(view: '/controlPanel/index', params:[editMode: editMode, categoryId: catId, userId: userId])

    }
}
