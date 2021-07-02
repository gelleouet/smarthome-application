package smarthome.core.query

import grails.async.Promise
import grails.async.Promises;
import grails.gorm.PagedResultList;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.ScrollMode
import org.hibernate.ScrollableResults
import org.hibernate.Session;

import smarthome.core.QueryUtils;
import smarthome.core.SmartHomeException;


/**
 * Construction d'une HQL
 * Facile l'ajout des criterion et des params
 * 
 * @author Gregory
 *
 */
class HQL {

	List<String> criterions = []
	List<String> orders = []
	List<String> groupBys = []
	Map params = [:]
	StringBuilder from = new StringBuilder()
	StringBuilder select = new StringBuilder()
	String selectCountPart
	Class domainClass

	
	public HQL() {
		
	}
	
	public HQL(String selectPart, String fromPart) {
		if (selectPart) {
			select.append("${selectPart}\n")
		}
		if (fromPart) {
			from.append(fromPart)
		}
	}
	
	
	public HQL(String selectPart, String fromPart, String selectCountPart) {
		this(selectPart, fromPart)
		this.selectCountPart = selectCountPart
	}

	
	/**
	 * Associe le domain root pour la création de session
	 *
	 * @param domainClass
	 * @return
	 */
	HQL domainClass(Class domainClass) {
		this.domainClass = domainClass
		return this
	}
	

	/**
	 * Ajout d'un criterion avec ses paramètres
	 * 
	 * @param criterion
	 * @param criterionParams
	 * @return
	 */
	HQL addCriterion(String criterion, Map criterionParams = null) {
		criterions << criterion
		if (criterionParams) {
			params.putAll(criterionParams)
		}
		return this	
	}
	
	
	/**
	 * Ajout d'un criterion LIKE
	 *
	 * @param property
	 * @param value
	 * @return
	 */
	HQL addCriterionLike(String property, String value) {
		// préfixe avec une lettre car si la propriété démarre par un chiffer ca plante
		String paramName = "A" + UUID.randomUUID().toString().replace("-", "")
		criterions << "lower($property) like lower(:$paramName)"
		params << [(paramName): QueryUtils.decorateMatchAll(value)]
		return this
	}
	
	
	/**
	 * Version spéciale criterion pour l'ajout d'un filtre sur id pouvant 
	 * prendre plusieurs formes : valeur unique, plusieurs valeurs séparées par virgule
	 * ou intervalle valeurs de la forme id1-id2
	 * Les espaces sont supportés
	 * 
	 * @param property
	 * @param ids
	 * @param prefixParam
	 * @param idType
	 * 
	 * @param 
	 * @return
	 */
	HQL addCriterionIds(String property, String ids, String prefixParam, Class idType = Long) {
		if (ids.contains("-")) {
			def tokenIds = ids.split("-")	
			criterions << "${property} BETWEEN :${prefixParam}IntervalInf AND :${prefixParam}IntervalSup"
			params.putAll(["${prefixParam}IntervalInf": tokenIds[0].trim().asType(idType),
				"${prefixParam}IntervalSup": tokenIds[1].trim().asType(idType)])
		} else if (ids.contains(",")) {
			def tokenIds = ids.split(",").toList().collect { it.trim().asType(idType) }	
			criterions << "${property} IN (:${prefixParam}List)"
			params["${prefixParam}List"] = tokenIds
		} else {
			criterions << "${property} = :${prefixParam}Value"
			params["${prefixParam}Value"] = ids.trim().asType(idType)
		}
		return this	
	}
	
	
	/**
	 * Ajout d'un criterion ORDER
	 *
	 * @param order
	 * @param orderType
	 * @return
	 */
	HQL addOrder(String order, String orderType = "ASC") {
		orders << "${order} ${orderType}"
		return this
	}
	
	
	/**
	 * Ajout d'un criterion GROUP BY
	 *
	 * @param groupBy
	 * @return
	 */
	HQL addGroupBy(String groupBy) {
		groupBys << "${groupBy}"
		return this
	}
	
	
	/**
	 * Surcharge opérateur << pour ajouter une nouvelle partie de la HQL
	 * 
	 * @param fromPart
	 * @return
	 */
	HQL leftShift(String fromPart) {
		from << "\n${fromPart}"
		return this
	}
	
	
	/**
	 * Idem leftShift mais en méthode classique
	 *
	 * @param fromPart
	 * @return
	 */
	HQL addFrom(String fromPart) {
		from << "\n${fromPart}"
		return this
	}
	
	
	/**
	 * Construction de la HQL avec tous les criterions
	 * 
	 * @return
	 */
	String build() {
		return buildHQL(false)
	}
	
	
	/**
	 * Construction de la HQL avec la projection count
	 * 
	 * @return
	 */
	String buildCount() {
		return buildHQL(true)
	}
	
	
	/**
	 * 
	 * @param select
	 * @return
	 */
	protected String buildHQL(boolean count) {
		String operator
		StringBuilder buildQuery = new StringBuilder()
		
		if (count) {
			if (selectCountPart) {
				buildQuery << "SELECT count(${selectCountPart})\n"
			} else {
				buildQuery << "SELECT count(*)\n"
			}
		} else if (select) {
			buildQuery << "SELECT ${select}\n"
		}
		
		if (count) {
			buildQuery << from.toString().replace("FETCH ", "").replace("fetch ", "")
		} else {
			buildQuery << from
		}
		
		for (int idx = 0; idx < criterions.size(); idx++) {
			operator = (idx == 0 ? "WHERE" : "AND")
			buildQuery << "\n${operator} (${criterions[idx]})"
		}
		
		if (!count && groupBys) {
			String groupBy = StringUtils.join(groupBys, ", ")
			buildQuery << "\nGROUP BY ${groupBy}"
		}
		
		if (!count && orders) {
			String ordering = StringUtils.join(orders, ", ")
			buildQuery << "\nORDER BY ${ordering}"
		}
		
		return buildQuery.toString()
	}
	
	
	/**
	 * Exécute la query et renvoit un seul élément
	 *
	 * @return
	 */
	Object get(Session session) {
		List rows = list(session)
		rows ? rows[0] : null
	}
	
	
	/**
	 * Exécute la query et renvoit un seul élément
	 *
	 * @return
	 */
	Object get() {
		domainClass.withSession { session ->
			get(session)
		}
	}
	
	
	/**
	 * Exécute la HQL query
	 *
	 * @param pagination
	 * @return
	 */
	List list(Map pagination = null) {
		domainClass.withSession { session ->
			list(session, pagination)
		}
	}
	
	
	/**
	 * Exécute la HQL query
	 * 
	 * @param session
	 * @param pagination
	 * @return
	 */
	List list(Session session, Map pagination = null) {
		Query query = createQuery(session, this.build())
		QueryUtils.bindParameters(query, params)
		PagedList result = new PagedList()

		if (pagination && pagination.max) {
			query.setMaxResults(pagination.max as Integer)
			query.setFirstResult(pagination?.offset ? pagination.offset as Integer : 0)
			result.addAll(query.list())

			result.totalCount = this.count(session)
		} else {
			result.addAll(query.list())
			result.totalCount = result.size()
		}

		return result
	}
	
	
	/**
	 * Exécute la HQL query en mode asynchrone (2 requetes sont lancées pour calculer
	 * le nombre total et ces 2 requetes sont lancées en parallèle)
	 *
	 * @param pagination
	 * @return
	 */
	List asyncList(Map pagination = null) {
		asyncList(domainClass, pagination)
	}

	
	/**
	 * Exécute la HQL query en mode asynchrone (2 requetes sont lancées pour calculer
	 * le nombre total et ces 2 requetes sont lancées en parallèle)
	 *
	 * @param domainClass pour créer la session
	 * @param pagination
	 * @return
	 */
	List asyncList(Class domainClass, Map pagination = null) {
		PagedList result = new PagedList()

		if (pagination && pagination.max) {
			// création des 2 tasks
			Promise promiseList = Promises.task { 
				domainClass.withNewSession { session ->
					Query query = createQuery(session, this.build())
					QueryUtils.bindParameters(query, params)
					query.setMaxResults(pagination.max as Integer)
					query.setFirstResult(pagination?.offset ? pagination.offset as Integer : 0)
					query.list()
				}
			}
			
			Promise promiseCount = Promises.task {
				domainClass.withNewSession { session ->
					count(session)
				}
			}
			
			// exécution des 2 tasks en // et récupère les 2 résultats
			List promiseResult = Promises.waitAll(promiseList, promiseCount)
			result.addAll(promiseResult[0])
			result.totalCount = promiseResult[1]
		} else {
			domainClass.withSession { session ->
				Query query = createQuery(session, this.build())
				QueryUtils.bindParameters(query, params)
				result.addAll(query.list())
				result.totalCount = result.size()
			}
		}
		
		return result
	}
	
	
	/**
	 * Compte le nombre de lignes de la requete
	 *
	 * @return
	 */
	int count() {
		domainClass.withSession { session ->
			count(session)
		}
	}
	
	/**
	 * Compte le nombre de lignes de la requete
	 *
	 * @param session
	 * @return
	 */
	int count(Session session) {
		Query query = createQuery(session, this.buildCount())
		QueryUtils.bindParameters(query, params)
		Number count = (Number) query.uniqueResult()
		return count == null ? 0 : count.intValue()
	}
	
	
	void visit(int maxPage, Closure closure) {
		visitSingleAndBatch(maxPage, closure, null)
	}

	
	void visit(Session session, int maxPage, Closure closure) {
		visitSingleAndBatch(session, maxPage, closure, null)
	}
	
	
	void visitBatch(int maxPage, Closure closure) {
		visitSingleAndBatch(maxPage, null, closure)
	}

	
	void visitBatch(Session session, int maxPage, Closure closure) {
		visitSingleAndBatch(session, maxPage, null, closure)
	}
	
	
	void visitSingleAndBatch(int maxPage, Closure closureItem, Closure closurePage) {
		domainClass.withSession { session ->
			visitSingleAndBatch(session, maxPage, closureItem, closurePage)
		}
	}
	
	
	/**
	 * Visite une query par lot. La query est chargée par morceau de maxPagination
	 * et chaque élément chargé est délégué à closure
	 * Un seul count est exécuté en début de méthode (si on passe par le findAll,
	 * le count sera exécuté à chaque passe)
	 *
	 * @param session
	 * @param maxPage <=0 pour désactiver la pagination et tout charger en
	 * 	une seule fois
	 * @param closureItem 3 paramètres : item, page, totalCount
	 * @param closurePage 3 paramètres : values, page, totalCount
	 */
	void visitSingleAndBatch(Session session, int maxPage, Closure closureItem, Closure closurePage) {
		maxPage = maxPage <= 0 ? Integer.MAX_VALUE : maxPage
		
		// calcule le total pour créer les pages
		int totalCount = this.count(session)

		Query query = createQuery(session, this.build())
		QueryUtils.bindParameters(query, params)

		for (int page = 0; page <= totalCount / maxPage; page++) {
			query.setMaxResults(maxPage)
			query.setFirstResult(page * maxPage)
			List values = query.list()
			
			if (closureItem) {
				values.each {
					closureItem(it, page, totalCount)
				}
			}
			
			if (closurePage) {
				closurePage(values, page, totalCount)
			}
			
			// on fliush la session à chaque page pour vider la mémoire
			// et les buffers de batch. Cela permet de ne pas dégrader l'exécution
			// au fur et à mesure que les pages se chargent
			session.flush()
		}
	}
	
	
	void scroll(Session session, int maxPage, Closure closure) {
		long totalCount = this.count(session)
		long idx = 0
		
		Query query = createQuery(session, this.build())
		QueryUtils.bindParameters(query, params)
		query.setFetchSize(maxPage)
		query.setReadOnly(true)
		query.setCacheable(false)
		
		ScrollableResults scrollResults = query.scroll(ScrollMode.FORWARD_ONLY)
		
		while (scrollResults.next()) {
			Object value = scrollResults.get(0)
			closure(value, idx, totalCount)
			idx++
		}
	}
	
	
	void scroll(int maxPage, Closure closure) {
		domainClass.withSession { session ->
			scroll(session, maxPage, closure)
		}
	}
	
	
	/**
	 * Parse une query pour en extraire les différents blocs (select, from, where, order, group)
	 * 
	 * @param query
	 * @return
	 */
	HQL parse(String query) throws SmartHomeException {
		int index
		String subQuery = query	
		
		// on extrait les éléments en commencant par la fin
		// et on les supprime de la query au fur et à mesure
		
		index = subQuery.indexOf("ORDER BY")
		
		if (index != -1) {
			orders << subQuery.substring(index).replace("ORDER BY", "")
			subQuery = subQuery.substring(0, index)
		}
		
		index = subQuery.indexOf("GROUP BY")
		
		if (index != -1) {
			groupBys << subQuery.substring(index).replace("GROUP BY", "")
			subQuery = subQuery.substring(0, index)
		}
		
		index = subQuery.indexOf("WHERE")
		
		if (index != -1) {
			criterions << subQuery.substring(index).replace("WHERE", "")
			subQuery = subQuery.substring(0, index)
		}
		
		index = subQuery.indexOf("FROM")
		
		if (index == -1) {
			throw new SmartHomeException("Le mot clé FROM est introuvable !")
		}
		
		from.append(subQuery.substring(index))
		subQuery = subQuery.substring(0, index)
		
		select.append(subQuery.replace("SELECT", ""))
		
		return this
	}
	
	
	/**
	 * Créé l'objet GORM pour exécuter la requete
	 *  
	 * @param session
	 * @param query
	 * @return
	 */
	protected Query createQuery(Session session, String query) {
		return session.createQuery(query)	
	}
	
}
