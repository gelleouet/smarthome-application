package smarthome.core

import grails.util.GrailsWebUtil;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.groovy.grails.web.mime.MimeType;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;



/**
 * Utilitaires JSON
 * 
 * @author Gregory
 *
 */
class JSONUtils {

	/**
	 * Serialise un objet en json
	 * 
	 * @param writer
	 * @param object
	 * @throws SmartHomeException
	 */
	static void write(Writer writer, Object object) throws SmartHomeException {
		ObjectMapper mapper = new ObjectMapper()
		mapper.setSerializationInclusion(Include.NON_EMPTY)
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true)
		
		writer.withWriter {
			mapper.writeValue(it, object)
		}
	}
	
	
	/**
	 * Serialise json direct dans response HTTP
	 * 
	 * @param response
	 * @param object
	 * @throws SmartHomeException
	 */
	static void write(HttpServletResponse response, Object object) throws SmartHomeException {
		response.setContentType("${MimeType.JSON.getName()};charset=UTF-8")
		write(response.writer, object)
	}
}
