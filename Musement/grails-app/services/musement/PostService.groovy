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
            notificationService.notifyUsers(p);
        } else {
            p = null;
        }
        return p;
    }

    Boolean deletePost(Post post) {
        User sender = post.getSender();
        sender.removeFromPosts(post);
        sender.save(flush:true)
        Category category = post.getCategory()
        category.removeFromPosts(post);
        category.save(flush:true)
        post.delete()
        return true
    }
}
