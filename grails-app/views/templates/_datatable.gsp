<div class="row">
	<div class="col-4">
		<div class="dataTables_info">
			${ paginateDebut } à ${ paginateFin } sur ${ recordsTotal }
		</div>
	</div>
	<div class="col">
		<div class="text-right">
			<ul class="pagination" data-form-id="${ paginateForm }">
				<g:paginate total="${ recordsTotal }"/>
			</ul>
		</div>
	</div>
</div>

