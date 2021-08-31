hibernate {
	generate_statistics = false
	cache.use_second_level_cache = true
	cache.use_query_cache = false
	cache.provider_class = 'org.hibernate.cache.EhCacheProvider'
	//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
	cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
	singleSession = true // configure OSIV singleSession mode
	flush.mode = 'manual' // OSIV session flush mode outside of transactional context
}

// environment specific settings
environments {
	development {
		dataSource {
			driverClassName = "org.postgresql.Driver"
			dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
			url = "jdbc:postgresql://localhost:5432/granddefi"
			username = "postgres"
			password = "${System.properties['smarthome.datasource.password']}"
			properties {
				validationQuery = "SELECT 1"
				maxActive = 10
				removeAbandoned = true
				logAbandoned = true
			}
		}
	}
	production { dataSource { jndiName = "java:comp/env/smartHomeDataSource" } }
}
