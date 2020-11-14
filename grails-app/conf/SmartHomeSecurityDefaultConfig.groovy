// Added by the Spring Security Core plugin:


grails.plugin.springsecurity.providerNames = ['daoAuthenticationProvider', 'rememberMeAuthenticationProvider'] // 'daoAuthenticationProvider', 'anonymousAuthenticationProvider', 'rememberMeAuthenticationProvider'
grails.plugin.springsecurity.logout.handlerNames = ['rememberMeServices', 'securityContextLogoutHandler'] // 'rememberMeServices', 'securityContextLogoutHandler'

grails.plugin.springsecurity.userLookup.userDomainClassName = 'smarthome.security.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'smarthome.security.UserRole'
grails.plugin.springsecurity.authority.className = 'smarthome.security.Role'
grails.plugin.springsecurity.useRoleGroups = false

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/assets/**':                  	  ['permitAll'],
	'/**/js/**':                      ['permitAll'],
	'/**/css/**':                     ['permitAll'],
	'/**/images/**':                  ['permitAll'],
	'/**/fonts/**':                   ['permitAll'],
	'/**/favicon.ico':                ['permitAll'],
	'/grails-errorhandler':			  ['permitAll'],
	'/websocket/**':		  		  ['permitAll'],
	'/*-endpoint/**':		  	  	  ['permitAll'],
	'/j_spring_security_switch_user': ["hasRole('ROLE_ADMIN')"],
	'/j_spring_security_exit_user':   ["permitAll"],
	'/j_spring_security_logout':   	  ["permitAll"]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	'/assets/**':      					'none',
	'/fonts/**':      					'none',
	'/js/**':       					'none',
	'/css/**':      					'none',
	'/images/**':   					'none',
	'/**/favicon.ico': 					'none',
	'/login/auth': 						'none',
	'/**':             					'JOINED_FILTERS'
]

// Configuration supplémentaire de Spring Security
grails.plugin.springsecurity.rejectIfNoRule = true // bloque par défaut toutes les URLS sauf celle mappées par annotation ou dans la map "staticRules"
grails.plugin.springsecurity.password.algorithm = 'bcrypt' /// encryption des mots de passe
grails.plugin.springsecurity.useSessionFixationPrevention = true // Session Fixation Prevention
grails.plugin.springsecurity.logout.postOnly = false // permet de faire des GET pour logout
grails.plugin.springsecurity.useSwitchUserFilter  = true // permet de basculer sur un autre utilisateur
grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/user/authfail?login_error=1'
grails.plugin.springsecurity.failureHandler.ajaxAuthFailUrl = '/user/authfail?ajax=true'


// Spring ACL
grails.plugin.springsecurity.acl.permissionClass = smarthome.security.SmartHomePermission


