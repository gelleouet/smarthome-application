<g:if test="${ house?.compteurEau }">
	<li class="sidebar-item">
		<g:link class="sidebar-link" action="saisieIndex" controller="compteur" params="['device.id': house.compteurEau.id]">
			<app:icon name="droplet"/>
			Eau
		</g:link>
	</li>
</g:if>