<%-- 
  - Copyright(c) 2004 Concursive Corporation (http://www.concursive.com/) All
  - rights reserved. This material cannot be distributed without written
  - permission from Concursive Corporation. Permission to use, copy, and modify
  - this material for internal use is hereby granted, provided that the above
  - copyright notice and this permission notice appear in all copies. CONCURSIVE
  - CORPORATION MAKES NO REPRESENTATIONS AND EXTENDS NO WARRANTIES, EXPRESS OR
  - IMPLIED, WITH RESPECT TO THE SOFTWARE, INCLUDING, BUT NOT LIMITED TO, THE
  - IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR ANY PARTICULAR
  - PURPOSE, AND THE WARRANTY AGAINST INFRINGEMENT OF PATENTS OR OTHER
  - INTELLECTUAL PROPERTY RIGHTS. THE SOFTWARE IS PROVIDED "AS IS", AND IN NO
  - EVENT SHALL CONCURSIVE CORPORATION OR ANY OF ITS AFFILIATES BE LIABLE FOR
  - ANY DAMAGES, INCLUDING ANY LOST PROFITS OR OTHER INCIDENTAL OR CONSEQUENTIAL
  - DAMAGES RELATING TO THE SOFTWARE.
  - 
  - Version: $Id$
  - Description: 
  --%>
<%@ taglib uri="/WEB-INF/dhv-taglib.tld" prefix="dhv" %>
<script language="javascript">
  var thisOrgId = -1;
  var thisHeaderId = -1;
  var menu_init = false;
  //Set the action parameters for clicked item
  function displayMenu(loc, id, orgId, headerId, trashed, hasPermission) {
    thisOrgId = orgId;
    thisHeaderId = headerId;
    if (hasPermission == 'true') {
      updateMenu(trashed);
    } else {
      updateMenu('true');
    }
    if (!menu_init) {
      menu_init = true;
      new ypSlideOutMenu("menuOpp", "down", 0, 0, 170, getHeight("menuOppTable"));
    }
    return ypSlideOutMenu.displayDropMenu(id, loc);
  }
  function updateMenu(trashed){
    if (trashed == 'true'){
      hideSpan('menuRename');
      hideSpan('menuModify');
      hideSpan('menuDelete');
    } else {
      showSpan('menuRename');
      showSpan('menuModify');
      showSpan('menuDelete');
    }
  }
  //Menu link functions
  function details() {
    window.location.href='Opportunities.do?command=Details&headerId=' + thisHeaderId + '&orgId=' + thisOrgId + '&reset=true<%= addLinkParams(request, "popup|popupType|actionId|actionplan") %>';
  }

  function modify() {
    window.location.href='Opportunities.do?command=Modify&headerId=' + thisHeaderId + '&orgId=' + thisOrgId + '&return=list<%= addLinkParams(request, "popup|popupType|actionId|actionplan") %>';
  }

  function deleteOpp() {
    popURLReturn('Opportunities.do?command=ConfirmDelete&orgId=' + thisOrgId + '&headerId=' + thisHeaderId+'&popup=true<%= isPopup(request)?"&popupType=inline":"" %>','Opportunities.do?command=View&orgId=' + thisOrgId + '&popup=true', 'Delete_opp','320','200','yes','no');
  }
</script>
<div id="menuOppContainer" class="menu">
  <div id="menuOppContent">
    <table id="menuOppTable" class="pulldown" width="170" cellspacing="0">
      <dhv:permission name="accounts-accounts-opportunities-view">
      <tr id="menuView" onmouseover="cmOver(this)" onmouseout="cmOut(this)" onclick="details()">
        <th>
          <img src="images/icons/stock_zoom-page-16.gif" border="0" align="absmiddle" height="16" width="16"/>
        </th>
        <td width="100%">
          <dhv:label name="accounts.accounts_calls_list_menu.ViewDetails">View Details</dhv:label>
        </td>
      </tr>
      </dhv:permission>
      <dhv:permission name="accounts-accounts-opportunities-edit">
      <tr id="menuRename" onmouseover="cmOver(this)" onmouseout="cmOut(this)" onclick="modify()">
        <th>
          <img src="images/icons/stock_edit-16.gif" border="0" align="absmiddle" height="16" width="16"/>
        </th>
        <td width="100%">
          <dhv:label name="accounts.Rename">Rename</dhv:label>
        </td>
      </tr>
      </dhv:permission>
      <dhv:permission name="accounts-accounts-opportunities-delete">
      <tr id="menuDelete" onmouseover="cmOver(this)" onmouseout="cmOut(this)" onclick="deleteOpp()">
        <th>
          <img src="images/icons/stock_delete-16.gif" border="0" align="absmiddle" height="16" width="16"/>
        </th>
        <td width="100%">
          <dhv:label name="global.button.delete">Delete</dhv:label>
        </td>
      </tr>
      </dhv:permission>
    </table>
  </div>
</div>
