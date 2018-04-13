package smarthome.automation

import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class ModeService extends AbstractService {
	
	
	
	/**
	 * Liste les modes d'un utilisateur
	 * 
	 * @param user
	 * @return
	 */
	List<Mode> listModesByUser(User user) {
		return Mode.createCriteria().list {
			eq 'user', user
			order 'name'
		}
	}
	
	
	/**
	 * Ajout d'un mode sans persistance
	 * 
	 * @param command
	 * @return
	 */
	ModeCommand addMode(ModeCommand command) {
		command.modes << new Mode()
		return command
	}
	
	
	/**
	 * Suppression d'un mode sans persistance
	 * 
	 * @param command
	 * @param status
	 * @return
	 */
	ModeCommand deleteMode(ModeCommand command, int status) {
		command.modes?.removeAll {
			it.status == status
		}
		
		return command
	}
	
	
	
	/**
	 * Enregistrement des modes pour un utilisateur
	 * 
	 * @param modes
	 * @param house
	 * 
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void saveModes(List<Mode> modes, User user) throws SmartHomeException {
		def existModes = listModesByUser(user)
		
		// enregistre d'abord les modifications
		modes.each {
			it.user = user
			this.save(it)
		}
		
		// détecte les suppression de modes
		// IMPORTANT : supprimer d'abord les associations avec ces modes (House, DeviceEvent)
		for (Mode existMode : existModes) {
			def found = modes.find {
				it.id == existMode.id
			}
			
			if (!found) {
				// suppression association House
				HouseMode.where({
					mode == existMode
				}).deleteAll()
				
				// suppression association DeviceEvent
				EventMode.where({
					mode == existMode
				}).deleteAll()
				
				existMode.delete()
			}
		}
	}
	
	
	/**
	 * Match si au moins 1 mode sélectionné est présent dans les modes actifs
	 * Si aucun mode sélectionné, ca matche aussi
	 * 
	 * Le mode peut être inversé : dans ce cas matche si tous les modes ne sont pas sélectionnés
	 * 
	 * @param selectModes
	 * @param actifModes
	 * @param inverse
	 * 
	 * @return
	 * @throws SmartHomeException
	 */
	boolean matchModes(def selectModes, def actifModes, boolean inverse) throws SmartHomeException {
		if (!selectModes) {
			return true
		}
		
		boolean defaultValue
		boolean ifFoundValue
		
		if (inverse) {
			// si aucun mode actif, alors par défaut c'est bon
			defaultValue = true
			// si au moins 1 mode est activé dans liste
			// alors c'est pas bon
			ifFoundValue = false
			
		} else {
			// si aucun mode sélectionné, alors par défaut c'est pas bon
			defaultValue = false
			// si au moins 1 mode est activé dans liste
			// alors c'est bon
			ifFoundValue = true
		}
		
		// check les modes actifs
		for (Mode mode : selectModes) {
			Mode found = actifModes.find {
				it.id == mode.id
			}
			
			if (found) {
				return ifFoundValue
			}
		}
		
		return defaultValue
	}
}
