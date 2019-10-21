<form class="form-inline d-none d-sm-inline-block">
	<input class="form-control form-control-no-border mr-sm-2" type="text" placeholder="Rechercher..." aria-label="Search">
</form>

<div class="navbar-collapse collapse">
	<ul class="navbar-nav ml-auto">
	
		<sec:ifNotGranted roles="ROLE_GRAND_DEFI">
			<li class="nav-item">
				<g:link class="nav-link" controller="deviceAlert" action="deviceAlerts" params="[open:true]" title="Alertes">
					<div class="position-relative">
						<i class="align-middle" data-feather="bell"></i>
						<%--<span class="indicator">2</span>--%>
					</div>
				</g:link>
			</li>
		</sec:ifNotGranted>
	
		<sec:ifNotGranted roles="ROLE_GRAND_DEFI">
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
		</sec:ifNotGranted>
	
		<li class="nav-item dropdown">
			<g:set var="loggedInUser" value="${ secUser ?: smarthome.security.User.read(sec.loggedInUserInfo(field: 'id')) }"/>
        	
        	<a class="nav-link dropdown-toggle d-none d-sm-inline-block" href="#" data-toggle="dropdown">
        		<asset:image src="useravatar.png" class="avatar img-fluid rounded-circle mr-1"/>
        		<span class="text-dark">${ loggedInUser.initiale }</span>
            </a>
            
            <g:set var="headers" value="${ app.navigationItems(category: 'configuration')?.subitems?.groupBy({ it.header }) }"/>
            
            <div class="dropdown-menu dropdown-menu-right">
            	<div class="dropdown-header">${ loggedInUser.username }</div>
            	
            	<g:each var="header" in="${ headers.sort{ it.key } }">
            		<g:set var="item" value="${ header.value.find{ it.defaultGroup } }"/>
            		<g:if test="${ item }">
						<g:link class="dropdown-item" controller="${ item.controller }" action="${ item.action }"><app:icon class="align-middle mr-1" name="${ item.icon }"/> ${ header.key }</g:link>
					</g:if>
				</g:each>
				
				<div class="dropdown-divider"></div>
				<sec:ifSwitched>
                	<g:link action="exitSwitchUser" controller="user" class="dropdown-item"><i class="align-middle mr-1" data-feather="log-out"></i> Revenir à votre session</g:link>
				</sec:ifSwitched>
				<g:link class="dropdown-item" controller="logout" ><i class="align-middle mr-1" data-feather="log-out"></i> Déconnexion</g:link>
			</div>
		</li>
	</ul>
</div>