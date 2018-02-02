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
		        	<div class="aui-buttons">
		        		<g:remoteLink url="[action: 'geocodeDefaultHouse', controller: 'house', id: user.id]" class="aui-button">GPS</g:remoteLink>
		        		<g:remoteLink url="[action: 'calculDefaultWeather', controller: 'houseWeather', id: user.id]" class="aui-button">Météo</g:remoteLink>
		        	</div>
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