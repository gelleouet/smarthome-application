package smarthome.core

class ComposantVueTagLib {
   static namespace = "app"
   
   /**
    * Renvoit la valeur d'une data d'un composant pour l'utilisateur connecté
    * 
    * @attr name REQUIRED le nom du composant
    * @attr dataName REQUIRED le nom de la classe
    * 
    */
   def stateDataUser = { attrs, body ->
	   // calcul de la page en fonction controller courant
	   def page = controllerName + "." + actionName
	    
	   def extraParams = [:]
	   extraParams.page = page
	   extraParams.name = attrs.name
	   extraParams.dataName = attrs.dataName
	   
	   out << include(controller: 'composantVue', action: 'getData', params: extraParams)
   }
   
   
   /**
    * Insère les attributs HTML pour la sauvegarde de l'état
    * 
    */
   def stateInsertAttr = { attrs, body ->
	   // calcul de la page en fonction controller courant
	   def page = controllerName + "." + actionName
	   out << " state-page=${page} "
	   
	   def url = createLink(controller: 'composantVue', action: 'saveData')
	   out << " state-url=${url} "
   }
}
