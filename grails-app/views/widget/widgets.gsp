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
		                <h3>Widgets</h3>
		            </div>		            
		        </div>
		        <div class="aui-toolbar2-secondary">
		        	<g:form >
		            <div class="aui-buttons">
						<g:actionSubmit class="aui-button" value="Ajouter un widget" action="create"/>
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		
		<h4>
			<g:form name="widget-form" class="aui" action="widgets">
				<fieldset>
					<input autofocus="true" class="text long-field" type="text" placeholder="Rechercher ..." name="search" value="${ command.search }"/>
					<button class="aui-button aui-button-subtitle"><span class="aui-icon aui-icon-small aui-iconfont-search"></span></button>
				</fieldset>
			</g:form>
		</h4>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }" paginateForm="widget-form">
		    <thead>
		        <tr>
		            <th>Nom</th>
		            <th>Description</th>
		            <th>Style</th>
		            <th>Rafraichissement</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ widgetInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">${ bean.libelle }</g:link></td>
			            <td>${ bean.description }</td>
			            <td>${ bean.style }</td>
			            <td>${ bean.refreshPeriod }</td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>