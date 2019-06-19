<%@ page import="smarthome.core.DateUtils" %>


<h3>Investissement participatif</h3>

	
<div class="aui-group">
	<div class="aui-item responsive">
		<div style="margin-top:20px">
			<div class="separator-bottom">
				<div class="aui-group aui-group-split">
					<div class="aui-item" style="width:50%">
						<h4>Action</h4>
					</div>
					<div class="aui-item">
						<h4><span class="link">${ totalAction }</span></h4>
					</div>
				</div>
			</div>	
			
			<div class="synthese-content">
				
				<div class="container-img">
					<asset:image src="actions.png" style="height:75px" />
					<div class="content-img-center">
						<h3><span style="color:white; font-weight:bold;"></span></h3>
					</div>
				</div>
				
				<table class="aui datatable" style="margin-bottom:20px;">
					<thead>
						<tr>
							<th></th>
							<th>Ma part</th>
							<th>Total</th>
						</tr>
					</thead>
					<tbody>
						<g:each var="action" in="${ actions }">
							<tr>
								<td>${ action.producteur.libelle }</td>
								<td><span class="link">${ action.nbaction }</span></td>
								<td><span class="link">${ action.producteur.nbaction }</span></td>
							</tr>
						</g:each>
					</tbody>
				</table>
				
				<div style="text-align:right; font-weight:bold;">
					
				</div>
			</div>
		</div>
	</div>
	
	<div class="aui-item responsive">
		<div style="margin-top:20px">
			<div class="separator-bottom">
				<div class="aui-group aui-group-split">
					<div class="aui-item" style="width:50%">
						<h4>Investissement</h4>
					</div>
					<div class="aui-item">
						<h4><span class="link">${ totalInvestissement as Long }€</span></h4>
					</div>
				</div>
			</div>	
			
			<div class="synthese-content">
				
				<div class="container-img">
					<asset:image src="retour-investissement.png" style="height:75px" />
					<div class="content-img-center">
						<h3><span style="color:white; font-weight:bold;"></span></h3>
					</div>
				</div>
				
				<table class="aui datatable" style="margin-bottom:20px;">
					<thead>
						<tr>
							<th>Ma part</th>
							<th>Total</th>
						</tr>
					</thead>
					<tbody>
						<g:each var="action" in="${ actions }">
							<tr>
								<td><span class="link">${ action.investissement() as Long }€</span></td>
								<td><span class="link">${ action.producteur.investissement as Long }€</span></td>
							</tr>
						</g:each>
					</tbody>
				</table>
				
				<div style="text-align:right; font-weight:bold;">
					<g:link class="link" controller="producteurEnergieAction" action="investissementChart">
						<span class="aui-icon aui-icon-small aui-iconfont-arrows-right"></span> Voir le détail
					</g:link>
				</div>
			</div>
		</div>
	</div>
	
	<div class="aui-item responsive">
		<div style="margin-top:20px">
			<div class="separator-bottom">
				<div class="aui-group aui-group-split">
					<div class="aui-item" style="width:50%">
						<h4>Production</h4>
					</div>
					<div class="aui-item">
						<h4><span class="link">${ totalSurface?.round(1) }m² / ${ actions.sum{it.productionTotal()} as Long }kWh</span></h4>
					</div>
				</div>
			</div>	
			
			<div class="synthese-content">
				
				<div class="container-img">
					<asset:image src="panneaux-solaires.png" style="height:75px" />
					<div class="content-img-center">
						<h3><span style="color:white; font-weight:bold;"></span></h3>
					</div>
				</div>
				
				<table class="aui datatable" style="margin-bottom:20px;">
					<thead>
						<tr>
							<th>Ma part</th>
							<th>Total</th>
						</tr>
					</thead>
					<tbody>
						<g:each var="action" in="${ actions }">
							<tr>
								<td><span class="link">${ action.surface()?.round(1) }m² / ${ action.production()?.round(1) }kWh</span></td>
								<td>
									<g:if test="${ action.device }">
										<g:link class="link" controller="device" action="deviceChart" params="['device.id': action.device.id]">
											<span class="aui-icon aui-icon-small aui-iconfont-macro-gallery"></span> ${ action.producteur.surface as Long }m² / ${ action.productionTotal() as Long }kWh
										</g:link>
									</g:if>
									<g:else>
										<span class="link">
											${ action.producteur.surface as Long }m² / ${ action.productionTotal() as Long }kWh
										</span>
									</g:else>
								</td>
							</tr>
						</g:each>
					</tbody>
				</table>
				
				<div style="text-align:right; font-weight:bold;">
					<g:link class="link" controller="producteurEnergieAction" action="productionChart">
						<span class="aui-icon aui-icon-small aui-iconfont-arrows-right"></span> Voir le détail
					</g:link>
				</div>
			</div>
		</div>
	</div>
	
</div>
