<!-- <form class="form-inline d-none d-sm-inline-block">
	<input class="form-control form-control-no-border mr-sm-2" type="text" placeholder="Rechercher..." aria-label="Search">
</form> -->

<div class="navbar-collapse collapse">
	<ul class="navbar-nav ml-auto">
	
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