<g:if test="${ house?.compteur }">
	<li class="sidebar-item">
		<g:link class="sidebar-link" action="saisieIndex" controller="compteur" params="['device.id': house.compteur.id]">
			<app:icon name="zap"/>
			Electricité
		</g:link>
	</li>
</g:if>
<g:if test="${ house?.compteurGaz }">
	<li class="sidebar-item">
		<g:link class="sidebar-link" action="saisieIndex" controller="compteur" params="['device.id': house.compteurGaz.id]">
			<app:icon name="fire" lib="awesome"/>
			Gaz
		</g:link>
	</li>
</g:if>
<g:if test="${ house?.compteurEau }">
	<li class="sidebar-item">
		<g:link class="sidebar-link" action="saisieIndex" controller="compteur" params="['device.id': house.compteurEau.id]">
			<app:icon name="droplet"/>
			Eau
		</g:link>
	</li>
</g:if>