// Added by the Spring Security Core plugin:


grails.plugin.springsecurity.providerNames = ['daoAuthenticationProvider', 'rememberMeAuthenticationProvider'] // 'daoAuthenticationProvider', 'anonymousAuthenticationProvider', 'rememberMeAuthenticationProvider'
grails.plugin.springsecurity.logout.handlerNames = ['rememberMeServices', 'securityContextLogoutHandler'] // 'rememberMeServices', 'securityContextLogoutHandler'
grails.plugin.springsecurityrememberMe.alwaysRemember = true

grails.plugin.springsecurity.userLookup.userDomainClassName = 'smarthome.security.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'smarthome.security.UserRole'
grails.plugin.springsecurity.authority.className = 'smarthome.security.Role'
grails.plugin.springsecurity.authority.groupAuthorityNameField = 'authorities'
grails.plugin.springsecurity.useRoleGroups = true
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/**/assets/**':                  ['permitAll'],
	'/**/js/**':                      ['permitAll'],
	'/**/css/**':                     ['permitAll'],
	'/**/images/**':                  ['permitAll'],
	'/**/favicon.ico':                ['permitAll'],
	'/grails-errorhandler':			  ['permitAll'],
	'/websocket/**':		  		  ['permitAll']
]

// Configuration suppl�mentaire de Spring Security
grails.plugin.springsecurity.rejectIfNoRule = true // bloque par défaut toutes les URLS sauf celle mappées par annotation ou dans la map "staticRules"
grails.plugin.springsecurity.password.algorithm = 'bcrypt' /// encryption des mots de passe
grails.plugin.springsecurity.useSessionFixationPrevention = true // Session Fixation Prevention
grails.plugin.springsecurity.logout.postOnly = false // permet de faire des GET pour logout


