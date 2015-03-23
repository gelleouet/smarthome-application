<div class="aui-tabs horizontal-tabs plugin-pane">

    <ul class="tabs-menu">
    	
    	<!-- un contenu par défaut doit être affiché -->
    	<g:if test="${ bodyContent }">
    		<li class="menu-item active-tab">
	            <a href="#tabs-general"><strong>Général</strong></a>
	        </li>
    	</g:if>
    	
    	<g:each in="${ plugins }" var="plugin" status="status">
    		<g:set var="async" value="${ status > 0 || bodyContent }"/>
    		<g:set var="updateId" value="${ 'tabs-' + status + '-content' }"/>
    		
    		<li class="menu-item ${ status == 0 && !bodyContent ? 'active-tab' :  '' }">
	    		<g:if test="${ async }">
					<a href="#tabs-${ status }" onclick="${ g.remoteFunction(controller: plugin.controller, action: plugin.action, params: [command: params.commandJson, srcController: params.srcController, srcAction: params.srcAction, updateId: updateId], update: updateId) }">
						<strong>${ plugin.label }</strong>
					</a>
				</g:if>
				
				<g:else>	    	
			     	<a href="#tabs-${ status }"><strong>${ plugin.label }</strong></a>
	        	</g:else>
    		</li>
	        
        </g:each>
    </ul>
    
    <!-- un contenu par défaut doit être affiché -->
    <g:if test="${ bodyContent }">
    	<div class="tabs-pane active-pane" id="tabs-general">
    		<div class="content-pane">
	    		${ bodyContent.encodeAsRaw() }
	    	</div>
	    </div>
    </g:if>
    
    <g:each in="${ plugins }" var="plugin" status="status">
    
    	<g:set var="async" value="${ status > 0 || bodyContent }"/>
    	<g:set var="updateId" value="${ 'tabs-' + status + '-content' }"/>
    
	    <div class="tabs-pane ${ status == 0 && !bodyContent ? 'active-pane' :  '' }" id="tabs-${ status }">
	    	<div class="content-pane" id="tabs-${status}-content">
	    	
				<!-- Les plugins non visibles au démarrage sont chargés en mode asynchrone à la demande -->
				<g:if test="${ !async }">
					<g:include controller="${ plugin.controller }" action="${ plugin.action }" 
		        		params="[command: params.command, srcController: params.srcController, srcAction: params.srcAction, updateId: updateId]"/>
				</g:if>
				
	        </div>
	    </div>
    </g:each>
</div><!-- .aui-tabs -->
