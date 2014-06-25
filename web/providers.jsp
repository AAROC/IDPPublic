<%-- 
    Document   : providers
    Created on : 04-Jan-2012, 13:45:36
    Author     : marco
--%>
<% pageContext.setAttribute("title", "Service Providers");%>

<%@include file="WEB-INF/jspf/header.jspf" %>
<h2>Service Providers</h2>


<p>
    Currently, IDPOPEN authenticates the users of the following Service Providers:
</p>

<ul>
    <li>
        <i>Science Gateways:</i>
        <ol>
            <li><a href="http://atlases.muni.cz">Atlases - PATHOLOGY IMAGES</a></li>
            <li><a href="http://sgw.africa-grid.org/">The Africa Grid Science Gateway</a></li>
            <li><a href="http://aginfra-sg.ct.infn.it/">The agINFRA Science Gateway</a></li>
            <li><a href="http://science-gateway.chain-project.eu/">The CHAIN Science Gateway</a></li>
            <li><a href="http://cogito-med.ct.infn.it/">The COGITO-MED Science Gateway</a></li>
            <li><a href="http://ecsg.dch-rp.eu">The DCH-RP e-Culture Science Gateway</a></li>
            <li><a href="http://applications.eu-decide.eu/">The DECIDE Science Gateway</a></li>
            <li><a href="http://earthserver-sg.consorzio-cometa.it/">The EarthServer Science Gateway</a></li>
            <li><a href="http://applications.eumedgrid.eu/">The EUMEDGRID Science Gateway</a></li>
            <li><a href="http://sgw.garr.it/">The GARR Science Gateway</a></li>
            <li><a href="http://gisela-gw.ct.infn.it/">The GISELA Science Gateway</a></li>
            <li><a href="http://klios.ct.infn.it/">The KLIOS Science Gateway</a></li>
            <li><a href="http://sgw.marwan.ma">The Moroccan Grid Science Gateway</a></li>
            <li><a href="https://recasgateway.ba.infn.it/">The ReCaS Science Gateway</a></li>
        </ol>
    </li>


    <li>
        <i>Project Portals:</i>
        <ol>
            <li><a href="http://www.chain-project.eu/">The CHAIN portal</a></li>
            <li><a href="http://www.special-project.it/">The SPECIAL portal</a></li>
            <li><a href="https://www.progettovespa.it">The VESPA portal (in italian)</a></li>
        </ol>
    </li>
    <li>
        <i>Training and Collaboration Portals:</i>
        <ol>
            <li><a href="http://gilda.ct.infn.it/">The GILDA t-Infrastructure portal</a></li>
            <li><a href="http://ricevi.ct.infn.it/">The RICeVI portal</a> (in italian)</li>
        </ol>
    </li>
    <li>
        <i>Other Services:</i>
        <ol>
            <li><a href="https://foodl.org/">Foodle (surveys, polls and meeting scheduler)</a></li>
        </ol>
    </li>

</ul>

<%@include file="WEB-INF/jspf/footer.jspf" %>