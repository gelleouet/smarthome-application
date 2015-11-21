<g:set var="opttarif" value="${  device?.metavalue('opttarif') }"/>
<g:set var="ptec" value="${  device?.metavalue('ptec') }"/>
<g:set var="isousc" value="${  device?.metavalue('isousc') }"/>
<g:set var="imax" value="${  device?.metavalue('imax') }"/>
<g:set var="hchp" value="${  device?.metavalue('hchp') }"/>
<g:set var="hchc" value="${  device?.metavalue('hchc') }"/>
<g:set var="papp" value="${  device?.metavalue('papp') }"/>
<g:set var="hcinst" value="${  device?.metavalue('hcinst') }"/>
<g:set var="hpinst" value="${  device?.metavalue('hpinst') }"/>
<g:set var="adps" value="${  device?.metavalue('adps') }"/>

<h4>Valeurs</h4>

<!-- ###############################################################" -->

<g:if test="${ opttarif?.id }">
	<g:hiddenField name="metavalues[0].id" value="${ opttarif.id }"/>
</g:if>

<g:hiddenField name="metavalues[0].name" value="opttarif"/>

<div class="field-group">
	<label title="opttarif">
		Option tarifaire
	</label>
	<g:field type="string" name="metavalues[0].value" disabled="true" value="${ opttarif?.value }" class="text medium-field"/>
</div>

<!-- ###############################################################" -->

<g:if test="${ ptec?.id }">
	<g:hiddenField name="metavalues[1].id" value="${ ptec.id }"/>
</g:if>

<g:hiddenField name="metavalues[1].name" value="ptec"/>

<div class="field-group">
	<label title="ptec">
		Période tarifaire en cours
	</label>
	<g:field type="string" name="metavalues[1].value" disabled="true" value="${ ptec?.value }" class="text medium-field"/>
</div>

<!-- ###############################################################" -->

<g:if test="${ isousc?.id }">
	<g:hiddenField name="metavalues[2].id" value="${ isousc.id }"/>
</g:if>

<g:hiddenField name="metavalues[2].name" value="isousc"/>

<div class="field-group">
	<label title="isousc">
		Instansité souscrite (A)
	</label>
	<g:field type="string" name="metavalues[2].value" disabled="true" value="${ isousc?.value }" class="text medium-field"/>
</div>

<!-- ###############################################################" -->

<g:if test="${ imax?.id }">
	<g:hiddenField name="metavalues[3].id" value="${ imax.id }"/>
</g:if>

<g:hiddenField name="metavalues[3].name" value="imax"/>

<div class="field-group">
	<label title="imax">
		Instansité maximale (A)
	</label>
	<g:field type="string" name="metavalues[3].value" disabled="true" value="${ imax?.value }" class="text medium-field"/>
	<div class="description">(Maximum depuis la mise en route du compteur)</div>
</div>

<!-- ###############################################################" -->

<g:if test="${ hchc?.id }">
	<g:hiddenField name="metavalues[4].id" value="${ hchc.id }"/>
</g:if>

<g:hiddenField name="metavalues[4].name" value="hchc"/>

<div class="field-group">
	<label title="hchc">
		Total heures creuses (Wh)
	</label>
	<g:field type="string" name="metavalues[4].value" disabled="true" value="${ hchc?.value }" class="text long-field"/>
	<div class="description">(Total depuis la mise en route du compteur)</div>
</div>

<!-- ###############################################################" -->

<g:if test="${ hchp?.id }">
	<g:hiddenField name="metavalues[5].id" value="${ hchp.id }"/>
</g:if>

<g:hiddenField name="metavalues[5].name" value="hchp"/>

<div class="field-group">
	<label title="hchp">
		Total heures pleines (Wh)
	</label>
	<g:field type="string" name="metavalues[5].value" disabled="true" value="${ hchp?.value }" class="text long-field"/>
	<div class="description">(Total depuis la mise en route du compteur)</div>
</div>

<!-- ###############################################################" -->

<g:if test="${ papp?.id }">
	<g:hiddenField name="metavalues[6].id" value="${ papp.id }"/>
</g:if>

<g:hiddenField name="metavalues[6].name" value="papp"/>

<div class="field-group">
	<label title="papp">
		Puissance apparente (VA)
	</label>
	<g:field type="string" name="metavalues[6].value" disabled="true" value="${ papp?.value }" class="text medium-field"/>
</div>

<!-- ###############################################################" -->

<g:if test="${ hcinst?.id }">
	<g:hiddenField name="metavalues[7].id" value="${ hcinst.id }"/>
</g:if>

<g:hiddenField name="metavalues[7].name" value="hcinst"/>

<div class="field-group">
	<label title="hcinst">
		Heures creuses (Wh)
	</label>
	<g:field type="string" name="metavalues[7].value" disabled="true" value="${ hcinst?.value }" class="text medium-field"/>
	<div class="description">(Valeur entre 2 lectures téléinfo)</div>
</div>

<!-- ###############################################################" -->

<g:if test="${ hpinst?.id }">
	<g:hiddenField name="metavalues[8].id" value="${ hpinst.id }"/>
</g:if>

<g:hiddenField name="metavalues[8].name" value="hpinst"/>

<div class="field-group">
	<label title="hpinst">
		Heures pleines (Wh)
	</label>
	<g:field type="string" name="metavalues[8].value" disabled="true" value="${ hpinst?.value }" class="text medium-field"/>
	<div class="description">(Valeur entre 2 lectures téléinfo)</div>
</div>

<!-- ###############################################################" -->

<g:if test="${ adps?.id }">
	<g:hiddenField name="metavalues[9].id" value="${ adps.id }"/>
</g:if>

<g:hiddenField name="metavalues[9].name" value="adps"/>

<div class="field-group">
	<label title="adps">
		Avertissement de dépassement de puissance souscrite (A)
	</label>
	<g:field type="string" name="metavalues[9].value" disabled="true" value="${ adps?.value }" class="text medium-field"/>
	<div class="description">(message émis uniquement en cas de dépassement effectif)</div>
</div>