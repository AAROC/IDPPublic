<%-- 
    Document   : validatereg
    Created on : 06-Jun-2012, 15:48:55
    Author     : Marco Fargetta <marco.fargetta@ct.infn.it>
--%>


<%@include file="WEB-INF/jspf/header.jspf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<h2>Verify and Confirm User Record</h2>
    <s:if test="#session.login != 'true'">
        <jsp:forward page="login.jsp" />  
    </s:if>
    <div style="float: right;">
        <a href="<%= request.getContextPath() %>/Logout.action" ><img src="img/Log-Out-icon.png" alt="" width="24" height="24">Logout</a>
    </div>

        <h3>Confirm registration for the user: <i><s:property value="userReq.givenname"/> <s:property value="userReq.surname"/></i></h3>
        
            <s:form action="RegisterUser.action" method="POST">
                <s:hidden name="user_id" value="%{userReq.id}"/>
                <s:label label="Name" value="%{userReq.title} %{userReq.givenname} %{userReq.surname}"/>
                <s:label label="Username" value="%{userReq.username}"/>
                <s:label label="PreferredMail" value="%{userReq.preferredMail}"/>
                <s:label label="AdditionalMails" value="%{userReq.additionalMails}"/>
                
                <s:textfield label="Organisation" name="organizationDN" value="%{userReq.organizationDN}" size="30"/><br/>
                <s:textfield label="Organisation Unit" name="organizationUnitDN" value="%{userReq.organizationUnitDN}" size="30"/><br/>
                <s:submit name="confirm" value="Confirm"/>
            </s:form>
            <s:form action="login.action" method="POST">
                <s:submit name="back" value="Back"/>
            </s:form>
        
 <%@include file="WEB-INF/jspf/footer.jspf" %>

