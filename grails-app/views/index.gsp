<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.TreeMap"%>
<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder" %>
<%=  "cas.loginUrl ->" + ConfigurationHolder.config.cas.loginUrl%></br>
<%=  "cas.disabled ->" + ConfigurationHolder.config.cas.disabled%></br>
Página inicial 
Usuario->

<% 
println "El usuario es...."  + request.getRemoteUser()
%>

<%if (request.getRemoteUser()) {%>
<g:link controller="logOut">Desconectar</g:link>
<% } %>

<p>** Parámetros de la sesión -> <%=session.id%>

<ul>
<% 
	Enumeration enames;
	Map map;
	map = new TreeMap();
   	enames = session.getAttributeNames();	
	while (enames.hasMoreElements()) {
    	String name = (String) enames.nextElement();
      	String value = "" + session.getAttribute(name); %>
		<li>Clave <strong><%= name%></strong> Valor <strong><%=value%></strong></li>
<% }%>	
</ul></p>

<p>** Parámetros del request
<ul>
	<% 	
Map<String, String[]> parms = request.getParameterMap();  
	//System.out.println("class :"+ parms.getClass());
	String[] valor = new String[10];
	for (Iterator iterator = parms.entrySet().iterator(); iterator.hasNext();)  {  
		Map.Entry entry = (Map.Entry) iterator.next();  
		%>
		<li>Clave <strong><%= entry.getKey()%></strong> Valor 	
		<%
		valor = (String[]) entry.getValue();
		for (String stringValor : valor) {
			%>
			<strong><%=stringValor%></strong>**
			<% 
		}  
	%>
	</li> 
   <%
	//System.out.println("value:"+entry.getValue());
	//System.out.println(" "+entry.getValue());
	//System.out.println("class " + entry.getValue().getClass());
	//System.out.println("value:"+entry.getValue().toString());
}%>
</ul></p>
