package smarthome.core

class DateTagLib {
	static namespace = 'app'
	static defaultEncodeAs = [taglib:'html']
	//static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

	// les tags renvoyant directement une valeur pouvant être utilisé comme fonction
	static returnObjectForTags = ['formatUser', 'formatPicker', 'formatUserDateTime']


	/**
	 * Formatte une date au format utilisateur
	 * 
	 * @attr date REQUIRED la date à formatter
	 */
	def formatUser = {attrs, body ->
		g.formatDate(date: attrs.date, format: 'dd/MM/yyyy')
	}


	/**
	 * Formatte une date pour l'insérer dans un input datepicker
	 * 
	 * @attr date REQUIRED la date à formatter
	 */
	def formatPicker = {attrs, body ->
		g.formatDate(date: attrs.date, format: 'yyyy-MM-dd')
	}
	
	
	/**
	 * Formatte une date au format utilisateur
	 *
	 * @attr date REQUIRED la date à formatter
	 */
	def formatUserDateTime = {attrs, body ->
		g.formatDate(date: attrs.date, format: "dd/MM/yyyy 'à' HH:mm")
	}
}
