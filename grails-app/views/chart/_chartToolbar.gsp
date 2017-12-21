<%@ page import="smarthome.automation.ChartViewEnum" %>

<g:hiddenField name="viewMode" value="${ command.viewMode }"/>
<g:hiddenField name="navigation" value=""/>
<g:each var="compareDevice" in="${ command.compareDevices }" status="status">
	<g:hiddenField name="compareDevices[${status}].id" value="${ compareDevice.id }"/>
</g:each>

<div class="aui-toolbar2">
    <div class="aui-toolbar2-inner">
        <div class="aui-toolbar2-primary">
        	<div class="aui-buttons">
        		<button id="navigation-chart-prev-button" class="aui-button"><span class="aui-icon aui-icon-small aui-iconfont-arrows-left">Précédent</span></button>
        		<button id="navigation-chart-next-button" class="aui-button"><span class="aui-icon aui-icon-small aui-iconfont-arrows-right">Suivant</span></button>
        	</div>
        	
        	<div class="aui-buttons">
        		<g:field type="date" name="dateChart" style="font-family: inherit; padding: 4px 5px; height:2.14285714em; margin-top:10px;" class="aui-date-picker" value="${ app.formatPicker(date: command.dateChart) }" required="true"/>	
        	</div>
        	
        	<div class="aui-buttons">
        		<button id="navigation-chart-day-button" class="aui-button ${ command.viewMode == ChartViewEnum.day ? 'aui-button-primary' : '' }">jour</button>
        		<button id="navigation-chart-month-button" class="aui-button ${ command.viewMode == ChartViewEnum.month ? 'aui-button-primary' : '' }">mois</button>
        		<button id="navigation-chart-year-button" class="aui-button ${ command.viewMode == ChartViewEnum.year ? 'aui-button-primary' : '' }">année</button>
        		<%--<button class="aui-button aui-dropdown2-trigger aui-button-split-more" aria-haspopup="true" aria-owns="split-navigation-chart-dropdown">More</button>
        		<div id="split-navigation-chart-dropdown" class="aui-dropdown2 aui-style-default" role="menu" aria-hidden="true">
			        <ul class="aui-list-truncate">
			            <li>
			            	<g:checkBox name="comparePreviousYear" value="${ command.comparePreviousYear }"/>
			            	<label for="comparePreviousYear">Comparer avec l'année précédente</label>
			            </li>
			        </ul>
			    </div>--%>
            </div>
        </div>
        
        <%--<div class="aui-toolbar2-secondary">
            <div id="button-set2" class="aui-buttons">
                <button class="aui-button aui-dropdown2-trigger" aria-owns="dropdown2-view" aria-haspopup="true" aria-controls="dropdown2-view" data-container="#button-set2"><span class="aui-icon aui-icon-small aui-iconfont-view">View</span></button>
            </div>
        </div>--%>
    </div><!-- .aui-toolbar-inner -->
</div>
