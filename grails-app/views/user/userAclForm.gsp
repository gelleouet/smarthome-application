<!-- Choix de la classe d'objet Ã  configurer -->
<br/>

<g:applyLayout name="messageInfo">
	<small class="text-muted">Les permissions objets sont prioritaires sur les permissions classes.
	Si rien n'est renseigné sur un objet, alors la permission classe sera utilisée.
	L'accès sera refusé par défaut si aucune permission n'est trouvée
	</small>
</g:applyLayout>
	
<br/>
	
<g:form>
	<g:hiddenField name="sid" value="${user.username}" />
    
    <div class="form-group required">
    	<label for="className">Sélectionner une classe d'objet</label>
    	<g:select name="className" from="${ aclClassList }" optionValue="fullName" class="form-control" optionKey="fullName" required="true"/>
    </div>
    
    <g:submitToRemote class="btn btn-light" url="[controller: 'aclEntry', action: 'aclObjectListTemplate']" value="Afficher"
    	update="[success: 'aclAjaxTable', failure: 'ajaxError']"/>
</g:form>


<div id="aclAjaxTable"/>