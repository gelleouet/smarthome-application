<g:set var="deviceService" bean="deviceService"/>

<g:select name="paramId" from="${ deviceService.listByPrincipal([:]) }" optionKey="id" optionValue="label" class="select combobox long-field" required="true"/>