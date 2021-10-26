/**
 * 
 */
package smarthome.core

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.CreationHelper
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.FillPatternType

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class ExcelUtils {
	
	private Workbook workbook
	private CreationHelper createHelper
	private Map<Short, CellStyle> headerCellStyleMap = [:]
	private CellStyle datetimeCellStyle
	private CellStyle dateCellStyle
	
	
	
	ExcelUtils(Workbook workbook) {
		this.workbook = workbook
	}
	
	
	ExcelUtils build() {
		createHelper = workbook.getCreationHelper()
		return this
	}
	
	
	Cell createHeaderCell(Sheet sheet, int rowIndex, int cellIndex, String label) {
		Row row = createRow(sheet, rowIndex)
		return createHeaderCell(row, cellIndex, label)
	}
	
	
	Cell createHeaderCell(Row row, int cellIndex, String label) {
		return createHeaderCell(row, cellIndex, label, IndexedColors.GREY_40_PERCENT)
	}
	
	
	Cell createHeaderCell(Row row, int cellIndex, String label, IndexedColors indexColor) {
		Cell cell = row.createCell(cellIndex)
		cell.setCellValue(label)
		CellStyle headerCellStyle = headerCellStyleMap.get(indexColor.index)
		
		if (!headerCellStyle) {
			headerCellStyle = workbook.createCellStyle()
			headerCellStyle.setFillForegroundColor(indexColor.index)
			headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			headerCellStyleMap.put(indexColor.index, headerCellStyle)
		}
		
		cell.setCellStyle(headerCellStyle)
		
		return cell
	}
	
	
	Cell createDatetimeCell(Row row, int cellIndex, Date date) {
		Cell cell = row.createCell(cellIndex)
		cell.setCellValue(date)
		
		if (!datetimeCellStyle) {
			datetimeCellStyle = workbook.createCellStyle()
			datetimeCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"))
		}
		
		cell.setCellStyle(datetimeCellStyle)
		
		return cell
	}
	
	
	Cell createDateCell(Row row, int cellIndex, Date date) {
		Cell cell = row.createCell(cellIndex)
		cell.setCellValue(date.clone().clearTime())
		
		if (!dateCellStyle) {
			dateCellStyle = workbook.createCellStyle()
			dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy"))
		}
		
		cell.setCellStyle(dateCellStyle)
		
		return cell
	}
	
	
	Cell createOrGetCell(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex)
		
		if (!cell) {
			cell = row.createCell(cellIndex)
		}
		
		return cell
	}
	
	
	Cell createOrGetCell(Sheet sheet, int rowIndex, int cellIndex) {
		Row row = sheet.getRow(rowIndex)
		
		if (!row) {
			row = sheet.createRow(rowIndex)
		}
		
		return createOrGetCell(row, cellIndex)
	}
	
	
	Row createRow(Sheet sheet, int rowIndex) {
		return sheet.createRow(rowIndex)
	}
}
