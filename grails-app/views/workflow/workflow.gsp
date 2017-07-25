<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<g:uploadForm controller="workflow" method="post" class="aui" name="workflow-form">
		
			<div class="aui-toolbar2">
			    <div class="aui-toolbar2-inner">
			        <div class="aui-toolbar2-primary">
			            <div>
			               <h3>${ workflow.id ? 'Workflow : ' + workflow.libelle : 'Nouveau workflow' } <span id="ajaxSpinner" class="spinner"/></h3>
			            </div>		            
			        </div>
			        <div class="aui-toolbar2-secondary">
			            <div class="aui-buttons">
			            </div>
			        </div>
			    </div><!-- .aui-toolbar-inner -->
			  </div>
		
			<g:hiddenField name="id" value="${workflow.id}" />
	
			<g:render template="form"/>
			
	
			<div class="buttons-container">
				<div class="buttons">
					<g:if test="${workflow.id }">
						<g:actionSubmit value="Mettre à jour" action="saveEdit" class="aui-button aui-button-primary" />
					</g:if>
					<g:else>
						<g:actionSubmit value="Créer" action="saveCreate" class="aui-button aui-button-primary" />
					</g:else>
					
					<g:link action="workflows" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:uploadForm>
		
	</g:applyLayout>
</body>
</html>