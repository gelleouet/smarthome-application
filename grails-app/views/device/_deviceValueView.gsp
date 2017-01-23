<g:set var="impl" value="${ value.device.newDeviceImpl() }"/>

<div class="aui-group">
	<div class="aui-item filActualiteIcon">
		<asset:image src="${ impl.icon() }" class="filActualiteIcon"/>
	</div>
	<div class="aui-item">
		<h4>
		<g:if test="${ value.device.user.id == user.id }">
			<g:remoteLink action="favori" id="${ value.device.id }" controller="device" params="[favori: !value.device.favori]" title="Favori" onSuccess="favoriteStar('#star-device-${ value.id }', ${ !value.device.favori})">
		    	<g:if test="${ value.device.favori }">
		    		<span id="star-device-${ value.id }" class="star aui-icon aui-icon-small aui-iconfont-star"></span>
		    	</g:if>
		    	<g:else>
		    		<span id="star-device-${value.id }" class="aui-icon aui-icon-small aui-iconfont-unstar"></span>
		    	</g:else>
		   	</g:remoteLink>
		</g:if>
		${ value.device.label } 
		<span class="h6-normal">
		<g:remoteLink class="h6-normal" action="dialogProfilPublic" controller="user" id="${ value.device.user.id }" update="ajaxDialog" onSuccess="AJS.dialog2('#profil-dialog').show();">${ value.device.user.prenomNom }</g:remoteLink>
		 - ${ app.formatTimeAgo(date: value.dateValue).replace('Il y a ', '') }</span></h4>
		
		<div class="filActualiteText">
			<g:render template="${ impl.viewFilActualite() }" model="[value: value, impl: impl]"></g:render>
		</div>
		
		<div style="padding-top:4px;">
			<g:render template="/device/deviceToolbar" model="[device: value.device]"></g:render>
		</div>
	</div>
</div>