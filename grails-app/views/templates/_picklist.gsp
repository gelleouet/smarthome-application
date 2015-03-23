<div class="aui">
	<select id="${ selectId }" name="${ selectId }" multiple="multiple">
		<g:each var="option" in="${ options }">
			<g:set var="selected" value="${ selectedOptions.find() { it."$idField" == option."$idField" } }"/>
			<option value="${ option."$idField" }" 
				<g:if test="${ selected }">
				selected="selected"
				</g:if>
			>
				${ fieldValue(bean: option, field: labelField) }
			</option>
		</g:each>
	</select>
</div>


<asset:script type="text/javascript">
	$("#${ selectId }").pickList({
		sourceListLabel:	"Disponibles",
		targetListLabel:	"Ajoutés",
		addClass: "pick-button",
		addAllClass: "pick-button",
		removeClass: "pick-button",
		removeAllClass: "pick-button",
	});
</asset:script>

