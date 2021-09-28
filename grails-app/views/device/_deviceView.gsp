<h4>
	<g:if test="${ device.user.id == user.id }">
		<g:remoteLink action="favori" id="${ device.id }" params="[favori: !device.favori]" title="Favori" onSuccess="favoriteStar('#star-device-${ device.id }', ${ !device.favori})">
	    	<g:if test="${ device.favori }">
	    		<span id="star-device-${ device.id }" class="star aui-icon aui-icon-small aui-iconfont-star"></span>
	    	</g:if>
	    	<g:else>
	    		<span id="star-device-${ device.id }" class="aui-icon aui-icon-small aui-iconfont-unstar"></span>
	    	</g:else>
	   	</g:remoteLink>
	</g:if>
				            	
	${ device.label }
	<span class="h6-normal" title="${ app.formatUserDateTime(date: device.dateValue)}"> 
	
	<g:link class="h6-normal" action="tableauBordFriend" controller="tableauBord" id="${ device.user.id }">${ device.user.prenomNom }</g:link> - ${ app.formatTimeAgo(date: device.dateValue) }</span>
	
	<g:render template="/deviceAlert/deviceAlertLozenge" model="[alert: device.deviceImpl.viewParams.lastAlert]"/>
</h4>
								
<div class="aui-group">
	<div class="aui-item">
		<g:render template="${ device.deviceImpl.viewGrid() }" model="[device: device, height: height]"></g:render>
	</div>
</div>

<div style="padding-top:4px; width:100%">
	<g:render template="deviceToolbar" model="[device: device]"></g:render>
</div>
