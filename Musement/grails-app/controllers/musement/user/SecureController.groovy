package musement.user

import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService

class SecureController {

    SpringSecurityService springSecurityService

    @Secured(['permitAll'])
    def anonymous() {
        render 'Public page'
    }

    @Secured(['ROLE_ADMIN'])
    def authenticated() {
        User user = (User) springSecurityService.currentUser
        render 'Secure access only. Authenticated user: ' + user.username
    }

}
