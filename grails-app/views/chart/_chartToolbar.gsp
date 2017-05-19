<form class="aui">
	<g:hiddenField name="viewMode" value="${ command.viewMode }"/>
	<div class="aui-toolbar2">
	    <div class="aui-toolbar2-inner">
	        <div class="aui-toolbar2-primary">
	        	<div class="aui-buttons">
	        		<button class="aui-button"><span class="aui-icon aui-icon-small aui-iconfont-arrows-left">Précédent</span></button>
	        		<button class="aui-button"><span class="aui-icon aui-icon-small aui-iconfont-arrows-right">Suivant</span></button>
	        	</div>
	        	
	        	<div class="aui-buttons">
	        		<g:field type="date" name="dateChart" style="font-family: inherit; padding: 4px 5px; height:2.14285714em; margin-top:10px;" class="aui-date-picker" value="${ app.formatPicker(date: command.dateChart) }" required="true"/>	
	        	</div>
	        	
	        	<div class="aui-buttons">
	        		<button class="aui-button ${ command.viewMode == 'day' ? 'aui-button-primary' : '' }">jour</button>
	        		<button class="aui-button ${ command.viewMode == 'week' ? 'aui-button-primary' : '' }">semaine</button>
	        		<button class="aui-button ${ command.viewMode == 'month' ? 'aui-button-primary' : '' }">mois</button>
	        		<button class="aui-button ${ command.viewMode == 'year' ? 'aui-button-primary' : '' }">année</button>
	            </div>
	        </div>
	        
	        <div class="aui-toolbar2-secondary">
	            <div id="button-set2" class="aui-buttons">
	                <button class="aui-button aui-dropdown2-trigger" aria-owns="dropdown2-view" aria-haspopup="true" aria-controls="dropdown2-view" data-container="#button-set2"><span class="aui-icon aui-icon-small aui-iconfont-view">View</span></button>
	            </div>
	        </div>
	    </div><!-- .aui-toolbar-inner -->
	</div>
</form>