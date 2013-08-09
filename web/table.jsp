<%@ taglib prefix="s" uri="/struts-tags" %>
        <table id="gradient-style">
            <thead>
                <tr>
                    <th>Username</th>
                    <th>Given Name</th>
                    <th>Surname</th>
                    <th>Address</th>
                    <th>Title</th>
                    <th>Email Addresses</th>
                    <th>Phone</th>
                    <th>Country</th>
                    <th>Organization</th>
                    <th>Delete</th>
                    <th>Write</th>
                </tr>
            </thead>
            <tbody>
                <s:iterator value="users">
                        <tr>
                            <td><s:property value="username" /></td>
                            <td><s:property value="givenname" /></td>
                            <td><s:property value="surname" /></td>
                            <td><s:property value="address" /></td>
                            <td><s:property value="title" /></td>
                            <td><s:property value="preferredMail" /></td>
                            <td><s:property value="phone" /></td>
                            <td><s:property value="country" /></td>
                            <td><s:property value="organization" /></td>
                            <!-- TODO: id delle request nei link, e form per Action DeleteAction e WriteAction -->
                            <td><a href="#" onclick="deleteRow(<s:property value="id" />)"><img src="img/Delete-icon.png" alt="" width="24" height="24"></a></td>
                            <td><a href="#" onclick="registerRow(<s:property value="id" />)"><img src="img/check_mark.png" alt="" width="24" height="24"></a></td>
                        </tr>
                </s:iterator>
            </tbody>
        </table>