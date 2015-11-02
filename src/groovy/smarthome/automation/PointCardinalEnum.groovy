package smarthome.automation

enum PointCardinalEnum {
	NORD("Nord"),
	NORD_EST("Nord-Est"),
	EST("Est"),
	SUD_EST("Sud-Est"),
	SUD("Sud"),
	SUD_OUEST("Sud-Ouest"),
	OUEST("Ouest"),
	NORD_OUEST("Nord-Ouest");
	
	String libelle
	
	PointCardinalEnum(String libelle) {
		this.libelle = libelle
	}
}
