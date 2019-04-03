<%@ page import="smarthome.core.LayoutUtils" %>

<html>
<head>
<meta name='layout' content='authenticated-chart' />
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


	<g:applyLayout name="applicationContent">
	
		<div class="aui-group">
	
			<div class="aui-item" style="width:50%">
				<div style="margin:0 10px;">
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
															<span class="aui-avatar aui-avatar-project aui-avatar-large">
															    <span class="aui-avatar-inner">
															        <asset:image src="useravatar.png" style="width:48px" />
															    </span>
															</span>
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
			</div> <!-- div.aui-item -->
			
			<div class="aui-item">
				<div style="margin:0 10px;">
					<h3 style="background-color:#f4f5f7">Tête du classement <span class="aui-icon aui-icon-small aui-iconfont-priority-high"></span></h3>
					<g:render template="chartClassement" model="[chartId: 'top5-user-classement-chart', datas: top5]"/>
					
					<h3 style="background-color:#f4f5f7">Queue du classement <span class="aui-icon aui-icon-small aui-iconfont-priority-low"></span></h3>
					<g:render template="chartClassement" model="[chartId: 'flop5-user-classement-chart', datas: flop5]"/>
					
					<h3 style="background-color:#f4f5f7">Répartition chauffage</h3>
					<g:render template="chartChauffage" model="[chartId: 'repartition-chauffage-chart', datas: repartitionChauffage]"/>
					
				</div>
			</div>
		
		</div> <!-- div.aui-group -->
	
	</g:applyLayout>
</body>
</html>