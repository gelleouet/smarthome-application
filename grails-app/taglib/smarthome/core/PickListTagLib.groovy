package smarthome.core

class PickListTagLib {
   static namespace = "app"
   
   /**
    * Transforme un multi select en un composant picklist
    * 
    * @attr selectId REQUIRED l'id du select
    * @attr options REQUIRED une liste d'objets pour le contenu du select
    * @attr idField REQUIRED le champ contenant l'ID à utiliser pour le select
    * @attr labelField REQUIRED le champ contenant le label à utiliser pour le select
    * @attr selectedOptions REQUIRED une liste d'objets déjà sélectionnés
    * 
    */
   def picklist = { attrs, body ->
	   out << render(template: '/templates/picklist', model: attrs)
   }
}
