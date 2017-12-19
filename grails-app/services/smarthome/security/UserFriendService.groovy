package smarthome.security

import org.springframework.transaction.annotation.Transactional;

import smarthome.automation.DeviceShareService;
import smarthome.automation.HouseService;
import smarthome.core.AbstractService
import smarthome.core.AsynchronousWorkflow;
import smarthome.core.QueryUtils;
import smarthome.core.SmartHomeException


/**
 * 
 * @author gregory
 *
 */
class UserFriendService extends AbstractService {
	DeviceShareService deviceShareService
	HouseService houseService
	
	
	
	/**
	 * Contrôle bloquant si pas ami
	 * 
	 * @param user
	 * @param friend
	 * @return
	 */
	UserFriend assertFriend(User user, User friend) throws SmartHomeException {
		UserFriend userFriend = isFriend(user, friend)
		
		if (!userFriend) {
			throw new SmartHomeException("Accès refusé !")
		}
		
		return userFriend	
	}
	
	
	/**
	 * Users amis ?
	 * 
	 * @param user
	 * @param friend
	 * @return
	 */
	UserFriend isFriend(User user, User friend) {
		return UserFriend.findByUserAndFriendAndConfirm(user, friend, true)
	}
	
	/**
	 * Recherche d'abonnements
	 *  
	 * @param command
	 * @param pagination
	 * @return
	 * @throws SmartHomeException
	 */
	List<UserFriend> searchFollowing(UserFriendCommand command, Map pagination) throws SmartHomeException {
		return 	UserFriend.createCriteria().list(pagination) {
			eq 'user.id', command.userId
			eq 'confirm', command.confirm
			
			friend {
				if (command.search) {
					def search = QueryUtils.decorateMatchAll(command.search)
					or {
						ilike 'nom', search
						ilike 'prenom', search
						ilike 'username', search
					}	
				}
				
				order "prenom"
				order "nom"
			}
		}	
	}
	
	
	/**
	 * Recherche d'abonnés
	 *
	 * @param command
	 * @param pagination
	 * @return
	 * @throws SmartHomeException
	 */
	List<UserFriend> searchFollower(UserFriendCommand command, Map pagination) throws SmartHomeException {
		return 	UserFriend.createCriteria().list(pagination) {
			eq 'friend.id', command.userId
			eq 'confirm', command.confirm
			
			user {
				if (command.search) {
					def search = QueryUtils.decorateMatchAll(command.search)
					or {
						ilike 'nom', search
						ilike 'prenom', search
						ilike 'username', search
					}
				}
				
				order "prenom"
				order "nom"
			}
		}
	}
	
	
	/**
	 * Demande invitation
	 * 
	 * @param user
	 * @param friend
	 * @return
	 */
	@AsynchronousWorkflow("userFriendService.inviteFriend")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	UserFriend inviteFriend(User user, User friend) throws SmartHomeException {
		UserFriend userFriend = UserFriend.findByUserAndFriend(user, friend)
		
		if (userFriend) {
			if (userFriend.confirm) {
				throw new SmartHomeException("Vous êtes déjà ami avec ${friend.prenomNom} !")
			} else {
				throw new SmartHomeException("Vous avez déjà envoyé une invitation à ${friend.prenomNom} !")
			}
		}
		
		// vérifie aussi qu'on a pas déjà une invitation à confirmer
		userFriend = UserFriend.findByUserAndFriend(friend, user)
		
		if (userFriend) {
			throw new SmartHomeException("Vous avez déjà reçu une invitation de ${friend.prenomNom} !")
		}
		
		userFriend = new UserFriend(user: user, friend: friend, confirm: false)
		
		return this.save(userFriend)
	}
	
	
	/**
	 * Annulation d'une invitation
	 *
	 * @param user
	 * @param friend
	 * @return
	 */
	@AsynchronousWorkflow("userFriendService.cancelFriend")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void cancelFriend(User user, UserFriend userFriend) throws SmartHomeException {
		// seul le user ayant lancé l'invit ou l'ami l'ayant reçu peut l'annuler
		if (user.id != userFriend.user.id && user.id != userFriend.friend.id) {
			throw new SmartHomeException("Vous n'avez pas les autorisations suffisantes !")
		}
		
		userFriend.delete()	
	}
	
	
	/**
	 * Suppression d'un ami
	 *
	 * @param user
	 * @param friend
	 * @return
	 */
	@AsynchronousWorkflow("userFriendService.deleteFriend")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void deleteFriend(User user, UserFriend userFriend) throws SmartHomeException {
		// seul le user ou l'ami peut supprimer la relation
		if (user.id != userFriend.user.id && user.id != userFriend.friend.id) {
			throw new SmartHomeException("Vous n'avez pas les autorisations suffisantes !")
		}
		
		userFriend.delete()	
		
		// recherche de la relation réciproque
		UserFriend reciproqFriend = UserFriend.findByUserAndFriend(userFriend.friend, userFriend.user)
		
		if (reciproqFriend) {
			reciproqFriend.delete()
		}
		
		// suppression des objets partagés entre chaque user
		deviceShareService.deleteAllShare(userFriend.user, userFriend.friend)
		deviceShareService.deleteAllShare(userFriend.friend, userFriend.user)
	}
	
	
	/**
	 * Confirmation d'une invitation
	 * Une relation réciproque est créée entre les 2 utilisateurs
	 *
	 * @param user
	 * @param friend
	 * @return
	 */
	@AsynchronousWorkflow("userFriendService.confirmFriend")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void confirmFriend(User user, UserFriend userFriend) throws SmartHomeException {
		// seul le user destinataire de l'invit peut la confirmer
		if (user.id != userFriend.friend.id) {
			throw new SmartHomeException("Vous n'avez pas les autorisations suffisantes !")
		}
		
		userFriend.confirm = true
		this.save(userFriend)
		
		// création relation réciproque sur l'ami
		UserFriend reciproqFriend = UserFriend.findByUserAndFriend(user, userFriend.user)
		
		if (!reciproqFriend) {
			reciproqFriend = new UserFriend(user: user, friend: userFriend.user)
		} 
		
		reciproqFriend.confirm = true
		this.save(reciproqFriend)
		
		// ajout des objets par défaut des maisons de chaque user
		houseService.shareDefaultHouse(userFriend.user, userFriend.friend)
		houseService.shareDefaultHouse(userFriend.friend, userFriend.user)
	}
}
