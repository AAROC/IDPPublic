<%-- 
    Document   : providers
    Created on : 04-Jan-2012, 13:45:36
    Author     : marco
--%>
<% pageContext.setAttribute("title", "Service Providers");%>

<%@include file="WEB-INF/jspf/header.jspf" %>
<h2>Service Providers</h2>


<p>
    Currently, the catch-all federation authenticates the users of the following Service Providers:
</p>

<ul>
    <li>
        <i>Science Gateways:</i>
        <ol>
            <li><a href="http://sgw.africa-grid.org/">The Africa Grid Science Gateway</a></li>
            
        <i>Project Portals:</i>
        <ol>
            <li><a href="http://ops.sagrid.ac.za/trac">SAGrid Operations Portal</a> (coming soon)</li>
            <li><a href="http://www.special-project.it/">The SPECIAL portal</a></li>
        </ol>
    </li>
    <li>
        <i>Training and Collaboration Portals:</i>
        <ol>
            <li><a href="http://gilda.ct.infn.it/">The GILDA t-Infrastructure portal</a></li>
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