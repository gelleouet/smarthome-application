<div class="widget-content">
	<div style="position:relative;">
		<div class="close-corner-button">
			<div class="aui-buttons">
				<g:if test="${ widgetUser.widget.configName }">
					<g:link class="aui-button aui-button-subtle" title="configuration" controller="${ widgetUser.widget.controllerName }" action="${ widgetUser.widget.configName }">
						<span class="aui-icon aui-icon-small aui-iconfont-configure"></span>
					</g:link>
				</g:if>
				<g:link class="aui-button aui-button-subtle" title="Supprimer" controller="widget" action="removeWidgetUser" id="${ widgetUser.id }">
					<span class="aui-icon aui-icon-small aui-iconfont-close-dialog"></span>
				</g:link>
			</div>
		</div>
		
		<div async-url="<%= createLink(controller: widgetUser.widget.controllerName, action: widgetUser.widget.actionName, id: widgetUser.paramId) %>"
				ajax="true" data-refresh="${ widgetUser.widget.refreshPeriod ? widgetUser.widget.refreshPeriod * 60 * 1000 : '' }"
				id="ajaxWidget_${widgetUser.id}">
			<div class="aui-progress-indicator">
			    <span class="aui-progress-indicator-value"></span>
			</div>
		</div>
	</div>
</div>