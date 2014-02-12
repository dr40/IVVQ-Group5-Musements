package musement

import grails.transaction.Transactional
import musement.user.User

import static org.springframework.http.HttpStatus.*
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured

class NotificationController {
    /** Services*/
    SpringSecurityService springSecurityService
    NotificationService notificationService

    /**
     * Method for counting the number of notifications, for an user
     * @return The number of notifications(posts made by others on the categories that the user is fallowing)
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    @Transactional
    def notificationsNumber(){
        //The logged user
        User currentUser =  (User)springSecurityService.getCurrentUser()

        def listPosts
        def listCat = []
        if(currentUser.notification.posts!=null){
            listPosts = currentUser.notification.posts*.getCategory()
            //We count only one post per category
            for(list in listPosts){
                if(!listCat.contains(list))
                    listCat.add(list)
            }
            [notificationsNumber : listCat.size()]
        }else
            [notificationsNumber: 0]
    }

    /**
     *Method for showing a notification to  user
     * @return List of notifications
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    @Transactional
    def showNotifications(){
        User currentUser =  (User)springSecurityService.getCurrentUser() //Logged user
        def map = [:] // Saves the category and the number of posts in that category
        def map_user = [:] // Saves the Sender for the categories where is one post
        List notifications = [] // The notifications shown for the user

        if(currentUser.notification.posts != null){
            for (post in currentUser.notification.posts){
                if(!map.containsKey(post.getCategory().name)){
                    map.put(post.getCategory().name,1)
                    map_user.put(post.getCategory().name, post.getSender().username)
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
        else
            [notifications: "None"]

    }

}




