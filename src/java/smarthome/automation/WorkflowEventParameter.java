package smarthome.automation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parametre exécution méthode
 * 
 * @author Gregory
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WorkflowEventParameter {
	String name();
	String label();
	String type();
	String minValue() default "";
	String maxValue() default "";
	boolean required() default true;
}
