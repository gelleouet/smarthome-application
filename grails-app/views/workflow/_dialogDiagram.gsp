<g:applyLayout name="dialog">

	<content tag="dialogId">diagram-workflow-dialog</content>
	<content tag="dialogTitle">Workflow BPMN Diagram</content>

	<content tag="dialogBody">
		<div style="overflow:auto;">
    		<img src="${ g.createLink(action: 'diagramImage', id: workflow.id) }" />
    	</div>
	</content>
	
	
	<content tag="dialogFooter">
		<a class="btn btn-primary" onclick="hideDialog('diagram-workflow-dialog')">Fermer</a>
	</content>

</g:applyLayout>
