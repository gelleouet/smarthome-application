<g:if test="${ agent.locked }">
	<span class="badge badge-dark">verrouillé</span>
</g:if>
<g:else>
	<span class="badge badge-primary">activé</span>
</g:else>
<g:if test="${ agent.online }">
	<span class="badge badge-primary">online</span>
</g:if>
<g:else>
	<span class="badge badge-light">offline</span>
</g:else>