<section role="dialog" id="diagram-workflow-dialog" class="aui-layer aui-dialog2 aui-dialog2-medium" aria-hidden="true" data-aui-modal="true" data-aui-remove-on-hide="true">
	    <header class="aui-dialog2-header">
	        <h2 class="aui-dialog2-header-main">Workflow BPMN Diagram<span id="ajaxSpinner" class="spinner"/></h2>
	        
	        <div class="aui-dialog2-header-secondary">
				<fieldset>
				</fieldset>
	        </div>
	        
	    </header>
	    
	    <div class="aui-dialog2-content">
	    
	    	<div id="ajaxDialogError"></div>
	    
	    	<div style="overflow:auto;">
	    		<img src="${ g.createLink(action: 'diagramImage', id: workflow.id) }" />
	    	</div>
	    </div>
	    
	    <footer class="aui-dialog2-footer">
	    	<div class="aui-dialog2-footer-actions">
	           	<div class="buttons">
					<a class="aui-button aui-button-link cancel" onclick="AJS.dialog2('#diagram-workflow-dialog').hide();">Fermer</a>
				</div>
	       	</div>
	    </footer>
	    
</section>