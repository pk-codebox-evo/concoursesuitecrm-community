<%@ taglib uri="/WEB-INF/dhv-taglib.tld" prefix="dhv" %>
<%@ page import="java.util.*,org.aspcfs.modules.contacts.base.*" %>
<jsp:useBean id="ContactDetails" class="org.aspcfs.modules.contacts.base.Contact" scope="request"/>
<jsp:useBean id="OrgDetails" class="org.aspcfs.modules.accounts.base.Organization" scope="request"/>
<jsp:useBean id="CallDetails" class="org.aspcfs.modules.contacts.base.Call" scope="request"/>
<%@ include file="../initPage.jsp" %>
<dhv:evaluate if="<%= !isPopup(request) %>">
<a href="Accounts.do">Account Management</a> > 
<a href="Accounts.do?command=View">View Accounts</a> >
<a href="Accounts.do?command=Details&orgId=<%=OrgDetails.getOrgId()%>">Account Details</a> >
<a href="Contacts.do?command=View&orgId=<%=OrgDetails.getOrgId()%>">Contacts</a> >
<a href="Contacts.do?command=Details&id=<%=ContactDetails.getId()%>&orgId=<%=OrgDetails.getOrgId()%>">Contact Details</a> >
<a href="AccountContactsCalls.do?command=View&contactId=<%=ContactDetails.getId()%>&orgId=<%=OrgDetails.getOrgId()%>">Calls</a> >
Add Call<br>
<hr color="#BFBFBB" noshade>
<body onLoad="javascript:document.forms[0].subject.focus();">
<form name="addCall" action="AccountContactsCalls.do?command=Save&auto-populate=true" onSubmit="return doCheck(this);" method="post">
</dhv:evaluate>
<%@ include file="accounts_details_header_include.jsp" %>
<dhv:container name="accounts" selected="contacts" param="<%= "orgId=" + OrgDetails.getOrgId() %>" style="tabs"/>
<table cellpadding="4" cellspacing="0" border="0" width="100%">
  <tr>
    <td class="containerBack">
    <%-- include contact menu --%>
    <% String param1 = "id=" + ContactDetails.getId(); 
    %>
        <strong><%= ContactDetails.getNameLastFirst() %>:</strong>
        [ <dhv:container name="accountscontacts" selected="calls" param="<%= param1 %>"/> ]
      <br>
      <br>
      <%-- include call add form --%>
      <input type="submit" value="Save" onClick="this.form.dosubmit.value='true';">
      <input type="submit" value="Cancel" onClick="javascript:this.form.action='AccountContactsCalls.do?command=View&contactId=<%= ContactDetails.getId() %>';this.form.dosubmit.value='false';">
      <input type="reset" value="Reset">
      <br>
      <%= showError(request, "actionError") %>
      <%@ include file="../contacts/call_include.jsp" %>
      &nbsp;
      <br>
      <input type="submit" value="Save" onClick="this.form.dosubmit.value='true';">
      <input type="submit" value="Cancel" onClick="javascript:this.form.action='AccountContactsCalls.do?command=View&contactId=<%= ContactDetails.getId() %>';this.form.dosubmit.value='false';">
      <input type="reset" value="Reset">
      <input type="hidden" name="dosubmit" value="true">
      <input type="hidden" name="contactId" value="<%= ContactDetails.getId() %>">
      <br>
      &nbsp;
    </td>
  </tr>
</table>
      