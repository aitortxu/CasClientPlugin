package casclient

import grails.util.Environment
import org.codehaus.groovy.grails.commons.ConfigurationHolder as GrailsConfig
import org.codehaus.groovy.grails.commons.ApplicationHolder


class LogOutController {

    def index = { 		
		String urlRedireccion = ""
		
		if (Environment.current != Environment.PRODUCTION){													
			session?.setAttribute('casUser', null)
			def appName = (ApplicationHolder.application == null)?"":(ApplicationHolder?.application?.applicationMeta['app.name'])
			urlRedireccion = "/" + appName	
		}else{			
			session.invalidate()
			urlRedireccion = GrailsConfig.config.cas.logOutUrl
		}		
		redirect(url: urlRedireccion)
	}
}
