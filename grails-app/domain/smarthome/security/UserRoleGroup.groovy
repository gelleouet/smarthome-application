package smarthome.security


import org.apache.commons.lang.builder.HashCodeBuilder

class UserRoleGroup implements Serializable {

	private static final long serialVersionUID = 1

	User user
	RoleGroup roleGroup


	static mapping = {
		id composite: ['roleGroup', 'user']
		version false
	}

	boolean equals(other) {
		if (!(other instanceof UserRoleGroup)) {
			return false
		}

		other.user?.id == user?.id &&
				other.roleGroup?.id == roleGroup?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (user) builder.append(user.id)
		if (roleGroup) builder.append(roleGroup.id)
		builder.toHashCode()
	}

	static UserRoleGroup get(long userId, long roleGroupId) {
		UserRoleGroup.where {
			user == User.load(userId) &&
					roleGroup == RoleGroup.load(roleGroupId)
		}.get()
	}

	static boolean exists(long userId, long roleGroupId) {
		UserRoleGroup.where {
			user == User.load(userId) &&
					roleGroup == RoleGroup.load(roleGroupId)
		}.count() > 0
	}

	static UserRoleGroup create(User user, RoleGroup roleGroup, boolean flush = false) {
		def instance = new UserRoleGroup(user: user, roleGroup: roleGroup)
		instance.save(flush: flush, insert: true)
		instance
	}

	static boolean remove(User u, RoleGroup g, boolean flush = false) {
		if (u == null || g == null) return false

		int rowCount = UserRoleGroup.where {
			user == User.load(u.id) &&
					roleGroup == RoleGroup.load(g.id)
		}.deleteAll()

		if (flush) {
			UserRoleGroup.withSession { it.flush() } }

		rowCount > 0
	}

	static void removeAll(User u, boolean flush = false) {
		if (u == null) return

			UserRoleGroup.where {
				user == User.load(u.id)
			}.deleteAll()

		if (flush) {
			UserRoleGroup.withSession { it.flush() } }
	}

	static void removeAll(RoleGroup g, boolean flush = false) {
		if (g == null) return

			UserRoleGroup.where {
				roleGroup == RoleGroup.load(g.id)
			}.deleteAll()

		if (flush) {
			UserRoleGroup.withSession { it.flush() } }
	}

	static constraints = {
		user validator: { User u, UserRoleGroup ug ->
			if (ug.roleGroup == null) return
				boolean existing = false
			UserRoleGroup.withNewSession {
				existing = UserRoleGroup.exists(u.id, ug.roleGroup.id)
			}
			if (existing) {
				return 'userGroup.exists'
			}
		}
	}
}
