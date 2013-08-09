<%-- 
    Document   : userAdmin
    Created on : 05-Jun-2012, 18:08:34
    Author     : Marco Fargetta <marco.fargetta@ct.infn.it>
--%>

<%@include file="WEB-INF/jspf/header.jspf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<h2>User Account administration</h2>
    <s:if test="#session.login != 'true'">
        <jsp:forward page="login.jsp" />  
    </s:if>
    <div style="float: right;">
        <a href="<%= request.getContextPath() %>/Logout.action" ><img src="img/Log-Out-icon.png" alt="" width="24" height="24">Logout</a>
    </div>

        <h2>User Information</h2>
        
            <s:form action="" method="POST">
                <s:label label="Name" value="%{user.title} %{user.givenname} %{user.surname}"/>
                <s:label label="Username" value="%{user.username}"/>
                <s:label label="PreferredMail" value="%{user.preferredMail}"/>
                <s:label label="MailsList" value="%{user.additionalMails}"/>
            </s:form>
         <h3>Update information</h3>
            <s:form action="ModifyUser" method="POST">
                <s:hidden name="user_cn" value="%{user.username}"/>
                <s:textfield label="Add Mail" name="newMail" value=""/>
                <s:submit name="addMail" value="Add Mail"/>
            </s:form>
        
         <h3>Change password</h3>               
            <s:form action="ModifyUser" method="POST">
                <s:hidden name="user_cn" value="%{user.username}"/>
                <s:password label="New Password" name="newPass" value=""/>
                <s:password label="New Password Again" name="newPassAgain" value=""/>
                
                <s:submit name="password" value="Change Password"/>
            </s:form>

         <p>For more relevant changes (e.g. change organisations) please write to <a href="mailto:sgwadmin@garr.it">sgwadmin@garr.it</a> </p>
<%@include file="WEB-INF/jspf/footer.jspf" %>
