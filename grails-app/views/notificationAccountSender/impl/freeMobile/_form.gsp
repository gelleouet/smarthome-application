<div class="field-group">
	<label>
		Login
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="jsonConfig.user" required="true" value="${ notificationAccount?.jsonConfig?.user }" class="text long-field"/>
</div>


<div class="field-group">
	<label>
		Clé identification
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="jsonConfig.pass" required="true" value="${ notificationAccount?.jsonConfig?.pass }" class="text long-field"/>
</div>

