package smarthome.api

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
			accreditation: "https://pprd.adict-api.grdf.fr"
		],
		"prod": [
			token: "https://accounts.adict-api.grdf.fr",
			accreditation: "https://adict-api.grdf.fr"
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
		String url = "${URLS[(grailsApplication.config.enedis.env)].token}/api/oauth/token"

		Http httpRequest = Http.Post(url)
				.formField("client_id", grailsApplication.config.grdf.client_id)
				.formField("client_secret", grailsApplication.config.grdf.client_secret)
				.formField("grant_type", "client_credentials")


		JSONElement result = httpRequest.execute(new JsonResponseTransformer("error_description"))?.content

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
		String url = "${URLS[(grailsApplication.config.enedis.env)].accreditation}/v1/pce/${accreditation.pce}/accreditation"

		Http httpRequest = Http.Put(url)
				.header("Authorization", "Bearer ${token}")
				.queryParam("role", accreditation.role)
				.queryParam("codePostal", accreditation.codePostal)
				.queryParam("titulaireType", accreditation.titulaireType)
				.queryParam("titulaireValeur", accreditation.titulaireValeur)
				.queryParam("emailTitulaire", accreditation.emailTitulaire)
				.queryParam("dateDebutAutorisation", DateUtils.formatDateIso(new Date()))
				.queryParam("dateFinAutorisation", DateUtils.formatDateIso(new Date() + 365))

		JSONElement result = httpRequest.execute(new JsonResponseTransformer("error_description"))?.content

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
}
