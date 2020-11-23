<%@ page import="smarthome.core.chart.GoogleChart" %>

<g:if test="${ data.error }">
	<div class="mt-4">
		<g:applyLayout name="messageWarning">
			${ data.error } 
			<g:link controller="compteur" action="compteur" class="btn btn-link"><app:icon name="settings"/> Configurer votre compteur</g:link>
		</g:applyLayout>
	</div>
</g:if>
<g:else>
	<div class="row mt-4">
		<div class="col">
			<div class="row ml-4 mr-4">
				<div class="col border-bottom border-right text-center p-2">
					<h5 class="font-weight-bold">Evolution de mes consommations</h5>
				</div>
				<g:if test="${ data.consos.type?.toString() != 'global' }">
					<div class="col border-bottom border-right text-center p-2">
						<h5 class="font-weight-bold">Moyenne des évolutions des consommations</h5>
					</div>
				</g:if>
				<div class="col border-bottom border-right text-center p-2">
					<g:if test="${ data.consos.type?.toString() == 'global' }">
						<h5 class="font-weight-bold">Mes "économies"</h5>
						<small class="text-muted">(Moyenne des économies électricité, gaz naturel et eau)</small>
					</g:if>
					<g:elseif test="${ (participant?.groupKey?.contains('ELEC') && data.consos.type?.toString() == 'electricite') || 
						(participant?.groupKey?.contains('GN') && data.consos.type?.toString() == 'gaz') }">
						<h5 class="font-weight-bold">Mes "économies"</h5>
						<small class="text-muted">(Différence entre mon évolution et la moyenne des évolutions)</small>
					</g:elseif>
					<g:else>
						<h5 class="font-weight-bold">Mes économies</h5>
					</g:else>
				</div>
				<div class="col border-bottom text-center p-2">
					<h5 class="font-weight-bold">Mon classement dans le ${ defi?.libelle }</h5>
				</div>
			</div>
			<div class="row ml-4 mr-4">
				<div class="col border-right text-center p-2">
					<h4 class="font-weight-bold">
						<g:applyLayout name="arrow" model="[value: data.consos.evolution != null ? data.consos.evolution : '-', reference: 0]"><span style="font-size:small;">%</span></g:applyLayout>
					</h4>
				</div>
				<g:if test="${ data.consos.type?.toString() != 'global' }">
					<div class="col border-right text-center p-2">
						<h4 class="font-weight-bold">
							<g:applyLayout name="arrow" model="[value: data.consos.moyenne != null ? data.consos.moyenne : '-', reference: 0]">
								<span style="font-size:small;">%</span>
							</g:applyLayout>
						</h4>
					</div>
				</g:if>
				<div class="col border-right text-center p-2">
					<h4 class="font-weight-bold">
						<g:applyLayout name="arrow" model="[value: data.consos.economie != null ? data.consos.economie : '-', reference: 0]">
							<span style="font-size:small;">%</span>
						</g:applyLayout>
					</h4>
				</div>
				<div class="col text-center p-2 bg-menu">
					<g:if test="${ data.consos.classement }">
						<h4 class="font-weight-bold text-menu">${ data.consos.classement } / ${ data.consos.total }</h4>
					</g:if>
				</div>
			</div>
		</div> <!-- div.col -->
	</div> <!-- div.row -->
</g:else>