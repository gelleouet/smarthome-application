<!-- Choix de la classe d'objet à configurer -->
<br/>

<div class="aui-message aui-message-hint">
	<span style="font-size:small">Les permissions objets sont prioritaires sur les permissions classes. Si rien n'est renseigné sur un objet, alors la permission classe sera utilisée. 
		L'accès sera refusé par défaut si aucune permission n'est trouvée.		
	</span>
</div>
	
<br/>
	
<g:form class="aui ${ mobileAgent ? 'top-label' : '' }">
	<g:hiddenField name="sid" value="${user.username}" />
    <label for="className">Sélectionner une classe d'objet<span
				class="aui-icon icon-required"> required</span></label>
    <g:select name="className" from="${ aclClassList }" optionValue="fullName" class="select" optionKey="fullName" required="true"/>
    <g:submitToRemote class="aui-button" url="[controller: 'aclEntry', action: 'aclObjectListTemplate']" value="Afficher"
    	update="[success: 'aclAjaxTable', failure: 'ajaxError']"/>
	<span class="spinner" id="ajaxSpinner"></span>			
</g:form>


<div id="aclAjaxTable"/>