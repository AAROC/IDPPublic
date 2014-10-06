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
            <li><a href="http://sp.example.org">Example - Science Gateway for science fiction</a></li>
        </ol>
    </li>


    <li>
        <i>Project Portals:</i>
        <ol>
            <li><a href="http://project.example.org">Example - The big project of science fiction</a></li>
        </ol>
    </li>
    <li>
        <i>Other Services:</i>
        <ol>
            <li><a href="http://other.example.org">Example (surveys, polls and meeting scheduler)</a></li>
        </ol>
    </li>

</ul>

<%@include file="WEB-INF/jspf/footer.jspf" %>
