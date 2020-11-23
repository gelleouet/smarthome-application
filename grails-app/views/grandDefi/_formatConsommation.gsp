<%@ page import="smarthome.core.NumberUtils" %>

<g:if test="${ value != null }">
	<g:formatNumber number="${ value }" format="###,###,###,###.##"/>	
</g:if>
<g:else>
-
</g:else>

<span style="font-size: x-small;">${ unite ?: 'kWh' }</span>