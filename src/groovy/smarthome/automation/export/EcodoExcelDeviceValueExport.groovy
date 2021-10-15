/**
 * 
 */
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

import smarthome.automation.DeviceValue;
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
 * Implémentation ECODO
 * 
 * Scope : request. Donc pas gênant de créer des propriétés de classe. Ca reste thread-safe
 * 
 * @author Gregory
 *
 */
class EcodoExcelDeviceValueExport implements DeviceValueExport {

	private static final log = LogFactory.getLog(this)
	
	
	@Autowired
	GrailsApplication grailsApplication
	
	
	/**
	 * Export admin :
	 * Les valeurs sont affichées en colonne (1 colonne par date) et une ligne par utilisateur
	 * Pour que les dates correspondantes, il faut d'abord calculer les dates distinctes de la
	 * recherche et venir insérer chaque index dans la bonne colonne
	 */
	@Override
	void exportAdmin(SupervisionCommand command, OutputStream outStream) throws Exception {
		Workbook workbook = new HSSFWorkbook()
		ExcelUtils excelUtils = new ExcelUtils(workbook).build()
		int cellIdx
		Sheet sheet
		Row row
		
		
		// création de l'entete avec les dates distinctes en colonne
		// référence sur les index de colonne des dates
		// plus facile après pour venir insérer un index dans la bonne colonne
		Map dateCellMap = [:]
		List<Date> dateValues = command.groupDateValues()
		sheet = workbook.createSheet("ECODO")
		cellIdx = 0
		row = sheet.createRow(0)
		excelUtils.createHeaderCell(row, cellIdx++, "Email")
		excelUtils.createHeaderCell(row, cellIdx++, "Prénom")
		excelUtils.createHeaderCell(row, cellIdx++, "Nom")
		excelUtils.createHeaderCell(row, cellIdx++, "Adresse")
		excelUtils.createHeaderCell(row, cellIdx++, "Code postal")
		excelUtils.createHeaderCell(row, cellIdx++, "Ville")
		excelUtils.createHeaderCell(row, cellIdx++, "Foyer")
		excelUtils.createHeaderCell(row, cellIdx++, "Autorisation exploitation")
		excelUtils.createHeaderCell(row, cellIdx++, "Autorisation publication")
		excelUtils.createHeaderCell(row, cellIdx++, "Date autorisation")
		// attention aux dates avec heure car on veut une seule colonne 
		for (Date date : dateValues) {
			Date clearDate = date.clearTime()
			
			if (!dateCellMap.containsKey(clearDate)) {
				excelUtils.createHeaderCell(row, cellIdx, clearDate.format("dd/MM/yyyy"))
				dateCellMap[(clearDate)] = cellIdx
				cellIdx++
			}
		}
		
		// prépare les lignes par utilisateur en gardant une référence de la ligne pour le user
		// les données sont chargées par lot pour éviter trop de données en mémoire
		// en même temps
		Map clientRowMap = [:]
		int rowIdx = 1
		
		int nbUser = command.visitUser(ApplicationUtils.configMaxBackend(grailsApplication)) { result ->
			row = sheet.createRow(rowIdx)
			cellIdx = 0
			User user = result[0]
			House house = result[1]
			
			row.createCell(cellIdx++).setCellValue(user.username)
			row.createCell(cellIdx++).setCellValue(user.prenom)
			row.createCell(cellIdx++).setCellValue(user.nom)
			row.createCell(cellIdx++).setCellValue(house?.adresse)
			row.createCell(cellIdx++).setCellValue(house?.codePostal)
			row.createCell(cellIdx++).setCellValue(house?.location)
			row.createCell(cellIdx++).setCellValue(house?.nbPersonne)
			row.createCell(cellIdx++).setCellValue(user.acceptUseData)
			row.createCell(cellIdx++).setCellValue(user.acceptPublishData)
			excelUtils.createDateCell(row, cellIdx++, user.lastActivation)
			
			clientRowMap[(user.id)] = rowIdx
			rowIdx++
		}
		
		log.info "Export admin ECODO : $nbUser utilisateur(s)"
		
		
		// charge les données par lot et insére les index dans la bonne cellule en fonction
		// user et date index
		
		int nbValue = command.visitDeviceValue(ApplicationUtils.configMaxBackend(grailsApplication)) { DeviceValue deviceValue ->
			Date dateValue = deviceValue.dateValue.clone().clearTime()
			
			// recherche position cellule 
			// logiquement on doit forcément en trouver une mais on vérifie quand même
			if (!clientRowMap.containsKey(deviceValue.device.user.id)) {
				throw new SmartHomeException("Utilisateur non référencé !")
			}
			if (!dateCellMap.containsKey(dateValue)) {
				throw new SmartHomeException("Date index non référencé !")
			}
			
			excelUtils.createOrGetCell(sheet, clientRowMap[deviceValue.device.user.id], dateCellMap[dateValue])
				.setCellValue(deviceValue.value)
		}
		
		log.info "Export admin ECODO : $nbValue valeur(s)"
		
		workbook.write(outStream)
	}

	
	@Override
	void exportUser(SupervisionCommand command, OutputStream outStream) throws Exception {
		throw new SmartHomeException("Fonctionnalité pas implémentée !", command)
		
	}
	
	
	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.export.DeviceValueExport#initExportAdmin(smarthome.automation.ExportCommand, javax.servlet.ServletResponse)
	 */
	@Override
	void initExportAdmin(SupervisionCommand command, ServletResponse response) {
		response.setContentType(MimeTypeEnum.EXCEL2003.mimeType)
		response.setHeader("Content-Disposition", "attachment; filename=export-ecodo-${command.dateDebut.format('yyyyMMdd')}-${command.dateFin.format('yyyyMMdd')}.${MimeTypeEnum.EXCEL2003.extension}")
	}


	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.export.DeviceValueExport#initExportUser(smarthome.automation.ExportCommand, javax.servlet.ServletResponse)
	 */
	@Override
	void initExportUser(SupervisionCommand command, ServletResponse response) {
		// TODO Auto-generated method stub
		
	}
}
