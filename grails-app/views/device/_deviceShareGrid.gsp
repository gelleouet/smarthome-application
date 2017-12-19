<%@ page import="smarthome.core.LayoutUtils" %>

<h3>Objets partagés (${ devices.size() })</h3>

<g:each var="deviceSplit" in="${ LayoutUtils.splitRow(devices?.sort{ it.label }, 2) }">
	<div class="aui-group">
		<g:each var="device" in="${ deviceSplit }" status="status">
			<div class="aui-item responsive">
				<g:if test="${ device }">
					<div>
						<div class="filActualiteItem2">
							<g:render template="deviceView" model="[device: device, user: user]"></g:render>
						</div>
					</div>
				</g:if>
			</div>
		</g:each>
	</div>
</g:each>
