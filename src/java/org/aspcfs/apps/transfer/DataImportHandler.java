package org.aspcfs.apps.transfer;

import java.util.logging.*;

/**
 *  Represents the necessary items for DataReader and DataWriter classes.
 *
 *@author     matt rajkowski
 *@created    September 3, 2002
 *@version    $Id: DataImportHandler.java,v 1.4 2003/01/13 19:16:01 mrajkowski
 *      Exp $
 */
public interface DataImportHandler {
  public static Logger logger = Logger.getLogger(Transfer.class.getName());


  /**
   *  Gets the configured attribute of the DataImportHandler object
   *
   *@return    The configured value
   */
  public boolean isConfigured();


  /**
   *  Gets the version attribute of the DataImportHandler object
   *
   *@return    The version value
   */
  public double getVersion();


  /**
   *  Gets the name attribute of the DataImportHandler object
   *
   *@return    The name value
   */
  public String getName();


  /**
   *  Gets the description attribute of the DataImportHandler object
   *
   *@return    The description value
   */
  public String getDescription();
}

