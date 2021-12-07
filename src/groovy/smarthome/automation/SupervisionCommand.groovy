package smarthome.automation

import smarthome.core.DateUtils;
import smarthome.core.PaginableCommand
import smarthome.core.QueryUtils
import smarthome.core.query.HQL
import grails.validation.Validateable;
import groovy.time.TimeCategory;


@Validateable
class SupervisionCommand extends PaginableCommand implements Serializable {
	Long deviceTypeId
	Long profilId
	Long adminId
	Long defiId
	Long userId
	String userSearch
	Date dateDebut = DateUtils.firstDayInMonth(new Date())
	Date dateFin = DateUtils.lastDayInMonth(new Date())
	String ville
	String exportImpl
	
	
	static constraints = {
		deviceTypeId nullable: true
		profilId nullable: true
		userId nullable: true
		userSearch nullable: true
		ville nullable: true
		exportImpl nullable: true
		defiId nullable: true
	}
	
	
	/**
	 * Bind l'admin et relance les validations
	 *
	 * @param admin
	 * @return
	 */
	SupervisionCommand admin(Long adminId) {
		this.adminId = adminId
		this.clearErrors()
		this.validate()
		return this
	}
	
	
	/**
	 * Calcule date/heure début en fonction dateDebut
	 * 
	 * @return
	 */
	Date datetimeDebut() {
		return DateUtils.copyTruncDay(dateDebut)
	}
	
	
	/**
	 * Calcule date/heure fin en fonction dateDebut
	 *
	 * @return
	 */
	Date datetimeFin() {
		Date datetimeFin
		
		use(TimeCategory) {
			datetimeFin = DateUtils.copyTruncDay(dateFin) + 23.hours + 59.minutes + 59.seconds
		}	
		
		return datetimeFin
	}
	
	
	List listDevice() {
		HQL hql = new HQL("device",	"""
			FROM Device device 
			JOIN FETCH device.deviceType deviceType
			JOIN FETCH device.user user
			LEFT JOIN FETCH user.profil profil""")
			.domainClass(Device)
		
		applyCriterion(hql, false, true)
			.addOrder("user.nom")
			.addOrder("user.prenom")
			.addOrder("device.label")
	
		return hql.list(pagination())
	}
	
	
	protected HQL applyCriterion(HQL hql, boolean joinHouse, boolean joinDevice) {
		//hql.addCriterion("""user.id in (select userAdmin.user.id from UserAdmin userAdmin
			//	where userAdmin.admin.id = :adminId)""", [adminId: command.adminId])
		
		if (userSearch) {
			hql.addCriterion("lower(user.username) like :userSearch or lower(user.prenom) like :userSearch or lower(user.nom) like :userSearch",
				[userSearch: QueryUtils.decorateMatchAll(userSearch.toLowerCase())])
		} else if (userId) {
			hql.addCriterion("user.id = :userId", [userId: userId])
		}
	
		if (joinDevice) {
		if (deviceTypeId) {
				hql.addCriterion("deviceType.id = :deviceTypeId", [deviceTypeId: deviceTypeId])
			}
		}
		
		if (profilId) {
			hql.addCriterion("user.profil.id = :profilId", [profilId: profilId])
		}
		
		if (ville) {
			if (joinHouse) {
				hql.addCriterion("lower(house.location) like :ville", [ville: QueryUtils.decorateMatchAll(ville.toLowerCase())])
			} else {
				hql.addCriterion("exists (select house.id from House house where house.user.id = user.id and lower(house.location) like :ville)",
					[ville: QueryUtils.decorateMatchAll(ville.toLowerCase())])
			}
		}
		
		if (defiId) {
			hql.addCriterion("exists (select participant.id from DefiEquipeParticipant participant "
				+ "join participant.defiEquipe equipe "
				+ "where participant.user.id = user.id and equipe.defi.id = :defiId)",
				[defiId: defiId])
		}
		
		return hql
	}
	
	
	int visitDeviceValue(int maxPage, Closure closure) {
		HQL hql = new HQL("deviceValue", """
			FROM DeviceValue deviceValue
			JOIN FETCH deviceValue.device device
			JOIN device.deviceType deviceType
			JOIN FETCH device.user user
			LEFT JOIN user.profil profil""")
			.domainClass(DeviceValue)
		
		applyCriterion(hql, false, true)
			.addCriterion("deviceValue.dateValue between :dateDebut and :dateFin", [dateDebut: datetimeDebut(), dateFin: datetimeFin()])
			.addCriterion("deviceValue.name is null")
			.addOrder("user.username")
			.addOrder("device.label")
			.addOrder("deviceValue.dateValue")
			
		return hql.visit(maxPage, closure)
	}
	
	
	int visitUser(int maxPage, Closure closure) {
		HQL hql = new HQL("user, house",	"""
			FROM User user, House house
			LEFT JOIN user.profil profil""")
			.domainClass(Device)
			.addCriterion("house.user = user and house.defaut = :defautHouse", [defautHouse: true])
		
		applyCriterion(hql, true, false)
			.addOrder("user.nom")
			.addOrder("user.prenom")
	
		return hql.visit(maxPage, closure)
	}
	
	
	/**
	 * Calcule les dates distinctes des valeurs sur la période
	 * 
	 * @return
	 */
	List<Date> groupDateValues() {
		HQL hql = new HQL("deviceValue.dateValue", """
			FROM DeviceValue deviceValue
			JOIN deviceValue.device device
			JOIN device.deviceType deviceType
			JOIN device.user user""")
		.domainClass(DeviceValue)
	
		applyCriterion(hql, false, true)
			.addCriterion("deviceValue.dateValue between :dateDebut and :dateFin", [dateDebut: datetimeDebut(), dateFin: datetimeFin()])
			.addCriterion("deviceValue.name is null")
			.addGroupBy("deviceValue.dateValue")
			.addOrder("deviceValue.dateValue")
			
		return hql.list()
	}
}
