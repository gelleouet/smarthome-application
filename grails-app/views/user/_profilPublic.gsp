
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
		
		<ul class="label">
			<li>Surface : <span class="link">${ house?.surface?.toInteger() ?: '' }m²</span></li>
			
			<g:if test="${ false }">
				<li>Consommation annuelle : <span class="link">${ (house.consoAnnuelle / 1000) as Integer }kWh</span> 
			</g:if>
			<g:else>
				<li>Consommation annuelle : <span class="link">kWh</span>			
			</g:else>
			<span style="font-size:xx-small">(estimation sur une année complète en fonction de votre consommation réelle depuis le 1er janvier)</span></li>
			
			<g:if test="${ false }">
				<g:set var="dpe" value="${ (house.consoAnnuelle / 1000 / house.surface) as Integer }"></g:set>
				<g:set var="dpeIndex" value="${ (smarthome.automation.House.classementDPE(dpe) as char) - ('A' as char) }"></g:set>
			</g:if>
			<g:else>
				<g:set var="dpeIndex" value=""></g:set>
			</g:else>
			
			<li>Classement énergétique : </li>
			<li><div style="position:relative">
				<asset:image src="dpe.jpg" width="200px"/>
				<g:if test="${ dpeIndex }">
					<div class="vignettedpe" style="top:${ 14 + (dpeIndex * 22) }px">${ dpe }</div>
				</g:if>
			</div></li>
		</ul>
		
		<g:if test="${ !house && !viewOnly }">
			<h6 class="h6">
			Pour calculer votre consommation annuelle et le classement énergétique de votre maison, <g:link action="profil" controller="user">veuillez compléter votre profil</g:link>
			</h6>
		</g:if>
