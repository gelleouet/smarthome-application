<header id="header" role="banner">
<!-- App Header goes inside #header -->

  <nav class="aui-header aui-dropdown2-trigger-group" role="navigation" >
  	<div class="aui-header-inner">
  		
  	  <div class="aui-header-before">
  	  	<ul class="aui-nav">
  	  		<li><a href="#dropdown2-app-switch" aria-owns="dropdown2-app-switch" aria-haspopup="true" class="aui-dropdown2-trigger aui-dropdown2-trigger-arrowless" >
  	  			<span class="aui-icon aui-icon-small aui-iconfont-appswitcher">Mes applications</span></a>
  	  			<div class="aui-dropdown2 aui-style-default aui-dropdown2-in-header" id="dropdown2-app-switch" aria-hidden="true">
                    <div class="aui-dropdown2-section">
                        <ul>
                        	
                        </ul>
                    </div>
                </div>
  	  		</li>      	 	
      	 </ul>
  	  </div>
  	
  	
      <div class="aui-header-primary">
      
          <div class="aui-header-logo logoParentHeader">
  	  		<g:link uri="/">
	        	<span class="logoTextHeader"><g:meta name="app.code"/></span>
        	</g:link>
      	 </div>	
      	 
      	 <ul class="aui-nav">
      	 	<g:render template="/templates/menuDropDownItem" model="[items: grailsApplication.navigationItems['navbarPrimary']?.subitems, category: 'navbarPrimary' ]"/>
      	 </ul>
          
      </div>
      
      
      <div class="aui-header-secondary">
          <ul class="aui-nav">
              <li>
                  <g:form  class="aui-quicksearch">
                      <label for="numeroEchantillon" class="assistive">Search</label>
                      <input class="search" type="text" placeholder="Recherche rapide" name="numeroEchantillon">
                  </g:form>
              </li>
              
              
              <!-- Menu aide (statique) -->
              <li><a href="#dropdown2-aide" aria-owns="dropdown2-aide" aria-haspopup="true" class="aui-dropdown2-trigger" ><span class="aui-icon aui-icon-small aui-iconfont-help">Help</span><span class="aui-icon-dropdown"></span></a>
                <div class="aui-dropdown2 aui-style-default aui-dropdown2-in-header" id="dropdown2-aide" style="display: none; top: 40px; min-width: 160px; left: 1213px; " aria-hidden="true">
                    <div class="aui-dropdown2-section">
                        <ul>
                            <li><a href=""><g:meta name="app.code"/> version <g:meta name="app.version"/></a></li>
                        </ul>
                    </div>
                </div>
              </li>
              
              
              <!-- Menu configuration (dynamique) -->
              <li><a href="#dropdown2-header8" aria-owns="dropdown2-header8" aria-haspopup="true" class="aui-dropdown2-trigger" ><span class="aui-icon aui-icon-small aui-iconfont-configure">Configuration</span><span class="aui-icon-dropdown"></span></a>
                <div class="aui-dropdown2 aui-style-default aui-dropdown2-in-header" id="dropdown2-header8" style="display: none; top: 40px; min-width: 160px; left: 1213px; " aria-hidden="true">
                    
                    <g:render template="/templates/dropDownItemDefaultLevel2" model="[items: grailsApplication.navigationItems['configuration']?.subitems ]"/>
                    
                </div>
              </li>
              
              
              <!-- Menu user (dynamique) -->
              <li><a href="#dropdown2-header9" aria-owns="dropdown2-header9" aria-haspopup="true" class="aui-dropdown2-trigger" >
                      <div class="aui-avatar aui-avatar-small">
                          <div class="aui-avatar-inner">
                              <asset:image src="useravatar.png" width="32px"/>
                          </div>
                      </div>
                  </a>
                <div class="aui-dropdown2 aui-style-default aui-dropdown2-in-header" id="dropdown2-header9" style="display: none; top: 40px; min-width: 160px; left: 1213px;" aria-hidden="true">
                    
                    <g:render template="/templates/dropDownItem" model="[items: grailsApplication.navigationItems['user']?.subitems ]"/>
                    
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