<g:if test="${ ! user.enabled }">
	<span class="aui-lozenge">Désactivé</span>
</g:if>

<g:else>

	<g:if test="${ user.accountLocked || user.accountExpired || user.passwordExpired }">
		<g:if test="${ user.accountLocked }">
			<span class="aui-lozenge aui-lozenge-error">Bloqué</span>
		</g:if>
	
		<g:if test="${ user.accountExpired }">
			<span class="aui-lozenge aui-lozenge-error">Compte expiré</span>
		</g:if>
	
		<g:if test="${ user.passwordExpired }">
			<span class="aui-lozenge aui-lozenge-current">Mot de passe expiré</span>
		</g:if>
	</g:if>
	
	<g:else>
		<span class="aui-lozenge aui-lozenge-success">OK</span>
	</g:else>
</g:else>