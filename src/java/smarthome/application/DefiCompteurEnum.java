package smarthome.application;

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
public enum DefiCompteurEnum {
	electricite("compteur"),
	gaz("compteurGaz"),
	/**
	 * virtuel non associé à un compteur Utilisé pour l'accès dynamique aux
	 * propriétés de la classe @see AbstractDefiResultat
	 */
	global("");
	
	
	private DefiCompteurEnum(String property) {
		this.property = property;
	}

	private String property;

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}
	
	
}
