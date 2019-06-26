package smarthome.api

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.json.JSONElement
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException
import smarthome.core.http.Http
import smarthome.core.http.transformer.JsonResponseTransformer
import smarthome.security.User

/**
 * API DataConnect Enedis
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class DataConnectApi {
	private static final Map URLS = [
		"dev": [
			authorize: "https://gw.hml.api.enedis.fr",
			token: "https://gw.hml.api.enedis.fr",
			metric: "https://gw.hml.api.enedis.fr"
		],
		"prod": [
			authorize: "https://espace-client-particuliers.enedis.fr",
			token: "https://gw.prd.api.enedis.fr",
			metric: "https://gw.prd.api.enedis.fr"
		]
	]

	GrailsApplication grailsApplication


	/**
	 * Appel de la page de consentement
	 * 
	 * Quand vous serez en production, ce lien dirigera le client vers la page 
	 * d’authentification à son compte client Enedis ; une fois connecté, il y 
	 * trouvera une page avec votre logo, votre nom, les données dont vous avez 
	 * besoin qui lui permettra d’autoriser ou pas le partage de ses données Linky.
	 * 
	 * https://datahub-enedis.fr/data-connect/documentation/authorize/
	 * 
	 * @return
	 */
	String authorize_uri() {
		String url = "${URLS[(grailsApplication.config.enedis.env)].authorize}/group/espace-particuliers/consentement-linky/oauth2/authorize"
		url += "?client_id=${grailsApplication.config.enedis.client_id}"
		url += "&duration=P3Y"
		url += "&response_type=code"
		url += "&state=${grailsApplication.config.enedis.state}"
		url += "&redirect_uri=${grailsApplication.config.enedis.redirect_uri}"
		return url
	}


	/**
	 *  Obtention des jetons
	 *  https://datahub-enedis.fr/data-connect/documentation/authorize/
	 *  
	 *  En retour, bingo, vous récupérez deux jetons ! Un jeton d’accès (access_token)
	 *  qui est la clé d’entrée auprès d’Enedis pour les données auxquelles le
	 *  client vient de vous donner accès. Il a une durée de validité de 3h30.
	 *  Vous disposez également d’un jeton de rafraîchissement (refresh_token) 
	 *  qui a une durée de validité d’un an et permet de renouveler le jeton d’accès.
	 * 
	 * @param code
	 * @return
	 * @throws SmartHomeException
	 */
	JSONElement authorization_code(String code) throws SmartHomeException {
		String url = "${URLS[(grailsApplication.config.enedis.env)].token}/v1/oauth2/token"

		Http httpRequest = Http.Post(url)
				.queryParam("redirect_uri", grailsApplication.config.enedis.redirect_uri)
				.formField("client_id", grailsApplication.config.enedis.client_id)
				.formField("client_secret", grailsApplication.config.enedis.client_secret)
				.formField("grant_type", GrantTypeEnum.authorization_code.toString())
				.formField("code", code)


		JSONElement result = httpRequest.execute(new JsonResponseTransformer("error_description"))?.content

		if (!result) {
			throw new SmartHomeException("Token response empty !")
		}

		return result
	}


	/**
	 *  Rafraîchissement du jeton d’accès
	 *  https://datahub-enedis.fr/data-connect/documentation/authorize/
	 *
	 *  Lorsque votre jeton d’accès est expiré, vous ne pouvez plus récupérer de
	 *  données et les API vous retournent un code erreur HTTP 401 (Unauthorized).
	 *  Pas de panique, il vous est possible d’en générer un nouveau sans
	 *  demander à l’utilisateur de se ré-authentifier.
	 *  C’est le rôle du jeton de rafraîchissement.
	 *
	 * @param refreshToken
	 * @return
	 * @throws SmartHomeException
	 */
	JSONElement refresh_token(String refreshToken) throws SmartHomeException {
		String url = "${URLS[(grailsApplication.config.enedis.env)].token}/v1/oauth2/token"

		Http httpRequest = Http.Post(url)
				.queryParam("redirect_uri", grailsApplication.config.enedis.redirect_uri)
				.formField("client_id", grailsApplication.config.enedis.client_id)
				.formField("client_secret", grailsApplication.config.enedis.client_secret)
				.formField("grant_type", GrantTypeEnum.refresh_token.toString())
				.formField("refresh_token", refreshToken)


		JSONElement result = httpRequest.execute(new JsonResponseTransformer("error_description"))?.content

		if (!result) {
			throw new SmartHomeException("Token response empty !")
		}

		return result
	}



	/**
	 * La courbe de consommation
	 * La courbe de consommation correspond à la puissance soutirée par le client
	 * moyennée sur des plages de 30 minutes, elle est exprimée en Watt.
	 * 
	 * https://datahub-enedis.fr/data-connect/documentation/metering-data/
	 * 
	 * @param start
	 * @param end
	 * @param usagePointId
	 * @param token
	 * @return
	 * @throws SmartHomeException
	 */
	List<JSONElement> consumptionLoadCurve(Date start, Date end, String usagePointId, String token) throws SmartHomeException {
		String url = "${URLS[(grailsApplication.config.enedis.env)].metric}/v3/metering_data/consumption_load_curve"

		JSONElement response = Http.Get(url)
				.queryParam("start", DateUtils.formatDateTimeIso(start))
				.queryParam("end", DateUtils.formatDateTimeIso(end))
				.queryParam("usage_point_id", usagePointId)
				.header("Authorization", "Bearer ${token}")
				.header("Accept", "application/json")
				.execute(new JsonResponseTransformer())?.content

		if (!response || !response.usage_point) {
			throw new SmartHomeException("consumptionLoadCurve response empty !")
		}

		List<JSONElement> datapoints = response.usage_point[0].meter_reading.interval_reading
		Date rankStart = DateUtils.parseDateIso(response.usage_point[0].meter_reading.start)

		datapoints.each { datapoint ->
			// on ramène les puissances moyenne sur 30min à une consommation en Wh
			datapoint.wh = (datapoint.value as Double) * 2

			use (TimeCategory) {
				// le rank correspond aux intervalles d'une 1/2H
				// il faut retrancher 1 minute pour ne pas tomber sur la tranche suivante
				datapoint.timestamp = rankStart + ((datapoint.rank as Integer) * 30).minutes - 1.minute

			}
		}

		return datapoints
	}



	public void setGrailsApplication(GrailsApplication grailsApplication) {
		this.grailsApplication = grailsApplication
	}
}
