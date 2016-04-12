function closeDeviceShareDialog() {
	AJS.dialog2('#device-share-dialog').hide();
}

function showDeviceShareDialog() {
	AJS.dialog2('#device-share-dialog').show();
	
	AJS.$("#selectSharedUser").auiSelect2({
		placeholder: "Rechercher des utilisateurs...",
		minimumInputLength: 3,
		ajax: {
	        url: $("#selectSharedUser").attr("data-url") + ".json",
	        dataType: 'json',
	        quietMillis: 250,
	        data: function (term, page) {
	            return {
	            	userSearch: term,
	            	offset: page-1,
	            	max: 25,
	            };
	        },
	        results: function (data, page) {
	        	var items = {results: [{id:-1, text: 'Tous mes amis'}]}
	        	
	        	if (data.length) {
	        		for (var i=0; i<data.length; i++) {
	        			items.results.push({
	        				id: data[i].id,
	        				text: data[i].nom + ' ' + data[i].prenom
	        			})
	        		}
	        	}
	        	
	            return items;
	        },
	    },
	});
}


function deleteDeviceShare(link) {
	if (confirm('Voulez-vous supprimer le partage ?')) {
		ajaxGet(link, 'data-url', null, '#ajaxDeviceShare')
	}
}
