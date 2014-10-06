<%-- 
    Document   : about
    Created on : 04-Jan-2012, 13:45:36
    Author     : brucellino
--%>
<% pageContext.setAttribute("title", "about"); %>

<%@include file="WEB-INF/jspf/header.jspf" %>
                    <h2>About</h2>
                    <p>Identity Federations are an easier way to use many day-to-day ICT services. You probably use an Identity Provider already - such as google or facebook - and many public institutions have identity services for their users. In order for service providers to trust identities from other institutes, a trusted environment - or federation - is needed. For more information about Identity Federations, see <a href="http://en.wikipedia.org/wiki/Federated_identity">the Wikipedia entry</a> on Identity Federations, or the <a href="http://www.geant.net/service/eduGAIN/Pages/home.aspx">the GEANT Edugain</a> project.
                <h3>About the South African Catch-All Identity Provider</h3>
The SAGrid Catch-All Identity Provider is a demonstration service developed in collaboration with the <a href="http://www.ei4africa.eu">ei4Africa project</a>, in concert with similar activities in Kenya, Nigeria and Tanzania. The project is still under way, and this service is actively developed, and this is true for the policies and procedures of the South African Catch-All Identity Federation as well. The development of these policies is in the finalisation stage and they will published here as soon as they will become available.
<h3>An IdP at your own institute ?</h3>
If you're interested in deploying a demo IdP at your own institute and having it registered in the Catch-All Federation, we can help. This IdP is based on the <a href="http://shibboleth.internet2.edu/">Shibboleth</a>, developed by <a href="http://www.internet2.edu/">Internet2</a>. For the installation and configuration of Shibboleth, the <a href="http://www.switch.ch/aai/support/">documentation of the Swiss research and technology network (SWITCH)'s federation;</a> <a href="https://spaces.internet2.edu/display/SHIB/">Shibboleth's Wiki</a> is an excellent documentation resource. 
<h3>Metadata</h3>
Should you have a service which would like to be able to authenticate users from this identity provider, you can find the metatadata <a href="/metadata/idp-metadata.xml">here</a>



<%@include file="WEB-INF/jspf/footer.jspf" %>
