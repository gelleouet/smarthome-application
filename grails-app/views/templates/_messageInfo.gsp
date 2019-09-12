<div class="alert alert-primary alert-outline-coloured" role="alert">
	<div class="alert-icon">
		<g:if test="${ icon }">
			<app:icon name="${ icon }"/>
		</g:if>
		<g:else>
			<i class="far fa-fw fa-bell"></i>
		</g:else>
	</div>
	<div class="alert-message">
		<strong>${ message }</strong>
	</div>
</div>
