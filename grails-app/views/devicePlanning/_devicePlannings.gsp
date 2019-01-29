<g:applyLayout name="messageInfo">
	Les plannings ci-dessous seront exécutés directement sur l'agent. Après enregistrement,
	pensez à synchroniser les modifications vers l'agent avec le bouton "Synchroniser". Vous pouvez
	créer plusieurs plannings seulement si une condition d'éxécution permet d'en sélectionner un seul.
</g:applyLayout>

<div class="aui-toolbar2-inner" style="margin:15px 0px;">
	<div class="aui-toolbar2-secondary">
		<div class="aui-buttons">
			<a class="aui-button" id="planning-device-add-button" title="Ajouter" data-url="${ g.createLink(controller: 'devicePlanning', action: 'addPlanning') }">
				<span class="aui-icon aui-icon-small aui-iconfont-add"></span> Ajouter
			</a>
			
			<g:link action="syncPlannings" id="${ device.id }" class="aui-button" title="Synchroniser agent">
				<span class="aui-icon aui-icon-small aui-iconfont-share-list"></span> Synchroniser
			</g:link>
		</div>
	</div>
</div>

<g:each var="devicePlanning" in="${ devicePlannings.sort{ it.planning.label } }" status="status">
	<g:set var="prefix" value="devicePlannings[${status}]."/>
	
	<g:if test="${ devicePlanning.id }">
		<g:hiddenField name="${ prefix}id" value="${ devicePlanning.id }"/>
	</g:if>
	<g:hiddenField name="${ prefix}device.id" value="${ device.id }"/>
	<g:hiddenField name="${ prefix}status" value="${ status }"/>
	
	<div class="device-planning">
		<g:render template="/planning/planning" model="[device: device, planning: devicePlanning.planning,
			prefix: prefix + 'planning.', planningStatus: status, controllerPlanning: 'devicePlanning']"/>
	</div>
</g:each>