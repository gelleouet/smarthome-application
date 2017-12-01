package smarthome.security

import smarthome.core.AbstractService;
import smarthome.core.SmartHomeException;
import smarthome.security.User;
import org.springframework.transaction.annotation.Transactional;


class UserConversationService extends AbstractService {
	
	/**
	 * Enregistre la conversation. A chaque fois les anciennes conversation (id différent)
	 * sont purgées
	 * 
	 * @param username
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
    UserConversation registerConversation(UserConversation userConversation) throws SmartHomeException {
		UserConversation.executeUpdate("delete UserConversation where user = :user and conversationId != :conversationId",
			[user: userConversation.user, conversationId: userConversation.conversationId])
		
		userConversation.dateQuery = new Date()
		return this.save(userConversation)	
    }
	
	
	/**
	 * Retrouve le dernier message d'une conversation
	 * 
	 * @param user
	 * @param conversationId
	 * @return
	 */
	List<UserConversation> conversation(User user, String conversationId, int max = 25) {
		return UserConversation.createCriteria().list {
			eq 'user', user
			eq 'conversationId', conversationId
			order 'dateQuery'
			maxResults max
		}
	}
}
