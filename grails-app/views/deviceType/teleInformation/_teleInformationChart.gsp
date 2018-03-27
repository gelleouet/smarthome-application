<%@ page import="smarthome.automation.ChartViewEnum" %>


<div class="aui-tabs horizontal-tabs">
    <ul class="tabs-menu">
       	<li class="menu-item active-tab">
            <a href="#tabs-chartconso">Consommations</a>
        </li>
        <li class="menu-item">
            <a href="#tabs-synthese">Synthèse détaillée</a>
        </li>
        <li class="menu-item">
            <a href="#tabs-puissance">Puissance instantanée</a>
        </li>
    </ul>
    
    <div class="tabs-pane active-pane" id="tabs-chartconso">
    	<br/>
     	<g:render template="/deviceType/teleInformation/teleInformationChartConso"/>
    </div>
    
    <div class="tabs-pane" id="tabs-synthese">
    	<br/>
    	<g:render template="/deviceType/teleInformation/teleInformationChartSynthese"/>
    </div>
    
    <div class="tabs-pane" id="tabs-puissance">
    	<br/>
    	<g:render template="/deviceType/teleInformation/teleInformationChartPuissance"/>
    </div>
</div>

