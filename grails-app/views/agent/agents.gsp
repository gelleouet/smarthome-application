<%@ page import="smarthome.endpoint.ShellEndPoint" %>
<%@ page import="smarthome.core.EndPointUtils" %>

<html>
<head>
<meta name='layout' content='main' />
<asset:stylesheet src="xterm.css"/>
<asset:javascript src="xterm.js"/>
<asset:javascript src="xterm-attach.js"/>
</head>

<body onload="onLoadAgents()">
	<g:applyLayout name="page-settings" model="[titre: 'Agents', navigation: 'Smarthome']">
	

		<div class="row">
			<div class="col-8">
				<g:form class="form-inline" action="agents">
					<fieldset>
						<input autofocus="true" class="form-control" type="text" placeholder="Nom ..." name="agentSearch" value="${ agentSearch }"/>
						<button class="btn btn-light"><app:icon name="search"/></button>
					</fieldset>
				</g:form>
			</div>
			<div class="col-4 text-right">
			</div>
		</div>
		
		<br/>

		
		<div id="xterm-color" data-endpoint-url="${ EndPointUtils.httpToWs(g.createLink(uri: ShellEndPoint.URL, absolute: true)) }">
		</div>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Agents</th>
		            <th>Status</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ agentInstanceList }">
			        <tr>
			            <td>
			            	<h5>
			            		<g:link action="edit" id="${ bean.id }">${ bean.agentModel } / ${ bean.mac } </g:link>
			            		<g:if test="${ bean.libelle }">
				            		${ bean.libelle }
				            	</g:if>
			            	</h5>
			            	<ul style="font-size:small">
			            		<li><label><strong>IP privée :</strong> ${ bean.privateIp }</label></li>
			            		<li><label><strong>IP publique :</strong> ${ bean.publicIp }</label></li>
			            		<li><label><strong>Dernière connexion :</strong> ${ app.formatTimeAgo(date: bean.lastConnexion) }</label></li>
			            	</ul>
			            	
							<div class="btn-group mt-4">
								<g:if test="${ bean.locked }">
				            		<g:link class="btn btn-light" title="Activer" action="activer" id="${ bean.id }" params="[actif: true]">
					            		Activer
					            	</g:link>
				            	</g:if>
				            	<g:else>
				            		<g:link class="btn btn-light" title="Vérrouiller" action="activer" id="${ bean.id }" params="[actif: false]">
					            		Verrouiller
					            	</g:link>
				            	</g:else>
				            	
				            	<g:link class="btn btn-light" title="Inclusion" action="startInclusion" id="${ bean.id }">
				            		Inclusion
				            	</g:link>

				            	<g:link class="btn btn-light" title="Exclusion" action="startExclusion" id="${ bean.id }">
				            		Exclusion
				            	</g:link>
				            	
				            	<g:link class="btn btn-light confirm-button" title="Reset" action="resetConfig" id="${ bean.id }">
				            		Reset
				            	</g:link>
				            	
				            	<a onclick="connectAgent(${ bean.id})" id="connect-agent-button" class="btn btn-light" title="Connexion">
				            		Connexion
				            	</a>
							</div>
			            </td>
			            <td>
			            	<g:render template="status" model="[agent: bean]"/>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>