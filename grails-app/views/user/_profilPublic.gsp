<g:set var="houseService" bean="houseService"/>


<h3><asset:image src="useravatar.png" width="48px" />
	<g:if test="${ viewOnly }">
		${ user.prenomNom }
	</g:if>
	<g:else>
		<g:link action="profil" controller="user" style="color:black;">${ user.prenomNom }</g:link>
	</g:else>
</h3>

<div class="aui-group">
	<div class="aui-item">
		<p class="label">Objets personnels</p>
	</div>
	<div class="aui-item">
		<p class="label">Objets partagés</p>
	</div>
</div>
<div class="aui-group">
	<g:if test="${ viewOnly }">
		<div class="aui-item">
			<span style="font-size:16pt" class="link">${ userDeviceCount }</span>
		</div>
		<div class="aui-item">
			<span style="font-size:16pt" class="link">${ sharedDeviceCount }</span>
		</div>
	</g:if>
	<g:else>
		<div class="aui-item">
			<g:link action="devicesGrid" controller="device" style="font-size:16pt">${ userDeviceCount }</g:link>
		</div>
		<div class="aui-item">
			<g:link action="devicesGrid" controller="device" params="[sharedDevice: true]"style="font-size:16pt">${ sharedDeviceCount }</g:link>
		</div>
	</g:else>
</div>

<h5>${ house?.name ?: 'Maison principale' }</h5>

<g:set var="currentConso" value="${ house?.currentConso() }"/>

<ul class="label">
	<li>Surface : <span class="link">${ house?.surface?.toInteger() ?: '' }m²</span></li>
	
	<g:if test="${ currentConso }">
		<g:set var="beforeConso" value="${ currentConso.before() }"/>
		
		<li>
			Consommations annuelles ${ currentConso.year() } : <span class="link">${ currentConso.consoTotale() }kWh</span>
			<g:if test="${ beforeConso }">
				<g:set var="compare" value="${ currentConso.comparePercentTotal(beforeConso) }"/>
				&nbsp;<span class="aui-lozenge aui-lozenge-subtle ${ compare < 0 ? 'aui-lozenge-success' : 'aui-lozenge-error' }"><g:formatNumber number="${ compare }" format="+0;-0"/>%</span>
			</g:if>
			
			<ul style="margin:0">
				<li>Heure pleine : <span class="link">${ currentConso.kwHP as Integer }kWh</span> 
				<g:if test="${ beforeConso }">
					<g:set var="compare" value="${ currentConso.comparePercentHP(beforeConso) }"/>
					&nbsp;<span class="aui-lozenge aui-lozenge-subtle ${ compare < 0 ? 'aui-lozenge-success' : 'aui-lozenge-error' }"><g:formatNumber number="${ compare }" format="+0;-0"/>%</span>
				</g:if>
				</li>
				<li>Heure creuse : <span class="link">${ currentConso.kwHC as Integer }kWh</span> 
				<g:if test="${ beforeConso }">
					<g:set var="compare" value="${ currentConso.comparePercentHC(beforeConso) }"/>
					&nbsp;<span class="aui-lozenge aui-lozenge-subtle ${ compare < 0 ? 'aui-lozenge-success' : 'aui-lozenge-error' }"><g:formatNumber number="${ compare }" format="+0;-0"/>%</span>
				</g:if>
				</li>
			</ul>
		</li>
	</g:if>
	<g:else>
		<li>
			Consommations annuelles : <span class="link">kWh</span>
			<ul style="margin:0">
				<li>Heure pleine : <span class="link">kWh</span> </li>
				<li>Heure creuse : <span class="link">kWh</span> </li>
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

<span style="font-size:xx-small">Estimation sur une année complète en fonction de votre consommation réelle depuis le 1er janvier</span>

<g:if test="${ !house?.compteur && !viewOnly }">
	<h6 class="h6">
	Pour calculer votre consommation annuelle et le classement énergétique de votre maison, <g:link action="profil" controller="user">veuillez compléter votre profil</g:link>
	</h6>
</g:if>
