package smarthome.core.dsl

import org.codehaus.groovy.control.CompilerConfiguration;


/**
 * Classe à hériter pour tous les DSL métier
 * 
 * Permet d'exécuter les closure sur les objets DSL
 * 
 * @author gregory
 *
 */
class AbstractDSL {
	
	/**
	 * Exécute la closure sur le DSL
	 * 
	 * @param closure
	 * @param dsl
	 * @return
	 */
	def executeClosure(Closure closure) {
		closure.delegate = this
		closure.resolveStrategy = Closure.DELEGATE_FIRST
		closure()
	}
	
	
	
	/**
	 * Exécute le script DSL
	 * 
	 * @param dsl
	 * @return
	 */
	def runDSL(String dsl) {		
		Script dslScript = new GroovyShell(this.class.classLoader).parse(dsl)
		def dslObject = this
		
		// le script va s'exécuter sur l'objet dslScript.
		// il faut lui dire de déléguer l'appel de la méthode racine sur l'objet DSL en cours
		dslScript.metaClass.methodMissing = { name, args ->
			if (args.length == 1) {
				if (args[0] instanceof Closure) {
					dslObject.executeClosure(args[0])
				} else {
					throw new MissingMethodException(name, this.class, args as Object[])
				}
			} else {
				throw new MissingMethodException(name, this.class, args as Object[])
			}
		}
		
		dslScript.run()
		return this
	}


}
