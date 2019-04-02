<g:set var="houseService" bean="houseService"/>

<g:set var="house" value="${ houses.find{ it.user.id == user.id } }"/>
<g:set var="conso" value="${ house ? consos.find{ it.house.id == house?.id } : null }"/>
<g:set var="dpe" value="${ houseService.classementDPE(house, conso) }"/>

<p>
	<label class="label"><strong>Classement : ${ dpe?.note ?: '-'} <g:if test="${ dpe }">(${ dpe.kwParAn }kWh/an/m²)</g:if></strong></label>
	<ul>
		<li>Chauffage : <span class=link>${ house?.chauffage?.libelle ?: '-' }</span></li>
		<li>Consommation annuelle : <span class=link>${ conso ? (conso.kwHC + conso.kwHP + conso.kwBASE) as Integer : '-'}kWh</span></li>
	</ul>
</p>