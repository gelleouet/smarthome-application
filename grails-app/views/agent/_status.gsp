<g:if test="${ agent.locked }">
	<span class="aui-lozenge">verrouillé</span>
</g:if>
<g:else>
	<span class="aui-lozenge aui-lozenge-success">activé</span>
</g:else>
<g:if test="${ agent.online }">
	<span class="aui-lozenge aui-lozenge-success">online</span>
</g:if>
<g:else>
	<span class="aui-lozenge">offline</span>
</g:else>