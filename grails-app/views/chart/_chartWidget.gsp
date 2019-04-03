<%@ page import="smarthome.automation.ChartTypeEnum" %>

<div class="separator-bottom" style="padding-bottom:5px;">
	<div class="aui-group aui-group-split">
		<div class="aui-item">
			<h3><g:link action="edit" id="${ chart.id }" title="Modifier">${ chart.label }</g:link></h3>
		</div>
		<div class="aui-item">
			<div class="aui-buttons">
				<button class="aui-button aui-dropdown2-trigger" aria-controls="chart-dropdown-${ chart.id }">
				    <span class="aui-icon aui-icon-small aui-iconfont-configure">Outils</span>
				</button>
				<aui-dropdown-menu id="chart-dropdown-${ chart.id }">
				    <%--<aui-section label="Exports">
				        <aui-item-link href="${ g.createLink(action: 'exportPDF', id: chart.id) }">PDF</aui-item-link>
				        <aui-item-link href="${ g.createLink(action: 'exportExcel', id: chart.id) }">Excel</aui-item-link>
				    </aui-section>--%>
				    <aui-section>
				        <aui-item-link class="confirm-button" href="${ g.createLink(action: 'delete', id: chart.id) }">Supprimer</aui-item-link>
				    </aui-section>
				</aui-dropdown-menu>
	        </div>
		</div>
	</div>
</div>

<div id="chartDiv-${ chart.id }" data-chart-type="${ ChartTypeEnum.valueOf(chart.chartType).factory }">
	<h6 class="h6">Loading chart...</h6>
	<div class="aui-progress-indicator">
	    <span class="aui-progress-indicator-value"></span>
	</div>
	<div async-url="${ createLink(action: 'chartDatas', params: [chartHeight: 500, viewMode: command.viewMode, dateChart: app.formatPicker(date: command.dateChart), 'chart.id': chart.id, comparePreviousYear: command.comparePreviousYear]) }" on-async-complete="buildGoogleChart('#chartDiv-${ chart.id }')">
	</div>
</div>