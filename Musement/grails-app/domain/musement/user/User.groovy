package musement.user

import musement.Post
import musement.Notification
import musement.Category

class User {

	transient springSecurityService

	String username
    String email
	String password

    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Notification notification

	static transients = ['springSecurityService']

	static constraints = {
        username    blank: false, nullable: false, unique: true, size: 4..16, matches: '^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*$'
        email       blank: false, nullable: false, email: true, size: 5..64, unique: true
        password    blank: false, nullable: false
	}

    static hasMany = [
            categories : Category,
            posts : Post
    ]

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
