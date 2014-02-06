package musement

import grails.transaction.Transactional
import musement.user.User;


@Transactional
class PostService {

    NotificationService notificationService;

    /**
     * Create a Post
     * @param sender
     * @param content
     * @param toCategory
     */
    Post sendPost(User sender, String content, Category toCategory) {
        Post p = new Post(content: content);
        sender.addToPosts(p);
        toCategory.addToPosts(p);
        toCategory.save flush:true
        if (p.validate()) {
            /* Save */
            p.save flush:true
            notificationService.Notify(p);
        } else {
            p = null;
        }
        return p;
    }

}
