<%@ page import="smarthome.automation.Agent" %>



<div class="field-group">
	<label for="mac">
		Mac
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="mac" required="" value="${agentInstance?.mac}"class="text medium-field"/>

</div>

<div class="field-group">
	<label for="privateIp">
		Private Ip
		
	</label>
	<g:textField name="privateIp" value="${agentInstance?.privateIp}"class="text medium-field"/>

</div>

<div class="field-group">
	<label for="publicIp">
		Public Ip
		
	</label>
	<g:textField name="publicIp" value="${agentInstance?.publicIp}"class="text medium-field"/>

</div>

<div class="field-group">
	<label for="agentModel">
		Agent Model
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="agentModel" required="" value="${agentInstance?.agentModel}"class="text medium-field"/>

</div>

<div class="field-group">
	<label for="devices">
		Devices
		
	</label>
	
<ul class="one-to-many">
<g:each in="${agentInstance?.devices?}" var="d">
    <li><g:link controller="device" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="device" action="create" params="['agent.id': agentInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'device.label', default: 'Device')])}</g:link>
</li>
</ul>


</div>

<div class="field-group">
	<label for="lastConnexion">
		Last Connexion
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:datePicker name="lastConnexion" precision="day"  value="${agentInstance?.lastConnexion}"  />

</div>

<div class="field-group">
	<label for="locked">
		Locked
		
	</label>
	<g:checkBox name="locked" value="${agentInstance?.locked}" class="checkbox"/>

</div>

<div class="field-group">
	<label for="online">
		Online
		
	</label>
	<g:checkBox name="online" value="${agentInstance?.online}" class="checkbox"/>

</div>

<div class="field-group">
	<label for="tokens">
		Tokens
		
	</label>
	
<ul class="one-to-many">
<g:each in="${agentInstance?.tokens?}" var="t">
    <li><g:link controller="agentToken" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="agentToken" action="create" params="['agent.id': agentInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'agentToken.label', default: 'AgentToken')])}</g:link>
</li>
</ul>


</div>

<div class="field-group">
	<label for="user">
		User
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:select id="user" name="user.id" from="${smarthome.security.User.list()}" optionKey="id" required="" value="${agentInstance?.user?.id}" class="select"/>

</div>

