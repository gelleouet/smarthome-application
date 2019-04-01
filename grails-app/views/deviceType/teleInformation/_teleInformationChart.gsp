<%@ page import="smarthome.automation.ChartViewEnum" %>


<div class="aui-tabs horizontal-tabs">
    <div id="tabs-chartconso">
     	<g:render template="/deviceType/teleInformation/teleInformationChartConso"/>
    </div>
    
    <div  id="tabs-chartcout">
    	<g:render template="/deviceType/teleInformation/teleInformationChartCout"/>
    </div>
    
    <div id="tabs-synthese" style="margin-top:15px">
    	<g:render template="/deviceType/teleInformation/teleInformationChartSynthese"/>
    </div>
</div>

