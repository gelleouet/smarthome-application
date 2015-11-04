<h4>Configuration volet</h4>

<g:set var="orientation" value="${ device?.metadata('orientation') }"/>

<g:if test="${ orientation?.id }">
	<g:hiddenField name="metadatas[0].id" value="${ orientation.id }"/>
</g:if>

<g:hiddenField name="metadatas[0].name" value="orientation"/>

<div class="field-group">
	<label title="ID : orientation">
		Orientation
	</label>
	<g:select name="metadatas[0].value" value="${ orientation?.value }" class="select" from="${smarthome.automation.PointCardinalEnum.values()}"
		optionValue="libelle"/>
</div>


<h4>Configuration périphérique</h4>
<g:render template="/deviceType/generic/metadatasForm" model="[device: device, exclude: ['orientation'], startStatus: 1, commitButton: true]"/>


<h4>Valeurs</h4>
<g:render template="/deviceType/generic/metavaluesForm" model="[device: device]"/>


