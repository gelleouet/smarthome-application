<g:if test="${ compteur?.metadata('modele')?.value == 'Mécanique' }">
	<asset:image src="compteur-elec-mecanique.png" height="200px"/>
</g:if>
<g:elseif test="${ compteur?.metadata('modele')?.value == 'Linky' }">
	<asset:image src="linky.png" height="200px"/>
</g:elseif>
<g:else>
	<asset:image src="compteur-elec-electronique.png" height="200px"/>
</g:else>