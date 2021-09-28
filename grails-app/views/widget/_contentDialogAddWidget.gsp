<app:datatable datatableId="datatableWidget" recordsTotal="${ widgets.size() ?: 0 }" paginateForm="form-add-widget-dialog">
	<thead>
		<tr>
			<th>Nom</th>
		</tr>
	</thead>
	<tbody>
  		<g:each var="widget" in="${ widgets }">
  			<tr><td class="command-column">
  				<g:form controller="widget" action="addWidgetUser" id="${ widget.id }" class="aui">
	  				<div class="aui-group aui-group-split">
				        <div class="aui-item">
				            <div>
				               <h5><strong>${ widget.libelle }</strong></h5>
				            </div>		            
				        </div>
				        <div class="aui-item" style="width:100px;">
				            <button class="aui-button"><span class="aui-icon aui-icon-small aui-iconfont-add"></span> Ajouter</button>
				        </div>
					 </div> <!-- div.aui-group  -->
	  					
	  				<div style="margin-top:10px">${ widget.description }</div>
	  				
	  				<g:if test="${ widget.contentView}">
	  					<div>
	  						<g:render template="${ widget.contentView}"/>
	  					</div>
	  				</g:if>
  				</g:form>
  			</td></tr>
  		</g:each>
	</tbody>
</app:datatable>
	    