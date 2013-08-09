<% pageContext.setAttribute("title", "Admin"); %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@include file="WEB-INF/jspf/header.jspf" %>
                    <h2>Admin</h2>
    <s:if test="#session.login != 'true'">
        <jsp:forward page="login.jsp" />  
    </s:if>
    <script>
        function deleteRow(id){
            $("#hidden_id").val(id);
            $("#delete").submit();
            return false;
        }
        function registerRow(id){
            $("#register_id").val(id);
            $("#register").submit();
            return false;
        }
    </script>
    <div style="float: right;">
        <a href="<%= request.getContextPath() %>/Logout.action" ><img src="img/Log-Out-icon.png" alt="" width="24" height="24">Logout</a>
    </div>
    <s:action name="AdminAction" executeResult="true" namespace="/" />
    
    <s:form action="ValidateRegisterUserData.action" id="register" >
        <s:hidden name="id" id="register_id" />
    </s:form>
    <s:form action="DeleteAction.action" id="delete" >
        <s:hidden name="id" id="hidden_id" />
    </s:form>
<%@include file="WEB-INF/jspf/footer.jspf" %>
