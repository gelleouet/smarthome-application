<html>
<head>
<meta name='layout' content='main' />
</head>

<body onload="onLoadEditDefi();">
	<g:applyLayout name="page-settings" model="[titre: 'Défis', navigation: 'Compte']">
	
		<div class="row mb-4">
			<div class="col-8">
				<h4>${ defi?.id ? 'Défi : ' + defi.libelle : 'Nouveau défi' }</h4>
			</div>
			<div class="col-4 text-right">
				<div class="btn-group">
				</div>
			</div>
		</div>
	
	
		<g:form controller="defi" method="post" action="save">
			<g:hiddenField name="id" value="${defi?.id}" />
	
			<div class="nav nav-tabs" role="tablist">
	            <a class="nav-item nav-link active" data-toggle="tab" role="tab" href="#tabs-defi-general">Général</a>
	            <a class="nav-item nav-link" data-toggle="tab" role="tab" href="#tabs-defi-organisation">Participants</a>
		    </div>
	
			<div class="tab-content">
			    <div class="tab-pane active" id="tabs-defi-general" role="tabpanel">
			    	<div class="smart-tabs-content">
			       		<g:render template="form"/>
			       	</div>
			    </div>
			    <div class="tab-pane" id="tabs-defi-organisation" role="tabpanel">
			    	<div class="smart-tabs-content">
			    		<div id="participant-defi-div">
				    		<g:if test="${ defi?.id }">
				    			<g:render template="participants"/>
				    		</g:if>
			    		</div>
			    	</div>
			    </div>
			</div> <!-- div.tab-content -->
	
			<br/>
	
			<button class="btn btn-primary">Enregistrer</button>
			<g:link action="defis" class="btn btn-link">Annuler</g:link>
		</g:form>
		
	</g:applyLayout>
</body>
</html>