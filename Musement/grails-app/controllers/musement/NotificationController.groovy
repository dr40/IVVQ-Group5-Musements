package musement

import musement.user.User

import static org.springframework.http.HttpStatus.*
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured

class NotificationController {

    SpringSecurityService springSecurityService
    NotificationService notificationService

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def notificationsNumber(){
        User currentUser =  (User)springSecurityService.getCurrentUser()
        def listPosts = currentUser.notification.posts*.getCategory()
        def listCat = []
        for(list in listPosts)
            if(!listCat.contains(list))
                listCat.add(list)
        [notificationsNumber : listCat.size()]
    }


    @Secured(['IS_AUTHENTICATED_FULLY'])
    def showNotifications(){
        User currentUser =  (User)springSecurityService.getCurrentUser()
        def map = [:]
        def map_user = [:]
        List notifications = []
        for (post in currentUser.notification.posts){
            if(!map.containsKey(post.getCategory().name)){
                map.put(post.getCategory().name,1)
                map_user.put(post.getCategory().name, post.sender)
            }
            else
                map[post.getCategory().name] = map.get(post.getCategory().name) +1
        }

        for (entrance in map){
            if (entrance.value == 1)
                notifications.add([category: Category.findByName(entrance.key), post_count: entrance.value, sender: User.findByUsername(map_user.get(entrance.getKey()))])
            else
                notifications.add([category: Category.findByName(entrance.key), post_count: entrance.value])

        }
        [notifications: notifications]

    }
    }




