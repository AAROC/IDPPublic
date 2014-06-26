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

        <h3>Confirm registration for the user: <i><s:property value="userReq.givenname"/> <s:property value="userReq.surname"/></i></h3>
        
            <s:form action="RegisterUser.action" method="POST">
                <s:hidden name="user_id" value="%{userReq.id}"/>
                <s:label label="Name" value="%{userReq.title} %{userReq.givenname} %{userReq.surname}"/>
                <s:label label="Username" value="%{userReq.username}"/>
                <s:label label="PreferredMail" value="%{userReq.preferredMail}"/>
                <s:label label="AdditionalMails" value="%{userReq.additionalMails}"/>
                <s:if test="%{userReq.organizationDN != null}">
                    <s:textfield label="Organisation" name="organizationDN" value="%{userReq.organizationDN}" size="30" readonly="true" /><br/>
                </s:if>
                <s:else>
                    <s:label label="Organisation specified" value="%{userReq.organization+' ('+userReq.description+')'}"/>

                    <s:select name="organizationDN" id="organisation" label="Organisation" list="orgs" headerKey="newOrg" headerValue="Add a new Organisation"/>
                    
                    <div id="newOrg">
                        <s:textfield label="Organisation name" key="orgname"  />
                        <s:textfield label="Organisation description" key="orgdesc" />
                        <s:textfield label="Organisation reference (url)" key="orgref" />
                    </div>
                    <script type="text/javascript">
                        $("#organisation").change(function(e){
                            var value= $("#organisation").val();
                            if(value!="newOrg"){
                                $("#RegisterUser_orgname").attr('disabled', true);
                                $("#RegisterUser_orgdesc").attr('disabled', true);
                                $("#RegisterUser_orgref").attr('disabled', true);
                            }
                            else{
                                $("#RegisterUser_orgname").attr('disabled', false);
                                $("#RegisterUser_orgdesc").attr('disabled', false);
                                $("#RegisterUser_orgref").attr('disabled', false);
                            }
                        });
                    </script>            

                </s:else>
                <s:textfield label="Organisation Unit" name="organizationUnitDN" value="%{userReq.organizationUnitDN}" size="30"/><br/>
                <s:submit name="confirm" value="Confirm"/>
            </s:form>
            <s:form action="globalAdmin" method="POST">
                <s:submit name="back" value="Back"/>
            </s:form>
        
 <%@include file="WEB-INF/jspf/footer.jspf" %>

