//Copyright 2001-2002 Dark Horse Ventures

package org.aspcfs.modules.admin.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;
import com.darkhorseventures.framework.beans.*;
import org.aspcfs.utils.*;
import org.aspcfs.utils.web.*;
import org.aspcfs.modules.base.Dependency;
import org.aspcfs.modules.base.DependencyList;
import org.aspcfs.modules.admin.base.*;
import org.aspcfs.modules.base.Constants;
import org.aspcfs.utils.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *  Builds a list of user Roles
 *
 *@author     matt rajkowski
 *@created    < January 14, 2002
 *@version    $Id$
 */
public class RoleList extends ArrayList {

  private PagedListInfo pagedListInfo = null;
  private String emptyHtmlSelectRecord = null;
  private int enabledState = Constants.TRUE;
  private boolean buildUsers = false;
  private boolean buildUserCount = false;
  private int roleType = -1;
  private int excludeRoleType = -1;


  /**
   *  Constructor for the RoleList object
   */
  public RoleList() { }


  /**
   *  Sets the pagedListInfo attribute of the RoleList object
   *
   *@param  tmp  The new pagedListInfo value
   */
  public void setPagedListInfo(PagedListInfo tmp) {
    this.pagedListInfo = tmp;
  }


  /**
   *  Sets the emptyHtmlSelectRecord attribute of the RoleList object
   *
   *@param  tmp  The new emptyHtmlSelectRecord value
   */
  public void setEmptyHtmlSelectRecord(String tmp) {
    this.emptyHtmlSelectRecord = tmp;
  }


  /**
   *  Sets the enabledState attribute of the RoleList object
   *
   *@param  booleanInt  The new enabledState value
   */
  public void setEnabledState(int booleanInt) {
    enabledState = booleanInt;
  }


  /**
   *  Sets the buildUsers attribute of the RoleList object
   *
   *@param  tmp  The new buildUsers value
   */
  public void setBuildUsers(boolean tmp) {
    this.buildUsers = tmp;
  }


  /**
   *  Sets the buildUserCount attribute of the RoleList object
   *
   *@param  tmp  The new buildUserCount value
   */
  public void setBuildUserCount(boolean tmp) {
    this.buildUserCount = tmp;
  }


  /**
   *  Sets the roleType attribute of the RoleList object
   *
   *@param  tmp  The new roleType value
   */
  public void setRoleType(int tmp) {
    this.roleType = tmp;
  }


  /**
   *  Sets the roleType attribute of the RoleList object
   *
   *@param  tmp  The new roleType value
   */
  public void setRoleType(String tmp) {
    this.roleType = Integer.parseInt(tmp);
  }

  public void setExcludeRoleType(int tmp) { this.excludeRoleType = tmp; }

  /**
   *  Gets the htmlSelect attribute of the RoleList object
   *
   *@param  selectName  Description of the Parameter
   *@return             The htmlSelect value
   */
  public String getHtmlSelect(String selectName) {
    return getHtmlSelect(selectName, -1);
  }


  /**
   *  Gets the htmlSelect attribute of the RoleList object
   *  Selects the regular roles from the role list
   *
   *@param  selectName  Description of the Parameter
   *@param  defaultKey  Description of the Parameter
   *@return             The htmlSelect value
   */
  public String getHtmlSelect(String selectName, int defaultKey) {
    HtmlSelect roleListSelect = new HtmlSelect();
    if (emptyHtmlSelectRecord != null) {
      roleListSelect.addItem(-1, emptyHtmlSelectRecord);
    }
    Iterator i = this.iterator();
    while (i.hasNext()) {
      Role thisRole = (Role) i.next();
        roleListSelect.addItem(
            thisRole.getId(),
            thisRole.getRole());
      }
    return roleListSelect.getHtml(selectName, defaultKey);
  }


  /**
   *  Description of the Method
   *
   *@param  db                Description of the Parameter
   *@exception  SQLException  Description of the Exception
   */
  public void buildList(Connection db) throws SQLException {
    PreparedStatement pst = null;
    ResultSet rs = null;
    int items = -1;
    StringBuffer sqlSelect = new StringBuffer();
    StringBuffer sqlCount = new StringBuffer();
    StringBuffer sqlFilter = new StringBuffer();
    StringBuffer sqlOrder = new StringBuffer();
    //Need to build a base SQL statement for counting records
    sqlCount.append(
        "SELECT COUNT(*) AS recordcount " +
        "FROM role r " +
        "WHERE r.role_id > -1 ");
    createFilter(sqlFilter);
    if (pagedListInfo != null) {
      //Get the total number of records matching filter
      pst = db.prepareStatement(sqlCount.toString() +
          sqlFilter.toString());
      items = prepareFilter(pst);
      rs = pst.executeQuery();
      if (rs.next()) {
        int maxRecords = rs.getInt("recordcount");
        pagedListInfo.setMaxRecords(maxRecords);
      }
      rs.close();
      pst.close();
      //Determine the offset, based on the filter, for the first record to show
      if (!pagedListInfo.getCurrentLetter().equals("")) {
        pst = db.prepareStatement(sqlCount.toString() +
            sqlFilter.toString() +
            "AND lower(role) < ? ");
        items = prepareFilter(pst);
        pst.setString(++items, pagedListInfo.getCurrentLetter().toLowerCase());
        rs = pst.executeQuery();
        if (rs.next()) {
          int offsetCount = rs.getInt("recordcount");
          pagedListInfo.setCurrentOffset(offsetCount);
        }
        rs.close();
        pst.close();
      }
      //Determine column to sort by
      pagedListInfo.setDefaultSort("role", null);
      pagedListInfo.appendSqlTail(db, sqlOrder);
    } else {
      sqlOrder.append("ORDER BY role ");
    }
    //Need to build a base SQL statement for returning records
    sqlSelect.append(
        "SELECT * " +
        "FROM role r " +
        "WHERE r.role_id > -1 ");
    pst = db.prepareStatement(sqlSelect.toString() + sqlFilter.toString() + sqlOrder.toString());
    items = prepareFilter(pst);
    rs = pst.executeQuery();
    if (pagedListInfo != null) {
      pagedListInfo.doManualOffset(db, rs);
    }
    int count = 0;
    while (rs.next()) {
      if (pagedListInfo != null && pagedListInfo.getItemsPerPage() > 0 &&
          DatabaseUtils.getType(db) == DatabaseUtils.MSSQL &&
          count >= pagedListInfo.getItemsPerPage()) {
        break;
      }
      ++count;
      Role thisRole = new Role(rs);
      this.add(thisRole);
    }
    rs.close();
    pst.close();
    //Build resources
    if (buildUsers || buildUserCount) {
      Iterator i = this.iterator();
      while (i.hasNext()) {
        Role thisRole = (Role) i.next();
        if (buildUsers) {
          thisRole.buildUserList(db);
        }
        if (buildUserCount) {
          thisRole.buildUserCount(db, true);
        }
      }
    }
  }


  /**
   *  Description of the Method
   *
   *@param  sqlFilter  Description of the Parameter
   */
  private void createFilter(StringBuffer sqlFilter) {
    if (sqlFilter == null) {
      sqlFilter = new StringBuffer();
    }
    if (roleType != -1) {
      sqlFilter.append("AND role_type = ? ");
    }
    if (excludeRoleType != -1) {
      sqlFilter.append("AND role_type <> ? ");
    }
    if (enabledState > -1) {
      sqlFilter.append("AND enabled = ? ");
    }
  }


  /**
   *  Description of the Method
   *
   *@param  pst               Description of the Parameter
   *@return                   Description of the Return Value
   *@exception  SQLException  Description of the Exception
   */
  private int prepareFilter(PreparedStatement pst) throws SQLException {
    int i = 0;
    if (roleType != -1) {
      pst.setInt(++i, roleType);
    }
    if (excludeRoleType != -1) {
      pst.setInt(++i, excludeRoleType);
    }
    if (enabledState > -1) {
      pst.setBoolean(++i, enabledState == Constants.TRUE);
    }
    return i;
  }

}

