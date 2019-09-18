<g:if test="${ compteur?.metadata('modele')?.value == 'Mécanique' }">
	<asset:image src="compteur-elec-mecanique.png"/>
</g:if>
<g:elseif test="${ compteur?.metadata('modele')?.value == 'Linky' }">
	<asset:image src="linky.png"/>
</g:elseif>
<g:else>
	<asset:image src="compteur-elec-electronique.png"/>
</g:else>