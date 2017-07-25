
<g:form controller="user" method="post" class="aui">
	<g:hiddenField name="id" value="${user.id}" />

	<h4>Profil</h4>

	<fieldset>
		<div class="field-group">
			<label for="username">Email<span
				class="aui-icon icon-required"> required</span></label>
			<g:textField name="username" value="${user.username}" type="email"
				class="text long-field" required="true" />
			<div class="description">En cas de changement d'email, un
				email vous sera envoyé à la nouvelle adresse pour valider votre
				accès.</div>
		</div>
		<div class="field-group">
			<label for="prenom">Prénom<span
				class="aui-icon icon-required"> required</span></label>
			<g:textField name="prenom" value="${user.prenom}" required="true" 
				class="text long-field"  />
		</div>
		<div class="field-group">
			<label for="nom">Nom<span class="aui-icon icon-required">
					required</span></label>
			<g:textField name="nom" value="${user.nom}" class="text long-field"
				required="true" />
		</div>
	</fieldset>
	
	
	<h4>Applications</h4>
			
	<fieldset>
		<div class="field-group">
			<label>Application ID<span
				class="aui-icon icon-required"> required</span></label>
			<h5>${ user.applicationKey }</h5>
			<div class="description">Utilisez cet ID pour connecter des agents à l'application <g:meta name="app.code"/>. Il vous sera demandé dans les identifiants de connexion.</div>
		</div>
	</fieldset>
	
	
	<g:if test="${user.id }">
		<h4>Authentification</h4>
		
		<fieldset>
			<div class="field-group">
				<label for="lastActivation">Dernière activation</label>
				<g:field class="text medium-field" name="lastActivation" value="${app.formatPicker(date: user.lastActivation)}" type="date" required="true"/>
			</div>
		</fieldset>
		
		<fieldset class="group">
	        <legend><span>Status</span></legend>
	        
	        <div class="checkbox">
	        	<g:checkBox class="checkbox" name="enabled" value="${user.enabled }"/>
	            <label for="enabled">Compte activé</label>
	        </div>                                
	        <div class="checkbox">
	        	<g:checkBox class="checkbox" name="accountExpired" value="${user.accountExpired }"/>
	            <label for="accountExpired">Compte expiré</label>
	        </div>                                
	        <div class="checkbox">
	        	<g:checkBox class="checkbox" name="accountLocked" value="${user.accountLocked }"/>
	            <label for="accountLocked">Compte bloqué</label>
	        </div>                                
	        <div class="checkbox">
	        	<g:checkBox class="checkbox" name="passwordExpired" value="${user.passwordExpired }"/>
	            <label for="passwordExpired">Mot de passe expiré</label>
	            <div class="description">
	            	<g:if test="${ registration }">
	            		<span class="aui-lozenge aui-lozenge-current">Mail d'activation envoyé le ${ g.formatDate(date: registration.dateCreated) }</span>
	            	</g:if>
	            </div>
	        </div>                                
	    </fieldset>
    </g:if>
    
    <h4>Sécurité</h4>
    
    <fieldset>
		<div class="field-group">
			<label for="roles">Groupes</label>
			<app:picklist options="${ roles }" idField="id" labelField="authority" selectId="roles" selectedOptions="${ userRoles }"/>
		</div>
	</fieldset>
    
    
	<br/>

	<div class="buttons-container">
				<div class="buttons">
					<g:if test="${user.id }">
						<g:actionSubmit value="Mettre à jour" action="saveEdit" class="aui-button aui-button-primary" />
					</g:if>
					<g:else>
						<g:actionSubmit value="Créer" action="saveCreate" class="aui-button aui-button-primary" />
					</g:else>
					
					<g:link action="users" class="cancel">Annuler</g:link>
				</div>
			</div>
</g:form>
