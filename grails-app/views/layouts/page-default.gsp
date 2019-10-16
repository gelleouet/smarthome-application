<div class="wrapper">
	<nav id="sidebar" class="sidebar">
		<div class="sidebar-content">
			<g:link class="sidebar-brand" uri="/">
				<app:icon name="home"/>
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
				<g:applyLayout name="content-error"/>
				<g:layoutBody/>
			</div>
		</main>
		
		<g:applyLayout name="footer"/>
	</div>
</div>