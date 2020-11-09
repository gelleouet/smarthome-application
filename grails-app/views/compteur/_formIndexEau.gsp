<g:set var="lastIndex" value="${ compteur.lastIndex() }"/>


<g:hiddenField name="lastIndex1" value="${ lastIndex?.value }"/>


<div class="saisie-compteur-eau" style="position: relative;">
	<div>
		<asset:image src="/compteur/dessin-compteur-eau.png" width="500"/>
	</div>
	<div style="left:164px; top:114px; width:114px; height:34px; position: absolute;">
		<g:textField name="highindex1" value="${ g.formatNumber(number: command.highindex1 ?: 0, format: '00000') }" class="form-control form-control-lg index-high-part"
			autofocus="true" required="true" maxlength="5" data-mask="00000"/>
		<small class="text-muted">m3</small>
	</div>
	<div style="left:278px; top:114px; width:68px; height:34px; position: absolute;">
		<g:textField name="lowindex1" value="${ g.formatNumber(number: command.lowindex1 ?: 0, format: '000') }" class="form-control form-control-lg index-low-part"
			required="true" maxlength="3" data-mask="000"/>
		<small class="text-muted">litre</small>
	</div>
</div>



