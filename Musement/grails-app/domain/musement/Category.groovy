package musement

class Category {

    String name;
    String description;

    static constraints = {
        name blank: false, matches: "[a-zA-Z][a-zA-Z0-9]*"
    }
}
