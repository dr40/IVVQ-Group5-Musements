package musement

import musement.user.User

class Post {

    User sender;
    Category postCategory;
    Date postDate = new Date();
    String content;


    static constraints = {
        sender nullable: false
        postCategory nullable: false
        content blank: false
    }
}
