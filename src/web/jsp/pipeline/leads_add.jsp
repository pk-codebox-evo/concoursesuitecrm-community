<%@ taglib uri="/WEB-INF/dhv-taglib.tld" prefix="dhv" %>
<%@ page import="org.aspcfs.modules.pipeline.base.*" %>
<jsp:useBean id="PipelineViewpointInfo" class="org.aspcfs.utils.web.ViewpointInfo" scope="session"/>
<jsp:useBean id="User" class="org.aspcfs.modules.login.beans.UserBean" scope="session"/>
<jsp:useBean id="OppDetails" class="org.aspcfs.modules.pipeline.beans.OpportunityBean" scope="request"/>
<jsp:useBean id="ContactDetails" class="org.aspcfs.modules.contacts.base.Contact" scope="request"/>
<jsp:useBean id="OrgDetails" class="org.aspcfs.modules.accounts.base.Organization" scope="request"/>
<%@ include file="../initPage.jsp" %>
<body onLoad="javascript:document.forms[0].header_description.focus();">
<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript" SRC="javascript/checkDate.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript" SRC="javascript/popCalendar.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript" SRC="javascript/submit.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" type="text/javascript" src="javascript/popContacts.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" type="text/javascript" src="javascript/popAccounts.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript" SRC="javascript/popLookupSelect.js"></SCRIPT>
<%
	String entity = "pipeline";
    if("contact".equals(request.getParameter("entity"))){
        entity = "contact";
    }else if("account".equals(request.getParameter("entity"))){
        entity = "account";
    }
    OpportunityHeader OpportunityHeader = OppDetails.getHeader();
	OpportunityComponent ComponentDetails = OppDetails.getComponent();
%>
<SCRIPT LANGUAGE="JavaScript">
function doCheck(form) {
  if (form.dosubmit.value == "false") {
    return true;
  } else {
    return(checkForm(form));
  }
}
function checkForm(form) {
  formTest = true;
  message = "";
  <% if("pipeline".equals(entity)){ %>
  selected = -1;
  for (i=0; i<form.opp_type.length; i++) {
    if (form.opp_type[i].checked) {
      selected = i;
    }
  }
  if (selected == -1) {
    message += "- Please select an opportunity type (account or contact)\r\n";
    formTest = false;
  }
  <% } %>
  if ((!form.component_closeDate.value == "") && (!checkDate(form.component_closeDate.value))) { 
    message += "- Check that Est. Close Date is entered correctly\r\n";
    formTest = false;
  }
  if ((!form.component_alertDate.value == "") && (!checkDate(form.component_alertDate.value))) { 
    message += "- Check that Alert Date is entered correctly\r\n";
    formTest = false;
  }
  if ((!form.component_alertDate.value == "") && (!checkAlertDate(form.component_alertDate.value))) { 
    message += "- Check that Alert Date is on or after today's date\r\n";
    formTest = false;
  }
  if ((!form.component_alertText.value == "") && (form.component_alertDate.value == "")) { 
    message += "- Please specify an alert date\r\n";
    formTest = false;
  }
  if ((!form.component_alertDate.value == "") && (form.component_alertText.value == "")) { 
    message += "- Please specify an alert description\r\n";
    formTest = false;
  }
  if (formTest == false) {
    alert("Form could not be saved, please check the following:\r\n\r\n" + message);
    return false;
  } else {
    var test = document.opportunityForm.selectedList;
    if (test != null) {
      return selectAllOptions(document.opportunityForm.selectedList);
    }
  }
}
</script>
<% if("pipeline".equals(entity)){ %>
<form name="opportunityForm" action="Leads.do?command=Save&auto-populate=true" onSubmit="return doCheck(this);" method="post">
<a href="Leads.do">Pipeline Management</a> > 
Add Opportunity<br>
<hr color="#BFBFBB" noshade>
<dhv:evaluate exp="<%= PipelineViewpointInfo.isVpSelected(User.getUserId()) %>">
  <b>Viewpoint: </b><b class="highlight"><%= PipelineViewpointInfo.getVpUserName() %></b><br>
  &nbsp;<br>
</dhv:evaluate>
<table cellpadding="4" cellspacing="0" border="0" width="100%" bordercolorlight="#000000" bordercolor="#FFFFFF">
  <tr>
    <td>
<input type="submit" value="Save" onClick="this.form.dosubmit.value='true';">
<input type="submit" value="Cancel" onClick="javascript:this.form.action='Leads.do?command=ViewOpp';this.form.dosubmit.value='false';">
<% }else if("contact".equals(entity)){ %>
<form name="opportunityForm" action="ExternalContactsOpps.do?command=Save&contactId=<%= ContactDetails.getId() %>&auto-populate=true" onSubmit="return doCheck(this);" method="post">
<a href="ExternalContacts.do">General Contacts</a> > 
<a href="ExternalContacts.do?command=ListContacts">View Contacts</a> >
<a href="ExternalContacts.do?command=ContactDetails&id=<%= ContactDetails.getId() %>">Contact Details</a> >
<a href="ExternalContactsOpps.do?command=ViewOpps&contactId=<%= ContactDetails.getId() %>">Opportunities</a> >
Add Opportunity<br>
<hr color="#BFBFBB" noshade>
<table cellpadding="4" cellspacing="0" border="1" width="100%" bordercolorlight="#000000" bordercolor="#FFFFFF">
  <tr class="containerHeader">
    <td>
      <strong><%= toHtml(ContactDetails.getNameFull()) %></strong>
    </td>
  </tr>
  <tr class="containerMenu">
    <td>
      <% String param1 = "id=" + ContactDetails.getId(); %>      
      <dhv:container name="contacts" selected="opportunities" param="<%= param1 %>" />
    </td>
  </tr>
  <tr>
    <td class="containerBack">
      <input type="submit" value="Save" onClick="this.form.dosubmit.value='true';">
      <input type="submit" value="Cancel" onClick="javascript:this.form.action='ExternalContactsOpps.do?command=ViewOpps&contactId=<%= ContactDetails.getId() %>';this.form.dosubmit.value='false';">
<% }else{ %>
<form name="opportunityForm" action="Opportunities.do?command=Save&auto-populate=true" onSubmit="return doCheck(this);" method="post">
<a href="Accounts.do">Account Management</a> > 
<a href="Accounts.do?command=View">View Accounts</a> >
<a href="Accounts.do?command=Details&orgId=<%=OrgDetails.getOrgId()%>">Account Details</a> >
<a href="Opportunities.do?command=View&orgId=<%=OrgDetails.getOrgId()%>">Opportunities</a> >
Add Opportunity<br>
<hr color="#BFBFBB" noshade>
<table cellpadding="4" cellspacing="0" border="1" width="100%" bordercolorlight="#000000" bordercolor="#FFFFFF">
  <tr class="containerHeader">
    <td>
      <strong><%= toHtml(OrgDetails.getName()) %></strong>
    </td>
  </tr>
  <tr class="containerMenu">
    <td>
      <% String param1 = "orgId=" + OrgDetails.getOrgId(); %>      
      <dhv:container name="accounts" selected="opportunities" param="<%= param1 %>" />
    </td>
  </tr>
  <tr>
    <td class="containerBack">
<input type="submit" value="Save" onClick="this.form.dosubmit.value='true';">
<input type="submit" value="Cancel" onClick="javascript:this.form.action='Opportunities.do?command=View&orgId=<%= OrgDetails.getOrgId() %>';this.form.dosubmit.value='false';">
<% } %>
<input type="reset" value="Reset">
<br>
<%= showError(request, "actionError") %>

<%--  include basic opportunity form --%>
<%@ include file="opportunity_form.jsp" %>

&nbsp;
<br>
<% if("pipeline".equals(entity)){ %>
<input type="submit" value="Save" onClick="this.form.dosubmit.value='true';">
<input type="submit" value="Cancel" onClick="javascript:this.form.action='Leads.do?command=ViewOpp';this.form.dosubmit.value='false';">
<% }else if("contact".equals(entity)){ %>
<input type="submit" value="Save" onClick="this.form.dosubmit.value='true';">
  <input type="submit" value="Cancel" onClick="javascript:this.form.action='ExternalContactsOpps.do?command=ViewOpps&contactId=<%= ContactDetails.getId() %>';this.form.dosubmit.value='false';">
<% }else if("account".equals(entity)){ %>
<input type="submit" value="Save" onClick="this.form.dosubmit.value='true';">
<input type="submit" value="Cancel" onClick="javascript:this.form.action='Opportunities.do?command=View&orgId=<%= OrgDetails.getOrgId() %>';this.form.dosubmit.value='false';">
<% } %>
<input type="reset" value="Reset">
<input type="hidden" name="dosubmit" value="true">
    </td>
  </tr>
</table>
</form>
</body>
