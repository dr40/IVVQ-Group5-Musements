package musement.user

import musement.Post
import musement.Category

class User {

	transient springSecurityService

	String username
    String email
	String password

	static transients = ['springSecurityService']

	static constraints = {
        username    blank: false, nullable: false, size: 1..64, unique: true
        email       blank: false, nullable: false, email: true, size: 1..128, unique: true
        password    blank: false, nullable: false, size: 8..64
	}

    static hasMany = [
            categories : Category,
            posts : Post
    ]

    static belongsTo = [
            Category
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
		password = springSecurityService.encodePassword(password, username)
	}
}
