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
import org.hibernate.Query;
import org.hibernate.ScrollMode;

import smarthome.automation.DeviceValue;
import smarthome.automation.SupervisionCommand
import smarthome.automation.deviceType.Compteur;
import smarthome.automation.deviceType.Humidite;
import smarthome.automation.deviceType.TeleInformation;
import smarthome.automation.deviceType.Temperature;
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
class DulceExcelDeviceValueExport implements DeviceValueExport {

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
		Sheet sheet = workbook.createSheet("DULCE")
		createHelper = workbook.getCreationHelper()
		int rowIdx = 0
		
		// creation de l'entete
		Row row = sheet.createRow(rowIdx++)
		createHeaderCell(row, 0, "Horodatage")
		createHeaderCell(row, 1, "Foyer")
		createHeaderCell(row, 2, "Conso gaz (Wh)")
		createHeaderCell(row, 3, "Conso électricité (Wh)")
		createHeaderCell(row, 4, "Tint 1 (°C)")
		createHeaderCell(row, 5, "Hint 1 (%)")
		createHeaderCell(row, 6, "Tint 2 (°C)")
		createHeaderCell(row, 7, "Hint 2 (%)")
		createHeaderCell(row, 8, "Text (°C)")
		createHeaderCell(row, 9, "Hext (%)")
		
		// Export des values par user
		for (def user : command.userIdsExport) {
			Date lastDate, valueDate
			row = null
			
			HQL hql = new HQL("""new map(deviceValue.dateValue as dateValue,
				deviceType.implClass as implClass,
				device.mac as mac, deviceValue.value as value, deviceValue.name as metaname)""",
				"""FROM DeviceValue deviceValue JOIN deviceValue.device device
				JOIN device.deviceType deviceType""")
			
			hql.addCriterion("deviceValue.dateValue BETWEEN :dateDebut AND :dateFin", [dateDebut:
				command.datetimeDebut(), dateFin: command.datetimeFin()])
			hql.addCriterion("device.user.id = :userId", [userId: user[0]])
			hql.addCriterion("deviceValue.name is null OR deviceValue.name in (:metanames)",
				[metanames: ['conso', 'hcinst', 'hpinst', 'baseinst']])
			
			if (command.deviceTypeClass) {
				hql.addCriterion("deviceType.implClass = :implClass", [implClass: command.deviceTypeClass])
			}
			
			hql.addOrder("deviceValue.dateValue")
			
			DeviceValue.withSession { session ->
				Query query = session.createQuery(hql.build())
				QueryUtils.bindParameters(query, hql.params)
				// globalement une journée entière pour un utilisateur qu'on peut charger en une fois
				// sachant qu'on ne charge que 3 valeurs (date, device, valeur)
				// 3 sondes = 24 x 3
				// 2 TIC = 144 x 2
				query.setFetchSize(500)	
				query.setReadOnly(true)
				
				def scrollResults = query.scroll(ScrollMode.FORWARD_ONLY)
				
				while (scrollResults.next()) {
					def deviceValue = scrollResults.get()[0]
					valueDate = DateUtils.truncMinute10(deviceValue.dateValue)
					
					// a chaque changement de date, on rajoute une nouvelle ligne avec tous les devices
					// de la même heure sur la même ligne
					if (valueDate != lastDate) {
						row = sheet.createRow(rowIdx++)
						createDatetimeCell(row, 0, valueDate)
						row.createCell(1).setCellValue(user[2])
						lastDate = valueDate 
					}	
					
					if (deviceValue.value != null) {
						createCellDeviceValue(row, deviceValue)
					}
				}
			}
		}
		
		// ajuste les colonnes
		for (int col=0; col<=9; col++) {
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
	
	
	private Cell createCellDeviceValue(Row row, Map rowMap) {
		int cellIndex = -1
		Cell cell
		double value = rowMap.value
		// si plusieurs valeurs pour le même intervalle (10min), indique si on écrase la valeur présente
		// ou si on doit ajouter les valeurs.
		// pour les compteurs, on ajoute les valeurs
		boolean ajouterValeur = false
		
		if (rowMap.implClass == Compteur.name && rowMap.metaname == "conso") {
			cellIndex = 2
			ajouterValeur = true
			
			// compteur gaz : la valeur enregistrée est en nombre d'impulsion
			// il faut la convertir en Wh en passant par m3 en utilisant coef de conversion gaz
			// la plupart des compteurs : 1 impulsion = 10dm3 = 0,01m3
			// coef conversion = 11.29 = valeur moyenne sur montreuil le gast = kWh pour 1m3
			value = rowMap.value * 0.01 * 11.29 * 1000
		} else if (rowMap.implClass == TeleInformation.name && rowMap.metaname in ['hcinst', 'hpinst', 'baseinst']) {
			cellIndex = 3
			ajouterValeur = true
		} else if (rowMap.implClass == Temperature.name && rowMap.mac.endsWith("_1")) {
			cellIndex = 4
		} else if (rowMap.implClass == Humidite.name && rowMap.mac.endsWith("_1")) {
			cellIndex = 5
		} else if (rowMap.implClass == Temperature.name && rowMap.mac.endsWith("_2")) {
			cellIndex = 6
		} else if (rowMap.implClass == Humidite.name && rowMap.mac.endsWith("_2")) {
			cellIndex = 7
		} else if (rowMap.implClass == Temperature.name && rowMap.mac.endsWith("_3")) {
			cellIndex = 8
		} else if (rowMap.implClass == Humidite.name && rowMap.mac.endsWith("_3")) {
			cellIndex = 9
		}
		
		if (cellIndex != -1) {
			cell = row.getCell(cellIndex)
			
			if (!cell) {
				cell = row.createCell(cellIndex)
			} else {
				// la cellule existe déjà, on regarde si on écrase la valeur
				// ou si on cumule les valeurs
				if (ajouterValeur) {
					value += cell.getNumericCellValue()
				}
			}
			
			cell.setCellValue(value)
		}
		
		return cell
	}


	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.export.DeviceValueExport#initExportAdmin(smarthome.automation.ExportCommand, javax.servlet.ServletResponse)
	 */
	@Override
	void initExportAdmin(SupervisionCommand command, ServletResponse response) {
		response.setContentType(MimeTypeEnum.EXCEL2003.mimeType)
		response.setHeader("Content-Disposition", "attachment; filename=exportdulce-${command.dateDebut.format('yyyyMMdd')}-${command.dateFin.format('yyyyMMdd')}.${MimeTypeEnum.EXCEL2003.extension}")
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
