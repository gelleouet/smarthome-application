<%@ page import="smarthome.core.LayoutUtils" %>

<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationHeader">
		<div class="aui-group aui-group-split">
			<div class="aui-item responsive">
				<g:form action="userFriendSearch" class="aui">
					<h3>Recherchez des amis</h3>
					<fieldset>
						<g:textField name="search" value="${ command.search }" class="text long-field" autofocus="true" placeholder="nom, prénom, email"/>
						<button class="aui-button aui-button-subtitle"><span class="aui-icon aui-icon-small aui-iconfont-search"></span></button>
					</fieldset>
				</g:form>
			</div>
			<div class="aui-item responsive">
				<div class="aui-buttons">
					<g:link action="userFriendFollowing" class="aui-button"><span class="aui-icon aui-icon-small aui-iconfont-user-large"></span> Mes amis</g:link>
				</div>
			</div>
		</div>	
		
	</g:applyLayout>


	<g:applyLayout name="applicationContent" params="[panelContentClass: 'panelContentGrey']">
	
		<div class="focus-large">
			<h3 style="background-color:#f4f5f7">Invitez de nouveaux amis</h3>
		
			<g:if test="${ userInstanceList  }">
				<g:set var="userSplits" value="${ LayoutUtils.splitRow(userInstanceList, 2) }"/>
				
				<g:each var="row" in="${ userSplits }">
					<div class="aui-group">
						<g:each var="col" in="${ row }">
							<div class="aui-item responsive">
								<g:if test="${ col }">
									<div>
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
													<h4>${ col.prenomNom }</h4>
													
													<g:render template="userFriendProfil" model="[user: col, houses: houses, consos: consos]"/>
													
													<div class="aui-buttons" style="float:right">
														<g:link action="inviteFriend" class="aui-button confirm-button" id="${ col.id }"><span class="aui-icon aui-icon-small aui-iconfont-user-status"></span> Inviter</g:link>
													</div>
												</div>
											</div>	
										</div>
									</div>
								</g:if>
							</div>
						</g:each>
					</div>
				</g:each>
				
				<div class="pagination">
					<g:paginate total="${ recordsTotal }"/>
				</div>
			</g:if>
			<g:else>
				<p class="label">Personne ne correspond à cette recherche !</p>
			</g:else>
		</div>
	
	</g:applyLayout>
</body>
</html>