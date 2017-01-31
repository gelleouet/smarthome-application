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


<g:set var="labelpush" value="${ device?.metadata('labelpush') }"/>

<g:if test="${ labelpush?.id }">
	<g:hiddenField name="metadatas[1].id" value="${ labelpush.id }"/>
</g:if>

<g:hiddenField name="metadatas[1].name" value="labelpush"/>

<div class="field-group">
	<label title="API : device.metadata('labelpush')?.value">
		Libellé PUSH
	</label>
	<g:field type="text" name="metadatas[1].value" value="${labelpush?.value}" class="text medium-field"/>
	<div class="description">Libellé utilisé par défaut quand aucun objet d'état n'est associé (Par défaut : actionner)</div>
</div>


<g:set var="deviceEtat" value="${ device?.metadata('deviceEtat') }"/>
<g:set var="deviceService" bean="deviceService"/>

<g:if test="${ deviceEtat?.id }">
	<g:hiddenField name="metadatas[2].id" value="${ deviceEtat.id }"/>
</g:if>

<g:hiddenField name="metadatas[2].name" value="deviceEtat"/>

<div class="field-group">
	<label title="API : device.metadata('deviceEtat')?.value">
		Object état associé
	</label>
	<g:select name="metadatas[2].value" from="${ deviceService.listByUser(new DeviceSearchCommand([userId: user.id])) }"
		value="${ deviceEtat?.value }"
		class="select combobox" optionKey="id" optionValue="label" noSelection="['' : ' ']"/>
	<div class="description">Un bouton poussoir ne connait pas forcément l'état de l'objet qu'il actionne.
		En lui associant un objet d'état, le bouton poussoir changera son apparence en fonction de l'état de cet objet associé.</div>
</div>


<g:set var="labelon" value="${ device?.metadata('labelon') }"/>

<g:if test="${ labelon?.id }">
	<g:hiddenField name="metadatas[3].id" value="${ labelon.id }"/>
</g:if>

<g:hiddenField name="metadatas[3].name" value="labelon"/>

<div class="field-group">
	<label title="API : device.metadata('labelon')?.value">
		Libellé ON
	</label>
	<g:field type="text" name="metadatas[3].value" value="${labelon?.value}" class="text medium-field"/>
	<div class="description">Libellé utilisé uniquement si un objet d'état est associé</div>
</div>


<g:set var="labeloff" value="${ device?.metadata('labeloff') }"/>

<g:if test="${ labeloff?.id }">
	<g:hiddenField name="metadatas[4].id" value="${ labeloff.id }"/>
</g:if>

<g:hiddenField name="metadatas[4].name" value="labeloff"/>

<div class="field-group">
	<label title="API : device.metadata('labeloff')?.value">
		Libellé OFF
	</label>
	<g:field type="text" name="metadatas[4].value" value="${labeloff?.value}" class="text medium-field"/>
	<div class="description">Libellé utilisé uniquement si un objet d'état est associé</div>
</div>



