package smarthome.automation.weather

class WeatherDayForecast implements Serializable {
	Date date
	Date sunrise	// levé soleil
	Date sunset		// coucher soleil
	Integer temperatureLow
	Integer apparentTemperatureLow
	Date temperatureLowTime
	Date apparentTemperatureLowTime
	Integer temperatureHigh
	Integer apparentTemperatureHigh
	Date temperatureHighTime
	Date apparentTemperatureHighTime
	Integer humidity	// en %
	Integer pressure	// en hPa
	Integer windSpeed	// vitesse vent
	Integer windGust // vitesse rafale de vent
	Integer windBearing // direction d'où vient le vent en degré
	Date windGustTime
	Integer uvIndex
	String icon
	Integer cloudCover // en %
	Integer precipProbability	// en %
	String precipType // pluie, neige, grele, etc....
	Date precipIntensityMaxTime 
	Double moonPhase
	String style
	String text
}
