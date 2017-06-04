<html>
<head>
<meta name='layout' content='authenticated' />
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
</head>

<body onload="onLoadChart();">

	<nav class="aui-navgroup aui-navgroup-horizontal">
	    <div class="aui-navgroup-inner">
	        <div class="aui-navgroup-primary">
	            <ul class="aui-nav">
	                <g:each var="groupe" in="${ groupes }">
						<li class="${ command.groupe == groupe ? 'aui-nav-selected': '' }">
							<g:link action="chartsGrid" params="[groupe: groupe]">${ groupe }</g:link>
						</li>	                
	                </g:each>
	            </ul>
	        </div><!-- .aui-navgroup-primary -->
	    </div><!-- .aui-navgroup-inner -->
	</nav>

	<g:applyLayout name="applicationContent" params="[panelContentClass: 'panelContentGrey']">
	
		<div class="aui-group aui-group-split">
	        <div class="aui-item">
	        	<g:form class="aui" name="navigation-chart-form" action="chartsGrid">
	        		<g:hiddenField name="groupe" value="${ command.groupe }"/>
	        		<g:render template="/chart/chartToolbar"/>
	            </g:form>
	        </div>
	        
	        <div class="aui-item">
	        	<g:form class="aui">
		            <div class="aui-buttons">
						<g:actionSubmit class="aui-button" value="Nouveau graphique" params="[groupe: command.groupe]" action="create"/>
		            </div>
	            </g:form>
	        </div>
		</div>
	
		<div>
			<div class="aui-group">
				<div class="aui-item responsive">
					<g:each var="chart" in="${ chartInstanceList.sort{ it.label } }" status="status">
						<g:if test="${ ! (status % 2) }">
							<div class="filActualite" style="padding:15px; margin-top:30px">
								<g:render template="chartWidget" model="[chart: chart]"/>
							</div>	
						</g:if>
					</g:each>
				</div>
				<div class="aui-item responsive">
					<g:each var="chart" in="${ chartInstanceList.sort{ it.label } }" status="status">
						<g:if test="${ (status % 2) }">
							<div class="filActualite" style="padding:15px; margin-top:30px">
								<g:render template="chartWidget" model="[chart: chart]"/>
							</div>	
						</g:if>
					</g:each>
				</div>
			</div>
		</div>
	
	</g:applyLayout>
	
	
	<asset:script type="text/javascript">
		google.load("visualization", "1.0", {packages:["corechart"]});
	</asset:script>
</body>
</html>