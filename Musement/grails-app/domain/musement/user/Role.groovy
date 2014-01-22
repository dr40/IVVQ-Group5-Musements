package musement.user

class Role {

	String authority

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true, inList: Roles.values()*.name()
	}
}

enum Roles {
    ADMIN,
    USER
}
