<g:set var="houseService" bean="houseService"/>


<h3 class="separator"><asset:image src="useravatar.png" width="48px" />
	<g:if test="${ viewOnly }">
		<g:link action="tableauBordFriend" controller="tableauBord" style="color:black;" id="${ user.id }">${ user.prenomNom }</g:link>
	</g:if>
	<g:else>
		<g:link action="profil" controller="profil" style="color:black;">${ user.prenomNom }</g:link>
	</g:else>
</h3>

<g:set var="currentConso" value="${ house?.currentConso() }"/>

<ul class="label">
	<li>Surface : <span class="link">${ house?.surface?.toInteger() ?: '' }m²</span></li>

	<li>Chauffage : <span class="link">${ house?.chauffage?.libelle }</span></li>
	
	<g:if test="${ currentConso && house?.compteur }">
		<g:set var="beforeConso" value="${ currentConso.before() }"/>
		<g:set var="compteurElectrique" value="${ house?.compteurElectriqueImpl() }"/>
		<g:set var="tarifHP" value="${ compteurElectrique.calculTarif('HP', currentConso.kwHP, currentConso.year()) }"/>
		<g:set var="tarifHC" value="${ compteurElectrique.calculTarif('HC', currentConso.kwHC, currentConso.year()) }"/>
		<g:set var="tarifTotal" value="${ tarifHP != null || tarifHC != null ? (tarifHP ?: 0) + (tarifHC ?: 0) : null }"/>
		
		<li>
			<span>Estimations consommations ${ currentConso.year() } :</span>
			
			<g:if test="${ compteurElectrique?.fournisseur }">
				<g:if test="${ viewOnly }">
					<br/><span style="font-size:xx-small">Fournisseur : <span class="link">${ compteurElectrique.fournisseur.libelle }</span></span>
				</g:if>
				<g:else>
					<br/><span style="font-size:xx-small">Fournisseur : <g:link action="edit" controller="device" id="${ house?.compteur?.id }" fragment="tabs-device-configuration">${ compteurElectrique.fournisseur.libelle }</g:link></span>
				</g:else>
			</g:if>
			<g:elseif test="${ !viewOnly }">
				<br/><span style="font-size:xx-small">Sélectionner un <g:link action="edit" controller="device" id="${ house?.compteur?.id }" fragment="tabs-device-configuration">gestionnaire d'énergie</g:link> pour calculer le prix de vos consommations</span>
			</g:elseif>
			
			<table class="aui datatable" style="margin-bottom:20px;">
				<thead>
					<tr>
						<th></th>
						<th>kWh</th>
						<th><span class="aui-icon aui-icon-small aui-iconfont-switch-small"></span></td>
						<th>€</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>HP</td>
						<td><span class="link">${ currentConso.kwHP as Integer }</span></td>
						<td>
							<g:if test="${ beforeConso }">
								<g:set var="compare" value="${ currentConso.comparePercentHP(beforeConso) }"/>
								<span class="aui-lozenge aui-lozenge-subtle ${ compare < 0 ? 'aui-lozenge-success' : 'aui-lozenge-error' }"><g:formatNumber number="${ compare }" format="+0;-0"/>%</span>
							</g:if>
						</td>
						<td><span class="link">${ tarifHP as Integer }</span></td>
					</tr>
					<tr>
						<td>HC</td>
						<td><span class="link">${ currentConso.kwHC as Integer }</span></td>
						<td>
							<g:if test="${ beforeConso }">
								<g:set var="compare" value="${ currentConso.comparePercentHC(beforeConso) }"/>
								<span class="aui-lozenge aui-lozenge-subtle ${ compare < 0 ? 'aui-lozenge-success' : 'aui-lozenge-error' }"><g:formatNumber number="${ compare }" format="+0;-0"/>%</span>
							</g:if>
						</td>
						<td><span class="link">${ tarifHC as Integer }</span></td>
					</tr>
					<tr>
						<td><strong>TOTAL</strong></td>
						<td><span class="link"><strong>${ currentConso.consoTotale() }</strong></span></td>
						<td>
							<g:if test="${ beforeConso }">
								<g:set var="compare" value="${ currentConso.comparePercentTotal(beforeConso) }"/>
								<span class="aui-lozenge aui-lozenge-subtle ${ compare < 0 ? 'aui-lozenge-success' : 'aui-lozenge-error' }"><g:formatNumber number="${ compare }" format="+0;-0"/>%</span>
							</g:if>
						</td>
						<td><span class="link"><strong>${ tarifTotal as Integer }</strong></span></td>
					</tr>
				</tbody>
			</table>
		</li>
	</g:if>
	<g:else>
		<li>
			Estimations consommations : <span class="link">-kWh</span>
			<ul style="margin:0">
				<li>Heure pleine : <span class="link">-kWh</span> </li>
				<li>Heure creuse : <span class="link">-kWh</span> </li>
			</ul>
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
