<g:applyLayout name="applicationHeader">
	<div class="aui-page-header-image">
		<div class="aui-avatar aui-avatar-medium">
			<div class="aui-avatar-inner">
				<span class="aui-icon aui-icon-large aui-iconfont-configure">administration</span>
			</div>
		</div>
	</div>
	<div class="aui-page-header-main">
		<h3>
			<g:set var="itemRoot" value="${app.currentItem(item: app.navigationItems(category: 'configuration')) }"/>
			${ itemRoot ? itemRoot.label : 'Administration' }
		</h3>
	</div>
</g:applyLayout>



<nav class="aui-navgroup aui-navgroup-horizontal">
    <div class="aui-navgroup-inner">
        <div class="aui-navgroup-primary">
            <ul class="aui-nav">
            	
            	<g:set var="navItems" value="${app.navigationItems(category: 'configuration')?.subitems?.findAll({app.isChildCurrentItem(item: it)}) }"/>
            	<g:render template="/templates/navItemDefaultLevel2" model="[items: navItems]"/>
            	
            </ul>
        </div><!-- .aui-navgroup-primary -->
    </div><!-- .aui-navgroup-inner -->
</nav>


<div class="aui-page-panel">
	<div class="aui-page-panel-inner">
	
		<div class="aui-page-panel-nav desktop-only">
		    <nav class="aui-navgroup aui-navgroup-vertical">
		        <div class="aui-navgroup-inner">
		        	
		        	<!-- Le 1er niveau de configuration saute car classement application métier, application technique -->
		        	<g:each var="level1" in="${ app.navigationItems(category: 'configuration')?.subitems }">
		        		<!-- Le 2e niveau saute aussi car il est affichée sur la barre de navigation horizontale -->
		        		<g:each var="level2" in="${ level1.subitems.findAll({app.isChildCurrentItem(item: it)}) }">
		        			<g:render template="/templates/navItem" model="[items: level2.subitems ]"/>
		        		</g:each>
		        	</g:each>
		        	
		        </div>
		    </nav>
		</div>
	
		<section class="aui-page-panel-content" >
			<g:applyLayout name="applicationError"/>
			<g:layoutBody/>
		</section>
	</div>
</div>