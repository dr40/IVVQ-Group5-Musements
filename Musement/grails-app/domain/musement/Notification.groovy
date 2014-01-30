package musement

import musement.user.User

class Notification {

    static hasOne = [
        user: User
    ]

    static hasMany = [
        posts: Post
    ]


    static constraints = {

        posts nullable: false
        user nullable: false
        user unique: true
    }



}
