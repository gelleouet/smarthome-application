<section role="dialog" id="device-share-dialog" class="aui-layer aui-dialog2 aui-dialog2-large" aria-hidden="true" data-aui-modal="true" data-aui-remove-on-hide="true">
	<g:form name="form-device-share-dialog" class="aui" action="save">
		
	    <header class="aui-dialog2-header">
	        <h2 class="aui-dialog2-header-main">Partage ${ device.label }<span id="ajaxSpinner" class="spinner"/></h2>
	        
	        <div class="aui-dialog2-header-secondary">
				<fieldset>
				</fieldset>
	        </div>
	        
	    </header>
	    
	    <div class="aui-dialog2-content">
	    
	    	<div id="ajaxDeviceShare">
		    	<g:render template="datatable"/>
	    	</div>
	    	
	    	
	    </div>
	    
	    <footer class="aui-dialog2-footer">
	    	<div class="aui-dialog2-footer-actions">
	           	<div class="buttons">
					<a class="aui-button aui-button-primary" onclick="closeDeviceShareDialog()">Fermer</a>
				</div>
	       	</div>
	    </footer>
	    
    </g:form>
</section>