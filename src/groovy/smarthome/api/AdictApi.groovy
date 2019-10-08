package smarthome.api

import java.text.SimpleDateFormat

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.json.JSONElement

import smarthome.core.DateUtils
import smarthome.core.SmartHomeException
import smarthome.core.http.Http
import smarthome.core.http.transformer.JsonResponseTransformer

/**
 * API GRDF ADICT
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class AdictApi {
	private static final Map URLS = [
		"dev": [
			token: "https://accounts.pprd.adict-api.grdf.fr",
			accreditation: "https://pprd.adict-api.grdf.fr",
			consommationInformative: "https://pprd.adict-api.grdf.fr"
		],
		"prod": [
			token: "https://accounts.adict-api.grdf.fr",
			accreditation: "https://adict-api.grdf.fr",
			consommationInformative: "https://adict-api.grdf.fr"
		]
	]

	GrailsApplication grailsApplication


	/**
	 *  Obtention du jeton d'accès
	 *  Validité : 1 heure
	 *  
	 *  JSON token
	 *  {
	 *	"access_token": "",
	 *  "token_type": "Bearer",
	 *	"expires_in": 3600,
	 *	"scope": "resource.WRITE resource.READ"
	 *	}
	 *
	 * @return
	 * @throws SmartHomeException
	 */
	JSONElement token() throws SmartHomeException {
		String url = "${URLS[(grailsApplication.config.grdf.env)].token}/api/oauth/token"

		Http httpRequest = Http.Post(url)
				.formField("client_id", grailsApplication.config.grdf.client_id)
				.formField("client_secret", grailsApplication.config.grdf.client_secret)
				.formField("grant_type", "client_credentials")


		JSONElement result = httpRequest.execute(new JsonResponseTransformer("error_message"))?.content

		if (!result.access_token) {
			throw new SmartHomeException("Token response empty !")
		}

		return result
	}


	/**
	 * Accréditation d'un PCE
	 * 
	 * @param token
	 * @param accreditation
	 * @return
	 * @throws SmartHomeException
	 */
	JSONElement accreditation(String token, AdictAccreditation accreditation) throws SmartHomeException {
		String url = "${URLS[(grailsApplication.config.grdf.env)].accreditation}/v1/pce/${accreditation.pce}/accreditation"

		Http httpRequest = Http.Put(url)
				.header("Authorization", "Bearer ${token}")
				.queryParam("role", accreditation.role)
				.queryParam("codePostal", accreditation.codePostal)
				.queryParam("titulaireType", accreditation.titulaireType)
				.queryParam("titulaireValeur", accreditation.titulaireValeur)
				.queryParam("emailTitulaire", accreditation.emailTitulaire)
				.queryParam("dateDebutAutorisation", DateUtils.formatDateIso(new Date()))
				.queryParam("dateFinAutorisation", DateUtils.formatDateIso(new Date() + 365))

		JSONElement result = httpRequest.execute(new JsonResponseTransformer("error_message"))?.content

		if (!result.reponsecreerAccreditation || !result.reponsecreerAccreditation.Retour) {
			throw new SmartHomeException("Accreditation response empty !")
		}

		// tente de parser le contenu du message car le code retour HTTP ne gère pas
		// les différents cas
		if (!result.reponsecreerAccreditation.Retour.Message.contains("succès") &&
		!result.reponsecreerAccreditation.Retour.Message.contains("déjà existante")) {
			throw new SmartHomeException("${result.reponsecreerAccreditation.Retour.Message} !")
		}

		return result
	}


	/**
	 * API consommationInformative
	 * L’API « Consommation Informative » renvoie des données uniquement pour les
	 * périodes où le PCE est 1M ou MM. Par exemple : si un PCE est actuellement
	 * 1M mais il était 6M avant, on peut avoir les données informatives sur la
	 * période où il est 1M mais pas sur la période où il était 6M.
	 * 
	 * 	"horodateIndexBrutDebut": "2019-06-12T06:00:00",
	 *	"indexBrutDebut": 1043,
	 *	"horodateIndexBrutFin": "2019-06-13T06:00:00",
	 *	"indexBrutFin": 1052,
	 *	"flagRetourZero": "0",
	 *	"volumeBrut": 8.67,
	 *	"energieKwh": 126,
	 *	"qualifConso": "Mesuré",
	 * 	"coeffConversion": "14,50169"
	 * 
	 * @param token
	 * @param pce
	 * @param role
	 * @param dateDebut
	 * @param dateFin
	 * @return
	 */
	List<JSONElement> consommationInformative(String token, String pce, String role, Date dateDebut, Date dateFin) {
		String url = "${URLS[(grailsApplication.config.grdf.env)].consommationInformative}/v1/pce/${pce}/consommationInformative"

		Http httpRequest = Http.Get(url)
				.header("Authorization", "Bearer ${token}")
				.queryParam("role", role)
				.queryParam("dateDebutDemandeConso", DateUtils.formatDateIso(dateDebut))
				.queryParam("dateFinDemandeConso", DateUtils.formatDateIso(dateFin))

		JSONElement result = httpRequest.execute(new JsonResponseTransformer("error_message"))?.content

		// il faut traiter le contenu de la response car les codes d'erreur ne sont
		// pas gérés avec le code HTTP.
		if (result.Message) {
			throw new SmartHomeException("${result.Message} !")
		}

		if (!result.consommationInformative || !result.Retour || !result.Retour.code) {
			throw new SmartHomeException("Aucune donnée retournée !")
		}

		if (result.Retour.code.toLowerCase() != "succès") {
			throw new SmartHomeException("${result.Retour.code} !")
		}

		// traite les données pour faire les conversions de base (date)
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

		result.consommationInformative.each { datapoint ->
			datapoint.timestamp = dateFormat.parse(datapoint.horodateIndexBrutDebut).clearTime()
			// l'index fin dans la valeur principale
			datapoint.value = datapoint.indexBrutFin
			// la conso est convertie en Wh
			datapoint.conso = datapoint.energieKwh * 1000
		}

		return result.consommationInformative
	}
}
