<div style="display:table;">
	<div style="display:table-cell; padding-right:10px;">
		<h2>${ device.value }lux</h2>
	</div>
	
	<div style="display:table-cell; padding-left:10px;; vertical-align:top" class="separator-left">
		<p style="font-size:8pt">
		<span style="color:black"><strong>Min :</strong> ${ device.deviceImpl.viewParams.min }lux</span>
		<br/>
		<span style="color:#f6c342"><strong>Max :</strong> ${ device.deviceImpl.viewParams.max }lux</span>
		</p>
	</div>
</div>
