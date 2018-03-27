var teleinfoWebsocket = null
var teleinfoCharts = {}
var mappingPuissance = {
		60: 12000,
		45: 9000,
		30: 6000,
		15: 3000
}

function onStartTraceTeleinfo(element, deviceId) {
	if (teleinfoWebsocket) {
		teleinfoWebsocket.close()
		teleinfoWebsocket = null
	}
	
	var url = $(element).attr('data-endpoint-url').replace("{deviceId}", deviceId)
	console.log("Try connecting teleinfo websocket...", url)
	teleinfoWebsocket = new WebSocket(url)
	
	teleinfoWebsocket.addEventListener('open', function(openEvent) {
		teleinfoWebsocket.addEventListener('close', function(closeEvent) {
			closeWebsocketTeleinfo()
		})
		
		teleinfoWebsocket.addEventListener('message', function(messageEvent) {
			refreshTeleinfoChart(JSON.parse(messageEvent.data))
		})
	})
	
	teleinfoWebsocket.addEventListener('error', function(event) {
		closeWebsocketTeleinfo()
	})
}


function onStopTraceTeleinfo() {
	if (teleinfoWebsocket) {
		teleinfoWebsocket.close()
	}
}


function closeWebsocketTeleinfo() {
	AJS.messages.warning("#teleinfo-statut", {
	   title: 'Connexion',
	   body: '<p>Connexion terminée ou interrompue par le serveur.</p>',
	   fadeout: true
	});
	
	teleinfoWebsocket = null
	refreshTeleinfoChart({})
}


function createTeleinfoChart(divChart) {
	var key = $(divChart).attr('data-key')
	var maxIntensite = parseInt($(divChart).attr('data-max-intensite'))
	var maxPuissance = mappingPuissance[maxIntensite]
	var redFrom, max
	
	if (key == "kva") {
		max = maxPuissance
	} else if (key == "watt") {
		max = maxPuissance
	} else if (key == "intensite") {
		max = maxIntensite
	}
	
	teleinfoCharts[key] = {
		data: google.visualization.arrayToDataTable([['Label', 'Value'], ['', 0]])
	}

	teleinfoCharts[key].options = {
		height: 225,
		redFrom: parseInt(max * 0.9),
		redTo: max,
		max: max,
		minorTicks: 5,
		majorTicks: ['0', max * 0.25, max * 0.5, max * 0.75, max]
    };

	teleinfoCharts[key].chart = new google.visualization.Gauge(document.getElementById($(divChart).attr('id')))
	teleinfoCharts[key].chart.draw(teleinfoCharts[key].data, teleinfoCharts[key].options)
}


function refreshTeleinfoChart(data) {
	if (data.iinst) {
		teleinfoCharts["intensite"].data.setValue(0, 1, parseInt(data.iinst.value))
	} else {
		teleinfoCharts["intensite"].data.setValue(0, 1, 0)
	}
	
	if (data.papp) {
		teleinfoCharts["kva"].data.setValue(0, 1, parseInt(data.papp.value))
		teleinfoCharts["watt"].data.setValue(0, 1, parseInt(data.papp.value))
	} else {
		teleinfoCharts["kva"].data.setValue(0, 1, 0)
		teleinfoCharts["watt"].data.setValue(0, 1, 0)
	}
	
	teleinfoCharts["intensite"].chart.draw(teleinfoCharts["intensite"].data, teleinfoCharts["intensite"].options)
	teleinfoCharts["kva"].chart.draw(teleinfoCharts["kva"].data, teleinfoCharts["kva"].options)
	teleinfoCharts["watt"].chart.draw(teleinfoCharts["watt"].data, teleinfoCharts["watt"].options)
	
	$('#teleinfo_option_tarifaire').val(data.opttarif ? data.opttarif.value : '')
	$('#teleinfo_periode_tarifaire').val(data.ptec ? data.ptec.value : '')
	$('#teleinfo_index_hc').val(data.hchc ? data.hchc.value : '')
	$('#teleinfo_index_hp').val(data.hchp ? data.hchp.value : '')
}