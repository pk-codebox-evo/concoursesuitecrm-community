<%-- 
  - Copyright Notice: (C) 2000-2004 Team Elements, All Rights Reserved.
  - License: This source code cannot be modified, distributed or used without
  -          written permission from Team Elements. This notice must remain in
  -          place.
  - Author(s): Matt Rajkowski
  - Version: $Id$
  - Description: 
  --%>
<%@ taglib uri="/WEB-INF/zeroio-taglib.tld" prefix="zeroio" %>
<%@ taglib uri="/WEB-INF/dhv-taglib.tld" prefix="dhv" %>
<%@ page import="java.util.*,com.zeroio.iteam.base.*,org.aspcfs.utils.web.*" %>
<%@ page import="org.aspcfs.utils.StringUtils" %>
<jsp:useBean id="applicationPrefs" class="org.aspcfs.controller.ApplicationPrefs" scope="application"/>
<jsp:useBean id="Project" class="com.zeroio.iteam.base.Project" scope="request"/>
<jsp:useBean id="newsArticle" class="com.zeroio.iteam.base.NewsArticle" scope="request"/>
<jsp:useBean id="clientType" class="org.aspcfs.utils.web.ClientType" scope="session"/>
<%@ include file="../initPage.jsp" %>
<%-- Editor must go here, before the body onload --%>
<dhv:evaluate if="<%= !clientType.showApplet() %>">
<jsp:include page="htmlarea_include.jsp" flush="true"/>
<body onload="initEditor('message');">
</dhv:evaluate>
<%-- Use applet instead --%>
<dhv:evaluate if="<%= clientType.showApplet() %>">
<body onload="document.inputForm.subject.focus();">
</dhv:evaluate>
<script language="JavaScript">
  function checkForm(form) {
    var formTest = true;
    var messageText = "";
<dhv:evaluate if="<%= clientType.showApplet() %>">
      document.inputForm.message.value = document.Kafenio.getDocumentBody();
</dhv:evaluate>
    //Check required fields
    if (document.inputForm.message.value == "") {    
      messageText += "- Message is a required field\r\n";
      formTest = false;
    }
    if (formTest == false) {
      messageText = "The message could not be submitted.          \r\nPlease verify the following items:\r\n\r\n" + messageText;
      alert(messageText);
      return false;
    } else {
      return true;
    }
  }
</script>
<form method="POST" name="inputForm" action="ProjectManagementNews.do?command=SavePage&pid=<%= Project.getId() %>&auto-populate=true" onSubmit="return checkForm(this);">
<table border="0" cellpadding="1" cellspacing="0" width="100%">
  <tr class="subtab">
    <td>
      <img src="images/icons/stock_announcement-16.gif" border="0" align="absmiddle">
      <a href="ProjectManagement.do?command=ProjectCenter&section=News&pid=<%= Project.getId() %>"><zeroio:tabLabel name="News" object="Project"/></a> >
      <a href="ProjectManagementNews.do?command=Edit&pid=<%= Project.getId() %>&id=<%= newsArticle.getId() %>">Edit Article</a> >
      Add Page
    </td>
  </tr>
</table>
<br>
  <input type="submit" value=" Save " />
  <dhv:evaluate if="<%= newsArticle.getMessage() != null %>">
    <input type="button" value="Delete this page" onClick="javascript:window.location.href='ProjectManagementNews.do?command=DeletePage&pid=<%= Project.getId() %>&id=<%= newsArticle.getId() %>';">
  </dhv:evaluate>
  <input type="button" value="Cancel" onClick="javascript:window.location.href='ProjectManagementNews.do?command=Edit&pid=<%= Project.getId() %>&id=<%= newsArticle.getId() %>';"><br>
  <dhv:formMessage />
  <table cellpadding="4" cellspacing="0" width="100%" class="pagedList">
    <tr>
      <th colspan="2" align="left">
        <strong>Add Page</strong>
      </th>
    </tr>
    <tr class="containerBody">
      <td class="formLabel" valign="top">
        Subject
      </td>
      <td>
        <%= toHtml(newsArticle.getSubject()) %>
      </td>
    </tr>
    <tr class="containerBody">
      <td class="formLabel" valign="top">
        Page 2
      </td>
      <td>
        <table border="0" cellpadding="0" cellspacing="0" class="empty">
          <tr>
            <td>

            
<dhv:evaluate if="<%= !clientType.showApplet() %>">
<textarea rows="20" id="message" name="message" cols="70"><%= toString(newsArticle.getMessage()) %></textarea>
</dhv:evaluate>

<dhv:evaluate if="<%= clientType.showApplet() %>">
<input type="hidden" name="message" value="<%= toHtmlValue(newsArticle.getMessage()) %>" />
<APPLET CODEBASE="." CODE="de.xeinfach.kafenio.KafenioApplet.class" ARCHIVE="applet.jar,gnu-regexp-1.1.4.jar,config.jar,icons.jar" NAME="Kafenio" WIDTH="510" HEIGHT="365" MAYSCRIPT>
	<PARAM NAME="CODEBASE" VALUE=".">
	<PARAM NAME="CODE" VALUE="de.xeinfach.kafenio.KafenioApplet.class">
	<PARAM NAME="ARCHIVE" VALUE="applet.jar,gnu-regexp-1.1.4.jar,config.jar,icons.jar">
	<PARAM NAME="NAME" VALUE="Kafenio">
	<PARAM NAME="TYPE" VALUE="application/x-java-applet;version=1.3">
	<PARAM NAME="SCRIPTABLE" VALUE="true">
	<PARAM NAME="STYLESHEET" VALUE="css/<%= applicationPrefs.get("LAYOUT.TEMPLATE") %>.css,css/<%= applicationPrefs.get("LAYOUT.TEMPLATE") %>-10pt.css">
	<PARAM NAME="LANGCODE" VALUE="en">
	<PARAM NAME="LANGCOUNTRY" VALUE="US">
	<PARAM NAME="TOOLBAR" VALUE="true">
	<PARAM NAME="TOOLBAR2" VALUE="true">
	<PARAM NAME="MENUBAR" VALUE="true">
	<PARAM NAME="SOURCEVIEW" VALUE="false">
	<PARAM NAME="MENUICONS" VALUE="true">
	<%-- all available toolbar items: NEW,OPEN,SAVE,CUT,COPY,PASTE,UNDO,REDO,FIND,BOLD,ITALIC,UNDERLINE,STRIKE,SUPERSCRIPT,SUBSCRIPT,ULIST,OLIST,CLEARFORMATS,INSERTCHARACTER,ANCHOR,VIEWSOURCE,STYLESELECT,LEFT,CENTER,RIGHT,JUSTIFY,DEINDENT,INDENT,IMAGE,COLOR,TABLE,SAVECONTENT,DETACHFRAME,SEPARATOR --%>
	<PARAM NAME="BUTTONS" VALUE="CUT,COPY,PASTE,SEPARATOR,BOLD,ITALIC,UNDERLINE,SEPARATOR,LEFT,CENTER,RIGHT,justify,SEPARATOR,DETACHFRAME">	
	<PARAM NAME="BUTTONS2" VALUE="ULIST,OLIST,SEPARATOR,UNDO,REDO,SEPARATOR,DEINDENT,INDENT,SEPARATOR,ANCHOR,SEPARATOR,IMAGE,SEPARATOR,CLEARFORMATS,SEPARATOR,VIEWSOURCE,SEPARATOR,STRIKE,SUPERSCRIPT,SUBSCRIPT,INSERTCHARACTER,SEPARATOR,FIND,COLOR,TABLE">
	<%-- all available menuitems: <PARAM NAME="MENUITEMS" VALUE="FILE,EDIT,VIEW,FONT,FORMAT,INSERT,TABLE,FORMS,SEARCH,TOOLS,HELP,DEBUG"> --%>
	<PARAM NAME="MENUITEMS" VALUE="EDIT,FONT,FORMAT,INSERT,TABLE,SEARCH">
  <PARAM NAME="SERVLETMODE" VALUE="cgi">
<%--
	<PARAM NAME="SERVLETURL" VALUE="<%= request.getScheme() %>://<%= getServerUrl(request) %>/Portal.do?command=Images&out=text">
	<PARAM NAME="IMAGEDIR" VALUE="/media/images/">
	<PARAM NAME="FILEDIR" VALUE="/media/images/">
--%>
<%--
	<PARAM NAME="SYSTEMID" VALUE="">
	<PARAM NAME="POSTCONTENTURL" VALUE="http://www.xeinfach.de/media/projects/kafenio/demo/postTester.php">
--%>
	<PARAM NAME="CONTENTPARAMETER" VALUE="message">
	<PARAM NAME="OUTPUTMODE" VALUE="off">
	<PARAM NAME="BGCOLOR" VALUE="#FFFFFF">
	<PARAM NAME="DEBUG" VALUE="false">
  <dhv:evaluate if="<%= hasText(newsArticle.getMessage()) %>">
	  <PARAM NAME="BASE64" VALUE="true">
    <PARAM NAME="DOCUMENT" VALUE="<%= StringUtils.toBase64(newsArticle.getMessage()) %>">
  </dhv:evaluate>
</APPLET>
</dhv:evaluate>
              
</td>
            <td valign="top">
              <font color="red">*</font>
              <%= showAttribute(request, "messageError") %>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <br />
  <input type="submit" value=" Save " />
  <dhv:evaluate if="<%= newsArticle.getMessage() != null %>">
    <input type="button" value="Delete this page" onClick="javascript:window.location.href='ProjectManagementNews.do?command=DeletePage&pid=<%= Project.getId() %>&id=<%= newsArticle.getId() %>';">
  </dhv:evaluate>
  <input type="button" value="Cancel" onClick="javascript:window.location.href='ProjectManagementNews.do?command=Edit&pid=<%= Project.getId() %>&id=<%= newsArticle.getId() %>';"><br>
  <input type="hidden" name="projectId" value="<%= Project.getId() %>" />
  <input type="hidden" name="id" value="<%= newsArticle.getId() %>" />
  <input type="hidden" name="modified" value="<%= newsArticle.getModified() %>" />
  <input type="hidden" name="newPage" value="false" />
  <input type="hidden" name="return" value="<%= request.getParameter("return") %>"/>
</form>
</body>