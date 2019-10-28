<%@ page import="smarthome.core.NumberUtils" %>

<g:if test="${ value != null }">
	<g:formatNumber number="${ NumberUtils.round(value, precision != null ? precision : 1) }" format="### ### ### ###.#" groupingUsed="true"/>	
</g:if>
<g:else>
-
</g:else>

<span style="font-size: x-small;">kWh</span>