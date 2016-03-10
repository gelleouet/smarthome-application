<section role="dialog" id="notification-account-dialog" class="aui-layer aui-dialog2 aui-dialog2-medium" aria-hidden="true" data-aui-modal="true" data-aui-remove-on-hide="true">
	<g:form name="form-notification-account-dialog" class="aui" action="save">
		
		<g:hiddenField name="type" value="${ notificationAccount?.type ?: typeNotification  }"/>
		<g:hiddenField name="id" value="${ notificationAccount?.id  }"/>
		
	    <header class="aui-dialog2-header">
	        <h2 class="aui-dialog2-header-main">Compte notification<span id="ajaxSpinner" class="spinner"/></h2>
	        
	        <div class="aui-dialog2-header-secondary">
				<fieldset>
				</fieldset>
	        </div>
	        
	    </header>
	    
	    <div class="aui-dialog2-content">
	    
	    	<fieldset>
	    		<div class="field-group">
					<label >
						Compte<span class="aui-icon icon-required">*</span>
					</label>
					<g:select name="className" value="${ notificationAccount?.className }" from="${ notificationSenders }" class="select" 
						optionValue="description" optionKey="name" required="true" onChange="onNotificationAccountChange(this)"
						data-url="${ g.createLink(action: 'formTemplateNotificationSender') }" data-immediate="true" noSelection="['': 'Sélectionner un compte']"/>
				</div>
	    	</fieldset>

	    	
	    	<div id="ajaxNotificationSenderForm">
		    	<g:if test="${ notificationAccount.id }">
		    		<g:include action="formTemplateNotificationSender" id="${ notificationAccount.id }"/>
		    	</g:if>
	    	</div>
	    	
	    	
	    </div>
	    
	    <footer class="aui-dialog2-footer">
	    	<div class="aui-dialog2-footer-actions">
	           	<div class="buttons">
	           		<button class="aui-button aui-button-primary">Enregistrer</button>
					<a class="aui-button aui-button-link cancel" onclick="closeNotificationAccountDialog()">Annuler</a>
				</div>
	       	</div>
	    </footer>
	    
    </g:form>
</section>