package casClient

import javax.servlet.Filter 
import javax.servlet.FilterChain 
import javax.servlet.FilterConfig 
import javax.servlet.ServletRequest 
import javax.servlet.ServletResponse 

class MockRemoteUserFilter implements Filter { 

   void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) { 	  
      chain.doFilter new MockRemoteUserRequestWrapper(request), response 
   } 
   void init(FilterConfig config) {} 
   void destroy() {} 
} 
