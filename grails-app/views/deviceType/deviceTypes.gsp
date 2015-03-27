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
		                <h3>Catalogue</h3>
		            </div>		            
		        </div>
		        <div class="aui-toolbar2-secondary">
		        	<g:form >
		            <div class="aui-buttons">
						<g:actionSubmit class="aui-button" value="Ajouter un produit" action="create"/>
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		
		<h4>
			<g:form class="aui" action="deviceTypes">
				<fieldset>
					<input autofocus="true" class="text long-field" type="text" placeholder="Libellé ..." name="deviceTypeSearch" value="${ deviceTypeSearch }"/>
				</fieldset>
			</g:form>
		</h4>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Libellé</th>
		            <th>Classe</th>
		            <th>Capteur</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ deviceTypeInstanceList }">
			        <tr>
			            <td><g:link action="deviceType" id="${bean.id }">${ bean.libelle }</g:link></td>
			            <td>${ bean.implClass }</td>
			            <td>${ bean.capteur ? 'X' : '' }</td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>