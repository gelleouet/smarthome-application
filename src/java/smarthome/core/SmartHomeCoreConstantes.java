/**
 * 
 */
package smarthome.core;

/**
 * @author gregory
 *
 */
public class SmartHomeCoreConstantes {

	public static final String DEFAULT_SCHEMA = "smarthome";
	
	public static final String DATE_BINDING_FORMAT = "yyyy-MM-dd";
	public static final String DATE_USER_FORMAT = "dd/MM/yyyy";

	public static final String SESSION_LAST_URI = "request.lastURI";

	public static final String GRAILS_SERVICE_CACHE = "grailsServiceCache";
	
	public static final String DIRECT_EXCHANGE = "amq.direct";
	public static final String FANOUT_EXCHANGE = "amq.fanout";
	
	public static final String WORKFLOW_QUEUE = "smarthome.core.workflow";
	public static final String EVENT_QUEUE = "smarthome.core.event";
	public static final String EMAIL_QUEUE = "smarthome.core.email";
}
