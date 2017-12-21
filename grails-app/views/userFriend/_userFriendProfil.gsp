<g:set var="houseService" bean="houseService"/>

<g:set var="house" value="${ houses.find{ it.user.id == user.id } }"/>
<g:set var="conso" value="${ house ? consos.find{ it.house.id == house?.id } : null }"/>
<g:set var="dpe" value="${ houseService.classementDPE(house, conso) }"/>

<p>
	<label class="label">
		<strong>Classement : ${ dpe?.note ?: '-'} <g:if test="${ dpe }">(${ dpe.kwParAn }kWh/an/m²)</g:if></strong>
		<br/>
		Surface : ${ house?.surface}m²,
		Chauffage : ${ house?.chauffage?.libelle ?: '-' }, 
		Consommation annuelle : ${ conso ? (conso.kwHC + conso.kwHP) as Integer : '-'}kWh
	</label>
</p>