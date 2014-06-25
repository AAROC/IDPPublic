<%@ taglib prefix="s" uri="/struts-tags" %>

<h3>User registrations</h3>
        <table class="gradient-style">
            <thead>
                <tr>
                    <th>Given Name</th>
                    <th>Surname</th>
                    <th>Address</th>
                    <th>Principal Email Address</th>
                    <th>Country</th>
                    <th>Organization</th>
                    <th>Delete</th>
                    <th>Write</th>
                </tr>
            </thead>
            <tbody>
                <s:iterator value="users">
                        <tr>
                            <td><s:property value="givenname" /></td>
                            <td><s:property value="surname" /></td>
                            <td><s:property value="address" /></td>
                            <td><s:property value="preferredMail" /></td>
                            <td><s:property value="country" /></td>
                            <td><s:property value="organization" /></td>
                            <!-- TODO: id delle request nei link, e form per Action DeleteAction e WriteAction -->
                            <td><a href="#" onclick="deleteRow(<s:property value="id" />)"><img src="img/Delete-icon.png" alt="" width="24" height="24"></a></td>
                            <td><a href="#" onclick="registerRow(<s:property value="id" />)"><img src="img/check_mark.png" alt="" width="24" height="24"></a></td>
                        </tr>
                </s:iterator>
            </tbody>
        </table>

<h3>Re-Activation requests</h3>
        <table class="gradient-style">
            <thead>
                <tr>
                    <th>Given Name</th>
                    <th>Surname</th>
                    <th>Address</th>
                    <th>Principal Email Address</th>
                    <th>Country</th>
                    <th>Organization</th>
                    <th>Delete</th>
                    <th>Write</th>
                </tr>
            </thead>
            <tbody>
                <s:iterator value="actvReqs" >
                        <tr>
                            <td><s:property value="value.givenname" /></td>
                            <td><s:property value="value.surname" /></td>
                            <td><s:property value="value.address" /></td>
                            <td><s:property value="value.preferredMail" /></td>
                            <td><s:property value="value.country" /></td>
                            <td><s:property value="value.organization" /></td>
                            <!-- TODO: id delle request nei link, e form per Action DeleteAction e WriteAction -->
                            <td><a href="#" onclick="activateRow(<s:property value="key" />,'delete')"><img src="img/Delete-icon.png" alt="" width="24" height="24"></a></td>
                            <td><a href="#" onclick="activateRow(<s:property value="key" />,'activate')"><img src="img/check_mark.png" alt="" width="24" height="24"></a></td>
                        </tr>
                </s:iterator>
            </tbody>
        </table>
