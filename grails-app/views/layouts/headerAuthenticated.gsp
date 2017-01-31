<header id="header" role="banner">
<!-- App Header goes inside #header -->

  <nav class="aui-header aui-dropdown2-trigger-group" role="navigation" >
  	<div class="aui-header-inner">
  		
  	  <div class="aui-header-before">
  	  	<g:if test="${ mobileAgent }">
	  	  	<ul class="aui-nav">
	  	  		<li><a href="#dropdown2-app-switch" aria-owns="dropdown2-app-switch" aria-haspopup="true" class="aui-dropdown2-trigger aui-dropdown2-trigger-arrowless" >
	  	  			<span class="aui-icon aui-icon-small aui-iconfont-appswitcher"></span></a>
	  	  			<div class="aui-dropdown2 aui-style-default aui-dropdown2-in-header" id="dropdown2-app-switch" aria-hidden="true">
	                    <div class="aui-dropdown2-section">
	                        <ul>
	                        	<g:render template="/templates/dropDownItem" model="[items: app.navigationItems(category: 'navbarPrimary')?.subitems, category: 'navbarPrimary' ]"/>
	                        </ul>
	                    </div>
	                    <div class="aui-dropdown2-section">
	                        <ul>
	                        	<g:render template="/templates/dropDownItem" model="[items: app.navigationItems(category: 'configuration')?.subitems, category: 'configuration' ]"/>
	                        </ul>
	                    </div>
	                </div>
	  	  		</li>      	 	
	      	 </ul>
      	 </g:if>
  	  </div>
  	
  	
      <div class="aui-header-primary">
      
          <div class="aui-header-logo logoParentHeader">
  	  		<g:link uri="/">
  	  			<asset:image src="home_w.png"/>
	        	<span class="logoTextHeader "><g:meta name="app.code"/></span>
        	</g:link>
      	 </div>	
      	 
      	 <ul class="aui-nav">
      	 	<g:if test="${ !mobileAgent }">
	      	 	<g:render template="/templates/menuDropDownItem" model="[items: app.navigationItems(category: 'navbarPrimary')?.subitems, category: 'navbarPrimary' ]"/>
	      	 </g:if>
      	 </ul>
      	 
      </div>
      
      
      <div class="aui-header-secondary">
          <ul class="aui-nav">
              <li class="hidden">
                  <g:form  class="aui-quicksearch">
                      <label for="numeroEchantillon" class="assistive">Search</label>
                      <input class="search" type="text" placeholder="Recherche rapide" name="numeroEchantillon">
                  </g:form>
              </li>
              
              
              <!-- Menu aide (statique) -->
              <li class="desktop-only">
              	<a href="#dropdown2-aide" aria-owns="dropdown2-aide" aria-haspopup="true" class="aui-dropdown2-trigger" ><span class="aui-icon aui-icon-small aui-iconfont-help">Help</span><span class="aui-icon-dropdown"></span></a>
                <div class="aui-dropdown2 aui-style-default aui-dropdown2-in-header" id="dropdown2-aide" style="display: none; top: 40px; min-width: 160px; left: 1213px; " aria-hidden="true">
                    <div class="aui-dropdown2-section">
                        <ul>
                            <li><a href="https://connectmyhomeblog.wordpress.com/" target="blank">Blog Smarthome</a></li>
                            <li><a href="https://connectmyhomeblog.wordpress.com/a-propos/" target="blank">Découvrir l'application</a></li>
                            <li><a href="https://github.com/gelleouet/smarthome-raspberry/wiki" target="blank">Installation d'un agent Raspberry</a></li>
                            <li><a href="https://github.com/gelleouet/smarthome-application" target="blank">Dépôt Github</a></li>
                        </ul>
                    </div>
                    <div class="aui-dropdown2-section">
                        <ul>
                            <li><a href=""><g:meta name="app.code"/> version <g:meta name="app.version"/></a></li>
                        </ul>
                    </div>
                </div>
              </li>
              
              
              <!-- Menu user (dynamique) -->
              <li><a href="#dropdown2-header9" aria-owns="dropdown2-header9" aria-haspopup="true" class="aui-dropdown2-trigger" >
                      <div class="aui-avatar aui-avatar-small">
                          <div class="aui-avatar-inner">
                              <!-- <asset:image src="useravatar.png" />  -->
	                           	<div class="vignette-user">
	                           		<g:set var="loggedInUser" value="${ smarthome.security.User.read(sec.loggedInUserInfo(field: 'id')) }"/>
	                           		<span>${ loggedInUser.initiale }</span>
	                           	</div>
                          </div>
                      </div>
                  </a>
                <div class="aui-dropdown2 aui-style-default aui-dropdown2-in-header" id="dropdown2-header9" style="display: none; top: 40px; min-width: 160px; left: 1213px;" aria-hidden="true">
                    
                    <div class="aui-dropdown2-section">
                    	<div class="aui-dropdown2-heading">
							<sec:username/>
						</div>
                    	<ul>
		                    <li>
		                    	<g:link controller="user" action="profil">Paramètres</g:link>
		                    </li>
                    	</ul>
                    </div>
                    
                    <sec:ifSwitched>
                    	<div class="aui-dropdown2-section">
	                        <ul>
	                            <li><g:link action="exitSwitchUser" controller="user">Revenir à votre session</g:link></li>
	                        </ul>
	                    </div>
					</sec:ifSwitched>
                    
                    <div class="aui-dropdown2-section">
                        <ul>
                            <li><g:link controller="logout">Déconnexion</g:link></li>
                        </ul>
                    </div>
                </div>
              </li>
          </ul>
      </div>
  	 </div>
  </nav>

<!-- App Header goes inside #header -->
</header>