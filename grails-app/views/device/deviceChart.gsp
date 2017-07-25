<html>
<head>
<meta name='layout' content='authenticated' />
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
</head>

<body onload="onLoadChart();">
	<nav class="aui-navgroup aui-navgroup-horizontal">
	    <div class="aui-navgroup-inner">
	        <div class="aui-navgroup-primary">
	            <ul class="aui-nav">
	                <li><g:link action="devicesGrid" controller="device" params="[favori: true]">Favoris</g:link></li>
	                <li><g:link action="devicesGrid" controller="device" params="[sharedDevice: true]">Partagés</g:link></li>
	                <g:each var="tableauBord" in="${ tableauBords }">
						<li class="${ command.device.tableauBord == tableauBord ? 'aui-nav-selected': '' }">
							<g:link action="devicesGrid" controller="device" params="[tableauBord: tableauBord]">${ tableauBord }</g:link>
						</li>	                
	                </g:each>
	            </ul>
	        </div><!-- .aui-navgroup-primary -->
	    </div><!-- .aui-navgroup-inner -->
	</nav>

	<g:applyLayout name="applicationContent">
		<g:form name="navigation-chart-form" action="deviceChart">
			<g:hiddenField name="device.id" value="${ command.device.id }"/>
			<g:render template="/chart/chartToolbar"/>
		</g:form>
		
		<br/>
		<h4><g:link style="color:black;" action="edit" controller="device" id="${ command.device.id }"><span class="aui-icon aui-icon-small aui-iconfont-edit"></span> ${ command.device.label } (${ command.device.value })</g:link> <g:render template="/deviceAlert/deviceAlertLozenge" model="[alert: command.device.lastDeviceAlert()]"/></h4>
		<p class="h6">${ app.formatUserDateTime(date: command.device.dateValue) } - Il y a ${ app.formatTimeAgo(date: command.device.dateValue) }</p>
		
		<g:render template="deviceChart"/>
	</g:applyLayout>
	
	<asset:script type="text/javascript">
		google.load("visualization", "1.0", {packages:["corechart"]});
		google.setOnLoadCallback(buildGoogleCharts);
	</asset:script>
	
</body>
</html>