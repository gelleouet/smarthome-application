package smarthome.application;

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
public enum DefiCompteurEnum {
	electricite("compteur"),
	gaz("compteurGaz");
	
	
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
