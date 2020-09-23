package smarthome.plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import smarthome.plugin.NavigationEnum;


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
public @interface NavigableAction {

	static final String SECUSERNAME = "_secusername";
	static final String CONFIG_APPLICATION = "Configuration application";


	/**
	 * Le style à appliquer sur l'item
	 * 
	 * @return
	 */
	String style() default "";


	/**
	 * Le nom de la section section où insérer le plugin
	 * 
	 * @return
	 */
	NavigationEnum navigation() default NavigationEnum.navbarPrimary;


	/**
	 * La description du plugin affichée sous forme de tooltip. Si rien = label
	 * 
	 * @return
	 */
	String tooltip() default "";


	/**
	 * Le label à afficher pour le lien
	 * 
	 * @return
	 */
	String label();


	/**
	 * Un groupe pour classer les items
	 * 
	 * @return
	 */
	String header() default "";
	
	/**
	 * Icon du lien vers controlleur
	 * 
	 * @return
	 */
	String icon() default "";
	
	
	/**
	 * Icon du lien vers controlleur
	 * 
	 * @return
	 */
	String iconLib() default "";
	
	
	/**
	 * Icon du lien vers controlleur
	 * 
	 * @return
	 */
	String iconHeader() default "";
	
	
	/**
	 * Icon du lien vers controlleur
	 * 
	 * @return
	 */
	String iconLibHeader() default "";


	/**
	 * Le déroulement de la navigation pour aller au menu. Utilise les éléments
	 * pour recontruire un menu. Si rien, l'action sera un bouton simple
	 * 
	 * Ex : ['Menu principal', 'Menu secondaire 1', 'Menu secondaire 2']
	 * 
	 * @return
	 */
	String[] breadcrumb() default "";


	/**
	 * Accès au controlleur via form ou lien normal
	 * 
	 * @return
	 */
	boolean useForm() default false;


	/**
	 * Iem par défaut dans un groupe
	 * 
	 * @return
	 */
	boolean defaultGroup() default false;
	
	
	/**
	 * Un menu personnalisé via un template GSP
	 * 
	 * @return
	 */
	boolean customMenuView() default false;
}
