<%@ page import="smarthome.core.LayoutUtils" %>

<h3 style="background-color:#f5f5f5">Invitations reçues (${ followers.size() })</h3>

<g:if test="${ followers }">
	<g:set var="friendSplits" value="${ LayoutUtils.splitRow(followers, 2) }"/>
	
	<g:each var="row" in="${ friendSplits }">
		<div class="aui-group">
			<g:each var="col" in="${ row }">
				<div class="aui-item responsive">
					<div>
						<g:if test="${ col }">
							<div class="filActualiteItem2">
								<div class="aui-group">
									<div class="aui-item" style="width:50px">
										<asset:image src="useravatar.png" width="48px" />
									</div>
									<div class="aui-item">
										<h4>${ col.user.prenomNom } <span class="aui-lozenge aui-lozenge-moved">à confirmer</span></h4> 
										
										<g:render template="userFriendProfil" model="[user: col.user, houses: followerHouses, consos: followerConsos]"/>
										
										<div class="aui-buttons" style="float:right">
											<g:link action="confirmFriend" class="aui-button aui-button-primary confirm-button" id="${ col.id }"><span class="aui-icon aui-icon-small aui-iconfont-approve"></span> Accepter</g:link>
											<g:link action="cancelFriend" class="aui-button confirm-button" id="${ col.id }"><span class="aui-icon aui-icon-small aui-iconfont-remove"></span> Refuser</g:link>
										</div>
									</div>
								</div>	
							</div>
						</g:if>
					</div>
				</div>
			</g:each>
		</div>
	</g:each>
</g:if>