<html>
<head>
<meta name='layout' content='authenticated' />
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


	<g:applyLayout name="applicationContent">
		<div class="aui-group aui-group-split">
			<div class="aui-item">
				<h3>Alertes</h3>
			</div>
			<div class="aui-item">
				<div class="aui-buttons">
					<g:link class="aui-button" controller="deviceAlert" action="deviceAlerts">Tous</g:link>
					<g:link class="aui-button" controller="deviceAlert" action="deviceAlerts" params="[open: true]">Open</g:link>
				</div>
			</div>
		</div>
		
		<div style="padding:15px; margin-top:15px;">
			<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
			    <thead>
			        <tr>
			            <th>Alerte</th>
			            <th>Début</th>
			            <th>Fin</th>
			            <th>Relance</th>
			            <th>Objet</th>
			            <th>Statut</th>
			            <th class="column-1-buttons"></th>
			        </tr>
			    </thead>
			    <tbody>
			    	<g:each var="bean" in="${ deviceAlertInstanceList }">
				        <tr>
				            <td><span style="color:#707070; font-weight:bold;">${ bean.level.toString().toUpperCase() }</span></td>
				            <td>${ app.formatTimeAgo(date: bean.dateDebut) }</td>
				            <td>${ app.formatTimeAgo(date: bean.dateFin) }</td>
				            <td><g:if test="${ bean.relance }"><aui-badge>${ bean.relance }</aui-badge></g:if></td>
							<td><g:link controller="device" action="deviceChart" params="['device.id': bean.device.id]">${ bean.device.label }</g:link></td>
				            <td><div id="ajaxAlertStatus${bean.id}"><g:render template="deviceAlertStatusLozenge" model="[alert: bean]"/></div></td>
				            <td class="column-1-buttons">
				            	<g:if test="${ bean.isOpen() }">
					            	<g:remoteLink class="aui-button aui-button-subtle" title="Viewed" url="[action: 'markViewed', controller: 'deviceAlert', id: bean.id]" update="ajaxAlertStatus${bean.id}">
					            		<span class="aui-icon aui-icon-small aui-iconfont-approve"></span>
					            	</g:remoteLink>
				            	</g:if>
				            </td>
				        </tr>
			        </g:each>
			    </tbody>
			</app:datatable>
		</div>		
	</g:applyLayout>
	
</body>
</html>