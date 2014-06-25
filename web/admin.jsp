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
        function activateRow(id, action){
            $("#request_id").val(id);
            $("#action").val(action);
            $("#activate").submit();
            return false;
        }
    </script>
    <s:action name="AdminAction" executeResult="true" namespace="/" />
    
    <s:form action="ValidateRegisterUserData.action" id="register" >
        <s:hidden name="id" id="register_id" />
    </s:form>
    <s:form action="DeleteAction.action" id="delete" >
        <s:hidden name="id" id="hidden_id" />
    </s:form>
    
    <s:form action="ActivateAccount.action" id="activate" >
        <s:hidden name="id" id="request_id" />
        <s:hidden name="action" id="action" />
    </s:form>
    
    
<%@include file="WEB-INF/jspf/footer.jspf" %>
