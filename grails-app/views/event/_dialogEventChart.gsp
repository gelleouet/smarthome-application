<section role="dialog" id="event-chart-dialog" class="aui-layer aui-dialog2 aui-dialog2-xlarge" aria-hidden="true" data-aui-modal="true" data-aui-remove-on-hide="true">
	    <header class="aui-dialog2-header">
	        <h2 class="aui-dialog2-header-main">Planification annuelle<span id="ajaxSpinner" class="spinner"/></h2>
	        
	        <div class="aui-dialog2-header-secondary">
				<fieldset>
					<div class="buttons">
					</div>
				</fieldset>
	        </div>
	    </header>
	    
	    <div class="aui-dialog2-content">
	    	<h4>${ event.libelle }</h4>
	    	<g:render template="eventChart"/>	
	    </div>
	    
	    <footer class="aui-dialog2-footer">
	    	<div class="aui-dialog2-footer-actions">
	           	<div class="buttons">
					<a class="aui-button aui-button-link cancel" onclick="closeEventChartDialog()">Fermer</a>
				</div>
	       	</div>
	    </footer>
	    
</section>