
<g:form controller="user" method="post" class="aui ${ mobileAgent ? 'top-label' : '' }">
	<g:hiddenField name="id" value="${user.id}" />

	<h4>Profil</h4>

	<div class="form-group required">
		<label for="username">Email</label>
		<g:textField name="username" value="${user.username}" type="email" class="form-control" required="true" />
		<g:applyLayout name="form-description">En cas de changement d'email, un
			email vous sera envoyé à la nouvelle adresse pour valider votre
			accès.</g:applyLayout>
	</div>
	<div class="form-group required">
		<label for="prenom">Prénom</label>
		<g:textField name="prenom" value="${user.prenom}" required="true"  class="form-control"  />
	</div>
	<div class="form-group required">
		<label for="nom">Nom</label>
		<g:textField name="nom" value="${user.nom}" class="form-control" required="true" />
	</div>
	
	
	<h4 class="mt-4">Applications</h4>
			
	<div class="field-group required">
		<label>Application ID</label>
		<h5>${ user.applicationKey }</h5>
		<g:applyLayout name="form-description">Utilisez cet ID pour connecter des agents à l'application <g:meta name="app.code"/>.
		Il vous sera demandé dans les identifiants de connexion.</g:applyLayout>
	</div>
	
	
	<g:if test="${user.id }">
		<h4 class="mt-4">Authentification</h4>
		
		<div class="form-group">
			<label for="lastActivation">Dernière activation</label>
			<g:field class="form-control small" name="lastActivation" value="${app.formatPicker(date: user.lastActivation)}" type="date" required="true"/>
		</div>
		
		<label class="custom-control custom-checkbox">
	        <g:checkBox class="custom-control-input" name="enabled" value="${user.enabled }"/>
	        <span class="custom-control-label" for="enabled">Compte activé</span>
	    </label>      
	                          
        <label class="custom-control custom-checkbox">
        	<g:checkBox class="custom-control-input" name="accountExpired" value="${user.accountExpired }"/>
            <span class="custom-control-label" for="accountExpired">Compte expiré</span>
        </label>      
                            
        <label class="custom-control custom-checkbox">
        	<g:checkBox class="custom-control-input" name="accountLocked" value="${user.accountLocked }"/>
            <span class="custom-control-label" for="accountLocked">Compte bloqué</span>
        </label>      
                               
        <label class="custom-control custom-checkbox">
        	<g:checkBox class="custom-control-input" name="passwordExpired" value="${user.passwordExpired }"/>
            <span class="custom-control-label" for="passwordExpired">Mot de passe expiré</span>
            <g:if test="${ registration }">
	            <g:applyLayout name="form-description">
	            Mail d'activation envoyé le ${ g.formatDate(date: registration.dateCreated) }
	            </g:applyLayout>
            </g:if>
        </label>                           
    </g:if>
    
    <h4 class="mt-4">Sécurité</h4>
    
	<div class="form-group">
		<label for="roles">Groupes</label>
		
		<div>
			<app:picklist options="${ roles }" idField="id" labelField="authority" selectId="roles" selectedOptions="${ userRoles }"/>
		</div>
	</div>
    

	<div class="mt-4">
		<g:if test="${user.id }">
			<g:actionSubmit value="Enregistrer" action="saveEdit" class="btn btn-primary" />
		</g:if>
		<g:else>
			<g:actionSubmit value="Créer" action="saveCreate" class="btn btn-primary" />
		</g:else>
		
		<g:link action="users" class="btn btn-link">Annuler</g:link>
	</div>
</g:form>
