<html>
<head>
<meta name='layout' content='authenticated' />
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
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
					<g:render template="/user/profilPublic" model="[userDeviceCount: userDeviceCount, sharedDeviceCount: sharedDeviceCount]"></g:render>
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
					<g:render template="/house/synthese"/>
				</div>
				<br/>
				<div class="filActualite" style="padding:15px;">
					<g:render template="/device/deviceActivite"/>
				</div>
				<br/>
				<div class="filActualite" style="padding:15px;">
					<g:render template="/deviceEvent/deviceEventActivite"/>
				</div>
				
			</div>
		</div>
	
	</g:applyLayout>
	
	
	<asset:script type="text/javascript">
		google.load("visualization", "1.0", {packages:["corechart"]});
		google.setOnLoadCallback(buildGoogleCharts);
	</asset:script>
	
</body>
</html>