grails.config.locations = [  "classpath:config.properties"]

// configuration for plugin testing - will not be included in the plugin zip
 
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}
	appenders {
		console name: 'stdout', layout: pattern(conversionPattern: '%d{dd-MM-yyyy HH:mm:ss,SSS} %5p %c{1} - %m%n')
	}

	debug stdout: 'grails.app'

	info 'org.jasig.cas.client'

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"

// Configuración del cliente Cas -> CasClientPlugin
cas {
	urlPattern = '/*'
	disabled = false
}

environments {
	development {				
		cas.loginUrl = 'https://dsedeelectronica.vitoria-gasteiz.org/cas/login'
		cas.logOutUrl = 'https://dsedeelectronica.vitoria-gasteiz.org/cas/logout'		
		cas.serverName = 'http://localhost:8080'
		cas.renew = false
		saml.casServerUrlPrefix = 'https://dsedeelectronica.vitoria-gasteiz.org/cas/'
		saml.serverName = 'http://localhost:8080'
		cas.disabled = true
		cas.mocking = true
	}
	test {
		cas.loginUrl = 'https://test.casserver.demo.com/cas/login'
		//cas.validateUrl = 'https://test.cas.com/cas/serviceValidate'
		cas.serverName = 'test.casclient.demo.com:80'
		// cas.serviceUrl = 'http://test.casclient.demo.com/access'
	}
	sede {
		cas.loginUrl = 'https://dsedeelectronica.vitoria-gasteiz.org/cas/login'
		cas.logOutUrl = 'https://dsedeelectronica.vitoria-gasteiz.org/cas/logout'		
		cas.serverName = 'http://localhost:8080'
		cas.renew = false
		saml.casServerUrlPrefix = 'https://dsedeelectronica.vitoria-gasteiz.org/cas/'
		saml.serverName = 'http://localhost:8080'
		cas.disabled = false
		cas.mocking = false
	}
	intra {			
		cas.loginUrl = 'https://dintranet2.vitoria-gasteiz.org/cas/login'
		cas.logOutUrl = 'https://dintranet2.vitoria-gasteiz.org/cas/logout'
		cas.serverName = 'https://dintranet2.vitoria-gasteiz.org/'
		cas.renew = false
		saml.casServerUrlPrefix = ''
		saml.serverName = ''
		cas.disabled = false
		cas.mocking = false 
	}
}
codenarc {
	reportName = 'target/test-reports/CodeNarcReport.xml'
	reportType = 'xml'
}
