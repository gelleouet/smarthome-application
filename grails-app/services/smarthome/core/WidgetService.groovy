package smarthome.core

import org.springframework.transaction.annotation.Transactional;


class WidgetService extends AbstractService {
	CacheService cacheService
	
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Widget save(Widget widget) throws SmartHomeException {
		// le template widget est mis en cache car la compilation prend du temps
		// on vide le cache pour rachraichir le widget
		return widget
	}
}
