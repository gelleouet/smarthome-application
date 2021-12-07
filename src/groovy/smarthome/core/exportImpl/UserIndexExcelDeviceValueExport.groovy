package smarthome.core.exportImpl


import java.io.OutputStream;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.LogFactory
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired

import smarthome.automation.Chauffage
import smarthome.automation.DeviceValue;
import smarthome.automation.ECS
import smarthome.automation.House
import smarthome.automation.SupervisionCommand
import smarthome.automation.deviceType.AbstractDeviceType
import smarthome.automation.deviceType.Compteur
import smarthome.automation.deviceType.CompteurEau
import smarthome.automation.deviceType.CompteurGaz
import smarthome.automation.deviceType.TeleInformation
import smarthome.core.ApplicationUtils
import smarthome.core.DateUtils;
import smarthome.core.DeviceValueExport
import smarthome.core.ExcelUtils
import smarthome.core.MimeTypeEnum;
import smarthome.core.SmartHomeException;
import smarthome.core.query.HQL;
import smarthome.security.User;


/**
 * Export des données utilisateur avec les index des différents compteurs (elec, gaz, eau)
 * 
 * Scope : request. Donc pas gênant de créer des propriétés de classe. Ca reste thread-safe
 * 
 * @author Gregory
 *
 */
class UserIndexExcelDeviceValueExport implements DeviceValueExport {

	private static final log = LogFactory.getLog(this)
	private static final String KEY_ROW_IDX = "rowIdx"
	private static final int MAX_INDEX = 5
	
	
	@Autowired
	GrailsApplication grailsApplication
	
	
	private Map cacheECS = [:]
	private Map cacheChauffage = [:]
	
	
	
	@Override
	void init(SupervisionCommand command, ServletResponse response) {
		String fileName = "export-profils-datas-${DateUtils.formatDate(command.dateDebut)}-${DateUtils.formatDate(command.dateFin)}.${MimeTypeEnum.EXCEL2003.extension}"
		response.setContentType(MimeTypeEnum.EXCEL2003.mimeType)
		response.setHeader("Content-Disposition", "attachment; filename=${ fileName }")
	}
	
	
	@Override
	void export(SupervisionCommand command, OutputStream outStream) throws Exception {
		Workbook workbook = new HSSFWorkbook()
		ExcelUtils excelUtils = new ExcelUtils(workbook).build()
		Sheet sheet = workbook.createSheet("Profils - Données")
		Row row = excelUtils.createRow(sheet, 0)
		User user
		House house
		int cellIdx = 0
		int rowIdx = 1
		// maintient les positions des index des différents compteurs
		int startCellIdxElec
		int startCellIdxGaz
		int startCellIdxEau
		int startCellIdx
		int nbIndex
		// maintient des infos de chaque utilisateur pour injecter ensuite les index
		// ex n° ligne, nombre d'index par compteur, etc.
		Map<Long, Map> configByUser = [:]
		
		
		// création de l'entete sur la 1ère ligne
		// D'abord les infos utilisateur
		excelUtils.createHeaderCell(row, cellIdx++, "Profil")
		excelUtils.createHeaderCell(row, cellIdx++, "Nom")
		excelUtils.createHeaderCell(row, cellIdx++, "Prénom")
		excelUtils.createHeaderCell(row, cellIdx++, "Numéro et rue")
		excelUtils.createHeaderCell(row, cellIdx++, "Code postal")
		excelUtils.createHeaderCell(row, cellIdx++, "Commune")
		excelUtils.createHeaderCell(row, cellIdx++, "Adresse courriel")
		excelUtils.createHeaderCell(row, cellIdx++, "Numéro de téléphone")
		excelUtils.createHeaderCell(row, cellIdx++, "A utiliser mon adresse e-mail et mon numéro de téléphone pour me contacter dans le cadre du Grand Défi Energie et Eau")
		excelUtils.createHeaderCell(row, cellIdx++, "A récupérer mes données de consommation d’énergie et d’eau à usage exclusif du Grand Défi Energie et Eau")
		excelUtils.createHeaderCell(row, cellIdx++, "A me faire apparaître dans la liste des participants de mon équipe.")
		excelUtils.createHeaderCell(row, cellIdx++, "A transmettre mon adresse e-mail et mon numéro de téléphone à ma commune/ma mairie pour me contacter dans le cadre du Grand Défi Energie et Eau.")
		excelUtils.createHeaderCell(row, cellIdx++, "Créer mon compte client Enedis https://mon-compte-particulier.enedis.fr et à transmettre mes relevés de compteurs d’énergie et d’eau avant et pendant le Grand Défi Energie et Eau")
		excelUtils.createHeaderCell(row, cellIdx++, "Nombre de personnes dans le foyer")
		excelUtils.createHeaderCell(row, cellIdx++, "Surface (m²)")
		excelUtils.createHeaderCell(row, cellIdx++, "Energie de chauffage principal")
		excelUtils.createHeaderCell(row, cellIdx++, "Energie de chauffage secondaire")
		excelUtils.createHeaderCell(row, cellIdx++, "Eau chaude sanitaire")
		
		// ensuite les index et consos du compteur elec
		startCellIdxElec = cellIdx
		excelUtils.createHeaderCell(row, cellIdx++, "ELEC - Index 1", IndexedColors.LIGHT_YELLOW)
		excelUtils.createHeaderCell(row, cellIdx++, "ELEC - Index 2", IndexedColors.LIGHT_YELLOW)
		excelUtils.createHeaderCell(row, cellIdx++, "ELEC - Index 3", IndexedColors.LIGHT_YELLOW)
		excelUtils.createHeaderCell(row, cellIdx++, "ELEC - Index 4", IndexedColors.LIGHT_YELLOW)
		excelUtils.createHeaderCell(row, cellIdx++, "ELEC - Index 5", IndexedColors.LIGHT_YELLOW)
		excelUtils.createHeaderCell(row, cellIdx++, "ELEC - Consommation référence", IndexedColors.LIGHT_YELLOW)
		excelUtils.createHeaderCell(row, cellIdx++, "ELEC - Consommation action", IndexedColors.LIGHT_YELLOW)
		excelUtils.createHeaderCell(row, cellIdx++, "ELEC - Différence", IndexedColors.LIGHT_YELLOW)
		excelUtils.createHeaderCell(row, cellIdx++, "ELEC - Evolution", IndexedColors.LIGHT_YELLOW)
		excelUtils.createHeaderCell(row, cellIdx++, "ELEC - Moyenne évolution", IndexedColors.LIGHT_YELLOW)
		excelUtils.createHeaderCell(row, cellIdx++, "ELEC - Economies", IndexedColors.LIGHT_YELLOW)
		
		// ensuite les index et consos du compteur gaz
		startCellIdxGaz = cellIdx
		excelUtils.createHeaderCell(row, cellIdx++, "GAZ - Index 1", IndexedColors.LIGHT_GREEN)
		excelUtils.createHeaderCell(row, cellIdx++, "GAZ - Index 2", IndexedColors.LIGHT_GREEN)
		excelUtils.createHeaderCell(row, cellIdx++, "GAZ - Index 3", IndexedColors.LIGHT_GREEN)
		excelUtils.createHeaderCell(row, cellIdx++, "GAZ - Index 4", IndexedColors.LIGHT_GREEN)
		excelUtils.createHeaderCell(row, cellIdx++, "GAZ - Index 5", IndexedColors.LIGHT_GREEN)
		excelUtils.createHeaderCell(row, cellIdx++, "GAZ - Consommation référence", IndexedColors.LIGHT_GREEN)
		excelUtils.createHeaderCell(row, cellIdx++, "GAZ - Consommation action", IndexedColors.LIGHT_GREEN)
		excelUtils.createHeaderCell(row, cellIdx++, "GAZ - Différence", IndexedColors.LIGHT_GREEN)
		excelUtils.createHeaderCell(row, cellIdx++, "GAZ - Evolution", IndexedColors.LIGHT_GREEN)
		excelUtils.createHeaderCell(row, cellIdx++, "GAZ - Moyenne évolution", IndexedColors.LIGHT_GREEN)
		excelUtils.createHeaderCell(row, cellIdx++, "GAZ - Economies", IndexedColors.LIGHT_GREEN)
		
		// ensuite les index et consos du compteur eau
		startCellIdxEau = cellIdx
		excelUtils.createHeaderCell(row, cellIdx++, "EAU - Index 1", IndexedColors.LIGHT_BLUE)
		excelUtils.createHeaderCell(row, cellIdx++, "EAU - Index 2", IndexedColors.LIGHT_BLUE)
		excelUtils.createHeaderCell(row, cellIdx++, "EAU - Index 3", IndexedColors.LIGHT_BLUE)
		excelUtils.createHeaderCell(row, cellIdx++, "EAU - Index 4", IndexedColors.LIGHT_BLUE)
		excelUtils.createHeaderCell(row, cellIdx++, "EAU - Index 5", IndexedColors.LIGHT_BLUE)
		excelUtils.createHeaderCell(row, cellIdx++, "EAU - Consommation référence", IndexedColors.LIGHT_BLUE)
		excelUtils.createHeaderCell(row, cellIdx++, "EAU - Consommation action", IndexedColors.LIGHT_BLUE)
		excelUtils.createHeaderCell(row, cellIdx++, "EAU - Différence", IndexedColors.LIGHT_BLUE)
		excelUtils.createHeaderCell(row, cellIdx++, "EAU - Evolution", IndexedColors.LIGHT_BLUE)
		excelUtils.createHeaderCell(row, cellIdx++, "EAU - Moyenne évolution", IndexedColors.LIGHT_BLUE)
		excelUtils.createHeaderCell(row, cellIdx++, "EAU - Economies", IndexedColors.LIGHT_BLUE)
		
		// résultat global
		excelUtils.createHeaderCell(row, cellIdx++, "GLOBAL - Economies")
		excelUtils.createHeaderCell(row, cellIdx++, "GLOBAL - Classement")
		
		
		// PHASE 1 : on commence par créer toutes les lignes pour chaque utilisateur
		
		int nbUser = command.visitUser(ApplicationUtils.configMaxBackend(grailsApplication)) { result ->
			user = result[0]
			house = result[1]
			// reset de compteur de colonnes à chaque ligne et incrément de la ligne pour la future insertion
			cellIdx = 0
			row = excelUtils.createRow(sheet, rowIdx)
			configByUser.put(user.id, [(KEY_ROW_IDX): rowIdx])
			
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(user.profil?.libelle)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(user.nom)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(user.prenom)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(house.adresse)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(house.codePostal)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(house.location)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(user.username)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(user.telephoneMobile)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(user.autorise_user_data)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(user.autorise_conso_data)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(user.profilPublic)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(user.autorise_share_data)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(user.engage_enedis_account)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(house.nbPersonne)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(house.surface)
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(chauffage(house.chauffage?.id))
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(chauffage(house.chauffageSecondaire?.id))
			excelUtils.createOrGetCell(row, cellIdx++).setCellValue(ecs(house.ecs?.id))
			
			rowIdx++
		}
		
		log.info "Export profils et données : $nbUser utilisateur(s)"
		
		// PHASE 2 : on charge les index et on les injecte sur les lignes déjà créées
		// des utilisateurs. Il faut calculer pour chaque index dans quelle colonne l'ajouter
		// en fonction de son indice. Pour cela, les index doivent être triés chronologiquement
		
		int nbValues = command.visitDeviceValue(ApplicationUtils.configMaxBackend(grailsApplication)) { DeviceValue deviceValue ->
			// détermination du type de compteur (elec, gaz, eau), ceci afin d'injecter 
			// les index dans les bonnes colonnes.
			// Cas spécifique DataConnect : il n'y a pas d'index mais que des consos
			// on ne peut pas les exporter
			AbstractDeviceType deviceTypeImpl = deviceValue.device.newDeviceImpl()
			startCellIdx = -1
			user = deviceValue.device.user
			
			// on s'assure que le user est bien présent dans le fichier
			Map configUser = configByUser.get(user.id)
			
			if (!configUser) {
				throw new SmartHomeException("User ${user.id} not found in file !")
			}
			
			// ne traite pas les compteurs connectés et traite uniquement des compteurs
			if (deviceTypeImpl instanceof Compteur && !deviceTypeImpl.isConnected(grailsApplication)) {
				if (deviceTypeImpl instanceof CompteurEau) {
					startCellIdx = startCellIdxEau
				} else if (deviceTypeImpl instanceof CompteurGaz) {
					// on  ne traite pas le ADICT car pas d'index
					
					startCellIdx = startCellIdxGaz
				} else if (deviceTypeImpl instanceof TeleInformation) {
					startCellIdx = startCellIdxElec
				}
			}
			
			if (startCellIdx != -1) {
				// ligne du user
				rowIdx = configUser.get(KEY_ROW_IDX)
				// calcul indice du compteur
				nbIndex = configUser.get(deviceValue.device.id) ?: 0
				
				if (nbIndex >= MAX_INDEX) {
					throw new SmartHomeException("${ MAX_INDEX } index maximum !")
				}
				
				excelUtils.createOrGetCell(sheet, rowIdx, startCellIdx + nbIndex).setCellValue(deviceValue.value)
				
				// enregistre le nouveau nombre d'index
				configUser.put(deviceValue.device.id, ++nbIndex)
			}
		}
		
		log.info "Export profils et données : $nbValues index(s)"
		
		workbook.write(outStream)
	}
	
	
	private String ecs(Long ecsId) {
		ECS ecs
		
		if (ecsId) {
			if (cacheECS.containsKey(ecsId)) {
				ecs = cacheECS.get(ecsId)
			} else {
				ecs = ECS.read(ecsId)
				cacheECS.put(ecsId, ecs)
			}
		}
		
		return ecs?.libelle
	}
	
	
	private String chauffage(Long chauffageId) {
		Chauffage chauffage
		
		if (chauffageId) {
			if (cacheChauffage.containsKey(chauffageId)) {
				chauffage = cacheChauffage.get(chauffageId)
			} else {
				chauffage = Chauffage.read(chauffageId)
				cacheChauffage.put(chauffageId, chauffage)
			}
		}
		
		return chauffage?.libelle
	}

}
