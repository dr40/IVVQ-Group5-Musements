package musement
import musement.user.User

class Notification {

    //Post Received_Notification
    static belongsTo = [
            user: User
    ]

    static hasMany = [
            posts: Post
    ]


    static constraints = {
        user nullable: false
    }
}
