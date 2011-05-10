/*
 Main class for the plugin.
 Normally plugin users should not need to change anything here as most of
 the configurations are done in Config.groovy of users' Grails applications;
 However, it is technically possible to change the code here, especially in 
 'doWithWebDescriptor' to reflect special requirements. But it is advisable
 not to change anything here as configurable options of the plugin provide 
 flexible support for different parameter settings for client integration by 
 the library supplied here and it may be worth considering to have another
 plugin if the requirement is very different from what it is expected to 
 achieve from this plugin.
 */

import grails.util.GrailsUtil
import grails.util.Environment
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class CasClientGrailsPlugin {
	def version = "3.10.01"
	def dependsOn = [:]
	/*def pluginExcludes = [
		"grails-app/views/error.gsp"
		"grails-app/views/index.gsp"
	]*/
	def author = 'Chen Wang'
	def authorEmail = 'contact@chenwang.org'
	def title = 'This plugin provides client integration for JA-SIG CAS'
	def description = '''
	The plugin handles configurations of JA-SIG CAS client integration using 
	its Java client library versioned 2.1.1 with some added features. 
	Please note there is another Java client library which is heavily Spring 
	based; Although it seems a natual fit for Grails applications, it requires 
	more configuration works to get started.
	
	The client integration page of the library supported by this plugin is
	
	http://grails.org/CAS+Client+Plugin
	
	Please make sure necessary configurations are made in your Grails 
	application's Config.groovy file. 
	'''
	def documentation = 'http://grails.org/CAS+Client+Plugin'

	def doWithSpring = {
		// nothing to do with spring here.
	}
	def doWithApplicationContext = { applicationContext ->
		// nothing to do with application context here.
	}
	def doWithWebDescriptor = { xml ->
		println('====== started adding JA-SIG CAS client support')
		println('====== cas.loginUrl ['+ ConfigurationHolder.config.cas.loginUrl + ']')
		println('====== cas.logOutUrl['+ ConfigurationHolder.config.cas.logOutUrl+ ']')
		println('====== cas.serverName['+ ConfigurationHolder.config.cas.serverName+ ']')
		println('====== cas.renew['+ ConfigurationHolder.config.cas.renew+ ']')
		println('====== saml.casServerUrlPrefix['+ ConfigurationHolder.config.saml.casServerUrlPrefix+ ']')
		println('====== saml.serverName['+ ConfigurationHolder.config.saml.serverName+ ']')
		println('====== cas.disabled[' + ConfigurationHolder.config.cas.disabled + ']')
		println('====== cas.disabled? [' + (ConfigurationHolder.config.cas.disabled == true) + ']')
		println('====== cas.mocking[' + ConfigurationHolder.config.cas.mocking+ ']')
				
		if (ConfigurationHolder.config.cas.disabled) {
			println('====== cas.disabled es verdad  [' + ConfigurationHolder.config.cas.disabled + ']')
		}else {
			println('====== cas.disabled es mentira [' + ConfigurationHolder.config.cas.disabled + ']')
		}

		if (ConfigurationHolder.config.cas.disabled) {
			println('CAS CLIENT PLUGIN INFO: the plugin is disabled Just adding mockRemoteUserFilter.')			
			if (Environment.PRODUCTION != Environment.current) {
				//Adding mockRemoteUserFilter
				def filters = xml.'filter'
				filters[0] + {
					'filter' {
						'filter-name' ('mockRemoteUserFilter')
						'filter-class' ('casClient.MockRemoteUserFilter')
					}
				}
				def filtermappings = xml.'filter-mapping'

				filtermappings[0] + {
					'filter-mapping' {
						'filter-name' ('mockRemoteUserFilter')
						'url-pattern' ("${ConfigurationHolder.config.cas.urlPattern}")
					}
				}
			}
		} 	
		else {	
			println('CAS CLIENT PLUGIN INFO: the plugin is ENABLED')
			def failed = false
			// to check configurations for filter and its mapping.
			if (ConfigurationHolder.config.cas.loginUrl instanceof ConfigObject
			|| ConfigurationHolder.config.cas.urlPattern instanceof ConfigObject) {
				log.error('CAS CLIENT PLUGIN ERROR: Please make sure that required parameters [cas.loginUrl, cas.validateUrl, cas.urlPattern] are set up correctly in Config.groovy of your application!')
				failed = true
			}
			else if (ConfigurationHolder.config.cas.serverName instanceof ConfigObject && ConfigurationHolder.config.cas.serviceUrl instanceof ConfigObject) {
				log.error('CAS CLIENT PLUGIN ERROR: Please make sure that one of required parameters [cas.serverName, cas.serviceUrl] is set up correctly in Config.groovy of your application!')
				failed = true
			}
			else {
				// to define name of the filter.
				def fname = 'CAS-Filter'

				// to add cas filter.
				def filters = xml.'filter'
				
				def logOutFilter = 'CAS Single Sign Out Filter'				
				
				filters[0] + {
					'filter' {
						'filter-name' (logOutFilter)
						'filter-class' ('org.jasig.cas.client.session.SingleSignOutFilter')						
					}
				}

				filters[0] + {
					'filter' {
						'filter-name' (fname)
						'filter-class' ('org.jasig.cas.client.authentication.AuthenticationFilter')
						'init-param' {
							'param-name' ('casServerLoginUrl')
							'param-value' ("${ConfigurationHolder.config.cas.loginUrl}")
						}

						if (ConfigurationHolder.config.cas.serverName instanceof String) {							
							'init-param' {
								//'param-name' ('org.jasig.cas.client.authentication.serverName')
								'param-name' ('serverName')
								'param-value' ("${ConfigurationHolder.config.cas.serverName}")
							}
						}
						else if (ConfigurationHolder.config.cas.serviceUrl instanceof String) {
							'init-param' {
								'param-name' ('serviceUrl')
								'param-value' ("${ConfigurationHolder.config.cas.serviceUrl}")
							}
						}

						if (ConfigurationHolder.config.cas.renew instanceof Boolean) {
							'init-param' {
								'param-name' ('renew')
								'param-value' ("${ConfigurationHolder.config.cas.renew}")
							}
						}
					}
				}

				if (!(ConfigurationHolder.config.saml.casServerUrlPrefix instanceof ConfigObject) && ConfigurationHolder.config.saml.casServerUrlPrefix)
				{
					println "Metemos el Saml11TicketValidationFilter "
					// Adding samlFilter
					filters[0] + {
						'filter' {
							'filter-name' ('Saml11TicketValidationFilter')
							'filter-class' ('org.jasig.cas.client.validation.Saml11TicketValidationFilter')
							'init-param' {
								'param-name' ('casServerUrlPrefix')
								'param-value' ("${ConfigurationHolder.config.saml.casServerUrlPrefix}")
							}
							'init-param' {
								'param-name' ('serverName')
								'param-value' ("${ConfigurationHolder.config.saml.serverName}")
							}
							'init-param' {
								'param-name' ('redirectAfterValidation')
								'param-value' ('false')
							}
						}
					}					
				}
				
				// Adding Wrapper Filter
				filters[0] + {
					'filter' {
						'filter-name' ('CAS HttpServletRequest Wrapper Filter')
						'filter-class' ('org.jasig.cas.client.util.HttpServletRequestWrapperFilter')
					}
				}
				
				// Adding AssertionThreadLocalFilter
				/* Este hace falta???
				filters[0] + {
					'filter' {
						'filter-name' ('CAS Assertion Thread Local Filter')
						'filter-class' ('org.jasig.cas.client.util.AssertionThreadLocalFilter')
					}
				}				*/
				
				log.info('CAS CLIENT PLUGIN INFO: added <filter/> section in web.xml')

				// to add cas filter mapping.
				def filtermappings = xml.'filter-mapping'

				if (ConfigurationHolder.config.cas.urlPattern instanceof String) {

					
					filtermappings[0] + {
						'filter-mapping' {
							'filter-name' (logOutFilter)
							'url-pattern' ("${ConfigurationHolder.config.cas.urlPattern}")
						}
					}
					
					filtermappings[0] + {
						'filter-mapping' {
							'filter-name' (fname)
							'url-pattern' ("${ConfigurationHolder.config.cas.urlPattern}")
						}						
					}
					log.info('CAS CLIENT PLUGIN INFO: added <filter-mapping/> section(s) in web.xml')
				}
				else if (ConfigurationHolder.config.cas.urlPattern instanceof java.util.List) {

					ConfigurationHolder.config.cas.urlPattern.each { u ->
						filtermappings[0] + {
							'filter-mapping' {
								'filter-name' (fname)
								'url-pattern' ("${u}")
							}
						}
					}
					log.info('CAS CLIENT PLUGIN INFO: added <filter-mapping/> section(s) in web.xml')
				}
				else {
					log.error('CAS CLIENT PLUGIN ERROR: Please make sure that required parameter [cas.urlPattern] is an instance of either java.lang.String or java.util.List in Config.groovy of your application!')
					//System.exit(1)
					failed = true
				}								
				
				if (!(ConfigurationHolder.config.saml.casServerUrlPrefix instanceof ConfigObject) && ConfigurationHolder.config.saml.casServerUrlPrefix)
				{
					filtermappings[0] + {
						'filter-mapping' {
							'filter-name' ('Saml11TicketValidationFilter')
							'url-pattern' ("${ConfigurationHolder.config.cas.urlPattern}")
						}
					}
				}
				
				filtermappings[0] + {
					'filter-mapping' {
						'filter-name' ('CAS HttpServletRequest Wrapper Filter')
						'url-pattern' ("${ConfigurationHolder.config.cas.urlPattern}")
					}
				}
				
			}

			if (failed) {
				log.info("PLEASE CORRECT THE ERROR ABOVE!")
			}
		}

		if (ConfigurationHolder.config.cas.mocking) {
			log.info('CAS CLIENT PLUGIN INFO: /cas.gsp?u=USERNAME is available for mocking cas-ified user session')
			log.info('CAS CLIENT PLUGIN WARNING: Please take extra care as mocking should NOT be allowed for production environment!')
		}

		//		log.info('====== finished adding JA-SIG CAS client support')
	}
	def doWithDynamicMethods = { ctx ->
		// nothing to do with dynamic methods here.
	}
	def onChange = { event ->
		// no interests in dynamic loading.
	}
	def onApplicationChange = { event ->
		// no interests in dynamic loading.
	}
}
