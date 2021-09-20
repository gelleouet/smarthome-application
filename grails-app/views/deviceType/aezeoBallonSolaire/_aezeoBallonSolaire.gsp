<div>
	<svg width="230" height="230" xmlns="http://www.w3.org/2000/svg">
	 <g id="Layer_1">
	  <title>BallonSolaire</title>
	  <rect id="svg_1" stroke-width="2" rx="5" fill="url(#def1)" stroke="#707070" x="10" y="10" width="100" height="200" />
	  <rect id="svg_2" stroke-width="2" height="10" width="10" y="210" x="80" stroke="#707070" fill="#ff0000"/>
	  <rect id="svg_3" stroke-width="2" fill="#007fff" stroke="#707070" x="30" y="210" width="10" height="10"/>
	  
	  <ellipse id="svg_4" ry="2" rx="2" cy="43" cx="60" stroke="#707070" fill="#0fffff" fill-opacity="0"/>
	  <line id="svg_5" fill="none" stroke="#707070" fill-opacity="0" x1="60" y1="43" x2="140" y2="43" stroke-dasharray="5,5"/>
	  <ellipse id="svg_6" ry="2" rx="2" cy="43" cx="140" stroke="#707070" fill="#0fffff" fill-opacity="0"/>
	  <text id="svg_7" text-anchor="start" font-family="serif" font-size="20" y="50" x="145" fill="#707070">${ device.metavalue('Temperature_haut')?.convertValueToDouble(1) ?: '-' }°C</text>
	  
	  <ellipse id="svg_8" ry="2" rx="2" cy="109" cx="60" stroke="#707070" fill="#0fffff" fill-opacity="0"/>
	  <line id="svg_9" fill="none" stroke="#707070" fill-opacity="0" x1="60" y1="109" x2="140" y2="109" stroke-dasharray="5,5"/>
	  <ellipse id="svg_10" ry="2" rx="2" cy="109" cx="140" stroke="#707070" fill="#0fffff" fill-opacity="0"/>
	  <text id="svg_11" text-anchor="start" font-family="serif" font-size="20" y="116" x="145" fill="#707070">${ device.metavalue('Temperature_milieu')?.convertValueToDouble(1) ?: '-' }°C</text>
	  
	  <ellipse id="svg_12" ry="2" rx="2" cy="175" cx="60" stroke="#707070" fill="#0fffff" fill-opacity="0"/>
	  <line id="svg_13" fill="none" stroke="#707070" fill-opacity="0" x1="60" y1="175" x2="140" y2="175" stroke-dasharray="5,5"/>
	  <ellipse id="svg_14" ry="2" rx="2" cy="175" cx="140" stroke="#707070" fill="#0fffff" fill-opacity="0"/>
	  <text id="svg_15" text-anchor="start" font-family="serif" font-size="20" y="182" x="145" fill="#707070">${ device.metavalue('Temperature_bas')?.convertValueToDouble(1) ?: '-' }°C</text>
	  
	   <path id="svg_16" d="m34.99166,220.98132c-0.24999,0 0,6.24965 -0.24359,6.00607c0.24359,0.24359 104.73778,-0.25639 104.49419,-0.49997" opacity="NaN" stroke="#707070" fill="#fff" stroke-width="2"/>
  	   <path id="svg_17" d="m84.7389,221.23131l0.0064,2.25627c0.24359,0.24359 54.49057,0.24359 54.24059,0.24359" opacity="NaN" stroke="#707070" fill="#fff" stroke-width="2"/>
	 </g>
	 <defs>
	  <linearGradient id="def1" y2="1" x2="0.5" y1="0" x1="0.5">
	   <stop stop-opacity="0.99609" offset="0" stop-color="#ff0000"/>
	   <stop stop-opacity="0.99609" offset="1" stop-color="#007fff"/>
	  </linearGradient>
	 </defs>
	</svg>
</div>