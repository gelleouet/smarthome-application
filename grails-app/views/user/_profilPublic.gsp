<div class="filActualite" style="padding:15px;">
		<h4><asset:image src="useravatar.png" width="48px" />
		<g:link action="profil" controller="user" style="color:black;">${ user.prenomNom }</g:link></h4>
		
		<div class="aui-group">
			<div class="aui-item">
				<p class="label">Objets personnels</p>
			</div>
			<div class="aui-item">
				<p class="label">Objets partagés</p>
			</div>
		</div>
		<div class="aui-group">
			<div class="aui-item">
				<g:link action="devicesGrid" controller="device" style="font-size:16pt">${ userDeviceCount }</g:link>
			</div>
			<div class="aui-item">
				<g:link action="devicesGrid" controller="device" params="[shared: true]"style="font-size:16pt">${ sharedDeviceCount }</g:link>
			</div>
		</div>
		
		<g:if test="${ house }">
			<h5>${ house.name }</h5>
			
			<ul class="label">
				<g:if test="${ house.surface }">
					<li>Surface : <span class="link">${ house.surface.toInteger() }m²</span></li>
				</g:if>
				
				<g:if test="${ house.consoAnnuelle }">
					<li>Consommation annuelle : <span class="link">${ (house.consoAnnuelle / 1000) as Integer }kWh</span> 
					<span style="font-size:xx-small">(estimation sur une année complète en fonction de votre consommation réelle depuis le 1er janvier)</span></li>
				</g:if>
				
				<g:if test="${ house.surface && house.consoAnnuelle }">
				
					<g:set var="dpe" value="${ (house.consoAnnuelle / 1000 / house.surface) as Integer }"></g:set>
					<g:set var="dpeIndex" value="${ (smarthome.automation.House.classementDPE(dpe) as char) - ('A' as char) }"></g:set>
				
					<li>Classement énergétique : </li>
					<li><div style="position:relative">
						<asset:image src="dpe.jpg" />
						<div class="vignettedpe" style="top:${ 16 + (dpeIndex * 25) }px">${ dpe }</div>
					</div></li>
				</g:if>
			</ul>
		</g:if>
		<g:else>
			<h6>
			Pour calculer votre consommation annuelle et le classement énergétique de votre maison, <g:link action="profil" controller="user">veuillez compléter votre profil</g:link>
			</h6>
		</g:else>
</div>