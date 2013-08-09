<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome</title>
        <s:head/>
    </head>
    <body>
           <s:if test="#session.login != 'true'">
            <jsp:forward page="login.jsp" />  
            </s:if>
            <a href="<%= request.getContextPath() %>/Logout.action" >Logout</a>
            <p>Benvenuto <s:property  value="username" /></p>
    </body>
</html>
