package smarthome.automation;

/**
 * niveaux d'alerte
 *  
 * @author Gregory
 *
 */
public enum LevelAlertEnum {
	error(200),
	monitoring(0),
	warning(100);
	
	private int criticite;

	
	private LevelAlertEnum(int criticite) {
		this.criticite = criticite;
	}
	
	public int getCriticite() {
		return criticite;
	}
}
