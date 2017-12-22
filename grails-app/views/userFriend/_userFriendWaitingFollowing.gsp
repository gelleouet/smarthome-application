<%@ page import="smarthome.core.LayoutUtils" %>

<h3 style="background-color:#f4f5f7">Invitations envoyées (${ followings.size() })</h3>

<g:if test="${ followings }">
	<g:set var="friendSplits" value="${ LayoutUtils.splitRow(followings, 2) }"/>
	
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
										<h4>${ col.friend.prenomNom } <span class="aui-lozenge aui-lozenge-moved">en attente</span></h4> 
										
										<g:render template="userFriendProfil" model="[user: col.friend, houses: followingHouses, consos: followingConsos]"/>
										
										<div class="aui-buttons" style="float:right">
											<g:link action="cancelFriend" class="aui-button confirm-button" id="${ col.id }"><span class="aui-icon aui-icon-small aui-iconfont-undo"></span> Annuler</g:link>
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