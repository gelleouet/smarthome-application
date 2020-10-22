<g:set var="lastIndex" value="${ compteur.lastIndex() }"/>

<g:hiddenField name="lastIndex1" value="${ lastIndex?.value }"/>

<div class="form-group">
	<label>Dernier Index</label>
	<div class="row">
		<div class="col">
			<g:field type="number" name="lastHighIndex1" value="${ compteur.indexHigh(lastIndex?.value) as Long }" class="form-control form-control-lg" readonly="true" style="color:white; background-color:#302C34; text-align:right"/>
			<small class="text-muted">Part index en m3</small>
		</div>
		<div class="col">
			<g:field type="number" name="lastLowIndex1" value="${ compteur.indexLow(lastIndex?.value) as Long }" class="form-control form-control-lg" readonly="true" style="color:white; background-color:#792D36; text-align:right"/>
			<small class="text-muted">Part index en litre</small>
		</div>
	</div>
	<small class="text-muted">Dernier relevé le <g:formatDate date="${ lastIndex?.dateValue }" format="dd/MM/yyyy 'à' HH:mm:ss"/></small>
</div>
<div class="form-group required">
	<label>Nouvel Index</label>
	<div class="row">
		<div class="col">
			<g:field type="number" name="highindex1" value="${ command.highindex1 as Long }" class="form-control form-control-lg" required="true" style="color:white; background-color:#302C34; text-align:right"/>
			<small class="text-muted">Part index en m3</small>
		</div>
		<div class="col">
			<g:field type="number" name="lowindex1" value="${ command.lowindex1 as Long }" class="form-control form-control-lg" required="true" style="color:white; background-color:#792D36; text-align:right"/>
			<small class="text-muted">Part index en litre</small>
		</div>
	</div>
</div>
