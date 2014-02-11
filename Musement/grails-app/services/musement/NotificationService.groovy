package musement

import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import musement.user.User

@Transactional
class NotificationService {
    SpringSecurityService springSecurityService


    void readNotification(User user, Category category) {
        def n = user.notification;
        if (n.validate()) {
            def postToRemove = []
                n.posts.each { p ->
                    if (p.getCategory().equals(category)) {
                        postToRemove.add(p);
                    }
                }
                for(Post p : postToRemove) {
                    n.posts.remove (p);
                }
                n.save flush: true
        }
    }

    void notifyUsers(Post post){

        if (post.category.validate()) {
            post.category.users.each {u ->
                if(!u.equals(post.sender))
                    u.notification.addToPosts(post).save(flush:true)
            }
        }


    }
}

