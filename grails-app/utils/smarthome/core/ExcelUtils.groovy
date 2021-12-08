/**
 * 
 */
package smarthome.core

import java.awt.Color

import javax.servlet.ServletResponse

import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.CreationHelper
import org.apache.poi.ss.usermodel.ExtendedColor
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.FillPatternType

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class ExcelUtils {
	
	private Workbook workbook
	private CreationHelper createHelper
	private CellStyle datetimeCellStyle
	private Map<String, CellStyle> styleMap = [:]
	private MimeTypeEnum mimeType
	
	
	
	ExcelUtils() {
		
	}
	
	
	ExcelUtils(Workbook workbook) {
		this.workbook = workbook
	}
	
	
	ExcelUtils createWorkbook(MimeTypeEnum mimeType) {
		this.mimeType = mimeType
		
		if (mimeType == MimeTypeEnum.EXCEL2007) {
			this.workbook = new XSSFWorkbook()
		} else {
			this.workbook = new HSSFWorkbook()
		}
		
		return this
	}
	
	ExcelUtils build() {
		createHelper = workbook.getCreationHelper()
		return this
	}
	
	Sheet createSheet(String name) {
		workbook.createSheet(name)
	}
	
	
	void writeTo(OutputStream stream) throws Exception {
		stream.withStream { outStream ->
			workbook.write(outStream)
		}
	}
	
	
	void writeTo(ServletResponse response, String fileName) throws Exception {
		response.setContentType(mimeType.mimeType)
		response.setHeader("Content-Disposition", "attachment; filename=${ fileName }")
		writeTo(response.outputStream)
	}
	
	
	/**
	 * 
	 * @param sheet
	 * @param startRow
	 * @param endRow
	 * @param startCell
	 * @param endCell
	 * @param value
	 * @param style
	 * @return
	 */
	Cell mergeCell(Sheet sheet, int startRow, int endRow, int startCell, int endCell, String value, Map style = [:]) {
		for (int idxRow = startRow; idxRow <= endRow; idxRow++) {
			Row row = createRow(sheet, idxRow)
			
			for (int idxCell = startCell; idxCell <= endCell; idxCell++) {
				createOrGetCell(row, idxCell, style)
			}
		}
		
		sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCell, endCell))
		
		Cell cell = createOrGetCell(sheet, startRow, startCell, style)
		
		if (value) {
			cell.setCellValue(value)
		}
		
		return cell
	}
	
	
	Cell applyStyle(Cell cell, Map style) {
		if (style?.name) {
			CellStyle cellStyle = styleMap.get(style.name) 
			
			if (!cellStyle) {
				cellStyle = workbook.createCellStyle()
				styleMap.put(style.name, cellStyle)
				
				if (style.color) {
					if (style.color instanceof IndexedColors) {
						cellStyle.setFillForegroundColor(style.color.index)
					} else if (style.color instanceof Color && cellStyle instanceof XSSFCellStyle) {
						(cellStyle as XSSFCellStyle).setFillForegroundColor(new XSSFColor(style.color))
					}
					cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				}
				
				if (style.format) {
					cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(style.format))
				}
				
				if (style.vAlign) {
					cellStyle.setVerticalAlignment(style.vAlign)
				}
				
				if (style.hAlign) {
					cellStyle.setAlignment(style.hAlign)
				}
				
				if (style.wrapped) {
					cellStyle.setWrapText(true)
				}
				
				if (style.border) {
					cellStyle.setBorderTop(style.border)
					cellStyle.setTopBorderColor(IndexedColors.BLACK.index)
					cellStyle.setBorderBottom(style.border)
					cellStyle.setBottomBorderColor(IndexedColors.BLACK.index)
					cellStyle.setBorderRight(style.border)
					cellStyle.setRightBorderColor(IndexedColors.BLACK.index)
					cellStyle.setBorderLeft(style.border)
					cellStyle.setLeftBorderColor(IndexedColors.BLACK.index)
				}
			}
			
			cell.setCellStyle(cellStyle)
		}
		
		return cell
	}
	
	
	Cell createHeaderCell(Sheet sheet, int rowIndex, int cellIndex, String label, IndexedColors indexColor) {
		Row row = createRow(sheet, rowIndex)
		return createHeaderCell(row, cellIndex, label, indexColor)
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
		return applyStyle(cell, [name: indexColor.toString(), color: indexColor])
	}
	
	
	Cell createDatetimeCell(Row row, int cellIndex, Date date) {
		Cell cell = row.createCell(cellIndex)
		cell.setCellValue(date)
		return applyStyle(cell, [format: "m/d/yy h:mm"])
	}
	
	
	Cell createDateCell(Row row, int cellIndex, Date date) {
		Cell cell = row.createCell(cellIndex)
		cell.setCellValue(date.clone().clearTime())
		return applyStyle(cell, [format: "m/d/yy"])
	}
	
	
	Cell createOrGetCell(Row row, int cellIndex, Map style = [:]) {
		Cell cell = row.getCell(cellIndex)
		
		if (!cell) {
			cell = applyStyle(row.createCell(cellIndex), style)
		}
		
		return cell
	}
	
	
	Cell createOrGetCell(Sheet sheet, int rowIndex, int cellIndex, Map style = [:]) {
		Row row = sheet.getRow(rowIndex)
		
		if (!row) {
			row = sheet.createRow(rowIndex)
		}
		
		return createOrGetCell(row, cellIndex, style)
	}
	
	
	Row createRow(Sheet sheet, int rowIndex) {
		Row row = sheet.getRow(rowIndex)
		
		if (!row) {
			row = sheet.createRow(rowIndex)
		}
		
		return row
	}
	
	
	Date getDateCellValue(Row row, int cellIndex) {
		Object value = getCellValue(row, cellIndex)
		value instanceof Date ? value : null
	}
	
	
	Long getLongCellValue(Row row, int cellIndex) {
		Object value = getCellValue(row, cellIndex)
		Long result
		
		if (value instanceof Number) {
			result = value.toLong()
		} else if (value instanceof String && value.isNumber()) {
			result = (value as Double).toLong()
		}
		
		return result
	}
	
	
	String getStringCellValue(Row row, int cellIndex) {
		Object value = getCellValue(row, cellIndex)
		String result
		
		if (value instanceof Date) {
			result = DateUtils.formatDateUser(value)
		} else {
			result = value?.toString()
		}
		
		return result
	}
	
	
	Object getCellValue(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex)
		Object result
		
		switch(cell?.cellTypeEnum) {
			case CellType.BOOLEAN:
				result = cell.getBooleanCellValue()
				break
			case CellType.NUMERIC:
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					result = cell.getDateCellValue()
				} else {
					result = new Double(cell.getNumericCellValue())
					
					// conversion en type Long si nombre entier
					if (result.doubleValue() == NumberUtils.round(result, 0)) {
						result = result.longValue()
					}
				}
				break
			case CellType.STRING:
				result = cell.getStringCellValue()
				break
		}
		
		return result
	}
	
	
	/**
	 * 
	 * @param row
	 * @param columnName A, B ...
	 * @return
	 */
	Object getCellValue(Row row, String columnName) {
		getCellValue(row, convertStrColumnToInt(columnName))
	}
	
	
	String getStringCellValue(Row row, String columnName) {
		getStringCellValue(row, convertStrColumnToInt(columnName))
	}
	
	
	Long getLongCellValue(Row row, String columnName) {
		getLongCellValue(row, convertStrColumnToInt(columnName))
	}
	
	
	Date getDateCellValue(Row row, String columnName) {
		getDateCellValue(row, convertStrColumnToInt(columnName))
	}
	
	
	protected int convertStrColumnToInt(String aIndex) {
		if (aIndex == null) {
			return -1;
		}

		char[] buffer = aIndex.toCharArray();
		int index = 0;

		for (int i = buffer.length - 1; i >= 0; i--) {
			index += ((int) buffer[i] - 64) * Math.pow(26, buffer.length - i - 1);
		}

		return index - 1;
	}
}
