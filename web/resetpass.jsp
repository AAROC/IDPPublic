<%-- 

/**************************************************************************
Copyright (c) 2011: 
Istituto Nazionale di Fisica Nucleare (INFN), Italy
Consorzio COMETA (COMETA), Italy

See http://www.infn.it and and http://www.consorzio-cometa.it for details on the
copyright holders.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/


    Document   : resetpass
    Created on : 12-Mar-2014, 15:33:56
    Author     : Marco Fargetta <marco.fargetta@ct.infn.it>
--%>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@include file="WEB-INF/jspf/header.jspf" %>
                    <h2>Reset Password</h2>
                    <p>
                        Please, insert the code we sent by e-mail along with the new password for your account!
                    </p>
            <s:form action="passreset" method="POST">
                <s:div>
                    <s:textfield label="Code" name="code" />
                </s:div>
                <s:div>
                    <s:password label="Password" name="password" />
                    <s:password label="Confirm Password" name="password2" />
                </s:div>
                <s:submit name="resetPass" value="Reset Password" />
            </s:form>

<%@include file="WEB-INF/jspf/footer.jspf" %>

