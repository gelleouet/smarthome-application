<div class="widget-sortable responsive">
	<g:each var="widgetUser" in="${ widgetUsers.sort{ it.col*100 + it.row } }">
		<div class="widget" id="widget-user-${ widgetUser.widget.id }" data-widget-id="${ widgetUser.id }">
			<g:render template="/widget/asyncWidget" model="[widgetUser: widgetUser]"/>
		</div>
	</g:each>
</div>

