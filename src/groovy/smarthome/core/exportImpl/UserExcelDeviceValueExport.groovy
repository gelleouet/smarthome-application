package smarthome.core.exportImpl


import java.io.OutputStream;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.LogFactory
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired

import smarthome.automation.Chauffage
import smarthome.automation.DeviceValue;
import smarthome.automation.ECS
import smarthome.automation.House
import smarthome.automation.SupervisionCommand
import smarthome.core.ApplicationUtils
import smarthome.core.DateUtils;
import smarthome.core.DeviceValueExport
import smarthome.core.ExcelUtils
import smarthome.core.MimeTypeEnum;
import smarthome.core.SmartHomeException;
import smarthome.core.query.HQL;
import smarthome.security.User;


/**
 * Export des données utilisateur
 * 
 * Scope : request. Donc pas gênant de créer des propriétés de classe. Ca reste thread-safe
 * 
 * @author Gregory
 *
 */
class UserExcelDeviceValueExport implements DeviceValueExport {

	private static final log = LogFactory.getLog(this)
	
	
	@Autowired
	GrailsApplication grailsApplication
	
	
	private Map cacheECS = [:]
	private Map cacheChauffage = [:]
	
	
	
	@Override
	void init(SupervisionCommand command, ServletResponse response) {
		response.setContentType(MimeTypeEnum.EXCEL2003.mimeType)
		response.setHeader("Content-Disposition", "attachment; filename=export-profils.${MimeTypeEnum.EXCEL2003.extension}")
	}
	
	
	@Override
	void export(SupervisionCommand command, OutputStream outStream) throws Exception {
		Workbook workbook = new HSSFWorkbook()
		ExcelUtils excelUtils = new ExcelUtils(workbook).build()
		Sheet sheet = workbook.createSheet("Profils")
		Row row = excelUtils.createRow(sheet, 0)
		int cellIdx = 0
		
		
		// création de l'entete sur la 1ère ligne
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
		
		// les données commencent à la 2e ligne
		int rowIdx = 1
		
		int nbUser = command.visitUser(ApplicationUtils.configMaxBackend(grailsApplication)) { result ->
			User user = result[0]
			House house = result[1]
			// reset de compteur de colonnes à chaque ligne et incrément de la ligne pour la future insertion
			cellIdx = 0
			row = excelUtils.createRow(sheet, rowIdx++)
			
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
		}
		
		log.info "Export profils : $nbUser utilisateur(s)"
		
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
