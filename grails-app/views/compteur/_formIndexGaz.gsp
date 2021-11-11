<g:set var="lastIndex" value="${ compteur.lastIndex(new Date()) }"/>


<g:hiddenField name="lastIndex1" value="${ lastIndex?.value }"/>
<g:hiddenField name="param1" value="${ defaultCoefGaz }"/>


<div class="saisie-compteur-gaz" style="position: relative;">
	<div>
		<asset:image src="/compteur/dessin-compteur-gaz.png"/>
	</div>
	<div style="left:202px; top:258px; width:120px; height:28px; position: absolute;">
		<g:textField name="highindex1" value="${ g.formatNumber(number: command.highindex1 ?: 0, format: '000000') }" class="form-control form-control-lg index-high-part"
			autofocus="true" required="true" maxlength="6" data-mask="000000"/>
		<small class="text-muted">m3</small>
	</div>
	<div style="left:322px; top:258px; width:70px; height:28px; position: absolute;">
		<g:textField name="lowindex1" value="${ g.formatNumber(number: command.lowindex1 ?: 0, format: '000') }" class="form-control form-control-lg index-low-part"
			required="true" maxlength="3" data-mask="000"/>
		<small class="text-muted">dm3</small>
	</div>
	
	
	<div style="left:200px; top:380px; width:195px; height:100px; position: absolute;">
		<div class="alert alert-dark alert-outline-coloured" role="alert">
			<div class="alert-icon">
				<i class="far fa-fw fa-bell"></i>
			</div>
			<div class="alert-message">
				Taux de conversion moyen <strong>${ defaultCoefGaz }</strong>
			</div>
			
		</div>
	</div>
</div>

<h6 class="text-muted">Le coefficient de conversion permet d’obtenir la quantité en kWh du volume exprimé en m3. <a href="https://www.grdf.fr/particuliers/coefficient-conversion-commune">Connaître le coefficient de conversion de votre commune</a></h6>



