<app:datatable datatableId="datatableWidget" recordsTotal="${ widgets.size() ?: 0 }" paginateForm="form-add-widget-dialog">
	<tbody>
  		<g:each var="widget" in="${ widgets }">
  			<tr><td>
  				<div class="aui-toolbar2">
				    <div class="aui-toolbar2-inner">
				        <div class="aui-toolbar2-primary">
				            <div>
				               <h5><strong>${ widget.libelle }</strong></h5>
				            </div>		            
				        </div>
				        <div class="aui-toolbar2-secondary">
				            <div class="aui-buttons">
				            	<g:link class="aui-button" controller="widget" action="addWidgetUser" id="${ widget.id }"><span class="aui-icon aui-icon-small aui-iconfont-add"></span> Ajouter</g:link>
				            </div>
				        </div>
				    </div><!-- .aui-toolbar-inner -->
				 </div>
  					
  				<p>${ widget.description }</p>
  			</td></tr>
  		</g:each>
	</tbody>
</app:datatable>
	    