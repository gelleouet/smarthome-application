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
		                <h3>${ user.id ? user.nom + ' ' + user.prenom: 'Nouvel utilisateur' }</h3>
		            </div>		            
		        </div>
		        <div class="aui-toolbar2-secondary">
		        	<g:form controller="user" >
		            <div class="aui-buttons">
		            	<g:hiddenField name="username" value="${user.username }"/>
		            	<g:if test="${ user.id }">
							<g:actionSubmit class="aui-button" value="Réinitialiser le mot de passe" action="resetPassword" 
								onclick="return confirm('Un mail va être envoyé à ${user.username } pour réinitialiser son mot de passe.Voulez-vous continuer ?')"/>
						</g:if>
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		
		<div class="aui-tabs horizontal-tabs">
		
			<ul class="tabs-menu">
	            <li class="menu-item active-tab">
	                <a href="#tabs-user-general"><strong>Général</strong></a>
	            </li>
	            <g:if test="${ user.id }">
		            <li class="menu-item">
		                <a href="#tabs-user-acl"><strong>Liste de contrôles d'accès (ACL)</strong></a>
		            </li>
	            </g:if>
	        </ul>
			
			
			<div class="tabs-pane active-pane" id="tabs-user-general">
				<g:include view="/user/userForm.gsp"/>
			</div>

			<g:if test="${ user.id }">
				<div class="tabs-pane" id="tabs-user-acl">
					<g:include view="/user/userAclForm.gsp"/>
				</div>
			</g:if>
		
		</div>
	
		
	</g:applyLayout>
</body>
</html>