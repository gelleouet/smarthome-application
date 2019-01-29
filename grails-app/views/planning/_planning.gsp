<g:set var="deviceImpl" value="${ device.newDeviceImpl() }"/>
<g:set var="keys" value="${ deviceImpl.planningKeys() }"/>
<g:set var="values" value="${ deviceImpl.planningValues() }"/>
<g:set var="tableId" value="planning-table-${ planningStatus }"/>

<g:if test="${ planning.id }">
	<g:hiddenField name="${ prefix }id" value="${ planning.id }"/>
</g:if>
<g:hiddenField name="${ prefix }data" value="${ planning.data }" id="planning-table-data-${ planningStatus }"/>

<div class="field-group">
	<label for="label">
		Titre <span class="aui-icon icon-required">*</span>
	</label>
	<g:textField name="${ prefix }label" value="${ planning.label }" class="text long-field" required="true"/>
</div>

<div class="field-group">
	<label for="rule">
		Condition d'exécution
	</label>
	<g:textField name="${ prefix }rule" value="${ planning.rule }" class="text long-field"/>
</div>

<div class="aui-toolbar2" style="padding:10px 0px">
	<div class="aui-toolbar2-inner">
        <div class="aui-toolbar2-primary">
        	<div class="aui-buttons">
        		<g:each var="value" in="${ values }">
					<a class="aui-button planning-button" data-table="${ tableId }" data-value="${ value.key }"
						style="color:${ value.value.color}; font-weight:bold;">${ value.value.label }</a>
				</g:each>
			</div>   	
			
			<div class="aui-buttons">
				<a class="aui-button" id="planning-copy-button" data-table="${ tableId }" title="Copier"><span class="aui-icon aui-icon-small aui-iconfont-copy-clipboard"></span></a>
				<a class="aui-button" id="planning-paste-button" data-table="${ tableId }" title="Coller"><span class="aui-icon aui-icon-small aui-iconfont-table-paste-row"></span></a>
			</div>   		            
        </div>
        
        <div class="aui-toolbar2-secondary">
        	<div class="aui-buttons">
				<a class="aui-button" id="planning-device-copy-button" data-table="${ tableId }" title="Dupliquer" data-url="${ g.createLink(controller: controllerPlanning, action: 'copyPlanning', params: [status: planningStatus]) }"><span class="aui-icon aui-icon-small aui-iconfont-table-copy-row"></span></a>
				<a class="aui-button" id="planning-device-delete-button" data-table="${ tableId }" title="Supprimer" data-url="${ g.createLink(controller: controllerPlanning, action: 'deletePlanning', params: [status: planningStatus]) }"><span class="aui-icon aui-icon-small aui-iconfont-delete"></span></a>
			</div>   
        </div>
	</div>
</div>


<g:if test="${ keys }">
	<table id="${ tableId }" style="border-collapse:collapse; font-size:x-small;" class="planning" data-field="planning-table-data-${ planningStatus }">
		<thead>
			<tr style="height:16px;">
				<th class="planning-header-cell" colspan="2">&nbsp;</th>
				<g:each var="hour" in="${ 0..23 }">
					<th style="width:16px" class="planning-header-cell">${ hour }</th>
					<th style="width:16px" class="planning-header-cell">&nbsp;</th>
				</g:each>
			</tr>
		</thead>
		<tbody>
			<g:each var="day" in="${ ['lundi', 'mardi', 'mercredi', 'jeudi', 'vendredi', 'samedi', 'dimanche'] }" status="statusDay">
				<g:each var="key" in="${ keys }" status="keyStatus">
					<tr style="height: 16px;" class="${ keyStatus == keys.size()-1 ? 'planning-day-row' : '' }">
						<g:if test="${ keyStatus == 0 }">
							<td class="planning-day-cell" rowspan="${ keys.size() }">${ day }</td>
						</g:if>
						<td class="planning-header-cell">${ key }</td>
						<g:each var="hour" in="${ 0..47 }">
							<g:set var="dataValue" value="${ planning.bindValue(values, key, statusDay, hour) }"/>
						
							<td class="planning-cell" data-value="${ dataValue.value }" data-index="${ hour }"
								data-key="${ key }" data-day="${ statusDay }" style="background-color:${ dataValue.color }"></td>
						</g:each>	
					</tr>
				</g:each>
			</g:each>
		</tbody>
	</table>
</g:if>
<g:else>
	<g:applyLayout name="messageWarning">
		L'implémentation n'a pas définie de clés pour la construction du planning
	</g:applyLayout>
</g:else>