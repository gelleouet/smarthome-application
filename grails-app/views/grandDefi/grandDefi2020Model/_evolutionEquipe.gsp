<%@ page import="smarthome.core.chart.GoogleChart" %>

<div class="row mt-4">
	
	<div class="col">
		
		<g:set var="consos" value="${ data.consos.values?.sort{ it.profil.libelle } }"/>

		<div class="row mt-3 ml-4 mr-4">
			<div class="col border-bottom border-right p-2">
				
			</div>
			<div class="col border-bottom border-right p-2 text-center">
				<h5 class="font-weight-bold">Economie</h5>
			</div>
			<div class="col border-bottom p-2 text-center">
				<h5 class="font-weight-bold">Classement dans le ${ defi?.libelle }</h5>
			</div>
		</div>
		<g:each var="conso" in="${ consos }" status="status">
			<div class="row ml-4 mr-4">
				<div class="col border-bottom border-right p-2">
					<h5 class="font-weight-bold">
						${ conso.profil.libelle }
						<g:if test="${ conso.profil.icon }"><asset:image src="${conso.profil.icon }" class="gd-icon-profil"/></g:if>
					</h5>
				</div>
				<div class="col border-bottom border-right p-2 text-center">
					<g:set var="value" value="${ conso."economie_${data.consos.type}"()  }"/>
					<h4 class="font-weight-bold">
						<g:applyLayout name="arrow" model="[value: value != null ? value : '-', reference: 0]">
							<span style="font-size:small;">%</span>
						</g:applyLayout>
					</h4>
				</div>
				<div class="col border-bottom p-2 text-center bg-menu">
					<g:set var="classement" value="${ conso."classement_${data.consos.type}"()  }"/>
					<g:set var="total" value="${ conso."total_${data.consos.type}"()  }"/>
					<g:if test="${ classement }">
						<h4 class="font-weight-bold text-menu">${ classement } / ${ total }</h4>
					</g:if>
				</div>
			</div>
		</g:each>
		<div class="row ml-4 mr-4">
			<div class="col border-right p-2">
				<h5 class="font-weight-bold">Equipe</h5>
			</div>
			<div class="col border-right p-2 text-center">
				<g:set var="value" value="${ data.consos.economie  }"/>
				<h4 class="font-weight-bold">
					<g:applyLayout name="arrow" model="[value: value != null ? value : '-', reference: 0]">
						<span style="font-size:small;">%</span>
					</g:applyLayout>
				</h4>
			</div>
			<div class="col p-2 text-center bg-menu">
				<g:if test="${ data.consos.classement }">
					<h4 class="font-weight-bold text-menu">${ data.consos.classement } / ${ data.consos.total }</h4>
				</g:if>
			</div>
		</div>
	</div> <!-- div.col -->
</div> <!-- div.row -->
