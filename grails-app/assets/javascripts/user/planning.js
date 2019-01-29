var _planningCopyBuffer = new Array()

/**
 * Copie les cellules sélectionnées d'une table
 * 
 * @param table
 */
function planningCopy(table) {
	var selection = getSelectedPlanning(table)
	
	if (selection.length) {
		_planningCopyBuffer = selection
	} else {
		_planningCopyBuffer = new Array()
	}
}


/**
 * Colle les cellules copiées dans la zone sélectionnée d'une table
 * 
 * @param table
 */
function planningPaste(table) {
	var selection = getSelectedPlanning(table)
	
	if (_planningCopyBuffer.length && selection.length) {
		// on fait une translation de la zone copiée vers la nouvelle zone référencée
		// par sa 1ère cellule
		var translateRow = selection[0].row - _planningCopyBuffer[0].row
		var translateCol = selection[0].col - _planningCopyBuffer[0].col
		
		_planningCopyBuffer.forEach(function(cell) {
			//+1 car le selector nth-child est indexé à partir de 1 (et non pas 0)
			var row = (cell.row + translateRow)+1
			var col = (cell.col + translateCol)+1
			table.find(`tbody tr:nth-child(${row})`).find(`td:nth-child(${col})`)
				.css('background-color', cell.backgroundColor)
				.attr('data-value', cell.value)
		})
	}
}


/**
 * Retourne la sélection du planning sous la forme d'un buffer d'objet
 * @param table
 * @returns {Array}
 */
function getSelectedPlanning(table) {
	var selection = new Array()
	
	table.find('tbody tr').each(function(rowIndex, tr) {
		$(tr).find('td').each(function(colIndex, td) {
			var $td = $(td)
			
			if ($td.hasClass('ui-selected')) {
				selection.push({row: rowIndex, col: colIndex,
					backgroundColor: $td.css('background-color'),
					value: $td.attr('data-value')})
			}
		})
	})
	
	return selection
}


/**
 * Construit le planning à partir de la table
 * 
 * @param table
 */
function buildPlanning(table) {
	var planning = {}
	
	table.find('tbody tr').each(function(rowIndex, tr) {
		$(tr).find('td.planning-cell').each(function(colIndex, td) {
			var $td = $(td)
			var key = $td.attr('data-key')
			var index = parseInt($td.attr('data-index'))
			var day = parseInt($td.attr('data-day'))
			var value = $td.attr('data-value')
			
			if (planning[key] == undefined) {
				// buffer de 7 éléments pour les jours de la semaine, contenant chacun 48 cellules
				// pour chaque 1/2H
				planning[key] = initPlanningBuffer(new Array())
			}
			
			if (value) {
				// ajoute la valeur au bon index
				planning[key][day][index] = value
			}
		})
	})
	
	return planning
}


/**
 * Initialise un buffer pour un planning à la semaine
 * 
 * @param planning
 * @returns
 */
function initPlanningBuffer(planning) {
	for (var jour=0; jour<7; jour++) {
		var bufferSemaine = new Array()
		
		for (var hour=0; hour<48; hour++) {
			bufferSemaine.push("off")
		}
		
		planning.push(bufferSemaine)
	}
	
	return planning
}


/**
 * Init UI des plannings (sélection, mise en forme, etc.)
 * 
 */
function initPlanningUI() {
	$(".planning").selectable({
        filter: "tbody td.planning-cell"
    })
}


/**
 * init event planning
 */
function initPlanningEvent() {
	$(document).on('click', '#planning-copy-button', function() {
    	var $this = $(this)
    	var $table = $('#' + $this.attr('data-table'))
    	planningCopy($table)
    })
    
    $(document).on('click', '#planning-paste-button', function() {
    	var $this = $(this)
    	var $table = $('#' + $this.attr('data-table'))
    	planningPaste($table)
    })
    
	$(document).on('click', 'a.planning-button', function() {
		var $this = $(this)
		var color = $this.css('color')
		var value = $this.attr('data-value')
		var $table = $('#' + $this.attr('data-table'))
		$table.find('td.ui-selected').css('background-color', color).attr('data-value', value)
	})
}


/**
 * Construit les plannings et injecte la donnée dans le champ associé
 * 
 */
function bindPlanningToData() {
	$(".planning").each(function() {
		var $table = $(this)
		var $field = $('#' + $table.attr('data-field'))
		var planning = buildPlanning($table)
		var data = JSON.stringify(planning)
		$field.val(data)
	})
}
