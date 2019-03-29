<div class="widget-content">
	<div async-url="<%= createLink(controller: widgetUser.widget.controllerName, action: widgetUser.widget.actionName, id: widgetUser.paramId) %>"
			ajax="true" data-refresh="${ widgetUser.widget.refreshPeriod ? widgetUser.widget.refreshPeriod * 60 * 1000 : '' }"
			id="ajaxWidget_${widgetUser.id}">
		<div class="aui-progress-indicator">
		    <span class="aui-progress-indicator-value"></span>
		</div>
	</div>
</div>