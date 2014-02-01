package musement.user

class Role {

	String authority

	static mapping = {
		cache true
        version(false)
	}

	static constraints = {
		authority blank: false, unique: true, inList: Roles.values()*.name()
	}
}

enum Roles {
    ROLE_ADMIN(1),
    ROLE_USER(2)

    Long id

    Roles(Long id) {
        this.id = id
    }

    Role getRole() {
        Role.get(id)
    }
}
