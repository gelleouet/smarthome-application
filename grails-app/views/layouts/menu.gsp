<g:set var="houseService" bean="houseService"/>
<g:set var="house" value="${ houseService.findDefaultByPrincipal() }"/>

<nav id="sidebar" class="sidebar">
	<div class="sidebar-content">
		<g:link class="sidebar-brand" uri="/">
			<asset:image src="granddefi/bonhomme.png" height="25px"/>
			<span class="align-middle"><g:meta name="app.code"/></span>
		</g:link>
		

		<ul class="sidebar-nav">
		
			<li class="sidebar-item">
				<a href="#menuleft-conso" data-toggle="collapse" class="sidebar-link" aria-expanded="true" role="button">
					<app:icon name="bar-chart"/>
					<span class="align-middle">Mes consommations</span>
				</a>
				
				<ul id="menuleft-conso" class="sidebar-dropdown list-unstyled collapse show">
					<li class="sidebar-item">
						<g:link class="sidebar-link" action="consommationElec" controller="grandDefi">
							<app:icon name="zap"/>
							Electricité
						</g:link>
					</li>
					<g:if test="${ house?.compteurGaz }">
						<li class="sidebar-item">
							<g:link class="sidebar-link" action="consommationGaz" controller="grandDefi">
								<app:icon name="fire" lib="awesome"/>
								Gaz
							</g:link>
						</li>
					</g:if>
					<g:if test="${ house?.compteurEau }">
						<li class="sidebar-item">
							<g:link class="sidebar-link" action="consommationEau" controller="grandDefi">
								<app:icon name="droplet"/>
								Eau
							</g:link>
						</li>
					</g:if>
				</ul>
			</li>
			
			
			<li class="sidebar-item">
				<a href="#menuleft-saisieindex" data-toggle="collapse" class="sidebar-link" aria-expanded="true" role="button">
					<app:icon name="edit"/>
					<span class="align-middle">Saise des index</span>
				</a>
				
				<ul id="menuleft-saisieindex" class="sidebar-dropdown list-unstyled collapse show">
					<g:render template="/compteur/customMenuView" model="[house: house]"/>
				</ul>
			</li>
			
			
			<li class="sidebar-item">
				<a href="#menuleft-defi" data-toggle="collapse" class="sidebar-link" aria-expanded="true" role="button">
					<app:icon name="award"/>
					<span class="align-middle">Mes défis</span>
				</a>
				
				<ul id="menuleft-defi" class="sidebar-dropdown list-unstyled collapse show">
					<g:render template="/grandDefi/customMenuView" model="[house: house]"/>
				</ul>
			</li>
			
			<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_VALIDATION_INDEX">
				<li class="sidebar-item">
					<g:link class="sidebar-link" action="compteurIndexs" controller="compteur">
						<app:icon name="check-square"/>
						Validation des index
					</g:link>
				</li>
				<li class="sidebar-item">
					<g:link class="sidebar-link" controller="supervision" action="supervision">
						<app:icon name="monitor"/> Supervision utilisateur
					</g:link>
				</li>
			</sec:ifAnyGranted>
		
		</ul>
	</div>
</nav>


