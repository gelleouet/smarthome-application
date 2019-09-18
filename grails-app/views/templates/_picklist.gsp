<select id="${ selectId }" name="${ selectId }" multiple="multiple" class="form-control picklist">
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

