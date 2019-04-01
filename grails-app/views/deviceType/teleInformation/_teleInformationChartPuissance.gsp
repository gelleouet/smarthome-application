<%@ page import="smarthome.endpoint.TeleinfoEndPoint" %>
<%@ page import="smarthome.core.EndPointUtils" %>

<h6>
	<p>Cet outil vous permet de suivre en temps réel la puissance totale consommée par votre installation électrique. 
	Les données reçues ne seront pas historisées sur les graphiques classiques mais l'écran ci-dessous sera 
	rafraichi instantanément avec les nouvelles valeurs.</p>
	<p>Appuyer sur le bouton "Démarrer" pour lancer la surveillance (Ce mode sera arrêté automatiquement au bout
	de 5 minutes. Vous pourrez le relancer autant de fois que nécessaire)</p> 
</h6>

<br/>

<div class="aui-buttons">
	<a id="teleinfo-start-trace-button" class="aui-button aui-button-primary" onclick="onStartTraceTeleinfo(this, ${device.id})" data-endpoint-url="${ EndPointUtils.httpToWs(g.createLink(uri: TeleinfoEndPoint.URL, absolute: true)) }">Démarrer</a>
	<a id="teleinfo-stop-trace-button" class="aui-button" onclick="onStopTraceTeleinfo()">Arrêter</a>
</div>

<br/>
<div id="teleinfo-statut"></div>

<br/>

<g:set var="isousc" value="${ device?.metavalue('isousc')?.value }"/>


<form class="aui">
	<div class="field-group">
		<label for="label">
			Option tarifaire
		</label>
		<g:textField name="teleinfo_option_tarifaire" class="text long-field"/>
	</div>
	<div class="field-group">
		<label for="label">
			Période tarifaire
		</label>
		<g:textField name="teleinfo_periode_tarifaire" class="text long-field"/>
	</div>
	<div class="field-group">
		<label for="label">
			Index heure creuse (Wh)
		</label>
		<g:textField name="teleinfo_index_hc" class="text long-field"/>
	</div>
	<div class="field-group">
		<label for="label">
			Index heure pleine (Wh)
		</label>
		<g:textField name="teleinfo_index_hp" class="text long-field"/>
	</div>
</form>


<div style="display:table; width:100%">
	<div class="teleinfo-gauge-container">
		<div class="separator" style="text-align:center; font-weight:bold; font-size: medium;">Puissance (VA)</div>
		<div id="chart_teleinfo_kva" data-chart-type="Gauge" data-delegate-chart="createTeleinfoChart" class="teleinfo-gauge"
			data-max-intensite="${ isousc }" data-key="kva">
		</div>
	</div>
	<div class="teleinfo-gauge-container">
		<p class="separator" style="text-align:center; font-weight:bold; font-size: medium;">Puissance (W)</p>
		<div id="chart_teleinfo_watt" data-chart-type="Gauge" data-delegate-chart="createTeleinfoChart" class="teleinfo-gauge"
			data-max-intensite="${ isousc }" data-key="watt">
		</div>
	</div>
	<div class="teleinfo-gauge-container">
		<p class="separator" style="text-align:center; font-weight:bold; font-size: medium;">Intensité (A)</p>
		<div id="chart_teleinfo_intensite" data-chart-type="Gauge" data-delegate-chart="createTeleinfoChart" class="teleinfo-gauge"
			data-max-intensite="${ isousc }" data-key="intensite">
		</div>
	</div>
</div>
	
	