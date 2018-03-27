var xterm
var xtermWebsocket


function onLoadAgents() {
	
}


function connectAgent(agentId) {
	// une connexion existe déjà, on la termine
	if (xterm) {
		xterm.destroy()
		xtermWebsocket.close()
		xtermWebsocket = null
		xterm = null
		return
	}
	
	xterm = new Terminal({
		cursorBlink: true
	})
	var $xterm = $('#xterm-color')
	var url = $xterm.attr('data-endpoint-url').replace("{agentId}", agentId)
	console.log("Try connecting shell websocket...", url)
	xtermWebsocket = new WebSocket(url)
	
	xterm.open($xterm[0])
	
	xtermWebsocket.addEventListener('open', function(event) {
		xterm.attach(xtermWebsocket, true, true)
	})
	
	xtermWebsocket.addEventListener('close', function(event) {
		if (xterm) {
			xterm.destroy()
			xterm = null
		}
	})
}









