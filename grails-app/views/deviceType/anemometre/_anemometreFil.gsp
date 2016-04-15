<g:set var="beaufort" value="${ impl.convertToBeaufort(value.value) }"></g:set>
<span class="label">Vitesse en km/h : ${value.value }km/h
<br/>Vitessse en noeud : ${ impl.convertToNoeud(value.value) }kt
<br/>Beaufort : force ${ beaufort.force } - ${ beaufort.desc }</span>