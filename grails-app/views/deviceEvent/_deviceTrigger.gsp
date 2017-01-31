<g:submitToRemote class="aui-button" value="Ajouter un déclencheur" url="[action: 'addTrigger']" update="eventTriggers"></g:submitToRemote>

<div class="description">
	<p>Pour chaque déclencheur, il est possible d'actionner un autre objet et/ou un scénario en même temps.</p>
	<p style="text-transform:none">Le script Groovy conditionnel permet de modifier l'état de l'objet actionné avant de déclencher l'action.
	En renvoyant true ou false, il permet en plus de la condition principale de conditionner l'action. Variables pré-définies :
		<ul>
			<li>device : l'objet device sur lequel est branché l'événement</li>
			<li>triggerDevice : l'objet device qu'on veut déclencher</li>
			<li>devices : tous les devices indexés par leur mac</li>
			<li>Ex : triggerDevice.value = device.value</li>
			<li>Ex : triggerDevice.value = devices['gpio4'].value</li>
		</ul>
	</p>
</div>

<div id="eventTriggers" style="margin-top: 10px">
	<g:render template="triggers"/>
</div>