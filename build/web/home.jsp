<%-- 
    Document   : Home
    Created on : 04-Jan-2012, 13:45:36
    Modified   : 08-Aug-2013 
    Author     : Bruce Becker
--%>
<% pageContext.setAttribute("title", "Home"); %>

<%@include file="WEB-INF/jspf/header.jspf" %>
                    <h2>Home</h2>
                    
                    <p>
                        The South African Catch-All Identity Provider is a <a href="http://saml.xml.org/">SAML</a> standard-based service powered by <a href="http://shibboleth.internet2.edu">Shibboleth</a> and jointly managed by <a href="http://www.sagrid.ac.za">SAGrid</a> and <a href="http://www.sanren.ac.za">SANREN</a> that allows people to use a single identity when they sign into web pages and Science Gateways that require user authentication.
                        </p>

                    <p>
                        This IdP is a member of <a href="http://gridp.ct.infn.it">Grid IDentity Pool (GrIDP) </a>federation and currently authenticates the users of several <a href="service-providers.html">Service Providers</a>
                    </p>
                    <p>If your institute does not have an Identity Provider which forms part of a national or catch-all federation, you can <a target="_blank" href="register">register to the catch-all IdP</a></p>


                    <p>
                       If you are interested in either using this IdP to authenticate users of your website(s), or registering it as an Identity Provider of your Identity Federation(s), please contact Bruce Becker at bbecker_at_csir.co.za.
                    </p>
                    <p>For more information, please see <a href="http://ei4africa.eu">the ei4Africa project website</a>.</p>


<%@include file="WEB-INF/jspf/footer.jspf" %>