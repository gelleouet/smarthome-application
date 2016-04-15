<span class="label">Etat du volet : ${value.value }%</span>
<g:if test="${ value.value == 0 }">
	<span class="aui-lozenge aui-lozenge-complete">FERME</span>
</g:if>
<g:elseif test="${ value.value >= 99 }">
	<span class="aui-lozenge aui-lozenge-complete">OUVERT</span>
</g:elseif>