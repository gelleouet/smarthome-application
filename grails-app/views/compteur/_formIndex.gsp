<g:if test="${ compteur instanceof smarthome.automation.deviceType.TeleInformation }">
	<g:render template="formIndexElec"/>
</g:if>


<g:elseif test="${ compteur instanceof smarthome.automation.deviceType.CompteurGaz }">
	<g:render template="formIndexGaz"/>
</g:elseif>


<g:elseif test="${ compteur instanceof smarthome.automation.deviceType.CompteurEau }">
	<g:render template="formIndexEau"/>
</g:elseif>


<g:elseif test="${ compteur instanceof smarthome.automation.deviceType.Compteur }">
	<g:set var="lastIndex" value="${ compteur.lastIndex() }"/>
	<div class="form-group">
		<label>Ancien Index (${ compteur.defaultUnite() })</label>
		<g:field type="number decimal" name="lastIndex1" value="${ lastIndex?.value as Long }" class="form-control" readonly="true"/>
		<small class="text-muted">Dernier relevé le <g:formatDate date="${ lastIndex?.dateValue }" format="dd/MM/yyyy 'à' HH:mm:ss"/></small>
	</div>
	<div class="form-group required">
		<label>Nouvel Index (${ compteur.defaultUnite() })</label>
		<g:field type="number decimal" name="index1" value="${ command.index1 as Long }" class="form-control" required="true"/>
	</div>
</g:elseif>

<div class="form-group required">
	<label>Date Index</label>
	<g:field name="dateIndex" class="form-control small" value="${ app.formatPicker(date: command.dateIndex) }" type="date" required="true"/>
	<small class="text-muted">Après calcul avec le dernier index enregistré, la consommation sera enregistrée à cette date</small>
</div>
		
