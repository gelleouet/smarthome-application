package smarthome.automation.notification

import grails.converters.JSON;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import smarthome.core.SmartHomeException

class FreeMobileNotificationSender extends AbstractNotificationSender {

	private static final String CHARSET_ISO8859 = "iso-8859-1"
	
	private static final String FREE_SMS_URL = "https://smsapi.free-mobile.fr/sendmsg"
	
	private static final Map ERRORS = [400: "Un des paramètres obligatoires est manquant",
		402: "Trop de SMS ont été envoyés en trop peu de temps",
		403: "Le service n'est pas activé sur l'espace abonné, ou login / clé incorrect",
		500: "Erreur côté serveur. Veuillez réessayer ultérieurement"]
	
	
	@Override
	void send(Notification notification) throws SmartHomeException {
		if (!config.user || !config.pass) {
			throw new SmartHomeException("Free mobile configuration incomplète ! (user, pass obligatoires)")
		}

		// encodage du message en iso sinon caractères spéciaux ne passent pas
		String encodMessage = new String((["user": config.user, "pass": config.pass, "msg": notification.message] as JSON).toString().bytes, CHARSET_ISO8859)
		
		try {
			HttpResponse response = Request.Post(FREE_SMS_URL)
				.addHeader("Content-type", "${ContentType.APPLICATION_JSON.mimeType}; charset=$CHARSET_ISO8859")
				.bodyString(encodMessage, ContentType.APPLICATION_JSON)
				.execute()
				.returnResponse()
			 
			if (response.statusLine.statusCode  != 200) {
				throw new SmartHomeException("Erreur envoi SMS : ${ERRORS[response.statusLine.statusCode]}")
			}
		} catch (Exception ex) {
			throw new SmartHomeException(ex) 
		}
	}


	@Override
	String getDescription() {
		return "Free Mobile Notification SMS";
	}

}
