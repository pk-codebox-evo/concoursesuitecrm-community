package org.aspcfs.modules.quotes.base;

import java.sql.*;
import java.text.*;
import java.util.*;
import org.aspcfs.utils.web.PagedListInfo;
import org.aspcfs.utils.DatabaseUtils;
import org.aspcfs.utils.DateUtils;

/**
 *  Description of the Class
 *
 * @author     ananth
 * @created    March 24, 2004
 * @version    $Id$
 */
public class QuoteProductOptionList extends ArrayList {
  private int itemId = -1;
  private int statusId = -1;


  /**
   *  Sets the itemId attribute of the QuoteProductOptionList object
   *
   * @param  tmp  The new itemId value
   */
  public void setItemId(int tmp) {
    this.itemId = tmp;
  }


  /**
   *  Sets the itemId attribute of the QuoteProductOptionList object
   *
   * @param  tmp  The new itemId value
   */
  public void setItemId(String tmp) {
    this.itemId = Integer.parseInt(tmp);
  }


  /**
   *  Sets the statusId attribute of the QuoteProductOptionList object
   *
   * @param  tmp  The new statusId value
   */
  public void setStatusId(int tmp) {
    this.statusId = tmp;
  }


  /**
   *  Sets the statusId attribute of the QuoteProductOptionList object
   *
   * @param  tmp  The new statusId value
   */
  public void setStatusId(String tmp) {
    this.statusId = Integer.parseInt(tmp);
  }


  /**
   *  Gets the itemId attribute of the QuoteProductOptionList object
   *
   * @return    The itemId value
   */
  public int getItemId() {
    return itemId;
  }


  /**
   *  Gets the statusId attribute of the QuoteProductOptionList object
   *
   * @return    The statusId value
   */
  public int getStatusId() {
    return statusId;
  }


  /**
   *Constructor for the QuoteProductOptionList object
   */
  public QuoteProductOptionList() { }


  /**
   *  Description of the Method
   *
   * @param  db                Description of the Parameter
   * @exception  SQLException  Description of the Exception
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
        "FROM quote_product_options qpo " +
        "WHERE qpo.quote_product_option_id > -1 ");
    createFilter(sqlFilter);
    sqlOrder.append("ORDER BY status_id");
    //Build a base SQL statement for returning records
    sqlSelect.append("SELECT ");
    sqlSelect.append(
        "     opt.quote_product_option_id, opt.item_id, opt.product_option_id, " +
        "     opt.quantity, opt.price_currency, opt.price_amount, opt.recurring_currency, " +
        "     opt.recurring_amount, opt.recurring_type, opt.extended_price, " +
        "     opt.total_price, opt.status_id, " +
        "     prod.product_id " +
        " FROM quote_product_options opt, quote_product prod " +
        " WHERE opt.item_id = prod.item_id AND opt.quote_product_option_id > -1 ");

    pst = db.prepareStatement(sqlSelect.toString() + sqlFilter.toString() + sqlOrder.toString());
    items = prepareFilter(pst);
    rs = pst.executeQuery();
    while (rs.next()) {
      QuoteProductOption thisQuoteProductOption = new QuoteProductOption(rs);
      this.add(thisQuoteProductOption);
    }
    rs.close();
    pst.close();
  }


  /**
   *  Description of the Method
   *
   * @param  sqlFilter  Description of the Parameter
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
  }


  /**
   *  Description of the Method
   *
   * @param  pst               Description of the Parameter
   * @return                   Description of the Return Value
   * @exception  SQLException  Description of the Exception
   */
  protected int prepareFilter(PreparedStatement pst) throws SQLException {
    int i = 0;
    if (itemId > -1) {
      pst.setInt(++i, itemId);
    }

    if (statusId > -1) {
      pst.setInt(++i, statusId);
    }
    return i;
  }


  /**
   *  Description of the Method
   *
   * @param  db                Description of the Parameter
   * @exception  SQLException  Description of the Exception
   */
  public void insert(Connection db) throws SQLException {
    Iterator i = this.iterator();
    while (i.hasNext()) {
      QuoteProductOption thisOption = (QuoteProductOption) i.next();
      thisOption.insert(db);
    }
  }


  /**
   *  Description of the Method
   *
   * @param  db                Description of the Parameter
   * @exception  SQLException  Description of the Exception
   */
  public void delete(Connection db) throws SQLException {
    Iterator i = this.iterator();
    while (i.hasNext()) {
      QuoteProductOption thisOption = (QuoteProductOption) i.next();
      thisOption.delete(db);
    }
  }
}
