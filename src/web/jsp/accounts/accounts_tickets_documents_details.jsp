<%@ taglib uri="/WEB-INF/dhv-taglib.tld" prefix="dhv" %>
<%@ page import="java.util.*,org.aspcfs.modules.troubletickets.base.*,com.zeroio.iteam.base.*" %>
<jsp:useBean id="OrgDetails" class="org.aspcfs.modules.accounts.base.Organization" scope="request"/>
<jsp:useBean id="TicketDetails" class="org.aspcfs.modules.troubletickets.base.Ticket" scope="request"/>
<jsp:useBean id="FileItem" class="com.zeroio.iteam.base.FileItem" scope="request"/>
<%@ include file="../initPage.jsp" %>
<body onLoad="document.inputForm.subject.focus();">
<a href="Accounts.do">Account Management</a> > 
<a href="Accounts.do?command=View">View Accounts</a> >
<a href="Accounts.do?command=Details&orgId=<%=TicketDetails.getOrgId()%>">Account Details</a> >
<a href="Accounts.do?command=ViewTickets&orgId=<%=TicketDetails.getOrgId()%>">Tickets</a> >
<a href="AccountTickets.do?command=TicketDetails&id=<%=TicketDetails.getId()%>">Ticket Details</a> >
<a href="AccountTicketsDocuments.do?command=View&tId=<%=TicketDetails.getId()%>">Documents</a> >
Details<br>
<hr color="#BFBFBB" noshade>
<%@ include file="accounts_details_header_include.jsp" %>
<% String param1 = "orgId=" + TicketDetails.getOrgId(); %>      
<dhv:container name="accounts" selected="tickets" param="<%= param1 %>" style="tabs"/>
<table cellpadding="4" cellspacing="0" border="0" width="100%">
  <tr>
  	<td class="containerBack">
      <% String param2 = "id=" + TicketDetails.getId(); %>
      <strong>Ticket # <%=TicketDetails.getPaddedId()%>:</strong>
      [ <dhv:container name="accountstickets" selected="documents" param="<%= param2 %>"/> ]
      <dhv:evaluate if="<%= TicketDetails.getClosed() != null %>">
        <br><font color="red">This ticket was closed on <%= toHtml(TicketDetails.getClosedString()) %></font>
      </dhv:evaluate>
      <br><br>
      <%= showError(request, "actionError") %>
      <table cellpadding="4" cellspacing="0" border="0" width="100%" class="details">
        <tr>
          <th colspan="7">
            <strong>All Versions of this Document</strong>
          </th>
        </tr>
        <tr class="title2">
          <td width="10" align="center">Action</td>
          <td>Item</td>
          <td>Size</td>
          <td>Version</td>
          <td>Submitted</td>
          <td>Sent By</td>
          <td>D/L</td>
        </tr>
      <%
        Iterator versionList = FileItem.getVersionList().iterator();
        int rowid = 0;
        while (versionList.hasNext()) {
          rowid = (rowid != 1?1:2);
          FileItemVersion thisVersion = (FileItemVersion)versionList.next();
      %>      
          <tr class="row<%= rowid %>">
            <td width="10" align="center" rowspan="2" nowrap>
              <a href="TroubleTicketsDocuments.do?command=Download&tId=<%= TicketDetails.getId() %>&fid=<%= FileItem.getId() %>&ver=<%= thisVersion.getVersion() %>">Download</a>
            </td>
            <td width="100%">
              <%= FileItem.getImageTag() %><%= thisVersion.getClientFilename() %>
            </td>
            <td align="right" nowrap>
              <%= thisVersion.getRelativeSize() %> k&nbsp;
            </td>
            <td align="right" nowrap>
              <%= thisVersion.getVersion() %>&nbsp;
            </td>
            <td nowrap>
              <%= thisVersion.getEnteredDateTimeString() %>
            </td>
            <td>
              <dhv:username id="<%= thisVersion.getEnteredBy() %>"/>
            </td>
            <td align="right">
              <%= thisVersion.getDownloads() %>
            </td>
          </tr>
          <tr class="row<%= rowid %>">
            <td colspan="6">
              <i><%= thisVersion.getSubject() %></i>
            </td>
          </tr>
        <%}%>
        <%-- ticket container end --%>
      </table>
    </td>
  </tr>
  <%-- account container end --%>
</table>
</body>
