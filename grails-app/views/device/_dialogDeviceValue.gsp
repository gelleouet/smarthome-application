<%@ page import="smarthome.automation.LevelAlertEnum" %>

<section role="dialog" id="device-value-dialog" class="aui-layer aui-dialog2 aui-dialog2-small" aria-hidden="true" data-aui-modal="true" data-aui-remove-on-hide="true">
	<g:formRemote name="form-device-value-dialog" class="aui" url="[controller: 'device', action: 'saveDeviceValue']" onSuccess="closeDeviceValueDialog(true)" update="[failure: 'ajaxError']">
		
		<g:hiddenField name="id" value="${ deviceValue?.id }"/>
		
	    <header class="aui-dialog2-header">
	        <h2 class="aui-dialog2-header-main">Modification valeur<span id="ajaxSpinner" class="spinner"/></h2>
	        
	        <div class="aui-dialog2-header-secondary">
				<fieldset>
					<div class="buttons">
					</div>
				</fieldset>
	        </div>
	    </header>
	    
	    <div class="aui-dialog2-content">
	    	<h4>${ app.formatUserDateTime(date: deviceValue?.dateValue) }</h4>
	    	
	    	<div class="field-group">
				<label>
					Valeur
					<span class="aui-icon icon-required">*</span>
				</label>
				<g:field type="number decimal" name="value" value="${deviceValue?.value}" class="text medium-field" required="true"/>
			</div>
			
			<div class="field-group">
				<label>
					Alerte
				</label>
				<g:select name="alertLevel" value="${deviceValue?.alertLevel}" from="${ LevelAlertEnum.values() }"
					class="select medium-field" noSelection="['': '']"/>
			</div>
	    </div>
	    
	    <footer class="aui-dialog2-footer">
	    	<div class="aui-dialog2-footer-actions">
	           	<div class="buttons">
	           		<button class="aui-button aui-button-primary">Enregistrer</button>
					<a class="aui-button aui-button-link cancel" onclick="closeDeviceValueDialog(false)">Annuler</a>
				</div>
	       	</div>
	    </footer>
	    
    </g:formRemote>
</section>