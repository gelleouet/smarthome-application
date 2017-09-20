package smarthome.automation.notification

import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import smarthome.automation.Notification;
import smarthome.core.ExchangeType;
import smarthome.core.SmartHomeException

class SmsNotificationSender extends AbstractNotificationSender {

	private static final String PREFIX_FRANCE = "+33"
	
	@Autowired
	GrailsApplication grailsApplication
	
	
	/**
	 * Un seul objet client SNS car thread-safe
	 *
	 * Doc AWS :
	 * Service clients in the SDK are thread-safe and, for best performance,
	 * you should treat them as long-lived objects. Each client has its own connection pool resource
	 * that is shut down when the client is garbage collected
	 */
	private static AmazonSNS amazonSNS = null
	
	
	@Override
	void send(Notification notification, Map context) throws SmartHomeException {
		if (amazonSNS == null) {
			BasicAWSCredentials credentials = new BasicAWSCredentials(
				grailsApplication.config.aws.sns.accessKeyId,
				grailsApplication.config.aws.sns.secretKey,
			)
			
			// construction du service amazon
			amazonSNS = AmazonSNSClientBuilder.standard()
				.withRegion(Regions.EU_WEST_1)
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.build()
		}
		
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<>()
		
		smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
			.withStringValue("BeHomeSmart").withDataType("String"))
		smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
			.withStringValue("Promotional").withDataType("String"))
		
		String destinataires = notification.jsonParameters.to ?: notification.user.telephoneMobile
		
		if (destinataires) {
			for (String destinataire : destinataires.split(",")) {
				if (!destinataire?.startsWith(PREFIX_FRANCE)) {
					throw new SmartHomeException("Format numero téléphone incorrect ! ${destinataire}")
				} else {
					PublishResult result = amazonSNS.publish(new PublishRequest()
						.withMessage(notification.convertMessage)
						.withPhoneNumber(destinataire)
						.withMessageAttributes(smsAttributes))
				}
			}
		}
	}
}
