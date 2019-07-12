class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?(.$format)?" {
			constraints {
				// apply constraints here
			}
		}
		"/"(controller: 'tableauBord', action: 'index')
		"500"(view:'/error')
		"404"(view:'/error')

		/**
		 * Les accès à l'API Smarthome
		 */
		group "/api", {
			"/device/push" (controller: "deviceApi", action: "push", method: "POST")
			"/device/fetch" (controller: "deviceApi", action: "fetch", method: "POST")
		}

		/**
		 * API DataConnect d'Enedis
		 */
		group "/dataconnect", {
			"/redirect" (controller: "dataConnect", action: "redirect")
			"/authorize" (controller: "dataConnect", action: "authorize")
			"/refresh_token" (controller: "dataConnect", action: "refresh_token")
			"/daily_consumption" (controller: "dataConnect", action: "daily_consumption")
			"/consumption_load_curve" (controller: "dataConnect", action: "consumption_load_curve")
			"/daily_consumption" (controller: "dataConnect", action: "daily_consumption")
			"/consumption_max_power" (controller: "dataConnect", action: "consumption_max_power")
		}
	}
}
