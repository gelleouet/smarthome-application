<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
	
		<g:form controller="chart" method="post" class="aui" name="chart-form">
		
			<div class="aui-toolbar2">
			    <div class="aui-toolbar2-inner">
			        <div class="aui-toolbar2-primary">
			            <div>
			                <h3>${ chart.id ? 'Graphique : ' + chart.label : 'Nouveau graphique' } <span id="ajaxSpinner" class="spinner"/></h3>
			            </div>		            
			        </div>
			        <div class="aui-toolbar2-secondary">
			            <div class="aui-buttons">
			            	<g:submitToRemote class="aui-button" value="Ajouter un objet" update="ajaxPeripherique" url="[action: 'addDevice']" id="${ chart.id }"></g:submitToRemote>
			            </div>
			        </div>
			    </div><!-- .aui-toolbar-inner -->
			</div>
		
		
			<g:hiddenField name="id" value="${chart.id}" />
	
			<g:render template="form"/>
			
			<h4>Objets associés</h4>
			<br/>
			<div id="ajaxPeripherique">
				<g:render template="chartDevice"></g:render>
			</div>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:if test="${chart.id }">
						<g:actionSubmit value="Mettre à jour" action="saveEdit" class="aui-button aui-button-primary" />
					</g:if>
					<g:else>
						<g:actionSubmit value="Créer" action="saveCreate" class="aui-button aui-button-primary" />
					</g:else>
					
					<g:link action="charts" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>