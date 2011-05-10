package casclient


import grails.test.*
import grails.util.Environment
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication

class LogOutControllerTests extends ControllerUnitTestCase {	
	protected void setUp() {
		super.setUp()
		mockLogging(LogOutController)
		ApplicationHolder.setApplication(new DefaultGrailsApplication())
		DefaultGrailsApplication.metaClass.getApplicationMeta = {
			['app.name':'nombreAplicacion']
		}
	}

	protected void tearDown() {
		super.tearDown()
		Environment.metaClass.'static'.getCurrent = { Environment.TEST }
	}
	void testEnNoProduccionRedireccionamosAlIndice() {
		controller.index()
		log.info "redirect " + redirectArgs.url		
		assert "/nombreAplicacion" == redirectArgs.url
	}
	void testEnProduccionRedireccionamosAlCas() {
		mockLogging(LogOutController)
		mockConfig('''
			cas{
				logOutUrl = 'UrlLogOut'
		  	}
		''')
		Environment.metaClass.'static'.getCurrent = { Environment.PRODUCTION }		
		controller.index()		
		assert "UrlLogOut" == redirectArgs.url
	}	
	void testEnNoProduccionEliminamosElUsuario() {		
		controller.session?.setAttribute('casUser', 'usuario')
		controller.index()		
		assert null ==  controller.session?.getAttribute('usuario')
	}
}
