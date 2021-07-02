<section role="dialog" id="supervision-export-dialog" class="aui-layer aui-dialog2 aui-dialog2-medium" aria-hidden="true" data-aui-modal="true" data-aui-remove-on-hide="true">
	<g:form name="supervision-export-form" class="aui" action="exportAdmin">
		
		<g:hiddenField name="userId" value="${ command.userId }"/>
		<g:hiddenField name="deviceTypeClass" value="${ command.deviceTypeClass }"/>
		<g:hiddenField name="search" value="${ command.search }"/>
		
		
	    <header class="aui-dialog2-header">
	        <h2 class="aui-dialog2-header-main">Export<span id="ajaxSpinner" class="spinner"/></h2>
	        
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
					Export
					<span class="aui-icon icon-required">*</span>
				</label>
				<g:select name="exportImpl" from="${ exportImpls }" class="select long-field" required="true"/>
			</div>
			
	    	<div class="field-group">
				<label>
					Date début
					<span class="aui-icon icon-required">*</span>
				</label>
				<g:field type="date" name="dateDebut" class="text medium-field" value="${ app.formatPicker(date: command.dateDebut) }" required="true"/>
			</div>
			
			<div class="field-group">
				<label>
					Date fin
				</label>
				<g:field type="date" name="dateFin" class="text medium-field" value="${ app.formatPicker(date: command.dateFin) }" required="true"/>
			</div>
			
			<div class="field-group">
				<label>
					Sélection des valeurs
				</label>
				<g:select name="metavalueNames" from="${ metavalueNames }" optionKey="name" optionValue="label" class="select combobox" multiple="true"/>
				<div class="description">Si aucune valeur n'est sélectionnée, l'export sera exécutée sur toutes les valeurs.</div>
			</div>
	    </div>
	    
	    <footer class="aui-dialog2-footer">
	    	<div class="aui-dialog2-footer-actions">
	           	<div class="buttons">
	           		<button class="aui-button aui-button-primary">Exporter</button>
					<a class="aui-button aui-button-link cancel" onclick="hideDialog('supervision-export-dialog')">Annuler</a>
				</div>
	       	</div>
	    </footer>
	    
    </g:form>
</section>