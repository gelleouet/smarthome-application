<section role="dialog" id="notification-dialog" class="aui-layer aui-dialog2 aui-dialog2-medium" aria-hidden="true" data-aui-modal="true" data-aui-remove-on-hide="true">
	<g:formRemote name="form-notification-dialog" class="aui" url="[action: 'saveNotification']" onComplete="closeNotificationDialog()">
		
		<g:hiddenField name="type" value="${ notification?.type ?: typeNotification  }"/>
		<g:hiddenField name="id" value="${ notification?.id  }"/>
		<g:hiddenField name="deviceEvent.id" value="${ deviceEvent?.id  }"/>
		
	    <header class="aui-dialog2-header">
	        <h2 class="aui-dialog2-header-main">Notification<span id="ajaxSpinner" class="spinner"/></h2>
	        
	        <div class="aui-dialog2-header-secondary">
				<fieldset>
				</fieldset>
	        </div>
	        
	    </header>
	    
	    <div class="aui-dialog2-content">
	    
	    	<fieldset>
	    		<div class="field-group">
					<label >
						Message
					</label>
					<g:textArea name="message" value="${ notification?.message }" class="textarea long-field" rows="10"/>
				</div>
	    	</fieldset>

	    	<fieldset>
	    		<div class="field-group">
					<label for="actif">
						Script		
					</label>
					<g:checkBox name="script" value="${notification?.script}" class="checkbox"/>
				</div>
	    	</fieldset>


			<p class="h6">L'application enverra un message par défaut avec le nom de l'objet ayant déclenché l'événement si aucun message n'est renseigné</p>
			<p class="h6">Un script Groovy peut être utilisée en tant que message avec les variables prédéfinies :</p>
			<ul class="h6">
				<li>device : objet smarthome.automation.Device. Représente l'objet associé à l'événement. Ex : device.command == "on", device.value == "1"</li>
				<li>deviceEvent : objet smarthome.automation.DeviceEvent. L'événement en cours. Ex : deviceEvent.lastEvent</li>
			</ul>
			
	    </div>
	    
	    <footer class="aui-dialog2-footer">
	    	<div class="aui-dialog2-footer-actions">
	           	<div class="buttons">
	           		<button class="aui-button aui-button-primary">Enregistrer</button>
					<a class="aui-button aui-button-link cancel" onclick="closeNotificationDialog()">Annuler</a>
				</div>
	       	</div>
	    </footer>
	    
    </g:formRemote>
</section>