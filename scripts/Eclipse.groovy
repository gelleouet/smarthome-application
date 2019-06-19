/**
 * Script Eclipse
 * 
 * Permet la création des fichiers .classpath et .project à partir du build Grails
 * 
 * Utile pour recréer les dépendances et les associations entre projet sur un IDE
 * dépourvu d'intégration avec Grails
 * 
 * Gère les dépendences compile, test, provided
 * Gère les projets inline (ie : grails.plugin.location = '.....')
 * Gère les sources vers les plugins
 * TODO : gérer les projets multi-modules pour parcourir chaque sous-projet depuis
 * le projet racine
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */

 
includeTargets << grailsScript("_GrailsClasspath")
 


target(default: "Configure project and build path in current project") {
	 println "Grails: $grailsHome"
	 
	 initProjectFile(projectCompiler, projectCompiler.buildSettings.baseDir)
	 initClasspathFile(projectCompiler, projectCompiler.buildSettings.baseDir)
}



/**
 * Création du fichier .classpath dans le dossier 'rootFile'
 * le projectCompiler compiler doit être initialisé sur le rootFile
 * dans le context d'un projet multi-module
 * 
 * @param projectCompiler
 * @param rootFile
 */
void initClasspathFile(projectCompiler, rootFile) {
	println "=> Creating .classpath file in ${rootFile}..."
	
	def rootPath = rootFile.toPath()
	
	new File(".classpath", rootFile).withWriter('UTF-8') { writer ->
		writer << """\
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER"/>
	<classpathentry kind="con" path="GROOVY_SUPPORT"/>
	<classpathentry exported="true" kind="con" path="GROOVY_DSL_SUPPORT"/>
	<classpathentry kind="output" path="target-eclipse"/>
"""
		// insère les dossiers sources. les sources sont relatives au projet
		// il faut calculer le path des sources en fonction du path du projet
		// exclusion des dossiers migrations et spring
		projectCompiler.srcDirectories.sort().each { srcDir ->
			if (!srcDir.endsWith('spring') && !srcDir.endsWith('migrations')) {
				def srcDirPath = new File(srcDir).toPath()
				def srcSubDirPath = rootPath.relativize(srcDirPath)
				writer << """\
	<classpathentry kind="src" path="${srcSubDirPath}"/>
"""
			}
		}
		
		// insère les projets inline
		// les noms des projets sont relatifs au workspace, donc on ne prend que
		// son nom final
		projectCompiler.pluginSettings.inlinePluginDirectories.sort().each { pluginResource ->
			def pluginFile = pluginResource.file
			writer << """\
	<classpathentry combineaccessrules="false" kind="src" path="/${pluginFile.name}"/>
"""
		}
		
		// insère les sources des plugins à partir du link source ".grails_plugins"
		// si le chemin du plugin ne correspond pas au dir base plugin, c'est que 
		// c'est un plugin inline et qu'il est géré autrement
		def pluginBaseDir = pluginBaseDirectory(projectCompiler, rootFile)
		def pluginBaseDirPath = pluginBaseDir.toPath()
		
		projectCompiler.pluginSettings.getPluginSourceDirectories().each { pluginResource ->
			File pluginFile = pluginResource.file
			def pluginFilePath = pluginFile.toPath()
			
			
			if (pluginFile.exists() && pluginFilePath.startsWith(pluginBaseDirPath)) {
				String pluginPath = pluginFile.absolutePath.replace(pluginBaseDir.absolutePath, '.grails_plugins')
			
				if (pluginPath.endsWith("grails-app/conf")) {
					writer << """\
	<classpathentry kind="src" path="${pluginPath}" excluding="BuidConfig.groovy|*DataSource.groovy|UrlMappings.groovy|Config.groovy|BootStrap.groovy|spring/resources.groovy"/>
"""
				} else {
					writer << """\
	<classpathentry kind="src" path="${pluginPath}"/>
"""
				}
			}
		}
		
		// insère les dépendences (compile + provided + test + runtime)
		// on passe par une map pour éviter les doublons de libs entre les scopes
		def libs = [:]
		
		projectCompiler.buildSettings.compileDependencies.sort().each { file ->
			libs[file.name] = file
		}
		projectCompiler.buildSettings.providedDependencies.sort().each { file ->
			libs[file.name] = file
		}
		projectCompiler.buildSettings.testDependencies.sort().each { file ->
			libs[file.name] = file
		}
		projectCompiler.buildSettings.runtimeDependencies.sort().each { file ->
			libs[file.name] = file
		}
		
		libs.each { name, file ->
			def sourceFile = new File(file.name.replace(".jar", "-sources.jar"), file.parentFile)
	
			writer << """\
	<classpathentry kind="lib" path="${file.getAbsolutePath()}" sourcepath="${ sourceFile.exists() ? sourceFile : ''}"/>
"""
		}
		
		writer << "</classpath>"
	}
	
}


/**
 * Création du fichier .project dans le dossier 'rootFile'
 * Par défaut, active les facets java et groovy
 * et ajoute un lien ".grails_plugin" vers les plugins du projet dézippés
 * ce lien est utilisé ensuite dans le classpath pour référencer les dossiers
 * sources de ces plugins
 * 
 * @param projectCompiler
 * @param rootFile
 */
void initProjectFile(projectCompiler, rootFile) {
	println "=> Creating .project file in ${rootFile}..."
	
	new File(".project", rootFile).withWriter('UTF-8') { writer ->
		writer << """\
<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
	<name>${rootFile.name}</name>
	<comment>${rootFile.name}</comment>
	<buildSpec>
		<buildCommand>
			<name>org.eclipse.jdt.core.javabuilder</name>
		</buildCommand>
	</buildSpec>
	<natures>
		<nature>org.eclipse.jdt.groovy.core.groovyNature</nature>
		<nature>org.eclipse.jdt.core.javanature</nature>
	</natures>
	<linkedResources>
		<link>
			<name>.grails_plugins</name>
			<type>2</type>
			<location>${pluginBaseDirectory(projectCompiler, rootFile).absolutePath.replace("\\", "/")}</location>
		</link>
	</linkedResources>
</projectDescription>
"""
	}
}


/**
 * Renvoit le chemin dans le contextu du projet vers les plugins dézippés
 * pour un accès aux sources
 * 
 * @param projectCompiler
 * @param rootFile
 * @return
 */
File pluginBaseDirectory(projectCompiler, rootFile) {
	new File("${rootFile}/target/work/plugins")
}
 
 
