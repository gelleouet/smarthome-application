<form class="form-inline d-none d-sm-inline-block">
	<input class="form-control form-control-no-border mr-sm-2" type="text" placeholder="Rechercher..." aria-label="Search">
</form>

<div class="navbar-collapse collapse">
	<ul class="navbar-nav ml-auto">
	
		<li class="nav-item">
			<g:link class="nav-link" controller="deviceAlert" action="deviceAlerts" params="[open:true]" title="Alertes">
				<div class="position-relative">
					<i class="align-middle" data-feather="bell"></i>
					<%--<span class="indicator">2</span>--%>
				</div>
			</g:link>
		</li>
	
	
		<li class="nav-item dropdown">
			<a class="nav-link dropdown-toggle d-none d-sm-inline-block" href="#" data-toggle="dropdown">
        		<i class="align-middle mr-1" data-feather="help-circle"></i> Aide
            </a>
			
			<div class="dropdown-menu dropdown-menu-right">
				<h6 class="dropdown-header">Application Web</h6>
				<a class="dropdown-item" href="https://github.com/gelleouet/smarthome-application">
                  <i class="align-middle" data-feather="github"></i> Github
                </a>
				<a class="dropdown-item" href="https://github.com/gelleouet/smarthome-application/wiki">
                  <i class="align-middle" data-feather="github"></i> Wiki
                </a>
                <div class="dropdown-divider"></div>
                <h6 class="dropdown-header">Contrôleur Raspberry</h6>
				<a class="dropdown-item" href="https://github.com/gelleouet/smarthome-raspberry">
                  <i class="align-middle" data-feather="github"></i> Github
                </a>
				<a class="dropdown-item" href="https://github.com/gelleouet/smarthome-raspberry/wiki">
                  <i class="align-middle" data-feather="github"></i> Wiki
                </a>
			</div>
		</li>
	
		<li class="nav-item dropdown">
			<g:set var="loggedInUser" value="${ secUser ?: smarthome.security.User.read(sec.loggedInUserInfo(field: 'id')) }"/>
        	
        	<a class="nav-link dropdown-toggle d-none d-sm-inline-block" href="#" data-toggle="dropdown">
        		<asset:image src="useravatar.png" class="avatar img-fluid rounded-circle mr-1"/>
        		<span class="text-dark">${ loggedInUser.prenom }</span>
            </a>
            
            <div class="dropdown-menu dropdown-menu-right">
				<g:link class="dropdown-item" controller="profil" action="profil"><i class="align-middle mr-1" data-feather="user"></i> Compte</g:link>
				<g:link class="dropdown-item" controller="device" action="devices"><i class="align-middle mr-1" data-feather="home"></i> Smarthome</g:link>
				<div class="dropdown-divider"></div>
				<sec:ifAllGranted roles="ROLE_SUPERVISION">
               		<g:link class="dropdown-item" controller="supervision" action="supervision"><i class="align-middle mr-1" data-feather="users"></i> Supervision</g:link>	
               	</sec:ifAllGranted>
               	<sec:ifAllGranted roles="ROLE_ADMIN">
					<g:link class="dropdown-item" controller="user" action="users"><i class="align-middle mr-1" data-feather="settings"></i> Système</g:link>
				</sec:ifAllGranted>
				<div class="dropdown-divider"></div>
				<sec:ifSwitched>
                	<g:link action="exitSwitchUser" controller="user" class="dropdown-item"><i class="align-middle mr-1" data-feather="log-out"></i> Revenir à votre session</g:link>
				</sec:ifSwitched>
				<g:link class="dropdown-item" controller="logout" ><i class="align-middle mr-1" data-feather="log-out"></i> Déconnexion</g:link>
			</div>
		</li>
	</ul>
</div>