package org.aspcfs.utils.formatter;

import org.aspcfs.modules.base.PhoneNumber;

/**
 *  Takes a phone number and formats the various fields to make the records
 *  consistent and presentable.
 *
 *@author     matt rajkowski
 *@created    March 5, 2003
 *@version    $Id: PhoneNumberFormatter.java,v 1.1.2.2 2003/03/06 17:49:29
 *      mrajkowski Exp $
 */
public class PhoneNumberFormatter {

  private final static String validChars = "0123456789()-., ";


  /**
   *  Constructor for the PhoneNumberFormatter object
   */
  public PhoneNumberFormatter() { }


  /**
   *  Description of the Method
   */
  public void config() {

  }


  /**
   *  Description of the Method
   *
   *@param  thisNumber  Description of the Parameter
   */
  public void format(PhoneNumber thisNumber) {
    String[] number = new String[]{thisNumber.getNumber(), null};
    if (number[0] != null && number[0].length() > 0) {
      number[0] = number[0].trim();
      //Split out the extention if there is one
      extractExtension(number, "ext.");
      extractExtension(number, "ext");
      extractExtension(number, "x");
      thisNumber.setExtension(number[1]);
      //Format just the number
      if (!(number[0].charAt(0) == '+')) {
        //Skip if someone has entered something other than basic numbers
        if (isMostlyNumbers(number[0])) {
          number[0] = formatNumber(number[0]);
        }
      }
      thisNumber.setNumber(number[0]);
    }
  }


  /**
   *  Description of the Method
   *
   *@param  number  Description of the Parameter
   *@return         Description of the Return Value
   */
  private String formatNumber(String number) {
    String tmpNum = extractNumbers(number);
    if (tmpNum.length() == 11 && tmpNum.charAt(0) == '1') {
      tmpNum = tmpNum.substring(1);
    }
    if (tmpNum.length() == 10) {
      if (tmpNum.charAt(0) == '1' ||
          tmpNum.charAt(0) == '0') {
        return ("+" + number);
      } else {
        StringBuffer result = new StringBuffer();
        result.append("(");
        result.append(tmpNum.substring(0, 3));
        result.append(") ");
        result.append(tmpNum.substring(3, 6));
        result.append("-");
        result.append(tmpNum.substring(6, 10));
        return result.toString();
      }
    }
    if (tmpNum.charAt(0) == '0') {
      return ("+" + number);
    }
    return number;
  }


  /**
   *  Gets the mostlyNumbers attribute of the PhoneNumberFormatter object
   *
   *@param  number  Description of the Parameter
   *@return         The mostlyNumbers value
   */
  private boolean isMostlyNumbers(String number) {
    boolean result = true;
    for (int i = 0; i < number.length(); i++) {
      if (validChars.indexOf(number.charAt(i)) == -1) {
        result = false;
      }
    }
    return result;
  }


  /**
   *  Description of the Method
   *
   *@param  tmp  Description of the Parameter
   *@return      Description of the Return Value
   */
  public final static String extractNumbers(String tmp) {
    StringBuffer sb = new StringBuffer();
    String allowed = "0123456789";
    for (int i = 0; i < tmp.length(); i++) {
      char theChar = tmp.charAt(i);
      if (allowed.indexOf(theChar) > -1) {
        sb.append(theChar);
      }
    }
    return sb.toString();
  }


  /**
   *  Description of the Method
   *
   *@param  number  Description of the Parameter
   *@param  check   Description of the Parameter
   */
  private void extractExtension(String[] number, String check) {
    if (number[0].toLowerCase().indexOf(check) > -1) {
      number[1] = number[0].substring(number[0].toLowerCase().indexOf(check) + check.length()).trim();
      number[0] = number[0].substring(0, number[0].toLowerCase().indexOf(check)).trim();
    }
  }
}
