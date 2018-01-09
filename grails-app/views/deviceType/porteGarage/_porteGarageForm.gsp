<%@ page import="smarthome.automation.DeviceSearchCommand" %>


<g:set var="timeout" value="${ device?.metadata('timeout') }"/>

<g:if test="${ timeout?.id }">
	<g:hiddenField name="metadatas[0].id" value="${ timeout.id }"/>
</g:if>

<g:hiddenField name="metadatas[0].name" value="timeout"/>

<div class="field-group">
	<label title="API : device.metadata('timeout')?.value">
		Timeout (en millisecondes)
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:field type="number" name="metadatas[0].value" required="true" value="${timeout?.value}" class="text medium-field"/>
</div>


<g:set var="deviceEtat" value="${ device?.metadata('deviceEtat') }"/>
<g:set var="deviceService" bean="deviceService"/>

<g:if test="${ deviceEtat?.id }">
	<g:hiddenField name="metadatas[1].id" value="${ deviceEtat.id }"/>
</g:if>

<g:hiddenField name="metadatas[1].name" value="deviceEtat"/>

<div class="field-group">
	<label title="API : device.metadata('deviceEtat')?.value">
		Object état associé
	</label>
	<g:select name="metadatas[1].value" from="${ deviceService.listByUser(new DeviceSearchCommand([userId: user.id])) }"
		value="${ deviceEtat?.value }"
		class="select combobox" optionKey="id" optionValue="label" noSelection="['' : ' ']"/>
	<div class="description">Un bouton poussoir ne connait pas forcément l'état de l'objet qu'il actionne.
		En lui associant un objet d'état, le bouton poussoir changera son apparence en fonction de l'état de cet objet associé.</div>
</div>


<g:set var="defaulttimer" value="${ device?.metadata('defaulttimer') }"/>

<g:if test="${ defaulttimer?.id }">
	<g:hiddenField name="metadatas[2].id" value="${ defaulttimer.id }"/>
</g:if>

<g:hiddenField name="metadatas[2].name" value="defaulttimer"/>

<div class="field-group">
	<label title="API : device.metadata('defaulttimer')?.value">
		Minuterie par défaut (en minutes)
	</label>
	<g:field type="text" name="metadatas[2].value" value="${defaulttimer?.value}" class="text medium-field"/>
</div>



<h4>Configuration périphérique</h4>
<g:render template="/deviceType/generic/metadatasForm" 
	model="[device: device, exclude: ['timeout', 'deviceEtat', 'defaulttimer'], startStatus: 3, commitButton: true]"/>



