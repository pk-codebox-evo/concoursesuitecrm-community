/*
 *  Copyright(c) 2004 Concursive Corporation (http://www.concursive.com/) All
 *  rights reserved. This material cannot be distributed without written
 *  permission from Concursive Corporation. Permission to use, copy, and modify
 *  this material for internal use is hereby granted, provided that the above
 *  copyright notice and this permission notice appear in all copies. CONCURSIVE
 *  CORPORATION MAKES NO REPRESENTATIONS AND EXTENDS NO WARRANTIES, EXPRESS OR
 *  IMPLIED, WITH RESPECT TO THE SOFTWARE, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR ANY PARTICULAR
 *  PURPOSE, AND THE WARRANTY AGAINST INFRINGEMENT OF PATENTS OR OTHER
 *  INTELLECTUAL PROPERTY RIGHTS. THE SOFTWARE IS PROVIDED "AS IS", AND IN NO
 *  EVENT SHALL CONCURSIVE CORPORATION OR ANY OF ITS AFFILIATES BE LIABLE FOR
 *  ANY DAMAGES, INCLUDING ANY LOST PROFITS OR OTHER INCIDENTAL OR CONSEQUENTIAL
 *  DAMAGES RELATING TO THE SOFTWARE.
 */
package org.aspcfs.modules.tasks.base;

import org.aspcfs.controller.SystemStatus;
import org.aspcfs.utils.DatabaseUtils;
import org.aspcfs.utils.web.HtmlSelect;
import org.aspcfs.utils.web.PagedListInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Queries the task category list depending on the supplied parameters
 *
 * @author matt rajkowski
 * @version $Id: TaskCategoryList.java,v 1.3 2002/12/04 13:12:42 mrajkowski
 *          Exp $
 * @created November 17, 2002
 */
public class TaskCategoryList extends ArrayList {

  protected PagedListInfo pagedListInfo = null;
  protected int projectId = -1;


  /**
   * Constructor for the TaskCategoryList object
   */
  public TaskCategoryList() {
  }


  /**
   * Sets the pagedListInfo attribute of the TaskCategoryList object
   *
   * @param tmp The new pagedListInfo value
   */
  public void setPagedListInfo(PagedListInfo tmp) {
    this.pagedListInfo = tmp;
  }


  /**
   * Sets the projectId attribute of the TaskCategoryList object
   *
   * @param tmp The new projectId value
   */
  public void setProjectId(int tmp) {
    this.projectId = tmp;
  }


  /**
   * Description of the Method
   *
   * @param db Description of the Parameter
   * @throws SQLException Description of the Exception
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
        "FROM lookup_task_category c " +
        "WHERE c.code > -1 ");

    createFilter(sqlFilter);

    if (pagedListInfo != null) {
      //Get the total number of records matching filter
      pst = db.prepareStatement(sqlCount.toString() + sqlFilter.toString());
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
        pst = db.prepareStatement(
            sqlCount.toString() +
            sqlFilter.toString() +
            "AND c.description > ? ");
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
      pagedListInfo.setDefaultSort("c." + DatabaseUtils.addQuotes(db, "level") + ", c.description", null);
      pagedListInfo.appendSqlTail(db, sqlOrder);
    } else {
      sqlOrder.append("ORDER BY c." + DatabaseUtils.addQuotes(db, "level") + ", c.description ");
    }

    //Need to build a base SQL statement for returning records
    if (pagedListInfo != null) {
      pagedListInfo.appendSqlSelectHead(db, sqlSelect);
    } else {
      sqlSelect.append("SELECT ");
    }
    sqlSelect.append(
        "c.code, c.description, c.default_item, c." + DatabaseUtils.addQuotes(db, "level") + ", c.enabled " +
        "FROM lookup_task_category c " +
        "WHERE c.code > -1 ");
    pst = db.prepareStatement(
        sqlSelect.toString() + sqlFilter.toString() + sqlOrder.toString());
    items = prepareFilter(pst);
    if (pagedListInfo != null) {
      pagedListInfo.doManualOffset(db, pst);
    }
    rs = pst.executeQuery();
    if (pagedListInfo != null) {
      pagedListInfo.doManualOffset(db, rs);
    }
    while (rs.next()) {
      TaskCategory thisCategory = new TaskCategory(rs);
      this.add(thisCategory);
    }
    rs.close();
    pst.close();

    Iterator categories = this.iterator();
    while (categories.hasNext()) {
      TaskCategory thisCategory = (TaskCategory) categories.next();
      thisCategory.buildResources(db);
    }
  }


  /**
   * Description of the Method
   *
   * @param sqlFilter Description of the Parameter
   */
  protected void createFilter(StringBuffer sqlFilter) {
    if (sqlFilter == null) {
      sqlFilter = new StringBuffer();
    }

    if (projectId > 0) {
      sqlFilter.append(
          "AND c.code IN (SELECT category_id FROM taskcategory_project WHERE project_id = ?) ");
    }
  }


  /**
   * Description of the Method
   *
   * @param pst Description of the Parameter
   * @return Description of the Return Value
   * @throws SQLException Description of the Exception
   */
  protected int prepareFilter(PreparedStatement pst) throws SQLException {
    int i = 0;

    if (projectId > 0) {
      pst.setInt(++i, projectId);
    }
    return i;
  }


  /**
   * Description of the Method
   *
   * @param db Description of the Parameter
   * @throws SQLException Description of the Exception
   */
  public void delete(Connection db) throws SQLException {
    Iterator categories = this.iterator();
    while (categories.hasNext()) {
      TaskCategory thisCategory = (TaskCategory) categories.next();
      thisCategory.delete(db);
    }
  }


  /**
   * Gets the htmlSelect attribute of the TaskCategoryList object
   *
   * @param selectName Description of the Parameter
   * @param selectedId Description of the Parameter
   * @return The htmlSelect value
   */
  public String getHtmlSelect(SystemStatus thisSystem, String selectName, int selectedId) {
    HtmlSelect thisSelect = new HtmlSelect();
    thisSelect.addItem(-1, thisSystem.getLabel("calendar.none.4dashes"));
    Iterator i = this.iterator();
    while (i.hasNext()) {
      TaskCategory thisCategory = (TaskCategory) i.next();
      thisSelect.addItem(
          thisCategory.getId(),
          thisCategory.getDescription());
    }
    return thisSelect.getHtml(selectName, selectedId);
  }


  /**
   * Gets the valueFromId attribute of the TaskCategoryList object
   *
   * @param id Description of the Parameter
   * @return The valueFromId value
   */
  public String getValueFromId(int id) {
    Iterator i = this.iterator();
    while (i.hasNext()) {
      TaskCategory thisCategory = (TaskCategory) i.next();
      if (thisCategory.getId() == id) {
        return thisCategory.getDescription();
      }
    }
    return null;
  }


  /**
   * Gets the htmlSelect attribute of the TaskCategoryList object
   *
   * @return The htmlSelect value
   */
  public HtmlSelect getHtmlSelect() {
    HtmlSelect thisSelect = new HtmlSelect();
    Iterator i = this.iterator();
    while (i.hasNext()) {
      TaskCategory thisCategory = (TaskCategory) i.next();
      thisSelect.addItem(
          thisCategory.getId(),
          thisCategory.getDescription());
    }
    return thisSelect;
  }
}

