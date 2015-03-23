package smarthome.plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import smarthome.plugin.NavigableAction;


/**
 * Annotation à insérer sur les controllers pour les intégrer automatiquement
 * dans l'application Les liens seront insérés aux endroits indiqués
 * 
 * @author gregory
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NavigableActions {

	/**
	 * Une liste d'actions
	 * 
	 * @return
	 */
	NavigableAction[] value();

}
