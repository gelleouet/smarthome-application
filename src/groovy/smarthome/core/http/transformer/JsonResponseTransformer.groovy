package smarthome.core.http.transformer

import org.codehaus.groovy.grails.web.json.JSONElement
import grails.converters.JSON

/**
 * Implémentation response transformer pour une conversion en objet Json
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class JsonResponseTransformer implements ResponseTransformer<JSONElement> {

	String fieldError


	JsonResponseTransformer() {
	}


	JsonResponseTransformer(String fieldError) {
		this.fieldError = fieldError
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see smarthome.core.http.transformer.ResponseTransformer#transform(java.io.InputStream)
	 */
	@Override
	JSONElement transform(InputStream inputStream) throws Exception {
		if (inputStream) {
			String content = inputStream.text

			if (content) {
				return JSON.parse(content)
			}
		}

		return null
	}


	/** 
	 * (non-Javadoc)
	 *
	 * @see smarthome.core.http.transformer.ResponseTransformer#error(java.lang.Object)
	 */
	@Override
	String error(JSONElement response) {
		if (fieldError && response) {
			return response[fieldError]
		} else {
			return null
		}
	}
}
