<div class="aui-message aui-message-error">
	<g:if test="${title }">
		<p class="title">
			<strong>
				${title}
			</strong>
		</p>
	</g:if>
	
	<g:if test="${message}">
		<p>${message}</p>
	</g:if>
	
	<g:if test="${errors}">
		<g:renderErrors bean="${errors}" />
	</g:if>
</div>

<br/>