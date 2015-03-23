package smarthome.core;

/**
 * Interface à implémenter pour les objets qui doivent renvoyer une description à l'utilisateur
 * 
 * @author gregory
 *
 */
public interface UserStringable {
	/**
	 * Description utilisateur
	 * 
	 * @return
	 */
	String userString();
}
