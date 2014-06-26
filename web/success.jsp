<% pageContext.setAttribute("title", "Success"); %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@include file="WEB-INF/jspf/header.jspf" %>
                    <h2>Success</h2>
                    <p>Your request has been sent successfully!</p>
<s:form action="login.action" method="POST">
    <s:submit name="back" value="Back"/>
</s:form>                    
<%@include file="WEB-INF/jspf/footer.jspf" %>