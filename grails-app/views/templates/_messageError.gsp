<div class="alert alert-danger alert-outline-coloured" role="alert">
	<div class="alert-icon">
		<i class="far fa-fw fa-bell"></i>
	</div>
	<div class="alert-message">
		<g:if test="${title }">
			<p class="title">
				<strong>${ title }</strong>
			</p>
		</g:if>
		
		<g:if test="${message}">
			<p>${message}</p>
		</g:if>
		
		<g:if test="${errors}">
			<g:renderErrors bean="${errors}" />
		</g:if>
	</div>
</div>
<br/>
