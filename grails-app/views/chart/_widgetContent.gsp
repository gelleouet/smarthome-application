<g:set var="chartService" bean="chartService"/>

<g:select name="paramId" from="${ chartService.listByPrincipal([:]) }" optionKey="id" optionValue="label" class="select combobox long-field" required="true"/>