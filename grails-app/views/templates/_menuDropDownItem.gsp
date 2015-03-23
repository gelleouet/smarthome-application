<!-- Affichage d'abord des menus avec sous menus -->
<g:each var="menu" in="${items?.findAll({it.subitems.size() > 0})?.sort({ it.label }) }" status="statut">

	<li>
		<a href="#dropdown2-${ category }-${statut}" aria-owns="dropdown2-${ category }-${statut}" aria-haspopup="true" class="aui-dropdown2-trigger" >
	        ${ menu.label }<span class="aui-icon-dropdown"></span>
		</a>
	  	<div class="aui-dropdown2 aui-style-default aui-dropdown2-in-header" id="dropdown2-${ category }-${statut}" style="display: none; top: 40px; min-width: 160px; left: 1213px;" aria-hidden="true">
	      
	    	<g:render template="/templates/dropDownItem" model="[items: menu.subitems ]"/>
	      
		</div>
	</li>

</g:each>

<!-- Affichage ensuite des menus simples (bouton avec style primary -->
<g:each var="menu" in="${items?.findAll({it.subitems.size() == 0})?.sort({ it.label }) }" status="statut">
	<li>
		<g:link class="aui-button aui-button-primary" controller="${ menu.controller }" action="${ menu.action }">${ menu.label }</g:link>
	</li>
</g:each>
