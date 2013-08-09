<% pageContext.setAttribute("title", "Register"); %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@include file="WEB-INF/jspf/header.jspf" %>
                    <h2>Login</h2>
            <s:form action="login.action" method="POST">
                <s:textfield label="Username" name="username" />
                <s:password label="Password" name="password" />
                <s:submit key="login" value="login" />
            </s:form>

            <h3><i>Note: to reset your password or for other login problems please write to <a href="mailto:bbecker@csir.co.za">the service admin</a>. </i></h3>
<%@include file="WEB-INF/jspf/footer.jspf" %>
