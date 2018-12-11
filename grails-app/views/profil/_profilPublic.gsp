<g:set var="houseService" bean="houseService"/>


<h3 class="separator">
	<span class="aui-avatar aui-avatar-project aui-avatar-large">
	    <span class="aui-avatar-inner">
	        <asset:image src="useravatar.png" style="width:48px" />
	    </span>
	</span>

	<g:if test="${ viewOnly }">
		<g:link action="tableauBordFriend" controller="tableauBord" style="color:black;" id="${ user.id }">${ user.prenomNom }</g:link>
	</g:if>
	<g:else>
		<g:link action="profil" controller="profil" style="color:black;">${ user.prenomNom }</g:link>
	</g:else>
</h3>

<g:set var="currentConso" value="${ house?.currentConso() }"/>

<ul class="label">
	<li>Chauffage : <span class="link">${ house?.chauffage?.libelle }</span></li>
	
	<g:if test="${ currentConso && house?.compteur }">
		<g:set var="beforeConso" value="${ currentConso.before() }"/>
		<g:set var="compteurElectrique" value="${ house?.compteurElectriqueImpl() }"/>
		<g:set var="compteurGaz" value="${ house?.compteurGazImpl() }"/>
		<g:set var="consos" value="${currentConso.calculTarif(compteurElectrique, compteurGaz) }"/>
		
		<li>Fournisseur électricité : <g:if test="${ compteurElectrique?.fournisseur }">
				<g:if test="${ viewOnly }">
					<span class="link">${ compteurElectrique.fournisseur.libelle }</span>
				</g:if>
				<g:else>
					<g:link action="edit" controller="device" id="${ house?.compteur?.id }" fragment="tabs-device-configuration" class="link">${ compteurElectrique.fournisseur.libelle }</g:link>
				</g:else>
			</g:if>
			<g:elseif test="${ !viewOnly }">
				<g:link action="edit" controller="device" id="${ house?.compteur?.id }" fragment="tabs-device-configuration" class="link">sélectionner un gestionnaire d'énergie</g:link>
			</g:elseif>
		</li>
		
		<g:if test="${ compteurGaz }">
			<li>Fournisseur gaz : <g:if test="${ compteurGaz?.fournisseur }">
					<g:if test="${ viewOnly }">
						<span class="link">${ compteurGaz.fournisseur.libelle }</span>
					</g:if>
					<g:else>
						<g:link action="edit" controller="device" id="${ house?.compteurGaz?.id }" fragment="tabs-device-configuration" class="link">${ compteurGaz.fournisseur.libelle }</g:link>
					</g:else>
				</g:if>
				<g:elseif test="${ !viewOnly }">
					<g:link action="edit" controller="device" id="${ house?.compteurGaz?.id }" fragment="tabs-device-configuration" class="link">sélectionner un gestionnaire d'énergie</g:link>
				</g:elseif>
			</li>
		</g:if>
		
		<li>
			<span>Prévisions ${ currentConso.year() } :</span>
			
			<table class="aui datatable" style="margin-bottom:20px;">
				<thead>
					<tr>
						<th>${ consos.optTarifElec }</th>
						<th>kWh</th>
						<th><span class="aui-icon aui-icon-small aui-iconfont-switch-small"></span></th>
						<th>€</th>
					</tr>
				</thead>
				<tbody>
					<g:if test="${ currentConso.kwHC }">
						<tr>
							<td>${ consos.optTarifElec == 'HC' ? 'Heures creuses' : 'Heures normales' }</td>
							<td><span class="link">${ currentConso.kwHC as Integer }</span></td>
							<td>
								<g:if test="${ beforeConso }">
									<g:set var="compare" value="${ currentConso.comparePercentHC(beforeConso) }"/>
									<span class="aui-lozenge aui-lozenge-subtle ${ compare < 0 ? 'aui-lozenge-success' : 'aui-lozenge-error' }"><g:formatNumber number="${ compare }" format="+0.0;-0.0"/>%</span>
								</g:if>
							</td>
							<td><span class="link">${ consos.tarifHC as Integer }</span></td>
						</tr>
					</g:if>
					<g:if test="${ currentConso.kwHP }">
						<tr>
							<td>${ consos.optTarifElec == 'HC' ? 'Heures pleines' : 'Heures pointe mobile' }</td>
							<td><span class="link">${ currentConso.kwHP as Integer }</span></td>
							<td>
								<g:if test="${ beforeConso }">
									<g:set var="compare" value="${ currentConso.comparePercentHP(beforeConso) }"/>
									<span class="aui-lozenge aui-lozenge-subtle ${ compare < 0 ? 'aui-lozenge-success' : 'aui-lozenge-error' }"><g:formatNumber number="${ compare }" format="+0.0;-0.0"/>%</span>
								</g:if>
							</td>
							<td><span class="link">${ consos.tarifHP as Integer }</span></td>
						</tr>
					</g:if>
					<g:if test="${ currentConso.kwBASE && currentConso.countFieldConso() > 1 }">
						<tr>
							<td>Heures bases</td>
							<td><span class="link">${ currentConso.kwBASE as Integer }</span></td>
							<td>
								<g:if test="${ beforeConso }">
									<g:set var="compare" value="${ currentConso.comparePercentBASE(beforeConso) }"/>
									<span class="aui-lozenge aui-lozenge-subtle ${ compare < 0 ? 'aui-lozenge-success' : 'aui-lozenge-error' }"><g:formatNumber number="${ compare }" format="+0.0;-0.0"/>%</span>
								</g:if>
							</td>
							<td><span class="link">${ consos.tarifBASE as Integer }</span></td>
						</tr>
					</g:if>
					<g:if test="${ currentConso.kwGaz }">
						<tr>
							<td>Gaz</td>
							<td><span class="link">${ currentConso.kwGaz as Integer }</span></td>
							<td>
								<g:if test="${ beforeConso }">
									<g:set var="compare" value="${ currentConso.comparePercentGaz(beforeConso) }"/>
									<span class="aui-lozenge aui-lozenge-subtle ${ compare < 0 ? 'aui-lozenge-success' : 'aui-lozenge-error' }"><g:formatNumber number="${ compare }" format="+0.0;-0.0"/>%</span>
								</g:if>
							</td>
							<td><span class="link">${ consos.tarifGaz as Integer }</span></td>
						</tr>
					</g:if>
					<tr>
						<td><strong>TOTAL</strong></td>
						<td><span class="link"><strong>${ currentConso.consoTotale() }</strong></span></td>
						<td>
							<g:if test="${ beforeConso }">
								<g:set var="compare" value="${ currentConso.comparePercentTotal(beforeConso) }"/>
								<span class="aui-lozenge aui-lozenge-subtle ${ compare < 0 ? 'aui-lozenge-success' : 'aui-lozenge-error' }"><g:formatNumber number="${ compare }" format="+0.0;-0.0"/>%</span>
							</g:if>
						</td>
						<td><span class="link"><strong>${ consos.tarifTotal as Integer }</strong></span></td>
					</tr>
				</tbody>
			</table>
		</li>
	</g:if>
	<g:else>
		<li>
			Prévisions ${ new Date()[Calendar.YEAR] } : <span class="link">-kWh</span>
		</li>		
	</g:else>
	
	<g:set var="dpe" value="${ houseService.classementDPE(house, currentConso) }"/>
	
	<li>Classement énergétique :</li>
	<li><div style="position:relative">
		<asset:image src="dpe.jpg" width="200px"/>
		<g:if test="${ dpe?.note }">
			<div class="vignettedpe" style="top:${ 14 + (dpe.index * 22) }px">${ dpe.kwParAn }</div>
		</g:if>
	</div></li>
</ul>

<g:if test="${ !house?.compteur && !viewOnly }">
	<h6 class="h6">
	Pour calculer votre consommation annuelle et le classement énergétique de votre maison, <g:link action="profil" controller="profil">veuillez compléter votre profil</g:link>
	</h6>
</g:if>
