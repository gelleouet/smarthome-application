<g:if test="${ typeCompteur == 'electricite' }">
<asset:image src="linky.png" height="40px" width="40px" class="img-thumbnail rounded"/>
</g:if>
<g:elseif test="${ typeCompteur == 'gaz' }">
<asset:image src="gazpar.png" height="45px" width="45px" class="img-thumbnail rounded"/>
</g:elseif>
<g:elseif test="${ typeCompteur == 'eau' }">
<asset:image src="compteur-eau.jpg" height="45px" width="45px" class="img-thumbnail rounded"/>
</g:elseif>
<g:if test="${ icon }"><app:icon name="${ icon }" lib="${ iconLib }"/></g:if>