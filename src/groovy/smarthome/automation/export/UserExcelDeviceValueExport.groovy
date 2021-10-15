package smarthome.automation.export


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
		Sheet sheet = workbook.createSheet("Profils")
		ExcelUtils excelUtils = new ExcelUtils(workbook).build()
		int cellIdx=1, rowIdx=0
		Row row
		
		
		// création de l'entete sur la 1ère colonne
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("Profil")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("Nom")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("Prénom")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("Numéro et rue")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("Code postal")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("Commune")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("Adresse courriel")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("Numéro de téléphone")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("A utiliser mon adresse e-mail et mon numéro de téléphone pour me contacter dans le cadre du Grand Défi Energie et Eau (nécessaire pour le bon déroulement du défi)")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("A récupérer mes données de consommation d’énergie et d’eau à usage exclusif du Grand Défi Energie et Eau (nécessaire pour le bon déroulement du défi)")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("A me faire apparaître dans la liste des participants de mon équipe.")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("A transmettre mon adresse e-mail et mon numéro de téléphone à ma commune/ma mairie pour me contacter dans le cadre du Grand Défi Energie et Eau.")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("Créer mon compte client Enedis https://mon-compte-particulier.enedis.fr et à transmettre mes relevés de compteurs d’énergie et d’eau avant et pendant le Grand Défi Energie et Eau (nécessaire pour le bon déroulement du défi)")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("Nombre de personnes dans le foyer (vous compris)")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("Surface (m²)")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("Energie de chauffage principal")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("Energie de chauffage secondaire (= appoint)")
		excelUtils.createOrGetCell(sheet, rowIdx++, 0).setCellValue("Eau chaude sanitaire")
		
		
		int nbUser = command.visitUser(ApplicationUtils.configMaxBackend(grailsApplication)) { result ->
			User user = result[0]
			House house = result[1]
			rowIdx = 0
			
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(user.profil?.libelle)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(user.nom)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(user.prenom)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(house.adresse)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(house.codePostal)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(house.location)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(user.username)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(user.telephoneMobile)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(user.autorise_user_data)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(user.autorise_conso_data)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(user.profilPublic)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(user.autorise_share_data)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(user.engage_enedis_account)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(house.nbPersonne)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(house.surface)
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(chauffage(house.chauffage?.id))
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(chauffage(house.chauffageSecondaire?.id))
			excelUtils.createOrGetCell(sheet, rowIdx++, cellIdx).setCellValue(ecs(house.ecs?.id))
			
			cellIdx++
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
