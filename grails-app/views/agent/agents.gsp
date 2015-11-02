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
		                <h3>Agents</h3>
		            </div>		            
		        </div>
		        <div class="aui-toolbar2-secondary">
		        	<g:form >
		            <div class="aui-buttons">
						
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		
		<h4>
			<g:form class="aui" action="agents">
				<fieldset>
					<input autofocus="true" class="text long-field" type="text" placeholder="Modèle ..." name="agentSearch" value="${ agentSearch }"/>
					<button class="aui-button aui-button-subtitle"><span class="aui-icon aui-icon-small aui-iconfont-search"></span></button>
				</fieldset>
			</g:form>
		</h4>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Modèle</th>
		            <th>Mac</th>
		            <th>Nom</th>
		            <th>IP privée</th>
		            <th>IP publique</th>
		            <th>Dernière connexion</th>
		            <th>Status</th>
		            <th class="column-3-buttons"></th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ agentInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${ bean.id }">${ bean.agentModel }</g:link></td>
			            <td>${ bean.mac }</td>
			            <td>${ bean.libelle }</td>
			            <td>${ bean.privateIp }</td>
			            <td>${ bean.publicIp }</td>
			            <td>${ app.formatTimeAgo(date: bean.lastConnexion) }</td>
			            <td>
			            	<g:if test="${ bean.locked }">
			            		<span class="aui-lozenge">verrouillé</span>
			            	</g:if>
			            	<g:else>
			            		<span class="aui-lozenge aui-lozenge-success">activé</span>
			            	</g:else>
			            	<g:if test="${ bean.online }">
			            		<span class="aui-lozenge aui-lozenge-success">online</span>
			            	</g:if>
			            	<g:else>
			            		<span class="aui-lozenge">offline</span>
			            	</g:else>
			            </td>
			            <td class="column-3-buttons command-column">
			            	<g:if test="${ bean.locked }">
			            		<g:link class="aui-button aui-button-subtle" title="Activer" action="activer" id="${ bean.id }" params="[actif: true]">
				            		<span class="aui-icon aui-icon-small aui-iconfont-locked">
				            	</g:link>
			            	</g:if>
			            	<g:else>
			            		<g:link class="aui-button aui-button-subtle" title="Vérrouiller" action="activer" id="${ bean.id }" params="[actif: false]">
				            		<span class="aui-icon aui-icon-small aui-iconfont-locked">
				            	</g:link>
			            	</g:else>
			            	
			            	<g:link class="aui-button aui-button-subtle" title="Ajouter un périphérique" action="addDevice" id="${ bean.id }">
			            		<span class="aui-icon aui-icon-small aui-iconfont-add">
			            	</g:link>

			            	<g:link class="aui-button aui-button-subtle" title="Inclusion" action="startInclusion" id="${ bean.id }">
			            		<span class="aui-icon aui-icon-small aui-iconfont-blogroll">
			            	</g:link>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>