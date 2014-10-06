<%-- 
    Document   : about
    Created on : 04-Jan-2012, 13:45:36
    Author     : marco
--%>
<% pageContext.setAttribute("title", "Documentation"); %>

<%@include file="WEB-INF/jspf/header.jspf" %>
<h2>Public Identity Provider notes:</h2>
<p>
<ul>
    <li>Policies and procedures of the IDPPublic Identity Provider are in the finalisation stage and they will published here as soon as they will become available.</li>
    <li><b>Software</b>: IDPPublic is based on the <a href="http://shibboleth.internet2.edu/">Shibboleth</a> software, of the American research and educational network <a href="http://www.internet2.edu/">Internet2</a>.
    <li> To install and configure Shibboleth, we reccommend reading the <a href="http://www.switch.ch/aai/support/">documentation of the Swiss research and technology network (SWITCH)'s federation;</a> <a href="https://spaces.internet2.edu/display/SHIB/">Shibboleth's Wiki</a> is also an excellent documentation resource.
    <li><b>Metadata</b>The metadata of this identity provider can be found at <a href="https://CHANGEME/path/to/metadata">CHANGEME Identity Provider's metadata</a></li>
</ul>

<%@include file="WEB-INF/jspf/footer.jspf" %>
