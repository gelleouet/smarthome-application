package smarthome.security

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import grails.plugin.springsecurity.userdetails.GormUserDetailsService;
import grails.plugin.springsecurity.userdetails.NoStackUsernameNotFoundException;


class SmartHomeUserDetailsService extends GormUserDetailsService {

	@Override
	UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException {
		User.withTransaction { status ->
			User user = User.findByUsername(username)
			
			if (!user) {
				log.warn "User not found: $username"
				throw new NoStackUsernameNotFoundException()
			}
			
			// trace la dernière connexion
			user.lastConnexion = new Date()
			user.save()
			
			Collection<GrantedAuthority> authorities = loadAuthorities(user, username, loadRoles)
			
			return createUserDetails(user, authorities)
		}
	}
}
