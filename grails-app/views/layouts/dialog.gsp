<div role="dialog" id="${ g.pageProperty(name: 'page.dialogId') }" class="modal" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered" role="document">
		<div class="modal-content">
		    <div class="modal-header">
		    	<h5 class="modal-title">${ g.pageProperty(name: 'page.dialogTitle') }</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		    </div>
		    
		    <div class="modal-body">
		    	<div id="ajaxDialogError"></div>
		    	<g:pageProperty name="page.dialogBody"/>
		    </div>
		    
		    <div class="modal-footer">
				<g:pageProperty name="page.dialogFooter"/>
		    </div>
	    </div> <!-- div.modal-content -->
	</div> <!-- div.modal-dialog -->
</div> <!-- div.modal -->