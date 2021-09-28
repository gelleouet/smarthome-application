<section role="dialog" id="add-widget-dialog" class="aui-layer aui-dialog2 aui-dialog2-medium" aria-hidden="true" data-aui-modal="true" data-aui-remove-on-hide="true">
	
		<g:formRemote name="form-add-widget-dialog" class="aui" url="[action: 'contentDialogAddWidget', controller: 'widget']" update="ajaxContentDialogWidget">
		    <header class="aui-dialog2-header">
		    	
			        <h2 class="aui-dialog2-header-main">Ajout widget<span id="ajaxSpinner" class="spinner"/></h2>
			        
			        <div class="aui-dialog2-header-secondary">
						<fieldset>
							<input class="text medium-field" type="text" placeholder="Rechercher..." name="search" value="${ command?.search }"/>
						</fieldset>
			        </div>
		    </header>
	     </g:formRemote>
	    
	    <div class="aui-dialog2-content" style="height:600px">
	    	<div id="ajaxContentDialogWidget" ajax="true">
	    		<g:render template="contentDialogAddWidget"/>			
	    	</div>
	    </div>
	    
	    <footer class="aui-dialog2-footer">
	    	<div class="aui-dialog2-footer-actions">
	           	<div class="buttons">
					<a class="aui-button aui-button-link cancel" onclick="hideDialog('add-widget-dialog')">Annuler</a>
				</div>
	       	</div>
	    </footer>
	    
</section>