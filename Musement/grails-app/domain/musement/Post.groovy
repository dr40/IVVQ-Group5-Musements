package musement

import musement.user.User

class Post {

    Date postDate =  new Date();
    String content;

    static belongsTo = [sender:User, category:Category];

    static constraints = {
        sender nullable: false
        category nullable: false
        content blank: false, maxSize: 256
    }

}