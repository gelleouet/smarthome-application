<div class="wrapper">
	<nav id="sidebar" class="sidebar">
		<div class="sidebar-content">
			<g:link class="sidebar-brand" uri="/">
				<i class="align-middle" data-feather="home"></i>
				<span class="align-middle">BeMyHomeSmart</span>
			</g:link>
			
			<g:applyLayout name="menu"/>
		</div>
	</nav>
	
	<div class="main">
		<nav class="navbar navbar-expand navbar-light bg-white">
			<a class="sidebar-toggle d-flex mr-2">
				<i class="hamburger align-self-center"></i>
			</a>
			
			<g:applyLayout name="header"/>
		</nav>
		
		<main class="content">
			<div class="container-fluid p-0">
			
				<h3 class="mb-3">${ titre ?: 'Paramètres' }</h3>
				
				<div class="row">
					<div class="col-md-3 col-xl-2">
						<div class="card">
							<g:set var="items" value="${ app.navigationItems(category: navigation)?.subitems?.sort({ it.label }) }"/>
						
							<g:if test="${ items && items[0].header }">
								<div class="card-header">
									<h5 class="card-title mb-0">${ items[0].header }</h5>
								</div>
							</g:if>
						
							<div class="list-group list-group-flush">
								<g:each var="item" in="${ items }">
									<g:link controller="${item.controller }"
											action="${item.action }"
											class="list-group-item list-group-item-action ${app.isCurrentItem(item: item) ? 'active' : '' }">
											${item.label }
									</g:link>
								</g:each>
							</div> <!-- div.list-group -->
						</div> <!-- div.card -->
					</div>	<!-- div.col  -->
					
					<div class="col-md-9 col-xl-10">
						<div class="card">
							<div class="card-body">
								<g:applyLayout name="content-error"/>
							
								<g:layoutBody/>
							</div>
						</div>
					</div> <!-- div.col  -->
				</div> <!-- div.row -->
				
			</div>
		</main>
		
		<g:applyLayout name="footer"/>
	</div>
</div>