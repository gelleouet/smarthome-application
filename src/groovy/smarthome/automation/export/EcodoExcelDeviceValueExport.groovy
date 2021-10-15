/**
 * 
 */
package smarthome.automation.export

import java.io.File;
import java.io.OutputStream;

import javax.servlet.ServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.springframework.beans.factory.annotation.Autowired

import smarthome.automation.DeviceValue;
import smarthome.automation.House
import smarthome.automation.SupervisionCommand
import smarthome.automation.deviceType.Compteur;
import smarthome.automation.deviceType.Humidite;
import smarthome.automation.deviceType.TeleInformation;
import smarthome.automation.deviceType.Temperature;
import smarthome.core.ApplicationUtils
import smarthome.core.DateUtils;
import smarthome.core.MimeTypeEnum;
import smarthome.core.QueryUtils;
import smarthome.core.SmartHomeException;
import smarthome.core.query.HQL;
import smarthome.security.User;


/**
 * Implémentation DULCE
 * 
 * Scope : request. Donc pas gênant de créer des propriétés de classe. Ca reste thread-safe
 * 
 * @author Gregory
 *
 */
class EcodoExcelDeviceValueExport implements DeviceValueExport {

	@Autowired
	GrailsApplication grailsApplication
	
	
	private CellStyle headerCellStyle
	private CellStyle datetimeCellStyle
	private Workbook workbook
	private CreationHelper createHelper
	
	
	/**
	 * Export des données au format Excel selon CCTP DULCE
	 * 
	 * @see smarthome.automation.export.DeviceValueExport#exportAdmin(smarthome.automation.ExportCommand, java.io.OutputStream)
	 */
	@Override
	void exportAdmin(SupervisionCommand command, OutputStream outStream) throws Exception {
		workbook = new HSSFWorkbook()
		createHelper = workbook.getCreationHelper()
		int rowIdx, cellIdx
		Sheet sheet
		Row row
		
		// 1er feuillet avec les infos des utilisateurs
		sheet = workbook.createSheet("PARTICIPANT")
		rowIdx = 0
		row = sheet.createRow(rowIdx++)
		createHeaderCell(row, 0, "Email")
		createHeaderCell(row, 1, "Prénom")
		createHeaderCell(row, 2, "Nom")
		createHeaderCell(row, 3, "Adresse")
		createHeaderCell(row, 4, "Code postal")
		createHeaderCell(row, 5, "Ville")
		createHeaderCell(row, 6, "Foyer")
		
		command.visitUser(ApplicationUtils.configMaxBackend(grailsApplication)) { result ->
			row = sheet.createRow(rowIdx++)
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
		}
		
		// ajuste les colonnes
		for (int col=0; col<cellIdx; col++) {
			sheet.autoSizeColumn(col)
		}
		
		// 2e feuillet avec les données
		sheet = workbook.createSheet("INDEX")
		rowIdx = 0
		row = sheet.createRow(rowIdx++)
		createHeaderCell(row, 0, "Email")
		createHeaderCell(row, 1, "Date index")
		createHeaderCell(row, 2, "Index (L)")
		
		command.visitDeviceValue(ApplicationUtils.configMaxBackend(grailsApplication)) { DeviceValue deviceValue ->
			row = sheet.createRow(rowIdx++)
			cellIdx = 0
			
			row.createCell(cellIdx++).setCellValue(deviceValue.device.user.username)
			createDatetimeCell(row, cellIdx++, deviceValue.dateValue)
			row.createCell(cellIdx++).setCellValue(deviceValue.value)
		}
		
		
		// ajuste les colonnes
		for (int col=0; col<cellIdx; col++) {
			sheet.autoSizeColumn(col)
		}
		
		workbook.write(outStream)
	}

	
	/**
	 * Export des données au format Excel selon CCTP DULCE
	 * 
	 * @see smarthome.automation.export.DeviceValueExport#exportUser(smarthome.automation.ExportCommand, java.io.OutputStream)
	 */
	@Override
	void exportUser(SupervisionCommand command, OutputStream outStream) throws Exception {
		throw new SmartHomeException("Fonctionnalité pas implémentée !", command)
		
	}

	
	private Cell createHeaderCell(Row row, int index, String label) {
		Cell cell = row.createCell(index)
		cell.setCellValue(label)
		
		if (!headerCellStyle) {
			headerCellStyle = workbook.createCellStyle()
			headerCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index)
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}
		
		cell.setCellStyle(headerCellStyle)
		
		return cell	
	}
	
	
	private Cell createDatetimeCell(Row row, int index, Date date) {
		Cell cell = row.createCell(index)
		cell.setCellValue(date)
		
		if (!datetimeCellStyle) {
			datetimeCellStyle = workbook.createCellStyle()
			datetimeCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"))
		}
		
		cell.setCellStyle(datetimeCellStyle)
		
		return cell
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
