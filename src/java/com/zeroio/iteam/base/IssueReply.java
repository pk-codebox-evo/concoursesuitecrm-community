/*
 *  Copyright 2000-2003 Matt Rajkowski
 *  matt@zeroio.com
 *  http://www.mavininteractive.com
 *  This class cannot be modified, distributed or used without
 *  permission from Matt Rajkowski
 */
package com.zeroio.iteam.base;

import java.sql.*;
import java.util.Calendar;
import java.text.*;
import com.darkhorseventures.framework.beans.*;
import com.darkhorseventures.framework.actions.*;
import org.aspcfs.utils.DatabaseUtils;

/**
 *  Description of the Class
 *
 *@author     matt rajkowski
 *@created    January 15, 2003
 *@version    $Id$
 */
public class IssueReply extends GenericBean {

  private int id = -1;
  private int replyToId = -1;
  private String subject = null;
  private String body = "";
  private int importance = -1;
  //private boolean enabled = false;
  private java.sql.Timestamp entered = null;
  private int enteredBy = -1;
  private java.sql.Timestamp modified = null;
  private int modifiedBy = -1;

  private int issueId = -1;
  private Issue issue = null;
  private String user = "";


  /**
   *  Constructor for the IssueReply object
   */
  public IssueReply() { }


  /**
   *  Constructor for the IssueReply object
   *
   *@param  rs                Description of the Parameter
   *@exception  SQLException  Description of the Exception
   */
  public IssueReply(ResultSet rs) throws SQLException {
    buildRecord(rs);
  }


  /**
   *  Constructor for the IssueReply object
   *
   *@param  db                Description of the Parameter
   *@param  replyId           Description of the Parameter
   *@exception  SQLException  Description of the Exception
   */
  public IssueReply(Connection db, int replyId) throws SQLException {
    queryRecord(db, replyId);
  }


  /**
   *  Constructor for the IssueReply object
   *
   *@param  db                Description of the Parameter
   *@param  replyId           Description of the Parameter
   *@param  issueId           Description of the Parameter
   *@exception  SQLException  Description of the Exception
   */
  public IssueReply(Connection db, int replyId, int issueId) throws SQLException {
    this.setIssueId(issueId);
    queryRecord(db, replyId);
  }


  /**
   *  Description of the Method
   *
   *@param  db                Description of the Parameter
   *@param  replyId           Description of the Parameter
   *@exception  SQLException  Description of the Exception
   */
  private void queryRecord(Connection db, int replyId) throws SQLException {
    StringBuffer sql = new StringBuffer();
    sql.append(
        "SELECT r.* " +
        "FROM project_issue_replies r " +
        "WHERE reply_id = ? ");
    if (issueId > -1) {
      sql.append("AND issue_id = ? ");
    }
    PreparedStatement pst = db.prepareStatement(sql.toString());
    pst.setInt(1, replyId);
    if (issueId > -1) {
      pst.setInt(2, issueId);
    }
    ResultSet rs = pst.executeQuery();
    if (rs.next()) {
      buildRecord(rs);
    } else {
      rs.close();
      pst.close();
      throw new SQLException("Issue Reply record not found.");
    }
    rs.close();
    pst.close();
  }


  /**
   *  Description of the Method
   *
   *@param  rs                Description of the Parameter
   *@exception  SQLException  Description of the Exception
   */
  private void buildRecord(ResultSet rs) throws SQLException {
    //project_issue_replies table
    id = rs.getInt("reply_id");
    issueId = rs.getInt("issue_id");
    replyToId = rs.getInt("reply_to");
    subject = rs.getString("subject");
    body = rs.getString("message");
    importance = rs.getInt("importance");
    entered = rs.getTimestamp("entered");
    enteredBy = rs.getInt("enteredBy");
    modified = rs.getTimestamp("modified");
    modifiedBy = rs.getInt("modifiedBy");
  }


  /**
   *  Gets the RelativeIssueDateString attribute of the Issue object
   *
   *@return    The RelativeIssueDateString value
   *@since
   */
  public String getRelativeEnteredString() {
    Calendar rightNow = Calendar.getInstance();
    rightNow.set(Calendar.HOUR_OF_DAY, 0);
    rightNow.set(Calendar.MINUTE, 0);
    Calendar issuePostedDate = Calendar.getInstance();
    issuePostedDate.setTime(entered);
    issuePostedDate.set(Calendar.HOUR_OF_DAY, 0);
    issuePostedDate.set(Calendar.MINUTE, 0);
    issuePostedDate.add(Calendar.DATE, 1);
    if (rightNow.before(issuePostedDate)) {
      return "today";
    } else {
      issuePostedDate.add(Calendar.DATE, 1);
      if (rightNow.before(issuePostedDate)) {
        return "yesterday";
      } else {
        return getEnteredString();
      }
    }
  }


  /**
   *  Sets the id attribute of the IssueReply object
   *
   *@param  tmp  The new id value
   */
  public void setId(int tmp) {
    this.id = tmp;
  }


  /**
   *  Sets the id attribute of the IssueReply object
   *
   *@param  tmp  The new id value
   */
  public void setId(String tmp) {
    this.id = Integer.parseInt(tmp);
  }


  /**
   *  Sets the replyToId attribute of the IssueReply object
   *
   *@param  tmp  The new replyToId value
   */
  public void setReplyToId(int tmp) {
    this.replyToId = tmp;
  }


  /**
   *  Sets the replyToId attribute of the IssueReply object
   *
   *@param  tmp  The new replyToId value
   */
  public void setReplyToId(String tmp) {
    this.replyToId = Integer.parseInt(tmp);
  }


  /**
   *  Sets the subject attribute of the IssueReply object
   *
   *@param  tmp  The new subject value
   */
  public void setSubject(String tmp) {
    this.subject = tmp;
  }


  /**
   *  Sets the body attribute of the IssueReply object
   *
   *@param  tmp  The new body value
   */
  public void setBody(String tmp) {
    this.body = tmp;
  }


  /**
   *  Sets the importance attribute of the IssueReply object
   *
   *@param  tmp  The new importance value
   */
  public void setImportance(int tmp) {
    this.importance = tmp;
  }


  /**
   *  Sets the importance attribute of the IssueReply object
   *
   *@param  tmp  The new importance value
   */
  public void setImportance(String tmp) {
    this.importance = Integer.parseInt(tmp);
  }


  /**
   *  Sets the entered attribute of the IssueReply object
   *
   *@param  tmp  The new entered value
   */
  public void setEntered(java.sql.Timestamp tmp) {
    this.entered = tmp;
  }


  /**
   *  Sets the entered attribute of the IssueReply object
   *
   *@param  tmp  The new entered value
   */
  public void setEntered(String tmp) {
    this.entered = DatabaseUtils.parseTimestamp(tmp);
  }


  /**
   *  Sets the enteredBy attribute of the IssueReply object
   *
   *@param  tmp  The new enteredBy value
   */
  public void setEnteredBy(int tmp) {
    this.enteredBy = tmp;
  }


  /**
   *  Sets the enteredBy attribute of the IssueReply object
   *
   *@param  tmp  The new enteredBy value
   */
  public void setEnteredBy(String tmp) {
    this.enteredBy = Integer.parseInt(tmp);
  }


  /**
   *  Sets the modified attribute of the IssueReply object
   *
   *@param  tmp  The new modified value
   */
  public void setModified(java.sql.Timestamp tmp) {
    this.modified = tmp;
  }


  /**
   *  Sets the modified attribute of the IssueReply object
   *
   *@param  tmp  The new modified value
   */
  public void setModified(String tmp) {
    this.modified = DatabaseUtils.parseTimestamp(tmp);
  }


  /**
   *  Sets the modifiedBy attribute of the IssueReply object
   *
   *@param  tmp  The new modifiedBy value
   */
  public void setModifiedBy(int tmp) {
    this.modifiedBy = tmp;
  }


  /**
   *  Sets the modifiedBy attribute of the IssueReply object
   *
   *@param  tmp  The new modifiedBy value
   */
  public void setModifiedBy(String tmp) {
    this.modifiedBy = Integer.parseInt(tmp);
  }


  /**
   *  Sets the issueId attribute of the IssueReply object
   *
   *@param  tmp  The new issueId value
   */
  public void setIssueId(int tmp) {
    this.issueId = tmp;
  }


  /**
   *  Sets the issueId attribute of the IssueReply object
   *
   *@param  tmp  The new issueId value
   */
  public void setIssueId(String tmp) {
    this.issueId = Integer.parseInt(tmp);
  }


  /**
   *  Sets the issue attribute of the IssueReply object
   *
   *@param  tmp  The new issue value
   */
  public void setIssue(Issue tmp) {
    this.issue = tmp;
  }


  /**
   *  Sets the user attribute of the IssueReply object
   *
   *@param  tmp  The new user value
   */
  public void setUser(String tmp) {
    this.user = tmp;
  }


  /**
   *  Gets the id attribute of the IssueReply object
   *
   *@return    The id value
   */
  public int getId() {
    return id;
  }


  /**
   *  Gets the replyToId attribute of the IssueReply object
   *
   *@return    The replyToId value
   */
  public int getReplyToId() {
    return replyToId;
  }


  /**
   *  Gets the subject attribute of the IssueReply object
   *
   *@return    The subject value
   */
  public String getSubject() {
    return subject;
  }


  /**
   *  Gets the body attribute of the IssueReply object
   *
   *@return    The body value
   */
  public String getBody() {
    return body;
  }


  /**
   *  Gets the importance attribute of the IssueReply object
   *
   *@return    The importance value
   */
  public int getImportance() {
    return importance;
  }


  /**
   *  Gets the entered attribute of the IssueReply object
   *
   *@return    The entered value
   */
  public java.sql.Timestamp getEntered() {
    return entered;
  }


  /**
   *  Gets the enteredBy attribute of the IssueReply object
   *
   *@return    The enteredBy value
   */
  public int getEnteredBy() {
    return enteredBy;
  }


  /**
   *  Gets the modified attribute of the IssueReply object
   *
   *@return    The modified value
   */
  public java.sql.Timestamp getModified() {
    return modified;
  }


  /**
   *  Gets the modifiedBy attribute of the IssueReply object
   *
   *@return    The modifiedBy value
   */
  public int getModifiedBy() {
    return modifiedBy;
  }


  /**
   *  Gets the issueId attribute of the IssueReply object
   *
   *@return    The issueId value
   */
  public int getIssueId() {
    return issueId;
  }


  /**
   *  Gets the issue attribute of the IssueReply object
   *
   *@return    The issue value
   */
  public Issue getIssue() {
    return issue;
  }


  /**
   *  Gets the user attribute of the IssueReply object
   *
   *@return    The user value
   */
  public String getUser() {
    return user;
  }


  /**
   *  Gets the enteredString attribute of the IssueReply object
   *
   *@return    The enteredString value
   */
  public String getEnteredString() {
    String tmp = "";
    try {
      return DateFormat.getDateInstance(3).format(entered);
    } catch (NullPointerException e) {
    }
    return tmp;
  }


  /**
   *  Gets the enteredDateTimeString attribute of the IssueReply object
   *
   *@return    The enteredDateTimeString value
   */
  public String getEnteredDateTimeString() {
    String tmp = "";
    try {
      return DateFormat.getDateTimeInstance(3, 3).format(entered);
    } catch (NullPointerException e) {
    }
    return tmp;
  }


  /**
   *  Gets the valid attribute of the IssueReply object
   *
   *@return    The valid value
   */
  private boolean isValid() {
    if (issueId == -1) {
      errors.put("actionError", "Issue ID not specified");
    }

    if (subject == null || subject.equals("")) {
      errors.put("subjectError", "Required field");
    }

    if (body == null || body.equals("")) {
      errors.put("bodyError", "Required field");
    }

    if (hasErrors()) {
      return false;
    } else {
      return true;
    }
  }


  /**
   *  Description of the Method
   *
   *@param  db                Description of the Parameter
   *@return                   Description of the Return Value
   *@exception  SQLException  Description of the Exception
   */
  public boolean insert(Connection db) throws SQLException {
    if (!isValid()) {
      return false;
    }

    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO project_issue_replies ");
    sql.append("(issue_id, reply_to, subject, message, importance, ");
    if (entered != null) {
      sql.append("entered, ");
    }
    if (modified != null) {
      sql.append("modified, ");
    }
    sql.append("enteredBy, modifiedBy ) ");
    sql.append("VALUES (?, ?, ?, ?, ?, ");
    if (entered != null) {
      sql.append("?, ");
    }
    if (modified != null) {
      sql.append("?, ");
    }
    sql.append("?, ?) ");

    int i = 0;
    PreparedStatement pst = db.prepareStatement(sql.toString());
    pst.setInt(++i, issueId);
    pst.setInt(++i, replyToId);
    pst.setString(++i, subject);
    pst.setString(++i, body);
    pst.setInt(++i, importance);
    if (entered != null) {
      pst.setTimestamp(++i, entered);
    }
    if (modified != null) {
      pst.setTimestamp(++i, modified);
    }
    pst.setInt(++i, enteredBy);
    pst.setInt(++i, modifiedBy);
    pst.execute();
    pst.close();

    id = DatabaseUtils.getCurrVal(db, "project_issue_repl_reply_id_seq");

    return true;
  }


  /**
   *  Description of the Method
   *
   *@param  db                Description of the Parameter
   *@param  context           Description of the Parameter
   *@return                   Description of the Return Value
   *@exception  SQLException  Description of the Exception
   */
  public boolean delete(Connection db, ActionContext context) throws SQLException {
    if (this.getId() == -1 || this.issueId == -1) {
      throw new SQLException("ID was not specified");
    }

    int recordCount = 0;
    PreparedStatement pst = db.prepareStatement(
        "DELETE FROM project_issue_replies " +
        "WHERE reply_id = ? ");
    pst.setInt(1, id);
    recordCount = pst.executeUpdate();
    pst.close();

    if (recordCount == 0) {
      errors.put("actionError", "Issue Reply could not be deleted because it no longer exists.");
      return false;
    } else {
      return true;
    }
  }


  /**
   *  Description of the Method
   *
   *@param  db                Description of the Parameter
   *@param  context           Description of the Parameter
   *@return                   Description of the Return Value
   *@exception  SQLException  Description of the Exception
   */
  public int update(Connection db, ActionContext context) throws SQLException {
    if (this.getId() == -1 || this.issueId == -1) {
      throw new SQLException("ID was not specified");
    }

    if (!isValid()) {
      return -1;
    }

    int resultCount = 0;

    PreparedStatement pst = null;
    StringBuffer sql = new StringBuffer();

    sql.append(
        "UPDATE project_issue_replies " +
        "SET subject = ?, message = ?, importance = ?, " +
        "modifiedBy = ?, modified = CURRENT_TIMESTAMP " +
        "WHERE reply_id = ? " +
        "AND modified = ? ");

    int i = 0;
    pst = db.prepareStatement(sql.toString());
    pst.setString(++i, subject);
    pst.setString(++i, body);
    pst.setInt(++i, importance);
    pst.setInt(++i, this.getModifiedBy());
    pst.setInt(++i, this.getId());
    pst.setTimestamp(++i, modified);

    resultCount = pst.executeUpdate();
    pst.close();

    return resultCount;
  }

}

