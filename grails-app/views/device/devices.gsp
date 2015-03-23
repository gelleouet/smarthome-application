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
		                <h3>Périphériques</h3>
		            </div>		            
		        </div>
		        <div class="aui-toolbar2-secondary">
		        	<g:form >
		            <div class="aui-buttons">
						<g:actionSubmit class="aui-button" value="Ajouter un périphérique" action="create"/>
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		
		<h4>
			<g:form class="aui" action="devices">
				<fieldset>
					<input autofocus="true" class="text long-field" type="text" placeholder="Nom, groupe ..." name="deviceSearch" value="${ deviceSearch }"/>
				</fieldset>
			</g:form>
		</h4>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Nom</th>
		            <th>Groupe</th>
		            <th>Mac</th>
		            <th>Valeur courante</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ deviceInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">${ bean.label }</g:link></td>
			            <td>${ bean.groupe }</td>
			            <td>${ bean.mac }</td>
			            <td>
			            	<g:if test="${ bean.value }">
			            		${ bean.value } le ${ bean.dateValue }
			            	</g:if>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>