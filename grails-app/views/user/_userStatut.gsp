<g:if test="${ ! user.enabled }">
	<span class="badge badge-secondary">Désactivé</span>
</g:if>

<g:else>

	<g:if test="${ user.accountLocked || user.accountExpired || user.passwordExpired }">
		<g:if test="${ user.accountLocked }">
			<span class="badge badge-danger">Bloqué</span>
		</g:if>
	
		<g:if test="${ user.accountExpired }">
			<span class="badge badge-danger">Compte expiré</span>
		</g:if>
	
		<g:if test="${ user.passwordExpired }">
			<span class="badge badge-warning">Mot de passe expiré</span>
		</g:if>
	</g:if>
	
	<g:else>
		<span class="badge badge-primary">OK</span>
	</g:else>
</g:else>