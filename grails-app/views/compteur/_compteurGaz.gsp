<div class="card">

	<div class="card-header">
		<div class="card-actions float-right">
			<div class="dropdown show">
				<a href="#" data-toggle="dropdown" data-display="static">
					<app:icon name="more-horizontal"/>
				</a>
				<div class="dropdown-menu dropdown-menu-right">
					<g:if test="${ gazpar }">
						<g:link class="dropdown-item" action="edit" controller="notificationAccount" id="${ gazpar.id }">Service Gazpar</g:link>
					</g:if>
					<g:if test="${ house?.compteurGaz }">
						<g:link class="dropdown-item" action="edit" controller="device" id="${ house.compteurGaz.id }">Compteur</g:link>
					</g:if>
					<g:link class="dropdown-item" action="resetCompteurGaz">Réinitialiser</g:link>
				</div>
			</div>
		</div>
		<h4 class="card-title"><app:icon name="burn" lib="awesome"/> Gaz</h4>
	</div> <!-- div.card-header -->
	
	<div class="card-body">
		<div>
			
			
			
			<g:if test="${ house?.compteurGaz }">
				<h6 class="text-center">Dernière valeur : il y a <app:formatTimeAgo date="${ house.compteurGaz.dateValue }"/></h6>
			</g:if>
		</div>
	</div> <!-- div.card-body -->
	
</div> <!-- div.card -->