package smarthome.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation à placer sur les méthodes exécutant un workflow en fin de traitement
 * Le workflow sera identifié par son nom et exécuté en mode asynchrone
 * 
 * 
 * @author gregory
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public  @interface AsynchronousWorkflow {
	/**
	 * Nom du workflow
	 * 
	 * @return
	 */
	String value();
}
