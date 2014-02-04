package musement

import grails.transaction.Transactional

@Transactional
class NotificationService {

    Notification updateNotification(Notification notification){
        notification.save()
        notification
    }

    void deleteNotification(Notification notification){
        notification.delete()
    }
}
