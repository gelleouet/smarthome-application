<div class="form-group">
	<label>
		Cron
	</label>
	<g:textField name="cron" value="${event?.cron}" class="form-control"/>
	<small class="text-muted">Expression cron : seconde | minute | heure | jour du mois | mois | jour semaine. Exemples :
		<ul>
			<li>0 15 10 ? * * : Fire at 10:15am every day</li>
			<li>0 15 10 * * ? 2005 : Fire at 10:15am every day during the year 2005</li>
			<li>0 * 14 * * ? : Fire every minute starting at 2pm and ending at 2:59pm, every day</li>
			<li>0 0/5 14 * * ? : Fire every 5 minutes starting at 2pm and ending at 2:55pm, every day</li>
			<li>0 15 10 ? * MON-FRI : Fire at 10:15am every Monday, Tuesday, Wednesday, Thursday and Friday</li>
			<li>0 15 10 L * ? : Fire at 10:15am on the last day of every month</li>
			<li>0 15 10 L-2 * ? : Fire at 10:15am on the 2nd-to-last day of every month</li>
			<li>0 15 10 ? * 6#3 : Fire at 10:15am on the third Friday of every month</li>
			<li>0 0 12 1/5 * ? : Fire at 12pm (noon) every 5 days every month, starting on the first day of the month.</li>
		</ul>
	</small>
</div>

<label class="custom-control custom-checkbox">
	<g:checkBox name="synchroSoleil" value="${event?.synchroSoleil}" class="custom-control-input"/>
	<span class="custom-control-label">Ajuster l'heure de planification pour déclencher l'événement à 
	<g:field type="time" name="heureDecalage" value="${ event?.heureDecalage }" style="width:100px; display:inline;" class="form-control"/>
		au solstice d'<g:select name="solstice" value="${ event?.solstice }" from="['été', 'hiver']" style="width:100px; display:inline;" class="form-control"/></span>
</label>
<small class="text-muted">
	Le cron doit toujours être programmé sur l'heure la plus tôt. La nouvelle heure sera calculée tous les jours. 
	<a id="event-chart-button" data-url="${ g.createLink(action: 'dialogEventChart') }" class="btn btn-light">
		<span class="aui-icon aui-icon-small aui-iconfont-macro-gallery"></span> Afficher le graphique annuel.
	</a>
</small>

<label class="custom-control custom-checkbox">
	<g:checkBox name="heureEte" value="${event?.heureEte}" class="custom-control-input"/>
	<span class="custom-control-label">Incrémenter d'une heure la planification pendant le changement d'heure en été.</span>
</label>
<small class="text-muted">Cette option est compatible avec la planification solsticiale.</small>

