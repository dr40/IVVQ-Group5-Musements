package musement.user

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ControlPanelController)
class ControlPanelControllerSpec extends Specification {


    void "Index called"() {
        given:"some mocked params"
        params.categoryId = 1
        params.userId = 1
        params.editMode = 'category'

        when: "index called"
        controller.index()

        then:"send to"
        view == '/controlPanel/index'
        params.editMode == "category"
        params.categoryId == 1
        params.userId ==1
    }

    void "Manage categories page"(){
        given:"some mocked params"
        params.categoryId = 5

        when:"click on manage categories"
        controller.manageCategories()

        then:"go to categories page"
        view == '/controlPanel/manageCategories'


    }
    void "Manage users page"(){
        given:"some mocked params"
        params.userId = 5

        when:"click on manage categories"
        controller.manageUsers()

        then:"go to users page"
        view == '/controlPanel/manageUsers'
    }


}
