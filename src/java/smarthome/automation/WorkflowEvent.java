package smarthome.automation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation à placer sur les méthodes des implémentations device
 * pour distinguer les actions possibles sur un device pouvant
 * être commandés par l'utilisateur ou un scénario
 * 
 * @author Gregory
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WorkflowEvent {

}
