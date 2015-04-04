<g:set var="opttarif" value="${  device?.metavalue('opttarif') }"/>
<g:set var="ptec" value="${  device?.metavalue('ptec') }"/>
<g:set var="isousc" value="${  device?.metavalue('isousc') }"/>
<g:set var="imax" value="${  device?.metavalue('imax') }"/>
<g:set var="hchp" value="${  device?.metavalue('hchp') }"/>
<g:set var="hchc" value="${  device?.metavalue('hchc') }"/>
<g:set var="papp" value="${  device?.metavalue('papp') }"/>

<!-- ###############################################################" -->

<g:if test="${ opttarif?.id }">
	<g:hiddenField name="metavalues[0].id" value="${ opttarif.id }"/>
</g:if>

<g:hiddenField name="metavalues[0].name" value="opttarif"/>

<div class="field-group">
	<label>
		Option tarifaire
		<span class="aui-icon icon-required"></span>
	</label>
	<g:field type="string" name="metavalues[0].value" disabled="true" value="${ opttarif?.value }" class="text medium-field"/>
</div>

<!-- ###############################################################" -->

<g:if test="${ ptec?.id }">
	<g:hiddenField name="metavalues[1].id" value="${ ptec.id }"/>
</g:if>

<g:hiddenField name="metavalues[1].name" value="ptec"/>

<div class="field-group">
	<label>
		Période tarifaire en cours
		<span class="aui-icon icon-required"></span>
	</label>
	<g:field type="string" name="metavalues[1].value" disabled="true" value="${ ptec?.value }" class="text medium-field"/>
</div>

<!-- ###############################################################" -->

<g:if test="${ isousc?.id }">
	<g:hiddenField name="metavalues[2].id" value="${ isousc.id }"/>
</g:if>

<g:hiddenField name="metavalues[2].name" value="isousc"/>

<div class="field-group">
	<label>
		Instansité souscrite (A)
		<span class="aui-icon icon-required"></span>
	</label>
	<g:field type="string" name="metavalues[2].value" disabled="true" value="${ isousc?.value }" class="text medium-field"/>
</div>

<!-- ###############################################################" -->

<g:if test="${ imax?.id }">
	<g:hiddenField name="metavalues[3].id" value="${ imax.id }"/>
</g:if>

<g:hiddenField name="metavalues[3].name" value="imax"/>

<div class="field-group">
	<label>
		Instansité maximale (A)
		<span class="aui-icon icon-required"></span>
	</label>
	<g:field type="string" name="metavalues[3].value" disabled="true" value="${ imax?.value }" class="text medium-field"/>
</div>

<!-- ###############################################################" -->

<g:if test="${ hchc?.id }">
	<g:hiddenField name="metavalues[4].id" value="${ hchc.id }"/>
</g:if>

<g:hiddenField name="metavalues[4].name" value="hchc"/>

<div class="field-group">
	<label>
		Index heures creuses (Wh)
		<span class="aui-icon icon-required"></span>
	</label>
	<g:field type="string" name="metavalues[4].value" disabled="true" value="${ hchc?.value }" class="text long-field"/>
</div>

<!-- ###############################################################" -->

<g:if test="${ hchp?.id }">
	<g:hiddenField name="metavalues[5].id" value="${ hchp.id }"/>
</g:if>

<g:hiddenField name="metavalues[5].name" value="hchp"/>

<div class="field-group">
	<label>
		Index heures pleines (Wh)
		<span class="aui-icon icon-required"></span>
	</label>
	<g:field type="string" name="metavalues[5].value" disabled="true" value="${ hchp?.value }" class="text long-field"/>
</div>

<!-- ###############################################################" -->

<g:if test="${ papp?.id }">
	<g:hiddenField name="metavalues[6].id" value="${ papp.id }"/>
</g:if>

<g:hiddenField name="metavalues[6].name" value="papp"/>

<div class="field-group">
	<label>
		Puissance apparente (VA)
		<span class="aui-icon icon-required"></span>
	</label>
	<g:field type="string" name="metavalues[6].value" disabled="true" value="${ papp?.value }" class="text medium-field"/>
</div>