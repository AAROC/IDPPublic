<%-- 
    Document   : about
    Created on : 04-Jan-2012, 13:45:36
    Author     : marco
--%>
<% pageContext.setAttribute("title", "About"); %>

<%@include file="WEB-INF/jspf/header.jspf" %>
                    <h2>About</h2>
                    
                    <p>
                        The IDentity Provider Open (IDPOPEN), formerly IDentity Provider Catania (IDPCT), is a <a href="http://saml.xml.org/">SAML</a> standard based service powered by <a href="http://shibboleth.internet2.edu">Shibboleth</a> and jointly managed by <a href="http://www.garr.it">GARR</a> and <a href="http://www.ct.infn.it">INFN Catania</a> that allows people to check their identity when they sign in on web pages and Science Gateways that require user authentication.
                        </p>

                    <p>
                        IDPOPEN is the "catch-all" Identity Provider of the <a href="http://gridp.garr.it">Grid IDentity Pool (GrIDP) </a>federation and currently authenticates the users of several <a href="service-providers.html">Service Providers</a>
                    </p>



                    <p>If you want to register as member of IDPOPEN, please fill <a target="_blank" href="register">this form</a></p>


                    <p>
                       If you are interested in either using IDPOPEN to authenticate people on your website(s) or registering it as an Identity Provider of your Identity Federation(s), please contact sgwadmin_AT_garr.it.
                    </p>


<%@include file="WEB-INF/jspf/footer.jspf" %>