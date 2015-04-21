<g:if test="${ datasTemplate }">
	<g:render template="${ datasTemplate }"/>
</g:if>
<g:else>
	<g:render template="/templates/chartDatas"/>
</g:else>


<asset:script type="text/javascript">
	//Load the Visualization API and the piechart package.
	google.load("visualization", "1.0", {packages:["corechart"]});
    google.setOnLoadCallback(drawChart);
    
    function drawChart() {
    	var chartDatas = loadChartDatas();
      	var chartOptions = loadChartOptions();
      	var chart = null;
      	
      	<g:if test="${ chartType == 'scatter' }">
      		chart = new google.visualization.ScatterChart(document.getElementById('chartDiv'));
      	</g:if>
      	<g:else>
      		chart = new google.visualization.LineChart(document.getElementById('chartDiv'));
      	</g:else>
      	 
      	chart.draw(chartDatas, chartOptions);
    }
</asset:script>