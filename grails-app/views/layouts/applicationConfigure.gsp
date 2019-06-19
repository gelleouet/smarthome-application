<g:applyLayout name="applicationHeader">
	<div class="aui-page-header-image">
		<div class="aui-avatar aui-avatar-medium">
			<div class="aui-avatar-inner">
				<span class="aui-icon aui-icon-large aui-iconfont-configure"></span>
			</div>
		</div>
	</div>
	<div class="aui-page-header-main">
		<h3>Paramètres</h3>
	</div>
</g:applyLayout>



<div class="aui-sidebar" data-aui-responsive="true">
	<div class="aui-sidebar-wrapper">
		<div class="aui-sidebar-body">
		    <nav class="aui-navgroup aui-navgroup-vertical">
		        <div class="aui-navgroup-inner">
		        	<g:render template="/templates/navItem" model="[items: app.navigationItems(category: 'configuration')?.subitems ]"/>
		        </div>
		    </nav>
	    </div>
	    <div class="aui-sidebar-footer">
            <a class="aui-button aui-button-subtle aui-sidebar-toggle aui-sidebar-footer-tipsy">
                <span class="aui-icon aui-icon-small"></span>
            </a>
        </div>
    </div>
</div>


<div class="aui-page-panel">
	<div class="aui-page-panel-inner">
		<section class="aui-page-panel-content" >
			<g:applyLayout name="applicationError"/>
			<g:layoutBody/>
		</section>
	</div>
</div>