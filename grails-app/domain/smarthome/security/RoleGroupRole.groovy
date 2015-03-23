package smarthome.security


import org.apache.commons.lang.builder.HashCodeBuilder

class RoleGroupRole implements Serializable {

	private static final long serialVersionUID = 1

	RoleGroup roleGroup
	Role role

	static mapping = {
		id composite: ['roleGroup', 'role']
		version false
	}

	boolean equals(other) {
		if (!(other instanceof RoleGroupRole)) {
			return false
		}

		other.role?.id == role?.id &&
				other.roleGroup?.id == roleGroup?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (roleGroup) builder.append(roleGroup.id)
		if (role) builder.append(role.id)
		builder.toHashCode()
	}

	static RoleGroupRole get(long roleGroupId, long roleId) {
		RoleGroupRole.where {
			roleGroup == RoleGroup.load(roleGroupId) &&
					role == Role.load(roleId)
		}.get()
	}

	static boolean exists(long roleGroupId, long roleId) {
		RoleGroupRole.where {
			roleGroup == RoleGroup.load(roleGroupId) &&
					role == Role.load(roleId)
		}.count() > 0
	}

	static RoleGroupRole create(RoleGroup roleGroup, Role role, boolean flush = false) {
		def instance = new RoleGroupRole(roleGroup: roleGroup, role: role)
		instance.save(flush: flush, insert: true)
		instance
	}

	static boolean remove(RoleGroup rg, Role r, boolean flush = false) {
		if (rg == null || r == null) return false

		int rowCount = RoleGroupRole.where {
			roleGroup == RoleGroup.load(rg.id) &&
					role == Role.load(r.id)
		}.deleteAll()

		if (flush) {
			RoleGroupRole.withSession { it.flush() } }

		rowCount > 0
	}

	static void removeAll(Role r, boolean flush = false) {
		if (r == null) return

			RoleGroupRole.where {
				role == Role.load(r.id)
			}.deleteAll()

		if (flush) {
			RoleGroupRole.withSession { it.flush() } }
	}

	static void removeAll(RoleGroup rg, boolean flush = false) {
		if (rg == null) return

			RoleGroupRole.where {
				roleGroup == RoleGroup.load(rg.id)
			}.deleteAll()

		if (flush) {
			RoleGroupRole.withSession { it.flush() } }
	}

	static constraints = {
		role validator: { Role r, RoleGroupRole rg ->
			if (rg.roleGroup == null) return
				boolean existing = false
			RoleGroupRole.withNewSession {
				existing = RoleGroupRole.exists(rg.roleGroup.id, r.id)
			}
			if (existing) {
				return 'roleGroup.exists'
			}
		}
	}
}
