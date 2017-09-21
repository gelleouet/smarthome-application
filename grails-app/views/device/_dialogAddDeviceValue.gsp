<%@ page import="smarthome.automation.LevelAlertEnum" %>

<section role="dialog" id="add-device-value-dialog" class="aui-layer aui-dialog2 aui-dialog2-small" aria-hidden="true" data-aui-modal="true" data-aui-remove-on-hide="true">
	<g:formRemote name="form-add-device-value-dialog" class="aui" url="[controller: 'device', action: 'addDeviceValue']" onSuccess="closeAddDeviceValueDialog(true)" update="[failure: 'ajaxError']">
		
		<g:hiddenField name="device.id" value="${ device?.id }"/>
		
	    <header class="aui-dialog2-header">
	        <h2 class="aui-dialog2-header-main">Ajout valeur<span id="ajaxSpinner" class="spinner"/></h2>
	        
	        <div class="aui-dialog2-header-secondary">
				<fieldset>
					<div class="buttons">
					</div>
				</fieldset>
	        </div>
	    </header>
	    
	    <div class="aui-dialog2-content">
	    	<div class="field-group">
				<label>
					Date
					<span class="aui-icon icon-required">*</span>
				</label>
				<g:field type="text" name="dateValue" class="text medium-field aui-datetime-picker aui-datetime-picker-dialog" required="true" 
					value="${ app.formatDateTimePickerUser(date: new Date()) }"/>
			</div>
	    	
	    	<div class="field-group">
				<label>
					Valeur
					<span class="aui-icon icon-required">*</span>
				</label>
				<g:field type="number decimal" name="value" class="text medium-field" required="true"/>
			</div>
			
			<div class="field-group">
				<label>
					Alerte
				</label>
				<g:select name="alertLevel" from="${ LevelAlertEnum.values() }" class="select medium-field" noSelection="['': '']"/>
			</div>
	    </div>
	    
	    <footer class="aui-dialog2-footer">
	    	<div class="aui-dialog2-footer-actions">
	           	<div class="buttons">
	           		<button class="aui-button aui-button-primary">Ajouter</button>
					<a class="aui-button aui-button-link cancel" onclick="closeAddDeviceValueDialog(false)">Annuler</a>
				</div>
	       	</div>
	    </footer>
	    
    </g:formRemote>
</section>