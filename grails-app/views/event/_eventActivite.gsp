<h3>Activité événements</h3>

<div style="overflow-x:auto;">
<app:datatable datatableId="datatable-last-events" recordsTotal="0" cssStyle="margin-top:10px">
	<thead>
		<tr>
			<th>Date</th>
			<th>Evénement</th>
			<th>Récurrent?</th>
		</tr>
	</thead>
	<tbody>
		<g:each var="event" in="${ lastEvents }">
			<tr>
				<td>${ app.formatTimeAgo(date: event.lastEvent) }</td>
				<td><g:link controller="event" action="edit" id="${ event.id }">${ event.libelle }</g:link></td>
				<td>${ event.cron ? 'X' : '' }</td>
			</tr>
		</g:each>
	</tbody>
</app:datatable>
</div>

<g:link class="aui-button-link" action="events" controller="event"><span class="aui-icon aui-icon-small aui-iconfont-arrows-right"></span> Voir tous les événements</g:link>

