package org.aspcfs.utils;

import java.util.ArrayList;
import java.util.zip.*;
import java.util.regex.*;
import java.io.*;
import java.util.*;
import org.aspcfs.modules.base.Import;

/**
 *  Parses a Line based on the delimiters or file type
 *  Assumes that all the fields have quotes around them.
 *
 *@author     Mathur
 *@created    April 6, 2004
 *@version    $id:exp$
 */
public class ParseLine {
  private String line = null;
  private String delimiter = null;


  /**
   *Constructor for the ParseLine object
   */
  public ParseLine() { }


  /**
   *Constructor for the ParseLine object
   *
   *@param  delimiter  Description of the Parameter
   */
  public ParseLine(String delimiter) {
    this.delimiter = delimiter;
  }


  /**
   *  Sets the delimiter attribute of the ParseLine object
   *
   *@param  tmp  The new delimiter value
   */
  public void setDelimiter(String tmp) {
    this.delimiter = tmp;
  }


  /**
   *  Sets the line attribute of the ParseLine object
   *
   *@param  tmp  The new line value
   */
  public void setLine(String tmp) {
    this.line = tmp;
  }


  /**
   *  Gets the line attribute of the ParseLine object
   *
   *@return    The line value
   */
  public String getLine() {
    return line;
  }


  /**
   *  Gets the delimiter attribute of the ParseLine object
   *
   *@return    The delimiter value
   */
  public String getDelimiter() {
    return delimiter;
  }


  /**
   *  Description of the Method
   *
   *@param  tmp            Description of the Parameter
   *@return                Description of the Return Value
   *@exception  Exception  Description of the Exception
   */
  public ArrayList parse(String tmp) throws Exception {
    this.line = tmp;
    if ("".equals(StringUtils.toString(line))) {
      return null;
    }
    ArrayList thisRecord = new ArrayList();
    boolean quote = false;
    boolean completeField = false;
    StringBuffer value = new StringBuffer("");
    for (int i = 0; i < line.length(); i++) {
      char thisChar = line.charAt(i);
      if (thisChar == delimiter.charAt(0)) {
        if (quote == false) {
          completeField = true;
        } else {
          value.append(thisChar);
        }
      } else if (thisChar == '\"') {
        if (quote) {
          quote = false;
        } else {
          quote = true;
        }
      } else {
        value.append(thisChar);
      }
      if (i == line.length() - 1) {
        completeField = true;
      }
      if (completeField) {
        /* if (System.getProperty("DEBUG") != null) {
          System.out.println("ParseLine -> FOUND COLUMN: " + value.toString());
        } */
        thisRecord.add(value.toString());
        value = new StringBuffer("");
        quote = false;
        completeField = false;
      }
    }
    return thisRecord;
  }



  /**
   *  Checks to make sure that a delimiter can be found
   *
   *@return    The valid value
   */
  public boolean isValid() {
    if (delimiter == null) {
      return false;
    }
    return true;
  }
}
