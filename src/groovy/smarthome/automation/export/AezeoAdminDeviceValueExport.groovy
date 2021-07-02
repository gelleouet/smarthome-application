package smarthome.automation.export

import javax.servlet.ServletResponse

import org.apache.commons.logging.LogFactory
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired

import smarthome.automation.Device
import smarthome.automation.DeviceValue
import smarthome.automation.ExportCommand
import smarthome.core.ApplicationUtils
import smarthome.core.DateUtils
import smarthome.core.ExcelUtils
import smarthome.core.MimeTypeEnum
import smarthome.core.query.HQL


/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class AezeoAdminDeviceValueExport implements DeviceValueExport {

	private static final log = LogFactory.getLog(this)
	
	
	@Autowired
	GrailsApplication grailsApplication
	
	
	@Override
	String libelle() {
		"AEZEO ADMIN"
	}
	
	
	@Override
	public void init(ExportCommand command, ServletResponse response) throws Exception {
		response.setContentType(MimeTypeEnum.EXCEL2003.mimeType)
		response.setHeader("Content-Disposition", "attachment; filename=aezeoadmin-${command.dateDebut.format('yyyyMMdd')}-${command.dateFin.format('yyyyMMdd')}.${MimeTypeEnum.EXCEL2003.extension}")
	}

	
	@Override
	public void export(ExportCommand command, OutputStream outStream) throws Exception {
		Workbook workbook = new HSSFWorkbook()
		ExcelUtils excelUtils = new ExcelUtils(workbook).build()
		
		HQL query = new HQL("deviceValue", """
			FROM DeviceValue deviceValue
			JOIN FETCH deviceValue.device device
			JOIN FETCH device.user user
			JOIN device.deviceType deviceType
			""")
			.domainClass(DeviceValue)
			.addOrder("user.nom")
			.addOrder("user.prenom")
			.addOrder("deviceValue.dateValue")
			.addOrder("device.label")
			.addOrder("deviceValue.name")
			
		command.applyCriterion(query)
		
		
		int headerCellIdx = 0
		int globalRowIdx = 1
		Sheet sheet = excelUtils.createSheet(workbook, "export")
		Row headerRow = excelUtils.createOrGetRow(sheet, 0)
		Row currentRow
		Cell currentCell
		// référence d'une ligne pour une date. Comme on traite les values une par une
		// on ne connnait pas à l'avance les dates à traiter. On va aussi travailler par batch 
		// donc on est obligé de conserver chaque date différente et de connaitre la ligne associée
		// une ligne par objet et date
		Map idxRowMap = [:]
		// on va garder une référence sur la colonne de chaque metavalue
		// car on ne les connait pas à l'avance. Dès qu'une nouvelle est détectée
		// une nouvelle colonne est créée et son indice est conservé avec son nom
		// pour les suivantes
		Map idxCellMap = [:]
		
		
		// construction de l'entete "statique"
		excelUtils.createHeaderCell(headerRow, headerCellIdx++, "Date")
		excelUtils.createHeaderCell(headerRow, headerCellIdx++, "Nom")
		excelUtils.createHeaderCell(headerRow, headerCellIdx++, "Prénom")
		excelUtils.createHeaderCell(headerRow, headerCellIdx++, "Email")
		excelUtils.createHeaderCell(headerRow, headerCellIdx++, "Objet")
		// le reste des colonnes (par metavalues) sera créé au fur et à mesure
		
		// charge les données par lot et insére les index dans la bonne cellule en fonction
		// date, objet et metavalue
		
		query.scroll(ApplicationUtils.configMaxBackend(grailsApplication)) { DeviceValue deviceValue, long idx, long totalCount ->
			if (idx == 0) {
				log.info "Export ${this.class.simpleName} found $totalCount value(s)"
			}
			
			String rowKey = "${deviceValue.device.id}#${DateUtils.formatDateTimeIso(deviceValue.dateValue) }"
			String cellKey = deviceValue.name ?: DeviceValue.DEFAULT_NAME
			
			// récupère la ligne ou en crée une nouvelle
			// à la création, complète les 1ères colonnes communes aux metavalues
			if (!idxRowMap.containsKey(rowKey)) {
				// avec le tri de la HQL, dès qu'on passe sur une nouvelle ligne, on considère
				// que indices précédents ne sont plus utiles. Pour éviter qu'il grossisse pour rien
				// on le vide à chaque changement de ligne
				idxRowMap.clear()
				currentRow = excelUtils.createOrGetRow(sheet, globalRowIdx)
				idxRowMap.put(rowKey, globalRowIdx++)
				
				excelUtils.createDatetimeCell(currentRow, 0, deviceValue.dateValue)
				excelUtils.createOrGetCell(currentRow, 1).setCellValue(deviceValue.device.user.nom)
				excelUtils.createOrGetCell(currentRow, 2).setCellValue(deviceValue.device.user.prenom)
				excelUtils.createOrGetCell(currentRow, 3).setCellValue(deviceValue.device.user.username)
				excelUtils.createOrGetCell(currentRow, 4).setCellValue(deviceValue.device.label)
			} else {
				currentRow = excelUtils.createOrGetRow(sheet, idxRowMap.get(rowKey))
			}
			
			// récupère la colonne ou en crée une nouvelle
			// à la création, remplir aussi le header
			if (!idxCellMap.containsKey(cellKey)) {
				excelUtils.createHeaderCell(headerRow, headerCellIdx, deviceValue.name ?: DeviceValue.DEFAULT_LABEL)
				currentCell = excelUtils.createOrGetCell(currentRow, headerCellIdx)
				idxCellMap.put(cellKey, headerCellIdx++)
			} else {
				currentCell = excelUtils.createOrGetCell(currentRow, idxCellMap.get(cellKey))
			}
			
			currentCell.setCellValue(deviceValue.value)
		}
		
		workbook.write(outStream)
		
	}
}
