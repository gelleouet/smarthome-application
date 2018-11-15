/**
 * 
 */
package smarthome.security

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.groovy.grails.commons.GrailsApplication;

import grails.plugin.springsecurity.SpringSecurityUtils



/**
 * @author gregory
 *
 */
class SmartHomeSecurityUtils {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("(.+)@(.+)\\.(.+)")
	
	/**
	 * Validator email
	 * Le EmailValidator de commons-validator bloque certains noms de domaine
	 */
	static final emailValidator = { String email, command ->
		Matcher emailMatcher = EMAIL_PATTERN.matcher(email)
		
		if (!emailMatcher.matches()) {
			return "Adresse email non valide !"
		}
		
		if (emailMatcher.group(1).length() < 4) {
			return "Utilisateur email non valide !"
		}
		
		if (emailMatcher.group(2).length() < 4) {
			return "Nom de domaine email non valide !"
		}
		
		if (emailMatcher.group(3).length() < 2) {
			return "Extension email non valide !"
		}
	}
	
	
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
