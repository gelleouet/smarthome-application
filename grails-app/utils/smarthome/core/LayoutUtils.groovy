package smarthome.core

/**
 * Méthode utilitaire de mise en page
 * 
 * @author Gregory
 *
 */
class LayoutUtils {
	/**
	 * Divise une liste en multiple liste de column éléments
	 * Les sous listes sont complétées pour avoir toujours la taille voulue
	 * 
	 * @param collection
	 * @param column
	 * @return
	 */
	static List splitRow(Collection datas, int column) {
		List rows = []
		
		datas?.eachWithIndex { data, index ->
			def row
			
			if (index % column == 0) {
				row = []
				rows << row
			} else {
				row = rows.last()
			}
			
			row << data
		}
		
		if (rows && rows.last()) {
			for (int idx = rows.last().size(); idx < column; idx++) {
				rows.last() << null
			}
		}
		
		return rows
	}
}
