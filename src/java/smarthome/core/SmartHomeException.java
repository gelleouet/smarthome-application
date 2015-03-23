/**
 * 
 */
package smarthome.core;

import grails.validation.ValidationErrors;

import org.springframework.validation.Errors;

/**
 * @author gregory
 *
 */
public class SmartHomeException extends Exception {

	private Object artefactObject;
	private ValidationErrors errors;


	public ValidationErrors getErrors() {
		return errors;
	}


	/**
	 * @return the artefactObject
	 */
	public Object getArtefactObject() {
		return artefactObject;
	}


	/**
	 * 
	 */
	public SmartHomeException() {
		super();
	}


	/**
	 * Construction d'une exception à partir d'un objet artefact (controller,
	 * domain) pouvant contenir des erreurs
	 * 
	 * @param titreMessage
	 * @param errors
	 */
	public SmartHomeException(String message, Object artefactObject) {
		this(message);
		this.artefactObject = artefactObject;
	}
	
	
	public SmartHomeException(Throwable cause, Object artefactObject) {
		this(cause);
		this.artefactObject = artefactObject;
	}
	
	
	public SmartHomeException(String message, Object artefactObject, ValidationErrors errors) {
		this(message);
		this.artefactObject = artefactObject;
		this.errors = errors;
	}


	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public SmartHomeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}


	/**
	 * @param message
	 * @param cause
	 */
	public SmartHomeException(String message, Throwable cause) {
		super(message, cause);
	}


	/**
	 * @param message
	 */
	public SmartHomeException(String message) {
		super(message);
	}


	/**
	 * @param cause
	 */
	public SmartHomeException(Throwable cause) {
		super(cause);
	}

}
