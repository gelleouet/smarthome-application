/**
 * 
 */
package smarthome.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Information de redirection quand exception sur l'action
 * 
 * Permet de définir la vue à renvoyer ou alors la redirection
 * 
 * @author gregory
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ExceptionNavigationHandler {

	/**
	 * L'action à exécuter (doit être spécifié avec controller)
	 * 
	 * @return
	 */
	String actionName();


	/**
	 * Le controller utilisé (si vide on utilise le controller courant)
	 */
	String controllerName() default "";


	/**
	 * Le nom de l'objet en erreur dans la map du modèle à retourner
	 * 
	 * @return
	 */
	String modelName();
}
