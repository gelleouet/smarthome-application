/**
 * 
 */
package smarthome.security

import org.codehaus.groovy.grails.commons.GrailsApplication;

import grails.plugin.springsecurity.SpringSecurityUtils



/**
 * @author gregory
 *
 */
class SmartHomeSecurityUtils {

	/**
	 * Validator pour le mot de passe
	 */
	static final passwordValidator = { String password, command ->
		if (command.username && command.username.equals(password)) {
			return 'Veuillez choisir un mot de passe différent de votre email'
		}

		if (!checkPasswordMinLength(password, command) ||
		!checkPasswordMaxLength(password, command) ||
		!checkPasswordRegex(password, command)) {
			return "Le mot de passe n'est pas assez sécurisé"
		}
	}


	/**
	 * Validator pour la confirmation d'un mot de passe
	 */
	static final passwordConfirmValidator = { value, command ->
		if (command.newPassword != command.confirmPassword) {
			return "Les mots de passe ne correspondent pas"
		}
	}


	/**
	 * Vérifie la taille minimale du mot de passe
	 * 
	 * @param password
	 * @param command
	 * @return
	 */
	static boolean checkPasswordMinLength(String password, command) {
		def conf = SpringSecurityUtils.securityConfig

		int minLength = conf.ui.password.minLength instanceof Number ? conf.ui.password.minLength : 8

		password && password.length() >= minLength
	}


	/**
	 * Vérifie la taille maximale du mot de passe
	 * 
	 * @param password
	 * @param command
	 * @return
	 */
	static boolean checkPasswordMaxLength(String password, command) {
		def conf = SpringSecurityUtils.securityConfig

		int maxLength = conf.ui.password.maxLength instanceof Number ? conf.ui.password.maxLength : 64

		password && password.length() <= maxLength
	}


	/**
	 * Vérifie le pattern du mot de passe
	 * 
	 * @param password
	 * @param command
	 * @return
	 */
	static boolean checkPasswordRegex(String password, command) {
		def conf = SpringSecurityUtils.securityConfig

		String passValidationRegex = conf.ui.password.validationRegex ?:
				'^.*(?=.*\\d)(?=.*[a-zA-Z]).*$'

		password && password.matches(passValidationRegex)
	}
	
}
