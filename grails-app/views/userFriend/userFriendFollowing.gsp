<%@ page import="smarthome.core.LayoutUtils" %>

<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationHeader">
		<div class="aui-group aui-group-split">
			<div class="aui-item responsive">
				<g:form action="userFriendFollowing" class="aui">
					<h3>Recherchez vos amis</h3>
					<fieldset>
						<g:textField name="search" value="${ command.search }" class="text long-field" autofocus="true" placeholder="nom, prénom, email"/>
						<button class="aui-button aui-button-subtitle"><span class="aui-icon aui-icon-small aui-iconfont-search"></span></button>
					</fieldset>
				</g:form>
			</div>
			<div class="aui-item responsive">
				<div class="aui-buttons">
					<g:link action="userFriendSearch" class="aui-button"><span class="aui-icon aui-icon-small aui-iconfont-user-large"></span> Voir tous les utilisateurs</g:link>
				</div>
			</div>
		</div>	
		
	</g:applyLayout>


	<g:applyLayout name="applicationContent" params="[panelContentClass: 'panelContentGrey']">
	
		<div class="focus-large">
			
			<div id="div-confirm-follower" async-url="${ g.createLink(action: 'userFriendConfirmFollower') }"></div>
			<div id="div-waiting-following" async-url="${ g.createLink(action: 'userFriendWaitingFollowing') }" style="margin-top:30px;"></div>
			
			<h3 style="background-color:#f4f5f7">Amis (${ recordsTotal })</h3>
			
			<g:if test="${ userFriendInstanceList  }">
				<g:set var="friendSplits" value="${ LayoutUtils.splitRow(userFriendInstanceList, 2) }"/>
				
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
													<h4><g:link action="tableauBordFriend" controller="tableauBord" id="${ col.friend.id }">${ col.friend.prenomNom }</g:link></h4>
													
													<g:render template="userFriendProfil" model="[user: col.friend, houses: houses, consos: consos]"/>
													
													<div class="aui-buttons" style="float:right">
														<g:link action="deleteFriend" class="aui-button confirm-button" id="${ col.id }"><span class="aui-icon aui-icon-small aui-iconfont-delete"></span> Supprimer</g:link>
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
				
				<div class="pagination">
					<g:paginate total="${ recordsTotal }"/>
				</div>
			</g:if>
		</div>
	
	</g:applyLayout>
</body>
</html>