<g:if test="${ compteur?.metadata('modele')?.value == 'Gazpar' }">
	<asset:image src="gazpar.png" height="200px"/>
</g:if>
<g:else>
	<asset:image src="compteur-gaz.png" height="200px"/>
</g:else>