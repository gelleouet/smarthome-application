/**
 * Transforme un select simple en combobox avec recherche intégrée
 */
function combobox() {
	AJS.$("select.combobox").auiSelect2({
		openOnEnter: false,
		matcher: function(term, text, option) {
			// rien n'est saisi, tout passe
			if (! term) {
				return true;
			} else {
				// si rien trouvé plus haut : recherche normale
				return text.toLowerCase().indexOf(term.toLowerCase()) != -1;
			}
		}
	});
}