
//
// This script is executed by Grails after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'Ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
// Ant.mkdir(dir:"/home/chen/projects/CAS/projects/CasClient/grails-app/jobs")
//

Ant.property(environment:"env")
grailsHome = Ant.antProject.properties."env.GRAILS_HOME"

// to add cas.gsp in order to mock cas-ified user session.
Ant.copy(file: "${pluginBasedir}/web-app/cas.gsp", 
	todir: "${basedir}/web-app")
Ant.echo(message: 'CAS CLIENT PLUGIN INFO: cas.gsp has been created and can be accessed if mocking is turned on')
