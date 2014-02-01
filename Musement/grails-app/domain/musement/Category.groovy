package musement

import musement.user.User

class Category {

    String name;
    String description;


    static constraints = {
        name blank: false, matches: "[a-zA-Z][a-zA-Z0-9]*", nullable: false
        description nullable: false
    }

    static hasMany = [
            posts : Post,
            users : User
    ]

    static belongsTo = [ User ]
}
