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
			<div class="aui-item">
				<p class="label">Amis</p>
			</div>
		</div>
		<div class="aui-group">
			<div class="aui-item">
				<g:link action="devicesGrid1" controller="device" style="font-size:16pt">${ deviceCount }</g:link>
			</div>
			<div class="aui-item">
				<g:link action="devicesGrid1" controller="device" params="[shared: true]"style="font-size:16pt">${ sharedDeviceCount }</g:link>
			</div>
			<div class="aui-item">
				<g:link action="friends" controller="user" style="font-size:16pt">${ friendCount }</g:link>
			</div>
		</div>
		
		<g:if test="${ !mobileAgent }">
			<h5>Description Smarthome</h5>
			
			<ul class="label">
				<li>Surface : </li>
				<li>Classement énergétique : </li>
				<li>Chauffage : </li>
			</ul>
		</g:if>
</div>