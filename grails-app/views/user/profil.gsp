<%@ page import="smarthome.automation.notification.NotificationAccountEnum" %>

<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
	
		<g:form name="profil-form" controller="user" method="post" class="aui">
			<g:hiddenField name="user.id" value="${user.id}" />
	
			<h4>Général</h4>
			
			<fieldset>
				<div class="field-group">
					<label>Email<span class="aui-icon icon-required"> required</span></label>
					<g:textField name="user.username" value="${user.username}" type="email"
						class="text long-field" required="true" disabled="true" />
				</div>
				<div class="field-group">
					<label>Prénom<span class="aui-icon icon-required"> required</span></label>
					<g:textField name="user.prenom" value="${user.prenom}" required="true" 
						class="text long-field"  />
				</div>
				<div class="field-group">
					<label>Nom<span class="aui-icon icon-required">required</span></label>
					<g:textField name="user.nom" value="${user.nom}" class="text long-field"
						required="true" />
				</div>
			</fieldset>

			
			<h4>Maison principale</h4>
			<g:include action="templateEditByUser" controller="house" params="[user: user]"/>
			
			<div id="ajaxModes">
				<g:include action="templateEditByUser" controller="mode" params="[user: user]"/> 
			</div> 

			
			<h4>Notifications SMS</h4>
			
			<fieldset>
				<div class="field-group">
					<label >N° téléphone mobile</label>
					<g:textField name="user.telephoneMobile" value="${user.telephoneMobile}" class="text medium-field" />
					<div class="description">Ne sert exclusiment qu'aux notifications SMS</div>
				</div>
			</fieldset>

			<fieldset>
				<div class="field-group">
					<label >Compte</label>
					<div>
						<g:if test="${ smsAccount }">
							${ smsAccount.senderInstance.description } 
							<g:remoteLink url="[controller: 'notificationAccount', action: 'dialogNotificationAccount', id: smsAccount.id]"
								update="ajaxDialog" onComplete="showNotificationAccountDialog()" class="aui-button aui-button-link">Modifier le compte</g:remoteLink>
						</g:if>
						<g:else>
							Vous n'avez pas encore configuré de compte pour l'envoi des SMS. Veuillez cliquer sur le bouton suivant pour en créer un.
							<g:remoteLink url="[controller: 'notificationAccount', action: 'dialogNotificationAccount', params: [typeNotification: NotificationAccountEnum.sms]]"
								update="ajaxDialog" onComplete="showNotificationAccountDialog()" class="aui-button aui-button-link">Créer un compte</g:remoteLink> 
						</g:else>
					</div>
				</div>
			</fieldset>
			
			
			
			<h4>Applications</h4>
			
			<fieldset>
				<div class="field-group">
					<label>Application ID<span class="aui-icon icon-required"> required</span></label>
					<h5>${ user.applicationKey }</h5>
					<div class="description">Utilisez cet ID pour connecter des agents à l'application <g:meta name="app.code"/>. Il vous sera demandé dans les identifiants de connexion.</div>
				</div>
			</fieldset>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:actionSubmit value="Enregistrer" action="saveProfil" class="aui-button aui-button-primary" />
				</div>
			</div>
			
		</g:form>
	</g:applyLayout>
</body>
</html>