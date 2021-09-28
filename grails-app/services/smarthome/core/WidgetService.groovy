package smarthome.core

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import smarthome.security.User;


class WidgetService extends AbstractService {
	
	
	/**
	 * Recherche paginée
	 * 
	 * @param command
	 * @param pagination
	 * @return
	 */
	List<Widget> list(WidgetCommand command, Map pagination) {
		return Widget.createCriteria().list(pagination) {
			if (command.search) {
				ilike 'libelle', QueryUtils.decorateMatchAll(command.search)
			}	
			order 'libelle'
		}
	}
	
	
	/**
	 * Recherche des widgets d'un utilisateur
	 *
	 * @param userId
	 * @return
	 */
	List<WidgetUser> findAllByUserId(Long userId) {
		return WidgetUser.createCriteria().list {
			user {
				idEq userId
			}
			
			join "widget"
			order "row"
		}
	}
	
	
	/**
	 * Ajout d'un widget à un utilisateur
	 * Vérifie si le widget n'est pas déjà associé
	 *
	 * @param widget
	 * @param paramId
	 * @param userId
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	WidgetUser addWidgetUser(Widget widget, Long paramId, Long userId) throws SmartHomeException {
		def user = User.read(userId)
		def widgetUser = WidgetUser.findByWidgetAndUserAndParamId(widget, user, paramId)
		
		if (!widgetUser) {
			widgetUser = new WidgetUser(widget: widget, user: user, paramId: paramId)
			
			if (!widgetUser.save()) {
				throw new SmartHomeException("Erreur ajout widget utilisateur", widgetUser)
			}
		}
		
		return widgetUser
	}
	
	
	/**
	 * Déplace un widget par rapport aux autres
	 *
	 * @param widgetUser
	 * @param row
	 * @param col
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void moveWidgetUser(WidgetUser widgetUser, int row, int col) throws SmartHomeException {
		// charge tous les widgets de la colonne
		List<WidgetUser> widgets = WidgetUser.findAllByUserAndCol(widgetUser.user, col)
		
		// tri avant réinsertion à la bonne place
		widgets.sort { it.row }
		
		// suppression du widget s'il était déjà dans la colonne
		widgets.removeAll { it.id == widgetUser.id }
		
		// réinsertion à la bonne place et association nouvelle colonne
		widgets.add(row, widgetUser)
		widgetUser.col = col
		
		// recalcul des ordres de chaque widget
		widgets.eachWithIndex { widget, index ->
			widget.row = index
			this.save(widget)
		}
	}
}
