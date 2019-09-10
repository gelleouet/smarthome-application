<%@ page import="smarthome.automation.ChartViewEnum" %>

<g:hiddenField name="viewMode" value="${ command.viewMode }"/>
<g:hiddenField name="navigation" value=""/>
<g:if test="${ command.hasProperty('compareDevices') }">
	<g:each var="compareDevice" in="${ command.compareDevices }" status="status">
		<g:hiddenField name="compareDevices[${status}].id" value="${ compareDevice.id }"/>
	</g:each>
</g:if>

<div class="btn-toolbar mb-4" role="toolbar">
	<div class="btn-group mr-2" role="group">
		<button id="navigation-chart-prev-button" class="btn btn-primary"><app:icon name="chevron-left"/></button>
		<g:field type="date" name="dateChart" class="form-control aui-date-picker" value="${ app.formatPicker(date: command.dateChart) }" required="true"/>	
		<button id="navigation-chart-next-button" class="btn btn-primary"><app:icon name="chevron-right"/></button>
	</div>
        	
	<div class="btn-group mr-2" role="group">
		<button id="navigation-chart-day-button" class="btn btn-primary ${ command.viewMode == ChartViewEnum.day ? 'active' : '' }">jour</button>
		<button id="navigation-chart-month-button" class="btn btn-primary ${ command.viewMode == ChartViewEnum.month ? 'active' : '' }">mois</button>
		<button id="navigation-chart-year-button" class="btn btn-primary ${ command.viewMode == ChartViewEnum.year ? 'active' : '' }">année</button>
		<button class="btn btn-primary dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"></button>
		<div id="split-navigation-chart-dropdown" class="dropdown-menu">
	 		<label class="custom-control custom-checkbox">
		     	<g:checkBox name="comparePreviousYear" value="${ command.comparePreviousYear }" class="custom-control-input"/>
		     	<span class="custom-control-label">Comparer année précédente</span>
		    </label>
		</div>
	</div>
</div>
