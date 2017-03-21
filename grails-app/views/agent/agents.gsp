<html>
<head>
<meta name='layout' content='authenticated' />
<asset:stylesheet src="xterm.css"/>
<asset:javascript src="xterm.js"/>
<asset:javascript src="xterm-attach.js"/>
</head>

<body onload="onLoadAgents()">
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
		
		<div id="xterm-color"></div>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Agents</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ agentInstanceList }">
			        <tr>
			            <td>
			            	<h5><g:link action="edit" id="${ bean.id }">${ bean.agentModel } / ${ bean.mac } </g:link> [${ bean.libelle }]
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
			            	</h5>
			            	<p style="font-size:small"><label><strong>IP privée :</strong> ${ bean.privateIp }
			            	<strong>IP publique :</strong> ${ bean.publicIp }
			            	<strong>Dernière connexion :</strong> ${ app.formatTimeAgo(date: bean.lastConnexion) }</label></p>
			            	
			            	<div class="buttons-container" style="padding-top:10px">
								<div class="buttons">
									<g:if test="${ bean.locked }">
					            		<g:link class="aui-button " title="Activer" action="activer" id="${ bean.id }" params="[actif: true]">
						            		<span class="aui-icon aui-icon-small aui-iconfont-locked"></span> Activer
						            	</g:link>
					            	</g:if>
					            	<g:else>
					            		<g:link class="aui-button " title="Vérrouiller" action="activer" id="${ bean.id }" params="[actif: false]">
						            		<span class="aui-icon aui-icon-small aui-iconfont-locked"></span> Verrouiller
						            	</g:link>
					            	</g:else>
					            	
					            	<g:link class="aui-button" title="Inclusion" action="startInclusion" id="${ bean.id }">
					            		<span class="aui-icon aui-icon-small aui-iconfont-blogroll"></span> Inclusion
					            	</g:link>

					            	<g:link class="aui-button" title="Exclusion" action="startExclusion" id="${ bean.id }">
					            		<span class="aui-icon aui-icon-small aui-iconfont-blogroll"></span> Exclusion
					            	</g:link>
					            	
					            	<g:link class="aui-button confirm-button" title="Reset" action="resetConfig" id="${ bean.id }">
					            		<span class="aui-icon aui-icon-small aui-iconfont-bp-troubleshooting"></span> Reset
					            	</g:link>
								</div>
							</div>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>