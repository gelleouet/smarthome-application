<html>
<head>
<meta name='layout' content='authenticated' />
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
</head>

<body>

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
	        	<g:form class="aui">
	        		<g:hiddenField name="timeAgo" value="${ command.timeAgo }"/>
		        	<div class="aui-buttons">
		        		<g:field type="date" name="dateChart" style="font-family: inherit; padding: 4px 5px;" class="aui-date-picker" value="${ app.formatPicker(date: command.dateChart) }" required="true"/>
		            </div>
		        	<div class="aui-buttons">
		        		<g:each var="timeAgo" in="${ timesAgo }">
		        			<g:actionSubmit value="${ timeAgo.value }" class="aui-button ${ command.timeAgo == timeAgo.key ? 'aui-button-primary' : '' }"/>
		        		</g:each>
		            </div>
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
	
		<div class="aui-group">
			<div class="aui-item responsive">
				<g:each var="chart" in="${ chartInstanceList }" status="status">
					<g:if test="${ ! (status % 2) }">
						<div class="filActualite" style="padding:15px;">
							<g:render template="chartPreview" model="[chart: chart]"/>
						</div>	
					</g:if>
				</g:each>
			</div>
			<div class="aui-item responsive">
				<g:each var="chart" in="${ chartInstanceList }" status="status">
					<g:if test="${ (status % 2) }">
						<div class="filActualite" style="padding:15px;">
							<g:render template="chartPreview" model="[chart: chart]"/>
						</div>	
					</g:if>
				</g:each>
			</div>
		</div>
	
	</g:applyLayout>
	
	
	<asset:script type="text/javascript">
		google.load("visualization", "1.0", {packages:["corechart"]});
		// ne pas appeler la méthode car déjà déclenché à la suite des appels ajax
		//google.setOnLoadCallback(buildGoogleCharts);
	</asset:script>
</body>
</html>