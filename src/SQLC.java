
/***
 * SQL Constants for laziness and uniformity.
 */
public enum SQLC
{
	DEFAULT_LOGIN_DATA ("jdbc:mysql://localhost:3306/testdb,root,password\r\n"
					  + "jdbc:mysql://goggle.ru/bestdb:6666,toor,root\r\n"),
	
	TABLE_PREFIX			("h1_j1_"),
	SQL_STR_TO_DATE_FORMAT	("%Y-%m-%d %H:%i:%s"),
	SQL_DATE_FORMAT			("yyyy-MM-dd hh:mm:ss"),
	
	TABLE_USERS					(TABLE_PREFIX+"users"),
	TABLE_TICKETS				(TABLE_PREFIX+"tickets"),
	TABLE_USER_TYPES			(TABLE_PREFIX+"user_types"),
	TABLE_CATEGORIES			(TABLE_PREFIX+"categories"),
	TABLE_SEVERITIES			(TABLE_PREFIX+"severities"),

	PASSWORD_COLUMN_NAME		("password"),
	USERTYPE_COLUMN_NAME		("user_type"),
	USER_ID_COLUMN_NAME			("PID"),
	TICKET_ID_COLUMN_NAME		("TID"),
	SHORT_DESC_COLUMN_NAME		("shortDesc"),
	LONG_DESC_COLUMN_NAME		("longDesc"),
	CATEGORY_COLUMN_NAME		("category"),
	CATEGORY_ID_COLUMN_NAME		("CID"),
	SEVERITY_COLUMN_NAME		("severity"),
	SEVERITY_ID_COLUMN_NAME		("SID"),
	START_DATE_COLUMN_NAME		("startDate"),
	END_DATE_COLUMN_NAME		("endDate"),
	
	SEARCH_PANE_DEFAULT_QUERY1 ("SELECT * FROM " + SQLC.TABLE_TICKETS),
	SEARCH_PANE_DEFAULT_QUERY2 ("SELECT\n\u0009TID 'Ticket ID',\n\u0009PID 'Person ID',\n\u0009name 'Name',\n\u0009RES.CID 'Category ID',\n\u0009category 'Category',\n\u0009RES.SID 'Severity ID',\n    severity_level 'Severity Level',\n\u0009shortDesc 'Short Description',\n\u0009longDesc 'Long Description',\n\u0009startDate 'Start date',\n\u0009endDate 'End date'\nFROM\n\u0009(\n    SELECT\n\u0009\u0009TID,\n\u0009\u0009PID,\n\u0009\u0009name,\n\u0009\u0009RES.CID,\n\u0009\u0009category,\n\u0009\u0009SID,\n\u0009\u0009shortDesc,\n\u0009\u0009longDesc,\n\u0009\u0009startDate,\n\u0009\u0009endDate\n\u0009FROM\n\u0009\u0009(\n\u0009\u0009SELECT\n\u0009\u0009\u0009TID,\n\u0009\u0009\u0009t.PID,\n\u0009\u0009\u0009name,\n\u0009\u0009\u0009CID,\n\u0009\u0009\u0009SID,\n\u0009\u0009\u0009shortDesc,\n\u0009\u0009\u0009longDesc, \n\u0009\u0009\u0009startDate, \n\u0009\u0009\u0009endDate \n\u0009\u0009FROM\n\u0009\u0009\u0009tickets t\n\u0009\u0009INNER JOIN\n\u0009\u0009\u0009(\n            SELECT\n\u0009\u0009\u0009\u0009name,\n                PID    -- get users\n\u0009\u0009\u0009FROM \n\u0009\u0009\u0009\u0009users\n\u0009\u0009\u0009) u\n\u0009\u0009ON\n\u0009\u0009\u0009t.PID = u.PID  -- put ticket ids on users\n\u0009\u0009) AS RES\n\u0009INNER JOIN\n\u0009\u0009(SELECT CID, category FROM categories) c \n\u0009ON\n\u0009\u0009c.CID = RES.CID -- put category name on CID\n\u0009) AS RES\nINNER JOIN \n\u0009(SELECT * FROM severities) s \nON\n\u0009s.SID = RES.SID -- put severity name on SID"), //generated using http://snible.org/java2/uni2java.html
	SEARCH_PANE_DEFAULT_QUERY_PAPADEMAS ("SELECT\n\u0009TID 'Ticket ID',\n\u0009PID 'Person ID',\n\u0009name 'Name',\n\u0009RES.CID 'Category ID',\n\u0009category 'Category',\n\u0009RES.SID 'Severity ID',\n    severity_level 'Severity Level',\n\u0009shortDesc 'Short Description',\n\u0009longDesc 'Long Description',\n\u0009startDate 'Start date',\n\u0009endDate 'End date'\nFROM\n\u0009(\n    SELECT\n\u0009\u0009TID,\n\u0009\u0009PID,\n\u0009\u0009name,\n\u0009\u0009RES.CID,\n\u0009\u0009category,\n\u0009\u0009SID,\n\u0009\u0009shortDesc,\n\u0009\u0009longDesc,\n\u0009\u0009startDate,\n\u0009\u0009endDate\n\u0009FROM\n\u0009\u0009(\n\u0009\u0009SELECT\n\u0009\u0009\u0009TID,\n\u0009\u0009\u0009t.PID,\n\u0009\u0009\u0009name,\n\u0009\u0009\u0009CID,\n\u0009\u0009\u0009SID,\n\u0009\u0009\u0009shortDesc,\n\u0009\u0009\u0009longDesc, \n\u0009\u0009\u0009startDate, \n\u0009\u0009\u0009endDate \n\u0009\u0009FROM\n\u0009\u0009\u0009h1_j1_tickets t\n\u0009\u0009INNER JOIN\n\u0009\u0009\u0009(\n            SELECT\n\u0009\u0009\u0009\u0009name,\n                PID    -- get users\n\u0009\u0009\u0009FROM \n\u0009\u0009\u0009\u0009h1_j1_users\n\u0009\u0009\u0009) u\n\u0009\u0009ON\n\u0009\u0009\u0009t.PID = u.PID  -- put ticket ids on users\n\u0009\u0009) AS RES\n\u0009INNER JOIN\n\u0009\u0009(SELECT CID, category FROM h1_j1_categories) c \n\u0009ON\n\u0009\u0009c.CID = RES.CID -- put category name on CID\n\u0009) AS RES\nINNER JOIN \n\u0009(SELECT * FROM h1_j1_severities) s \nON\n\u0009s.SID = RES.SID -- put severity name on SID"),
	;
	
	private String constants;

	private SQLC(String s)
	{
		this.constants = s;
	}

	@Override
	public String toString()
	{
			return constants;
	}
	
	public String s()
	{
		return toString();
	}
	
	public String ts()
	{
		return toString();
	}
}


