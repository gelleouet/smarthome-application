<div class="row mt-4">
	<div class="col-4">
		<div class="chart">
			<canvas id="chart-test"></canvas>
		</div>
		<ul class="list-group list-group-flush">
		  <li class="list-group-item">Référence : -kWh</li>
		  <li class="list-group-item">Action : -kWh</li>
		  <li class="list-group-item">Différence : -kWh</li>
		</ul>
	</div>
	<div class="col-8">
	
	</div>
</div>


<asset:script type="text/javascript">
	new Chart(document.getElementById("chart-test"), {
				type: "bar",
				data: {
					labels: ["Référence", "Action"],
					datasets: [{
						label: "Référence",
						backgroundColor: window.theme.primary,
						borderColor: window.theme.primary,
						hoverBackgroundColor: window.theme.primary,
						hoverBorderColor: window.theme.primary,
						data: [200]
					}, {
						label: "Action",
						backgroundColor: "#E8EAED",
						borderColor: "#E8EAED",
						hoverBackgroundColor: "#E8EAED",
						hoverBorderColor: "#E8EAED",
						data: [150]
					}]
				},
				options: {
					maintainAspectRatio: false,
					legend: {
						display: false
					},
					scales: {
						yAxes: [{
							gridLines: {
								display: false
							},
							stacked: false,
							ticks: {
								stepSize: 20
							}
						}],
						xAxes: [{
							barPercentage: .75,
							categoryPercentage: .5,
							stacked: false,
							gridLines: {
								color: "transparent"
							}
						}]
					}
				}
			});
</asset:script>