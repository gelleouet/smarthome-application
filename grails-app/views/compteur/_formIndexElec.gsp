<g:set var="lastIndex" value="${ compteur.lastIndex(new Date()) }"/>
<g:hiddenField name="lastIndex1" value="${ lastIndex?.value }"/>


<div class="saisie-compteur-elec" style="position: relative;">
	<div>
		<asset:image src="/compteur/dessin-compteur-elec.png"/>
	</div>
	
	<g:if test="${ compteur.isDoubleTarification() }">
		<g:set var="lastIndexHC" value="${ compteur.lastIndexHC(new Date()) }"/>
		<g:hiddenField name="lastIndex2" value="${ lastIndexHC?.value }"/>
		
		<div style="left:158px; top:243px; width:174px; height:26px; position: absolute;">
			<g:textField name="highindex1" value="${ command.highindex1 as Long }" class="form-control form-control index-elec" autofocus="true" required="true" style="height:28px"/>
		</div>
		<div style="left:332px; top:248px; width:50px; height:26px; position: absolute;">
			<span><strong>HP</strong></span>
		</div>
		<div style="left:158px; top:271px; width:174px; height:26px; position: absolute;">
			<g:textField name="highindex2" value="${ command.highindex2 as Long }" class="form-control form-control index-elec" required="true" style="height:26px"/>
		</div>
		<div style="left:332px; top:276px; width:50px; height:26px; position: absolute;">
			<span><strong>HC</strong></span>
		</div>
	</g:if>
	<g:else>
		<div style="left:158px; top:249px; width:174px; height:46px; position: absolute;">
			<g:textField name="highindex1" value="${ command.highindex1 as Long }" class="form-control form-control-lg index-elec" autofocus="true" required="true" style="height:46px"/>
			<small class="text-muted">Index base en kWh</small>
		</div>
	</g:else>
</div>


