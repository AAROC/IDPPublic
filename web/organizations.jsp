<%@ taglib prefix="s" uri="/struts-tags" %>
<s:iterator value="OrgList">
    orgs.push(new tmpOrg("<s:property value="key" />","<s:property value="countryCode" />","<s:property value="description" escapeHtml="true" />"));
</s:iterator>