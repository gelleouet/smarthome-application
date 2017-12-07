<html>
<head>
<meta name='layout' content='authenticated-chart' />
</head>

<body>

	<nav class="aui-navgroup aui-navgroup-horizontal">
	    <div class="aui-navgroup-inner">
	        <div class="aui-navgroup-primary">
	            <ul class="aui-nav">
	                <li><g:link action="devicesGrid" controller="device" params="[favori: true]">Favoris</g:link></li>
	                <li><g:link action="devicesGrid" controller="device" params="[sharedDevice: true]">Partagés</g:link></li>
	                <g:each var="tableauBord" in="${ tableauBords }">
						<li>
							<g:link action="devicesGrid" controller="device" params="[tableauBord: tableauBord]">${ tableauBord }</g:link>
						</li>	                
	                </g:each>
	            </ul>
	        </div><!-- .aui-navgroup-primary -->
	    </div><!-- .aui-navgroup-inner -->
	</nav>


	<g:applyLayout name="applicationContent" params="[panelContentClass: 'panelContentGrey']">
		<div class="aui-group">
			<div class="aui-item responsive" style="width:350px">
				<div class="filActualite" style="padding:15px;">
					<g:render template="/profil/profilPublic"></g:render>
				</div>
				<br/>
				<div class="filActualite" style="padding:15px;">
					<div id="ajaxHouseModeChange">
						<g:render template="/house/changeMode" model="[house: house, user: user, modes: modes]"></g:render>
					</div>
				</div>
			</div>
			<div class="aui-item responsive">
				<div class="filActualite" style="padding:15px;">
					<div id="divHouseSyntheseConfort" async-url="${ g.createLink(controller: 'house', action: 'syntheseConfort', id: house?.id) }"></div>
				</div>
				<br/>
				<div class="filActualite" style="padding:15px;">
					<div id="divHouseSyntheseConsommation" async-url="${ g.createLink(controller: 'house', action: 'syntheseConsommation', id: house?.id) }"></div>
				</div>
				<br/>
				<div class="filActualite" style="padding:15px;">
					<div id="divDeviceSynthese" async-url="${ g.createLink(controller: 'device', action: 'synthese') }"></div>
				</div>
				<br/>
				<div class="filActualite" style="padding:15px;">
					<div id="divEventSynthese" async-url="${ g.createLink(controller: 'event', action: 'synthese') }"></div>
				</div>
				
			</div>
		</div>
	
	</g:applyLayout>
	
</body>
</html>