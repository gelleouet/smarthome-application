<g:if test="${ compteur instanceof smarthome.automation.deviceType.TeleInformation }">
	<g:if test="${ compteur.isDoubleTarification() }">
		<g:set var="lastIndex" value="${ compteur.lastIndexHP() }"/>
		<div class="form-group">
			<label>Ancien Index Heures Pleines</label>
			<g:field type="number decimal" name="lastIndex1" value="${ lastIndex?.value as Long }" class="form-control" readonly="true"/>
			<small class="text-muted">Dernier relevé le <g:formatDate date="${ lastIndex?.dateValue }" format="dd/MM/yyyy 'à' HH:mm:ss"/></small>
		</div>
		<div class="form-group required">
			<label>Nouvel Index Heures Pleines</label>
			<g:field type="number decimal" name="index1" value="${ command.index1 as Long }" class="form-control" required="true"/>
		</div>
		<g:set var="lastIndex" value="${ compteur.lastIndexHC() }"/>
		<div class="form-group">
			<label>Ancien Index Heures Creuses</label>
			<g:field type="number decimal" name="lastIndex2" value="${ lastIndex?.value as Long }" class="form-control" readonly="true"/>
			<small class="text-muted">Dernier relevé le <g:formatDate date="${ lastIndex?.dateValue }" format="dd/MM/yyyy 'à' HH:mm:ss"/></small>
		</div>
		<div class="form-group required">
			<label>Nouvel Index Heures Creuses</label>
			<g:field type="number decimal" name="index2" value="${ command.index2 as Long }" class="form-control" required="true"/>
		</div>
	</g:if>
	<g:else>
		<g:set var="lastIndex" value="${ compteur.lastIndex() }"/>
		<div class="form-group">
			<label>Ancien Index Heures Base</label>
			<g:field type="number decimal" name="lastIndex1" value="${ lastIndex?.value as Long }" class="form-control" readonly="true"/>
			<small class="text-muted">Dernier relevé le <g:formatDate date="${ lastIndex?.dateValue }" format="dd/MM/yyyy 'à' HH:mm:ss"/></small>
		</div>
		<div class="form-group required">
			<label>Nouvel Index Heures Base</label>
			<g:field type="number decimal" name="index1" value="${ command.index1 as Long }" class="form-control" required="true"/>
		</div>
	</g:else>
</g:if>

<g:elseif test="${ compteur instanceof smarthome.automation.deviceType.CompteurGaz }">
	<g:set var="lastIndex" value="${ compteur.lastIndex() }"/>
	<div class="form-group">
		<label>Ancien Index</label>
		<g:field type="number decimal" name="lastIndex1" value="${ lastIndex?.value as Long }" class="form-control" readonly="true"/>
		<small class="text-muted">Dernier relevé le <g:formatDate date="${ lastIndex?.dateValue }" format="dd/MM/yyyy 'à' HH:mm:ss"/></small>
	</div>
	<div class="form-group required">
		<label>Nouvel Index</label>
		<g:field type="number decimal" name="index1" value="${ command.index1 as Long }" class="form-control" required="true"/>
	</div>
	<div class="form-group required">
		<label>Coefficient de conversion</label>
		<g:field type="number decimal" name="param1" value="${ (command.param1 ?: (device.metadata('coefConversion')?.value ?: defaultCoefGaz)) }" class="form-control" required="true" readonly="${ modeAdmin ? 'false' : 'true' }"/>
		<small class="text-muted">Ce coefficient sert à convertir les volumes en kWh. Il sera conservé dans la configuration du compteur</small>
	</div>
</g:elseif>

<div class="form-group required">
	<label>Date Index</label>
	<g:field name="dateIndex" class="form-control small" value="${ app.formatPicker(date: command.dateIndex) }" type="date" required="true"/>
	<small class="text-muted">Après calcul avec le dernier index enregistré, la consommation sera enregistrée à cette date</small>
</div>
		
