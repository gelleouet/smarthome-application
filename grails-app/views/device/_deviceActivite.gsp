<div class="aui-group aui-group-split">
	<div class="aui-item">
		<h3>Activité objets</h3>
	</div>
	<div class="aui-item">
		<g:if test="${ countOpenAlert }">
			<g:link style="color: #d04437; font-weight: bold;" controller="deviceAlert" action="deviceAlerts" params="[open:true]">
				<span class="aui-icon aui-icon-small aui-iconfont-warning"></span> ${ countOpenAlert } alerte${ countOpenAlert > 1 ? 's' : '' } en cours
			</g:link>
		</g:if>
	</div>
</div>

<div style="overflow-x:auto;">
<app:datatable datatableId="datatable-last-devices" recordsTotal="0" cssStyle="margin-top:10px">
	<thead>
		<tr>
			<th>Date</th>
			<th>Objet</th>
			<th>Valeur</th>
			<th>Alerte</th>
		</tr>
	</thead>
	<tbody>
		<g:each var="device" in="${ lastDevices }">
			<tr>
				<td>${ app.formatTimeAgo(date: device.dateValue) }</td>
				<td><g:link controller="device" action="edit" id="${ device.id }">${ device.label }</g:link></td>
				<td><g:link controller="device" action="deviceChart" params="['device.id': device.id]">${ device.value }</g:link></td>
				<td><g:render template="/deviceAlert/deviceAlertLozenge" model="[alert: device.lastDeviceAlert()]"></g:render></td>
			</tr>
		</g:each>
	</tbody>
</app:datatable>
</div>

<div class="aui-group aui-group-split">
	<div class="aui-item">
		<g:link class="aui-button-link" action="devices" controller="device"><span class="aui-icon aui-icon-small aui-iconfont-arrows-right"></span> Voir tous les objets</g:link>
	</div>
	<div class="aui-item">
		<g:link style="color:#707070;" class="aui-button-link" action="deviceAlerts" controller="deviceAlert"><span class="aui-icon aui-icon-small aui-iconfont-arrows-right"></span> Voir toutes les alertes</g:link>
	</div>
</div>


