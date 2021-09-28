<div>
	<svg version="1.1" width="350" height="230" viewBox="0 0 350 230" xmlns="http://www.w3.org/2000/svg">
		<g id="Layer_1">
			<title>PanneauSolaire</title>
			<rect x="10" y="10" width="226" height="136" fill="#000000" stroke="none"/>
			
			<rect x="236" y="18.908" width="10" height="10" fill="#ff0000" stroke-width="2" stroke="#000000"/>
			<line x1="246" x2="266" y1="40" y2="40" fill="none" stroke-dasharray="5, 5" stroke="#707070"/>
			<circle cx="266" cy="40" r="2" fill-opacity="0" stroke="#707070"/>
			<text x="272" y="47" fill="#707070" font-family="serif" font-size="20">${ device.metavalue('Tchaud')?.convertValueToDouble(1) ?: '-' }°C</text>
			
			<rect x="236" y="127" width="10" height="10" fill="#007fff" stroke-width="2" stroke="#000000"/>
			<line x1="246" x2="266" y1="127" y2="127" fill="none" stroke-dasharray="5, 5" stroke="#707070"/>
			<circle cx="266" cy="127" r="2" fill-opacity="0" stroke="#707070"/>
			<text x="272" y="134" fill="#707070" font-family="serif" font-size="20">${ device.metavalue('Tfroid')?.convertValueToDouble(1) ?: '-' }°C</text>
			
			<rect x="14" y="14" width="218" height="128" style="fill:url(#def2);stroke:#ffffff"/>
			<path fill="none" stroke-width="5" stroke="url(#def3)" d="m31.582 131.07h201m-201 0c-3.793 0-7.2979-2.0236-9.1945-5.3084-1.8965-3.2849-1.8965-7.332 0-10.617 1.8965-3.2849 5.4014-5.3084 9.1945-5.3084h182.31c3.793-2e-5 7.2979-2.0236 9.1945-5.3084 1.8965-3.2849 1.8965-7.332 0-10.617-1.8965-3.2849-5.4014-5.3084-9.1945-5.3084h-182.31c-3.793-9e-6 -7.2979-2.0236-9.1945-5.3084-1.8965-3.2849-1.8965-7.332 0-10.617 1.8965-3.2849 5.4014-5.3084 9.1945-5.3084h182.31c3.7931-2e-6 7.298-2.0236 9.1945-5.3084 1.8965-3.2849 1.8965-7.332 0-10.617-1.8965-3.2849-5.4014-5.3084-9.1945-5.3084h-182.31c-3.793-3e-6 -7.2979-2.0236-9.1945-5.3084-1.8965-3.2849-1.8965-7.332 0-10.617 1.8965-3.2849 5.4014-5.3084 9.1945-5.3084h200.36"/>
		</g>
		<defs>
			<linearGradient y2="1" x2="1" y1="0" x1="0" id="def2">
				<stop stop-color="#343434" stop-opacity=".99216" offset="0"/>
				<stop stop-color="#555555" stop-opacity=".99608" offset="1"/>
			</linearGradient>
			<linearGradient y2="1" x2="0.5" y1="0" x1="0.5" id="def3">
				<stop stop-color="#ff0000" stop-opacity=".99216" offset="0"/>
				<stop stop-color="#007fff" stop-opacity=".99608" offset="1"/>
			</linearGradient>
		</defs>
	</svg>
</div>