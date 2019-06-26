package smarthome.core.http

/**
 * Réponse d'une requête HTTP
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class HttpResult {
	/**
	 * Code retour HTTP
	 */
	int code

	/**
	 * Message associé au code HTTP
	 */
	String message

	/**
	 * Le contenu de la réponse HTTP au format binaire
	 * Par défaut, un byte[] si aucun transformer n'est renseigné, sinon c'est l'objet retourné par le transformer
	 */
	Object content

	/**
	 * Content type (depuis le header)
	 * Renseigné si seulement retourné par la requête
	 */
	String contentType

	/**
	 * Encodage (depuis le header)
	 * Renseigné si seulement retourné par la requête
	 */
	String encoding

	/**
	 * Taille contenu réponse (depuis le header)
	 * Renseigné si seulement retourné par la requête
	 */
	long contentLength
}
