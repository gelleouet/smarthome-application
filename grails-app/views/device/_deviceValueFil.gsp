<div class="filActualite">
	<g:each var="value" in="${ filActualite }">
		<div class="filActualiteItem">
			<g:render template="/device/deviceValueView" model="[value: value]"></g:render>
		</div>
	</g:each>
</div>

<div class="pagination">
	<g:paginate total="${ params.offset.toLong()  + params.max.toLong() + 1 }" maxsteps="1" omitLast="true" omitFirst="true" id="${ device?.id }"/>
</div>
