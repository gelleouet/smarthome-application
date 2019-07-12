<h3>Modes</h3>

<g:form name="change-mode-form" class="aui">

	<g:if test="${ house?.id }">
		<g:hiddenField name="house.id" value="${ house.id }"/>
	</g:if>

	<g:select name="modes" from="${ modes }" value="${ house ? house.modes*.mode : null }" 
		optionKey="id" optionValue="name" class="select combobox" multiple="true"
		onchange="onChangeMode(event)" data-url="${ g.createLink(action: 'changeMode', controller: 'house') }">
	</g:select>
</g:form>

<h6 class="h6" style="padding-top:10px;">Activer un ou plusieurs modes et changer ainsi le comportement de votre maison</h6>

