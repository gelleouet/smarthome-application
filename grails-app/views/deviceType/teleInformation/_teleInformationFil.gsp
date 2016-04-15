<g:set var="valueLabel" value="${ !value.name ? 'Intensité (A)' : impl.metaValuesInfo().get(value.name).label }"/>

<span class="label">${ valueLabel } : ${ value.name in ['hchp', 'hchc'] ? g.formatNumber(number: value.value, format: '### ### ### ### ###') : value.value }</span>