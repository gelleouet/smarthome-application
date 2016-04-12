<section role="dialog" id="device-share-dialog" class="aui-layer aui-dialog2 aui-dialog2-medium" aria-hidden="true" data-aui-modal="true" data-aui-remove-on-hide="true">
		
	    <header class="aui-dialog2-header">
	        <h2 class="aui-dialog2-header-main">${ device.label }<span id="ajaxSpinner" class="spinner"/></h2>
	        
	        <div class="aui-dialog2-header-secondary">
	        	
	        </div>
	        
	    </header>
	    
	    <div class="aui-dialog2-content">
	    
	    	<g:formRemote name="form-device-share-ajout" class="aui" url="[action: 'addShare']" update="ajaxDeviceShare">
	    		<g:hiddenField name="id" value="${ device.id }"/>
	    		<fieldset>
					<input name="sharedUserId" id="selectSharedUser" type="hidden" data-url="${ g.createLink(controller: 'user', action: 'userList') }"></input>
					<button class="aui-button aui-button-subtle" style="vertical-align:bottom"><span class="aui-icon aui-icon-small aui-iconfont-add">Ajouter </span> Ajouter</button>
				</fieldset>
			</g:formRemote>
	    
	    	<br/>
	    
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
	    
</section>