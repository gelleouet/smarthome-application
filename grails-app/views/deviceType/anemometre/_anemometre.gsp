<g:set var="noeud" value="${ device.deviceImpl.convertToNoeud() }"/>
<g:set var="beaufort" value="${ device.deviceImpl.convertToBeaufort() }"/>

<div style="display:table;">
	<div style="display:table-cell; padding-right:10px;">
		<h2>${ device.value != null ? device.value : '-' }km/h</h2>
	</div>
	
	
	<div style="display:table-cell; padding-left:10px;; vertical-align:top" class="separator-left">
		<p style="font-size:8pt">
		<span style="color:#707070"><strong>Min :</strong> ${ device.deviceImpl.viewParams.min }km/h</span>
		<br/>
		<span style="color:#707070"><strong>Max :</strong> ${ device.deviceImpl.viewParams.max }km/h</span>
		</p>
		
		<p style="font-size:8pt">
		<span style="color:#707070"><strong>Noeud :</strong> ${ noeud != null ? noeud : '-' }kt</span>
		<br/>
		<span style="color:#707070"><strong>Beaufort :</strong> ${ beaufort?.force } - ${ beaufort?.desc}</span>
		</p>
	</div>
</div>
