package com.darkhorseventures.cfsmodule;

import javax.servlet.*;
import javax.servlet.http.*;
import org.theseus.actions.*;
import com.darkhorseventures.cfsbase.*;
import com.darkhorseventures.utils.*;
import java.sql.*;
import java.util.Vector;
import com.darkhorseventures.webutils.*;
import java.util.Enumeration;

/**
 *  The MyCFS module.
 *
 *@author     chris
 *@created    July 3, 2001
 *@version    $Id$
 */
public final class MyCFS extends CFSModule {

	/**
	 *  Description of the Method
	 *
	 *@param  context  Description of Parameter
	 *@return          Description of the Returned Value
	 *@since
	 */
	public String executeCommandDefault(ActionContext context) {
		String module = context.getRequest().getParameter("module");
		String includePage = context.getRequest().getParameter("include");
		context.getRequest().setAttribute("IncludePage", includePage);
		addModuleBean(context, module, module);
		return ("IncludeOK");
	}


	/**
	 *  Description of the Method
	 *
	 *@param  context  Description of Parameter
	 *@return          Description of the Returned Value
	 *@since
	 */
	public String executeCommandDeleteHeadline(ActionContext context) {
		//Process all parameters
		Enumeration parameters = context.getRequest().getParameterNames();
		Exception errorMessage = null;

		int orgId = -1;
		Connection db = null;

		try {
			db = this.getConnection(context);
			while (parameters.hasMoreElements()) {
				String param = (String) parameters.nextElement();

				if (context.getRequest().getParameter(param).equals("on")) {
					orgId = Integer.parseInt(param);
					Organization newOrg = new Organization();
					newOrg.setOrgId(orgId);
					newOrg.deleteMinerOnly(db);
					System.out.println(param + ": " + context.getRequest().getParameter(param) + "<br>");
				}
			}
		}
		catch (SQLException e) {
			errorMessage = e;
		}
		finally {
			this.freeConnection(context, db);
		}

		if (errorMessage == null) {
			addModuleBean(context, "", "Headline Delete OK");
			return ("DeleteOK");
		}
		else {
			context.getRequest().setAttribute("Error", errorMessage);
			return ("SystemError");
		}
	}


	/**
	 *  Takes a look at the User Session Object and prepares the MyCFSBean for the
	 *  JSP. The bean will contain all the information that the JSP can see.
	 *
	 *@param  context  Description of Parameter
	 *@return          Description of the Returned Value
	 *@since           1.1
	 */
	public String executeCommandHeadline(ActionContext context) {
		addModuleBean(context, "Customize Headlines", "Customize Headlines");
		Exception errorMessage = null;

		PagedListInfo orgListInfo = this.getPagedListInfo(context, "HeadlineListInfo");
		orgListInfo.setLink("/MyCFS.do?command=Headline");

		Connection db = null;
		OrganizationList organizationList = new OrganizationList();

		try {
			db = this.getConnection(context);
			organizationList.setPagedListInfo(orgListInfo);
			organizationList.setMinerOnly(true);
			organizationList.setEnteredBy(getUserId(context));
			organizationList.buildList(db);
		}
		catch (Exception e) {
			errorMessage = e;
		}
		finally {
			this.freeConnection(context, db);
		}

		if (errorMessage == null) {
			context.getRequest().setAttribute("OrgList", organizationList);
			return ("HeadlineOK");
		}
		else {
			context.getRequest().setAttribute("Error", errorMessage);
			return ("SystemError");
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  context  Description of Parameter
	 *@return          Description of the Returned Value
	 *@since
	 */
	public String executeCommandInsertHeadline(ActionContext context) {
		addModuleBean(context, "Customize Headlines", "");
		int headlines = 0;
		Exception errorMessage = null;

		Connection db = null;

		String name = new String();
		name = context.getRequest().getParameter("name");

		Organization newOrg = (Organization) new Organization();

		try {
			newOrg.setName(name);
			newOrg.setIndustry("1");
			newOrg.setEnteredBy(getUserId(context));
			newOrg.setMiner_only(true);

			db = this.getConnection(context);
			newOrg.insert(db);
		}
		catch (SQLException e) {
			errorMessage = e;
		}
		finally {
			this.freeConnection(context, db);
		}

		if (errorMessage == null) {
			context.getRequest().setAttribute("OrgDetails", newOrg);
			return ("GoHomeOK");
		}
		else {
			context.getRequest().setAttribute("Error", errorMessage);
			return ("SystemError");
		}
	}


	/**
	 *  Takes a look at the User Session Object and prepares the MyCFSBean for the
	 *  JSP. The bean will contain all the information that the JSP can see.
	 *
	 *@param  context  Description of Parameter
	 *@return          Description of the Returned Value
	 *@since           1.1
	 */
	public String executeCommandHome(ActionContext context) {
		addModuleBean(context, "Home", "");
		int headlines = 0;
		Exception errorMessage = null;

		String whereClause = new String();

		//	
		//	Alerts Selection
		//
		
		HtmlSelect alertsListSelect = new HtmlSelect();
		alertsListSelect.setSelectName("alerts");
		alertsListSelect.addItem(0, "Opportunities");
		alertsListSelect.addItem(1, "Leads Contacted");
		alertsListSelect.addItem(2, "Presentations");
		alertsListSelect.addItem(3, "Proposals");
		alertsListSelect.addItem(4, "Contracts Signed");
		
		String alertsRequest = (String) context.getRequest().getParameter("alerts");
		if (alertsRequest == null) { alertsRequest = "0"; }
			
		int i_alerts = Integer.parseInt(alertsRequest);
		alertsListSelect.setDefaultKey(i_alerts);
		alertsListSelect.build();

		Vector newsList = new Vector();
		
		String industryCheck = context.getRequest().getParameter("industry");
		
		OpportunityList alertOpps = new OpportunityList();
		
		Connection db = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet headline_rs = null;

		try {
			StringBuffer sql = new StringBuffer();
			
			db = this.getConnection(context);
			
			LookupList indSelect = new LookupList(db, "lookup_industry");
			indSelect.setJsEvent("onChange=\"document.forms['miner_select'].submit();\"");
			
			indSelect.addItem(0, "Latest News");

			//used to check the number of customized headlines
			PreparedStatement pst = null;
			StringBuffer headlineCount = new StringBuffer();
			headlineCount.append("SELECT COUNT(org_id) AS headlinecount " +
					"FROM organization " +
					"WHERE miner_only = 't' and industry_temp_code = 1 and enteredby = " + getUserId(context) + " ");


			sql.append("SELECT * from news ");

			pst = db.prepareStatement(headlineCount.toString());
			headline_rs = pst.executeQuery();
			if (headline_rs.next()) {
				headlines = headline_rs.getInt("headlinecount");
			}
			pst.close();
			headline_rs.close();
			
			if (headlines > 0) { indSelect.addItem(1, "My News"); }
			
			//new
			
			if (industryCheck == null && headlines > 0) { industryCheck = "1"; }
			
			if (industryCheck != null && !(industryCheck.equals("0"))) {
				whereClause = " WHERE organization.industry_temp_code = " + Integer.parseInt(industryCheck) +
						" AND organization.org_id = news.org_id ";

				if (industryCheck.equals("1")) {
					whereClause = whereClause + "AND organization.enteredby = "
							 + getUserId(context) + "AND organization.miner_only = 't' ";
				}

				sql.append(whereClause);
			}
			else if (industryCheck == null || industryCheck.equals("0")) {
				sql.append("WHERE organization.miner_only = 'f' AND organization.org_id = news.org_id ");
			}
			
			context.getRequest().setAttribute("IndSelect", indSelect);
			
			//end
			
			sql.append(" ORDER BY dateentered desc limit 10 ");

			st = db.createStatement();

			//Execute the main query
			rs = st.executeQuery(sql.toString());
			while (rs.next()) {
				NewsArticle thisNews = new NewsArticle(rs);
				newsList.addElement(thisNews);
			}
			rs.close();
			st.close();

			//I'm just going to put the alertOpps here
			PagedListInfo alertOppsPaged = new PagedListInfo();
			alertOppsPaged.setMaxRecords(5);
			alertOppsPaged.setColumnToSortBy("alertdate");

			alertOpps.setPagedListInfo(alertOppsPaged);
			alertOpps.setEnteredBy(getUserId(context));
			alertOpps.setHasAlertDate(true);
			alertOpps.buildList(db);

		}
		catch (SQLException e) {
			errorMessage = e;
		}
		finally {
			this.freeConnection(context, db);
		}

		if (errorMessage == null) {
			context.getRequest().setAttribute("NewsList", newsList);
			context.getRequest().setAttribute("AlertsListSelection", alertsListSelect);
			context.getRequest().setAttribute("AlertOppsList", alertOpps);
			return ("HomeOK");
		}
		else {
			context.getRequest().setAttribute("Error", errorMessage);
			return ("SystemError");
		}
	}


	/**
	 *  Displays a list of profile items the user can select to modify
	 *
	 *@param  context  Description of Parameter
	 *@return          Description of the Returned Value
	 *@since
	 */
	public String executeCommandMyProfile(ActionContext context) {
		addModuleBean(context, "MyProfile", "");
		return ("MyProfileOK");
	}


	/**
	 *  The user wants to modify their name, etc.
	 *
	 *@param  context  Description of Parameter
	 *@return          Description of the Returned Value
	 *@since
	 */
	public String executeCommandMyCFSProfile(ActionContext context) {
		addModuleBean(context, "MyProfile", "");
		return ("ProfileOK");
	}


	/**
	 *  The user's name was modified
	 *
	 *@param  context  Description of Parameter
	 *@return          Description of the Returned Value
	 *@since
	 */
	public String executeCommandUpdateProfile(ActionContext context) {
		return ("UpdateProfileOK");
	}


	/**
	 *  The user wants to change the password
	 *
	 *@param  context  Description of Parameter
	 *@return          Description of the Returned Value
	 *@since
	 */
	public String executeCommandMyCFSPassword(ActionContext context) {
		addModuleBean(context, "MyProfile", "");
		return ("PasswordOK");
	}


	/**
	 *  The password was modified
	 *
	 *@param  context  Description of Parameter
	 *@return          Description of the Returned Value
	 *@since
	 */
	public String executeCommandUpdatePassword(ActionContext context) {
		return ("UpdatePasswordOK");
	}


	/**
	 *  Description of the Method
	 *
	 *@param  context  Description of Parameter
	 *@return          Description of the Returned Value
	 *@since
	 */
	public String executeCommandMyCFSSettings(ActionContext context) {
		addModuleBean(context, "MyProfile", "");
		return ("SettingsOK");
	}


	/**
	 *  Description of the Method
	 *
	 *@param  context  Description of Parameter
	 *@return          Description of the Returned Value
	 *@since
	 */
	public String executeCommandUpdateSettings(ActionContext context) {
		return ("UpdateSettingsOK");
	}
}

