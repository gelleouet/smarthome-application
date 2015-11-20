<%@ page import="smarthome.automation.DeviceEvent" %>

<div class="field-group">
	<label for="device">
		Périphérique
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:select id="device" name="device.id" required="true" from="${devices}" optionKey="id" optionValue="label"
		value="${deviceEvent?.device?.id}" class="select"/>
</div>

<div class="field-group">
	<label for="libelle">
		Description
		<span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="libelle" required="true" value="${deviceEvent?.libelle}" class="text long-field"/>
</div>

<div class="field-group">
	<label for="actif">
		Actif		
	</label>
	<g:checkBox name="actif" value="${deviceEvent?.actif}" class="checkbox"/>
	<div class="description">Suspend temporairement un événement</div>
</div>

<div class="field-group">
	<label for="cron">
		Planification
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

<div class="field-group">
	<label for="condition">
		Condition
	</label>
	<g:textArea name="condition" value="${deviceEvent?.condition}" class="short-script textarea text long-field"/>
	<div class="description">Expression Groovy renvoyant en dernière instruction un boolean. Variables pré-définies :
	<ul>
	<li>device : objet smarthome.automation.Device. Représente le device associé à l'événement. Ex : device.command == "on", device.value == "1"</li>
	<li>devices : objet Map. Contient tous les devices indexés par leur mac. Ex : devices['gpio4']</li>
	</ul>
	</div>
</div>







