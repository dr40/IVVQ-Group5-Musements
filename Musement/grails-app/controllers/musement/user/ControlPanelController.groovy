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
    /** Services **/
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

    /**
     * Load Control Panel ManageCategories view.
     * @return Load Category page.
     */
    def manageCategories() {
        def catId = (params.containsKey('categoryId') ?  params.categoryId : 1)
        render(view: '/controlPanel/manageCategories', params:[categoryId: catId])
    }

    /**
     * Load Control Panel ManageUsers view
     * @return Load User page.
     */
    def manageUsers() {
        def userId = (params.containsKey('userId') ?  params.userId : 1)

        render(view: '/controlPanel/manageUsers', params:[ userId: userId])
    }
}
