// Added by the Spring Security Core plugin:


grails.plugin.springsecurity.providerNames = ['daoAuthenticationProvider', 'rememberMeAuthenticationProvider'] // 'daoAuthenticationProvider', 'anonymousAuthenticationProvider', 'rememberMeAuthenticationProvider'
grails.plugin.springsecurity.logout.handlerNames = ['rememberMeServices', 'securityContextLogoutHandler'] // 'rememberMeServices', 'securityContextLogoutHandler'
grails.plugin.springsecurityrememberMe.alwaysRemember = true

grails.plugin.springsecurity.userLookup.userDomainClassName = 'smarthome.security.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'smarthome.security.UserRole'
grails.plugin.springsecurity.authority.className = 'smarthome.security.Role'
grails.plugin.springsecurity.useRoleGroups = false

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/**/assets/**':                  ['permitAll'],
	'/**/js/**':                      ['permitAll'],
	'/**/css/**':                     ['permitAll'],
	'/**/images/**':                  ['permitAll'],
	'/**/favicon.ico':                ['permitAll'],
	'/grails-errorhandler':			  ['permitAll'],
	'/websocket/**':		  		  ['permitAll'],
	'/j_spring_security_switch_user': ["hasRole('ROLE_ADMIN')"],
	'/j_spring_security_exit_user':   ["isAuthenticated()"],
]

// Configuration supplémentaire de Spring Security
grails.plugin.springsecurity.rejectIfNoRule = true // bloque par défaut toutes les URLS sauf celle mappées par annotation ou dans la map "staticRules"
grails.plugin.springsecurity.password.algorithm = 'bcrypt' /// encryption des mots de passe
grails.plugin.springsecurity.useSessionFixationPrevention = true // Session Fixation Prevention
grails.plugin.springsecurity.logout.postOnly = false // permet de faire des GET pour logout
grails.plugin.springsecurity.useSwitchUserFilter  = true // permet de basculer sur un autre utilisateur


