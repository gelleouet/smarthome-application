<%@ page import="smarthome.automation.ChartViewEnum" %>


<div class="card card-margin-top">
    <div class="card-body">
     	<g:render template="/deviceType/compteur/compteurConsoChart"/>
    </div>
</div>

<div class="card">
    <div class="card-body">
     	<g:render template="/deviceType/compteur/compteurTarifChart"/>
    </div>
</div>

<div class="card">
    <div class="card-body">
     	<g:render template="/deviceType/compteur/compteurTableChart"/>
    </div>
</div>

