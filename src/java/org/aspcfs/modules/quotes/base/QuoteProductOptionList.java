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
package org.aspcfs.modules.quotes.base;

import org.aspcfs.modules.base.Constants;
import org.aspcfs.utils.DatabaseUtils;
import org.aspcfs.utils.web.PagedListInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Description of the Class
 *
 * @author ananth
 * @version $Id: QuoteProductOptionList.java,v 1.2 2004/05/04 15:52:27
 *          mrajkowski Exp $
 * @created March 24, 2004
 */
public class QuoteProductOptionList extends ArrayList {
  public final static String tableName = "quote_product_options";
  public final static String uniqueField = "quote_product_option_id";
  private java.sql.Timestamp lastAnchor = null;
  private java.sql.Timestamp nextAnchor = null;
  private int syncType = Constants.NO_SYNC;
  private PagedListInfo pagedListInfo = null;

  private int itemId = -1;
  private int statusId = -1;
  //resources
  private boolean buildConfigDetails = false;

  /**
   * Sets the lastAnchor attribute of the QuoteProductOptionList object
   *
   * @param tmp The new lastAnchor value
   */
  public void setLastAnchor(java.sql.Timestamp tmp) {
    this.lastAnchor = tmp;
  }


  /**
   * Sets the lastAnchor attribute of the QuoteProductOptionList object
   *
   * @param tmp The new lastAnchor value
   */
  public void setLastAnchor(String tmp) {
    this.lastAnchor = java.sql.Timestamp.valueOf(tmp);
  }


  /**
   * Sets the nextAnchor attribute of the QuoteProductOptionList object
   *
   * @param tmp The new nextAnchor value
   */
  public void setNextAnchor(java.sql.Timestamp tmp) {
    this.nextAnchor = tmp;
  }


  /**
   * Sets the nextAnchor attribute of the QuoteProductOptionList object
   *
   * @param tmp The new nextAnchor value
   */
  public void setNextAnchor(String tmp) {
    this.nextAnchor = java.sql.Timestamp.valueOf(tmp);
  }


  /**
   * Sets the syncType attribute of the QuoteProductOptionList object
   *
   * @param tmp The new syncType value
   */
  public void setSyncType(int tmp) {
    this.syncType = tmp;
  }

  /**
   * Sets the PagedListInfo attribute of the QuoteProductOptionList object. <p>
   * <p/>
   * The query results will be constrained to the PagedListInfo parameters.
   *
   * @param tmp The new PagedListInfo value
   * @since 1.1
   */
  public void setPagedListInfo(PagedListInfo tmp) {
    this.pagedListInfo = tmp;
  }

  /**
   * Gets the tableName attribute of the QuoteProductOptionList object
   *
   * @return The tableName value
   */
  public String getTableName() {
    return tableName;
  }


  /**
   * Gets the uniqueField attribute of the QuoteProductOptionList object
   *
   * @return The uniqueField value
   */
  public String getUniqueField() {
    return uniqueField;
  }


  /**
   * Gets the buildConfigDetails attribute of the QuoteProductOptionList object
   *
   * @return The buildConfigDetails value
   */
  public boolean getBuildConfigDetails() {
    return buildConfigDetails;
  }


  /**
   * Sets the buildConfigDetails attribute of the QuoteProductOptionList object
   *
   * @param tmp The new buildConfigDetails value
   */
  public void setBuildConfigDetails(boolean tmp) {
    this.buildConfigDetails = tmp;
  }


  /**
   * Sets the buildConfigDetails attribute of the QuoteProductOptionList object
   *
   * @param tmp The new buildConfigDetails value
   */
  public void setBuildConfigDetails(String tmp) {
    this.buildConfigDetails = DatabaseUtils.parseBoolean(tmp);
  }


  /**
   * Sets the itemId attribute of the QuoteProductOptionList object
   *
   * @param tmp The new itemId value
   */
  public void setItemId(int tmp) {
    this.itemId = tmp;
  }


  /**
   * Sets the itemId attribute of the QuoteProductOptionList object
   *
   * @param tmp The new itemId value
   */
  public void setItemId(String tmp) {
    this.itemId = Integer.parseInt(tmp);
  }


  /**
   * Sets the statusId attribute of the QuoteProductOptionList object
   *
   * @param tmp The new statusId value
   */
  public void setStatusId(int tmp) {
    this.statusId = tmp;
  }


  /**
   * Sets the statusId attribute of the QuoteProductOptionList object
   *
   * @param tmp The new statusId value
   */
  public void setStatusId(String tmp) {
    this.statusId = Integer.parseInt(tmp);
  }


  /**
   * Gets the itemId attribute of the QuoteProductOptionList object
   *
   * @return The itemId value
   */
  public int getItemId() {
    return itemId;
  }


  /**
   * Gets the statusId attribute of the QuoteProductOptionList object
   *
   * @return The statusId value
   */
  public int getStatusId() {
    return statusId;
  }


  /**
   * Constructor for the QuoteProductOptionList object
   */
  public QuoteProductOptionList() {
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
    //Build a base SQL statement for counting records
    sqlCount.append(
        "SELECT COUNT(*) AS recordcount " +
            "FROM quote_product_options opt " +
            "LEFT JOIN quote_product_option_boolean bool ON ( opt.quote_product_option_id = bool.quote_product_option_id ) " +
            "LEFT JOIN quote_product_option_float " + DatabaseUtils.addQuotes(db, "float") + " ON ( opt.quote_product_option_id = " + DatabaseUtils.addQuotes(db, "float") + ".quote_product_option_id ) " +
            "LEFT JOIN quote_product_option_timestamp tst ON ( opt.quote_product_option_id = tst.quote_product_option_id ) " +
            "LEFT JOIN quote_product_option_integer intr ON ( opt.quote_product_option_id = intr.quote_product_option_id ) " +
            "LEFT JOIN quote_product_option_text txt ON ( opt.quote_product_option_id = txt.quote_product_option_id ) " +
            "LEFT JOIN product_option_map pom ON (opt.product_option_id = pom.product_option_id) " +
            "LEFT JOIN product_option po ON (pom.option_id = po.option_id), " +
            "quote_product prod " +
            "WHERE opt.item_id = prod.item_id " +
            "AND opt.quote_product_option_id > -1 ");
    createFilter(sqlFilter);
    sqlOrder.append("ORDER BY opt.status_id");
    //Build a base SQL statement for returning records
    sqlSelect.append("SELECT ");
    sqlSelect.append(
        "opt.*, " +
            "bool." + DatabaseUtils.addQuotes(db, "value") + " AS boolean_value, " +
            "" + DatabaseUtils.addQuotes(db, "float") + "." + DatabaseUtils.addQuotes(db, "value") + " AS float_value, intr." + DatabaseUtils.addQuotes(db, "value") + " AS integer_value, " +
            "tst." + DatabaseUtils.addQuotes(db, "value") + " AS timestamp_value, txt." + DatabaseUtils.addQuotes(db, "value") + " AS text_value, " +
            "pom.option_id, po.configurator_id, " +
            "prod.product_id " +
            "FROM quote_product_options opt " +
            "LEFT JOIN quote_product_option_boolean bool ON ( opt.quote_product_option_id = bool.quote_product_option_id ) " +
            "LEFT JOIN quote_product_option_float " + DatabaseUtils.addQuotes(db, "float") + " ON ( opt.quote_product_option_id = " + DatabaseUtils.addQuotes(db, "float") + ".quote_product_option_id ) " +
            "LEFT JOIN quote_product_option_timestamp tst ON ( opt.quote_product_option_id = tst.quote_product_option_id ) " +
            "LEFT JOIN quote_product_option_integer intr ON ( opt.quote_product_option_id = intr.quote_product_option_id ) " +
            "LEFT JOIN quote_product_option_text txt ON ( opt.quote_product_option_id = txt.quote_product_option_id ) " +
            "LEFT JOIN product_option_map pom ON (opt.product_option_id = pom.product_option_id) " +
            "LEFT JOIN product_option po ON (pom.option_id = po.option_id), " +
            "quote_product prod " +
            "WHERE opt.item_id = prod.item_id " +
            "AND opt.quote_product_option_id > -1 ");
    pst = db.prepareStatement(
        sqlSelect.toString() + sqlFilter.toString() + sqlOrder.toString());
    items = prepareFilter(pst);
    rs = pst.executeQuery();
    while (rs.next()) {
      QuoteProductOption thisQuoteProductOption = new QuoteProductOption(rs);
      this.add(thisQuoteProductOption);
    }
    rs.close();
    pst.close();
    if (buildConfigDetails) {
      Iterator i = this.iterator();
      while (i.hasNext()) {
        QuoteProductOption thisOption = (QuoteProductOption) i.next();
        thisOption.buildConfigDetails(db);
      }
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
    if (itemId > -1) {
      sqlFilter.append("AND opt.item_id = ? ");
    }
    if (statusId > -1) {
      sqlFilter.append("AND opt.status_id = ? ");
    }
    if (syncType == Constants.SYNC_INSERTS) {
      if (lastAnchor != null) {
        sqlFilter.append("AND o.entered > ? ");
      }
      sqlFilter.append("AND o.entered < ? ");
    }
    if (syncType == Constants.SYNC_UPDATES) {
      sqlFilter.append("AND o.modified > ? ");
      sqlFilter.append("AND o.entered < ? ");
      sqlFilter.append("AND o.modified < ? ");
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
    if (itemId > -1) {
      pst.setInt(++i, itemId);
    }

    if (statusId > -1) {
      pst.setInt(++i, statusId);
    }
    if (syncType == Constants.SYNC_INSERTS) {
      if (lastAnchor != null) {
        pst.setTimestamp(++i, lastAnchor);
      }
      pst.setTimestamp(++i, nextAnchor);
    }
    if (syncType == Constants.SYNC_UPDATES) {
      pst.setTimestamp(++i, lastAnchor);
      pst.setTimestamp(++i, lastAnchor);
      pst.setTimestamp(++i, nextAnchor);
    }

    return i;
  }


  /**
   * Description of the Method
   *
   * @param db Description of the Parameter
   * @throws SQLException Description of the Exception
   */
  public void insert(Connection db) throws SQLException {
    Iterator i = this.iterator();
    while (i.hasNext()) {
      QuoteProductOption thisOption = (QuoteProductOption) i.next();
      thisOption.insert(db);
    }
  }


  /**
   * Description of the Method
   *
   * @param db Description of the Parameter
   * @throws SQLException Description of the Exception
   */
  public void delete(Connection db) throws SQLException {
    Iterator i = this.iterator();
    while (i.hasNext()) {
      QuoteProductOption thisOption = (QuoteProductOption) i.next();
      thisOption.delete(db);
    }
  }
}

