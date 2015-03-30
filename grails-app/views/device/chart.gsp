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
			<g:hiddenField name="chartType" value="${ chartType }"/>
		
			<div class="aui-toolbar2">
			    <div class="aui-toolbar2-inner">
			        <div class="aui-toolbar2-primary">
			        	<div class="aui-buttons">
			        		<g:each var="timeAgo" in="${ timesAgo }">
			        			<g:actionSubmit value="${ timeAgo.value }" class="aui-button ${ sinceHour == timeAgo.key ? 'aui-button-primary' : '' }" action="chart" onclick="jQuery('#sinceHour').val('${ timeAgo.key }')"/>
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
			
			
			<div id="chartDiv">
				<br/>
				<h6>Loading chart...</h6>
				<div class="aui-progress-indicator">
				    <span class="aui-progress-indicator-value"></span>
				</div>
			</div>
		</form>
	</g:applyLayout>
	
	
	<asset:script type="text/javascript">
		//Load the Visualization API and the piechart package.
		google.load("visualization", "1.0", {packages:["corechart"]});
	    google.setOnLoadCallback(drawChart);
	    
	    function drawChart() {
	    	var data = google.visualization.arrayToDataTable([
	    		[{label: 'Date', type: 'datetime'},
	    		 {label: '${device.label}', type: 'number'}],
	    		<g:each var="data" in="${datas}">
	    			[new Date(<g:formatDate date="${data.dateValue}" format="yyyy,M,d,H,m,s,0"/>),  ${data.value}],
	    		</g:each>
	    	]);
	
	    	var format = new google.visualization.NumberFormat({'pattern': '#.#'});
	    	format.format(data, 1);
	    	
	      	var options = {
	    		  'title': '${device.label }',
	    		  'pointSize': '5',
	    	      'hAxis': {'title': 'Date'},
	    	      'vAxis': {'title': 'Valeurs'},
	    	      'width': '100%',
	              'height': '600',
	              'curveType': 'function'
	      	};
	
	      	var chart
	      	
	      	<g:if test="${ chartType == 'scatter' }">
	      		chart = new google.visualization.ScatterChart(document.getElementById('chartDiv'));
	      	</g:if>
	      	<g:else>
	      		chart = new google.visualization.LineChart(document.getElementById('chartDiv'));
	      	</g:else>
	      	 
	      	chart.draw(data, options);
	    }
	</asset:script>
</body>
</html>