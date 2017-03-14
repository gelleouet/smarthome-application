<h3>Activité objets</h3>

<app:datatable datatableId="datatable-last-devices" recordsTotal="0" cssStyle="margin-top:10px">
	<thead>
		<tr>
			<th>Date</th>
			<th>Objet</th>
			<th>Modèle</th>
			<th>Valeur</th>
		</tr>
	</thead>
	<tbody>
		<g:each var="device" in="${ lastDevices }">
			<tr>
				<td>${ app.formatTimeAgo(date: device.dateValue) }</td>
				<td><g:link controller="device" action="devicesGrid" params="[tableauBord: device.tableauBord]">${ device.label }</g:link></td>
				<td>${ device.deviceType.libelle }</td>
				<td>${ device.value }</td>
			</tr>
		</g:each>
	</tbody>
</app:datatable>

<g:link class="aui-button-link" action="devices" controller="device"><span class="aui-icon aui-icon-small aui-iconfont-arrows-right"></span> Voir tous les objets</g:link>

