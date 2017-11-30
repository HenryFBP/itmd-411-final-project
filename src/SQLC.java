
/***
 * SQL Constants for laziness and uniformity.
 */
public enum SQLC
{
	 TABLE_USERS				("users"),
	 TABLE_TICKETS				("tickets"),
	 TABLE_USER_TYPES			("user_types"),
	 TABLE_CATEGORIES			("categories"),
	 TABLE_SEVERITIES			("severities"),
	 
	 PASSWORD_COLUMN_NAME		("password"),
	 USERTYPE_COLUMN_NAME		("user_type"),
	 USER_ID_COLUMN_NAME		("PID"),
	 TICKET_ID_COLUMN_NAME		("TID"),
	 SHORT_DESC_COLUMN_NAME		("shortDesc"),
	 LONG_DESC_COLUMN_NAME		("longDesc"),
	 CATEGORY_COLUMN_NAME		("category"),
	 CATEGORY_ID_COLUMN_NAME	("CID"),
	 SEVERITY_COLUMN_NAME		("SID"),
	 START_DATE_COLUMN_NAME		("startDate"),
	 END_DATE_COLUMN_NAME		("endDate");
	
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


