<html>
<head>
<meta name='layout' content='authenticated' />
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
</head>

<body>
	<g:applyLayout name="applicationContent">
	
		<h2>
			<asset:image src="${ device.icon() }" class="device-icon-grid"/>
			Graphique : ${ device.label }
		</h2>
	
		<form class="aui">
		
			<g:hiddenField id="sinceHour" name="sinceHour" value="${ sinceHour }"/>
		
			<div class="aui-toolbar2">
			    <div class="aui-toolbar2-inner">
			        <div class="aui-toolbar2-primary">
			        	<div class="aui-buttons">
			        		<g:each var="timeAgo" in="${ timesAgo }">
			        			<g:actionSubmit value="${ timeAgo.value }" class="aui-button ${ sinceHour == timeAgo.key ? 'aui-button-primary' : '' }" action="chartView" onclick="jQuery('#sinceHour').val('${ timeAgo.key }')"/>
			        		</g:each>
			            	
			            </div>
			        </div>
			        
			        <div class="aui-toolbar2-secondary">
			            <div id="button-set2" class="aui-buttons">
			                <button class="aui-button aui-dropdown2-trigger" aria-owns="dropdown2-view" aria-haspopup="true" aria-controls="dropdown2-view" data-container="#button-set2"><span class="aui-icon aui-icon-small aui-iconfont-view">View</span></button>
			            </div>
			        </div>
			    </div><!-- .aui-toolbar-inner -->
			</div>
			
			
			<div id="chartDiv" data-chart-type="${ chartType.factory }">
				<br/>
				<h6>Loading chart...</h6>
				<div class="aui-progress-indicator">
				    <span class="aui-progress-indicator-value"></span>
				</div>
				<g:render template="${ device.chartDataTemplate() ?: '/chart/chartDatas' }" model="[label: device.label, datas: datas]"/>
			</div>
		</form>
	</g:applyLayout>
	
	<asset:script type="text/javascript">
		google.load("visualization", "1.0", {packages:["corechart"]});
		google.setOnLoadCallback(buildGoogleCharts);
	</asset:script>
	
</body>
</html>