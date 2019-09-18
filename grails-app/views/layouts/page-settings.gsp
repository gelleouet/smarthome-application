<div class="wrapper">
	<nav id="sidebar" class="sidebar">
		<div class="sidebar-content">
			<g:link class="sidebar-brand" uri="/">
				<i class="align-middle" data-feather="home"></i>
				<span class="align-middle"><g:meta name="app.code"/></span>
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
						
						<g:set var="items" value="${ app.navigationItems(category: 'configuration')?.subitems?.sort({ it.label }) }"/>
						<g:set var="headers" value="${ app.navigationItems(category: 'configuration')?.subitems?.groupBy({ it.header }) }"/>
						
						<g:each var="header" in="${ headers.sort{ it.key } }">
			            		
			            	<g:if test="${ header.key != navigation }">
			            		<g:set var="item" value="${ header.value.find{ it.defaultGroup } }"/>
			            		
			            		<g:if test="${ item }">
			            			<div class="card">
			            				<div class="list-group list-group-flush">
											<g:link class="list-group-item list-group-item-action font-weight-bold" style="font-size:16px;" controller="${ item.controller }" action="${ item.action }"><app:icon name="${ item.icon }"/> ${ header.key }</g:link>
										</div>
									</div> <!-- div.card -->
								</g:if>
							</g:if>
							
							<g:else>
								<div class="card">
									<div class="card-header">
										<h5 class="card-title mb-0">${ header.key }</h5>
									</div>
									
									<div class="list-group list-group-flush">
										<g:each var="item" in="${ header.value.sort{ it.label } }">
											<g:link controller="${item.controller }"
													action="${item.action }"
													class="list-group-item list-group-item-action ${app.isCurrentItemController(item: item) ? 'active' : '' }">
													${item.label }
											</g:link>
										</g:each>
									</div> <!-- div.list-group -->
								</div> <!-- div.card -->
							</g:else>
						</g:each>
						
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