package smarthome.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation à placer sur les méthodes devant envoyer un message AMQP en fin de traitement
 * Par défaut, les paramètres de la méthode et l'objet retour sont sérialisés dans le message
 * Si une exception est déclenchée par la méthode appelante, le message n'est pas envoyé
 * 
 * 
 * @author gregory
 *
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public  @interface AsynchronousMessage {
	/**
	 * Clé de redirection depuis le exchange
	 * 
	 * @return
	 */
	String routingKey() default "";
	
	
	/**
	 * Nom du exchange où poster les messages
	 * 
	 * @return
	 */
	String exchange() default "amq.direct";
	
	
	/**
	 * Le mode d'envoi des messages vers le exchange
	 * 
	 * @return
	 */
	ExchangeType exchangeType() default ExchangeType.DIRECT;
}
