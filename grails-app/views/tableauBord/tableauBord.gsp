<html>
<head>
<meta name='layout' content='main' />
</head>

<body onload="onLoadTableauBord()">

	<g:applyLayout name="page-default">
	
		<div class="aui-toolbar2">
		    <div class="aui-toolbar2-inner">
		        <div class="aui-toolbar2-primary">
		            <div>
		               <h2>Tableau de bord</h2>
		            </div>		            
		        </div>
		        <!--  <div class="aui-toolbar2-secondary">
		            <div class="aui-buttons">
		            	<a id="layout-1-col-button" class="aui-button aui-button-subtle" title="1 colonne" data-layout="layout-1-col" data-layout-for="tableau-bord-widget-container">
		            		<span class="aui-icon aui-icon-small aui-iconfont-layout-1col-large"></span>
		            	</a>
		            	<a id="layout-2-col-button" class="aui-button aui-button-subtle" title="2 colonnes" data-layout="layout-2-col" data-layout-for="tableau-bord-widget-container">
		            		<span class="aui-icon aui-icon-small aui-iconfont-layout-2col-large"></span>
		            	</a>
		            	<a id="layout-2-col-menu-button" class="aui-button aui-button-subtle" title="2 colonnes menu" data-layout="layout-2-col-menu" data-layout-for="tableau-bord-widget-container">
		            		<span class="aui-icon aui-icon-small aui-iconfont-layout-2col-right-large"></span>
		            	</a>
		            </div>
		            
		            <div class="aui-buttons">
		            	<g:remoteLink class="aui-button aui-button-subtle" url="[controller: 'widget', action: 'dialogAddWidget']" update="ajaxDialog"
		            			onSuccess="showDialog('add-widget-dialog')">
		            		<span class="aui-icon aui-icon-small aui-iconfont-add"></span> Ajouter widget
		            	</g:remoteLink>
		            </div>-->
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>
	
		<g:set var="layout" value="${ app.stateDataUser(name: 'layout-css', page: 'tableauBord.index') ?: 'layout-1-col' }"/>
	
		<!--  
		<div id="tableau-bord-widget-container" data-state-page="tableauBord.index" data-state-name="layout-css" class="widget-container ${ layout }" data-immediate="true"
				data-url-move="${ g.createLink(controller: 'widget', action: 'moveWidgetUser') }">
			<g:if test="${ layout?.startsWith('layout-1-col') }">
				<g:render template="/widget/layout/1collayout"/>
			</g:if>
			<g:elseif test="${ layout?.startsWith('layout-2-col') }">
				<g:render template="/widget/layout/2collayout"/>
			</g:elseif>
		</div>-->
	</g:applyLayout>
	
</body>
</html>