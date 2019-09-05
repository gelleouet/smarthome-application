<%@ page import="smarthome.automation.ChartViewEnum" %>


<div class="card card-margin-top">
    <div class="card-body">
     	<g:render template="/deviceType/teleInformation/teleInformationChartConso"/>
    </div>
</div>

<div class="card">
    <div class="card-body">
     	<g:render template="/deviceType/teleInformation/teleInformationChartCout"/>
    </div>
</div>

<div class="card">
    <div class="card-body">
     	<g:render template="/deviceType/teleInformation/teleInformationChartSynthese"/>
    </div>
</div>

