<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
	
		<div class="aui-toolbar2">
		    <div class="aui-toolbar2-inner">
		        <div class="aui-toolbar2-primary">
		            <div>
		                <h3>Mes actions</h3>
		            </div>		            
		        </div>
		        <div class="aui-toolbar2-secondary">
		        	<g:form >
		            <div class="aui-buttons">
						<g:actionSubmit class="aui-button" value="Ajouter" action="edit"/>
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		
		<h4>
			<g:form class="aui" action="producteurEnergieActions">
				<fieldset>
					<input autofocus="true" class="text long-field" type="text" placeholder="Rechercher ..." name="producteur" value="${ command?.producteur }"/>
					<button class="aui-button aui-button-subtitle"><span class="aui-icon aui-icon-small aui-iconfont-search"></span></button>
				</fieldset>
			</g:form>
		</h4>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Nom</th>
		            <th>Actions</th>
		            <th>Investissement</th>
		            <th>Surface</th>
		            <th>Objet</th>
		            <th class="column-1-buttons"></th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ producteurEnergieActionInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">${ bean.producteur.libelle }</g:link></td>
			            <td>${ bean.nbaction } / ${ bean.producteur.nbaction } (<g:formatNumber number="${ bean.percentAction() }" type="percent" maxFractionDigits="1"/>)</td>
			            <td>${ bean.investissement() }€ / ${ bean.producteur.investissement }€</td>
			            <td>${ bean.surface() }m² / ${ bean.producteur.surface }m²</td>
			            <td>${ bean.device?.label }</td>
			            <td class="column-1-buttons command-column">
			            	<g:link class="aui-button aui-button-subtle confirm-button" title="Suppimer" action="delete" id="${ bean.id }">
			            		<span class="aui-icon aui-icon-small aui-iconfont-delete">
			            	</g:link>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>