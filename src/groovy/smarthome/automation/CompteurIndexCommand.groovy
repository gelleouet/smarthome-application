package smarthome.automation

import grails.validation.Validateable
import smarthome.core.PaginableCommand
import smarthome.core.QueryUtils
import smarthome.core.SortCommand
import smarthome.core.query.HQL
import smarthome.security.Profil
import smarthome.security.User


@Validateable
class CompteurIndexCommand extends PaginableCommand implements Serializable {
	Long deviceTypeId
	Long profilId
	Long adminId
	String userSearch
	

	static constraints = {
		deviceTypeId nullable: true
		profilId nullable: true
		userSearch nullable: true
	}


	@Override
	Object bindDefaultSort() {
		if (!sortProperties) {
			sortProperties = [
				new SortCommand(property: "user.nom"),
				new SortCommand(property: "user.prenom"),
				new SortCommand(property: "compteurIndex.dateIndex")
			]
		}
	}
		
	
	/**
	 * Bind l'admin et relance les validations
	 * 
	 * @param admin
	 * @return
	 */
	CompteurIndexCommand admin(Long adminId) {
		this.adminId = adminId
		this.clearErrors()
		this.validate()
		return this
	}
	
	
	/**
	 * Construction d'une query à partir du command
	 * 
	 * @return
	 */
	HQL query() {
		HQL hql = new HQL("compteurIndex",	"""
			FROM CompteurIndex compteurIndex
			JOIN FETCH compteurIndex.device device
			JOIN FETCH device.deviceType deviceType
			JOIN FETCH device.user user
			LEFT JOIN FETCH user.profil profil""")

		if (userSearch) {
			hql.addCriterion("lower(user.username) like :userSearch or lower(user.prenom) like :userSearch or lower(user.nom) like :userSearch",
				[userSearch: QueryUtils.decorateMatchAll(userSearch.toLowerCase())])
		}
	
		if (deviceTypeId) {
			hql.addCriterion("deviceType.id = :deviceTypeId", [deviceTypeId: deviceTypeId])
		}
	
		if (profilId) {
			hql.addCriterion("user.profil.id = :profilId", [profilId: profilId])
		}
	
		return addOrder(hql).domainClass(CompteurIndex)
	}
}
