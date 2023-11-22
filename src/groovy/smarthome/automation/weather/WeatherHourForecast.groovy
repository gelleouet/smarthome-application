package smarthome.automation.weather

class WeatherHourForecast implements Serializable {
	Date date
	Integer temperature
	Integer apparentTemperature
	Integer humidity	// en %
	Integer pressure	// en hPa
	Integer windSpeed	// vitesse vent
	Integer windGust // vitesse rafale de vent
	Integer windBearing // direction d'où vient le vent en degré
	Integer uvIndex
	String icon
	Integer cloudCover // en %
	Integer precipProbability	// en %
	String precipType // pluie, neige, grele, etc....
	String style
	String text
}
