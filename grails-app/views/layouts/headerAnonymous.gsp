<header id="header" role="banner">
<!-- App Header goes inside #header -->

  <nav class="aui-header aui-dropdown2-trigger-group" role="navigation">
  	  <div class="aui-header-primary desktop-only">
  	  	 <div class="aui-header-logo logoParentHeader" >
  	  		<a href="">
	        	<span class="logoTextHeader"><g:meta name="app.code"/></span>
        	</a>
      	 </div>	
      </div>
      
      <div class="aui-header-secondary">
          <ul class="aui-nav">
              <li>
              	<g:link controller="site" action="decouvrir" class="aui-button aui-button-primary">Découvrir</g:link>
              </li>
              <li>
              	<g:form controller="register" action="account">
              		<button class="aui-button aui-button-primary">Créer un compte</button>
              	</g:form>
              </li>
          </ul>
      </div>
  </nav>

<!-- App Header goes inside #header -->
</header>