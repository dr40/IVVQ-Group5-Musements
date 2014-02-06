package musement

import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import musement.user.User

@Transactional
class NotificationService {
    SpringSecurityService springSecurityService


    Notification updateNotification(Notification notification){
        notification.save()
        notification
    }

    void deleteNotification(Notification notification){
        notification.delete()
    }

    void Notify(Post post){

        User.findAllByCategories(post.category).each {user ->
            if(!user.equals(post.sender))
            user.notification.addToPosts(post).save() }
    }

}
