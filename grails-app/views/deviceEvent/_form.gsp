<%@ page import="smarthome.automation.DeviceEvent" %>
<%@ page import="smarthome.automation.notification.NotificationAccountEnum" %>

<div class="field-group">
	<label for="device">
		Objet
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:select id="device" name="device.id" required="true" from="${devices}" optionKey="id" optionValue="label"
		value="${deviceEvent?.device?.id}" class="select"/>
</div>

<div class="field-group">
	<label for="libelle">
		Description
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="libelle" required="true" value="${deviceEvent?.libelle}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="actif">
		Actif		
	</label>
	<g:checkBox name="actif" value="${deviceEvent?.actif}" class="checkbox"/>
	<div class="description">Si désactivé, suspend temporairement un événement</div>
</div>


<div class="field-group">
	<label for="modes">
		Modes
	</label>
	<g:select name="modeList" from="${ modes }" value="${ deviceEvent?.modes*.mode }" 
		optionKey="id" optionValue="name" class="select combobox" multiple="true"/>
	<div class="description">Déclenche l'événement si au moins un mode est activé</div>
</div>

<div class="field-group">
	<label for="condition">
		Condition
	</label>
	<g:textArea name="condition" value="${deviceEvent?.condition}" class="short-script textarea text long-field"/>
	<div class="description">Expression Groovy renvoyant en dernière instruction un boolean. Variables pré-définies :
	<ul>
	<li>device : objet smarthome.automation.Device. Représente le device associé à l'événement. Ex : device.command == "on", device.value == "1"</li>
	<li>devices : objet Map. Contient tous les devices indexés par leur mac. Ex : devices['gpio4'].value</li>
	<li>deviceEvent : l'événement courant. Ex : deviceEvent.lastEvent, deviceEvent.isBlindTime()</li>
	</ul>
	</div>
</div>

<g:if test="${ deviceEvent?.id }">
	<fieldset class="group">
	    <legend><span>Notifications</span></legend>
	    <div class="checkbox">
	    	<g:checkBox name="notificationSms" class="checkbox" value="${ deviceEvent.notificationSms }"/>
	        <label for="notificationSms">SMS</label>
	        <g:remoteLink class="aui-button-link" url="[action: 'dialogNotification', id: deviceEvent.id]" params="[typeNotification: NotificationAccountEnum.sms]" onComplete="showNotificationDialog()" update="ajaxDialog">Modifier le message</g:remoteLink>
	    </div>                                
	    <div class="checkbox">
	    	<g:checkBox name="notificationMail" class="checkbox" value="${ deviceEvent.notificationMail }"/>
	        <label for="notificationMail">Email</label>
	        <g:remoteLink class="aui-button-link" url="[action: 'dialogNotification', id: deviceEvent.id]" params="[typeNotification: NotificationAccountEnum.mail]" onComplete="showNotificationDialog()" update="ajaxDialog">Modifier le message</g:remoteLink>
	    </div>                                
	</fieldset>
</g:if>



