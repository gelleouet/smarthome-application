<g:applyLayout name="applicationHeader">
	<div class="aui-page-header-image">
		<div class="aui-avatar aui-avatar-medium">
			<div class="aui-avatar-inner">
				<span class="aui-icon aui-icon-large aui-iconfont-configure">paramètres</span>
			</div>
		</div>
	</div>
	<div class="aui-page-header-main">
		<h3>Paramètres</h3>
	</div>
</g:applyLayout>



<div class="aui-page-panel">
	<div class="aui-page-panel-inner">
	
		<g:if test="${ !mobileAgent }">
			<div class="aui-page-panel-nav">
			    <nav class="aui-navgroup aui-navgroup-vertical">
			        <div class="aui-navgroup-inner">
			        	<g:render template="/templates/navItem" model="[items: app.navigationItems(category: 'configuration')?.subitems ]"/>
			        </div>
			    </nav>
			</div>
		</g:if>
	
		<section class="aui-page-panel-content" >
			<g:applyLayout name="applicationError"/>
			<g:layoutBody/>
		</section>
	</div>
</div>