<div class="wrapper">
	<g:applyLayout name="menu"/>
	
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