package musement

class Post {

    Date postDate = new Date();
    String content;


    static constraints = {
        content blank: false
    }
}
