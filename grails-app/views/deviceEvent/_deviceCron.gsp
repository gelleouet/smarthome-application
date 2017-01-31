<div class="field-group">
	<label>
		Cron
	</label>
	<g:textField name="cron" value="${deviceEvent?.cron}" class="text long-field"/>
	<div class="description">Expression cron. Exemples :
		<ul>
			<li>0 0 12 * * ? : Fire at 12pm (noon) every day</li>
			<li>0 15 10 ? * * : Fire at 10:15am every day</li>
			<li>0 15 10 * * ? : Fire at 10:15am every day</li>
			<li>0 15 10 * * ? 2005 : Fire at 10:15am every day during the year 2005</li>
			<li>0 * 14 * * ? : Fire every minute starting at 2pm and ending at 2:59pm, every day</li>
			<li>0 0/5 14 * * ? : Fire every 5 minutes starting at 2pm and ending at 2:55pm, every day</li>
			<li>0 15 10 ? * MON-FRI : Fire at 10:15am every Monday, Tuesday, Wednesday, Thursday and Friday</li>
			<li>0 15 10 L * ? : Fire at 10:15am on the last day of every month</li>
			<li>0 15 10 L-2 * ? : Fire at 10:15am on the 2nd-to-last day of every month</li>
			<li>0 15 10 ? * 6#3 : Fire at 10:15am on the third Friday of every month</li>
			<li>0 0 12 1/5 * ? : Fire at 12pm (noon) every 5 days every month, starting on the first day of the month.</li>
			<li>0 11 11 11 11 ? : Fire every November 11th at 11:11am.</li>
		</ul>
	</div>
</div>

<fieldset class="group">	
	<legend><span>Options</span></legend>
	<div class="checkbox">
		<g:checkBox name="synchroSoleil" value="${deviceEvent?.synchroSoleil}" class="checkbox"/>
		<label>Ajuster l'heure de planification pour déclencher l'événement à <g:field type="time" name="heureDecalage" value="${ deviceEvent?.heureDecalage }" class="text" style="width:100px;"/>
			au solstice d'<g:select name="solstice" value="${ deviceEvent?.solstice }" from="['été', 'hiver']" class="select"/>.
		<div class="description">Le cron doit toujours être programmé sur l'heure la plus tôt. La nouvelle heure sera calculée tous les jours.</div>
	</div>
	<div class="checkbox">
		<g:checkBox name="heureEte" value="${deviceEvent?.heureEte}" class="checkbox"/>
		<label>Incrémenter d'une heure la planification pendant le changement d'heure en été.</label>
		<div class="description">Cette option est compatible avec la planification solsticiale.</div>
	</div>
</fieldset>
