package casClient

import grails.util.Environment 
import javax.servlet.http.HttpServletRequestWrapper 
import javax.servlet.http.HttpServletRequest 
import org.jasig.cas.client.authentication.AttributePrincipalImpl
import java.security.Principal

class MockRemoteUserRequestWrapper extends HttpServletRequestWrapper { 

   MockRemoteUserRequestWrapper(HttpServletRequest request) { 
      super(request) 
   } 

   String getRemoteUser() { 
      Environment.PRODUCTION != Environment.current ? (session?.getAttribute('casUser')) : super.remoteUser 
   } 
   Principal getUserPrincipal() {
	   Environment.PRODUCTION != Environment.current ? (new AttributePrincipalImpl('miPrincipal', [nombre:'Aitor', apellidos:'Alzola Garrido'])) : super.getUserPrincipal
	}
} 
