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

    Boolean deletePost(Post post, boolean flush = true) {
        User sender = post.getSender();
        sender.removeFromPosts(post);
        sender.save()
        Category category = post.getCategory()
        category.removeFromPosts(post);
        category.save()
        Notification.list().each {n ->
            def postToRemove = [];
            n.posts.each {p ->
                if (p.id == post.id) {
                    postToRemove.add(p);
                }
            }
            if (postToRemove.size() > 0) {
                for(Post p : postToRemove) {
                    n.posts.remove (p);
                }
                n.save()
            }
        }
        if (flush) {
            post.delete(flush: true)
        } else {
            post.delete()
        }
        return true
    }

}
