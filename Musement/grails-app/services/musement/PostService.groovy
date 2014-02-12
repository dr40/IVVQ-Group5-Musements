package musement

import grails.transaction.Transactional
import musement.user.User;


@Transactional
class PostService {

    /** Services **/
    NotificationService notificationService;

    /**
     * Send a Post into a category and notify all others users which have subscribed to the category
     * @param sender User which send the post
     * @param content Content of the post
     * @param toCategory Category where to post
     * @return Post saved Or null if any error(s) occur(s)
     */
    Post sendPost(User sender, String content, Category toCategory) {
        /* Prevent an user that are not subscribed to a category, to post */
        if ((sender.categories == null) || (!sender.categories.contains(toCategory))) {
            return null;
        }
        Post p = new Post(content: content);
        sender.addToPosts(p);
        toCategory.addToPosts(p);
        toCategory.save flush:true
        if (p.validate()) {
            /* Save */
            p = p.save flush:true
            notificationService.notifyUsers(p);
        } else {
            p = null;
        }
        return p;
    }

    /**
     * Edit post
     * @param post Post to edit
     * @param newContent New content to set
     * @return
     */
    Boolean editPost(Post post, String newContent) {
        def oldContent = post.content;
        post.content = newContent;
        if (post.validate()) {
            post.save(flush:true);
            return true;
        } else {
            post.content = oldContent
            return false;
        }
    }

    /**
     * Delete properly a Post
     * @param post Post to delete
     * @param flush Indicate if have to flush just after post deletion completed
     * @return True if post deleted, false otherwise
     */
    Boolean deletePost(Post post, boolean flush = true) {
        if (post == null) return false;
        User sender = post.getSender();
        if (sender == null) return false;
        sender.removeFromPosts(post);
        sender.save()
        Category category = post.getCategory()
        if (category == null) return false;
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
