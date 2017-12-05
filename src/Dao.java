import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.DateFormatter;

public class Dao
{
	
	private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

	public static final int USERNAME_NOT_FOUND = -0x0001;
	public static final String USERNAME_NOT_FOUND_S = "Username '%s' not found!";

	public static final int PASSWORD_INCORRECT = -0x0002;
	public static final String PASSWORD_INCORRECT_S = "Password for user '%s' incorrect!";

	public static final int NORMAL_USER = 0x0001;
	public static final String NORMAL_USER_S = "Welcome, user '%s'!";

	public static final int ADMINISTRATOR = 0x0002;
	public static final String ADMINISTRATOR_S = "Welcome, admin '%s'!";

	public static final String[] OPERATOR_TYPES = { "<", "=", "CONTAINS", ">", "<>" };
	public static final String[] OPERATOR_TYPES_S = { "less than", "is", "contains", "greater than", "is not" };

	public static final String[] VALID_SEARCH_FIELDS_USERS = { "username", "name" };
	public static final String[] VALID_SEARCH_FIELDS_USERS_S = { "Username", "Name" };

	public static final String[] VALID_SEARCH_FIELDS_TICKETS = { "TID", "PID", "shortDesc", "category", "longDesc",
			"startDate", "endDate" };
	public static final String[] VALID_SEARCH_FIELDS_TICKETS_S = { "Ticket ID", "Person ID", "Short Description",
			"Category", "Long Description", "Start Date", "End Date" };

	public Connection c = null;
	
	public Integer USER_TYPE = null;
	public String USER_NAME = null;
	public String USER_PASS = null;
	public Integer USER_ID = null;



	public Dao() // default constructor
	{
		
	}

	public Dao(String host, String user, String password) // nondefault constructor
	{
		this.connect(host, user, password);
	}

	/***
	 * Tries to connect to user@host using password as a password.
	 * 
	 * @param host
	 * @param user
	 * @param password
	 * @return A 1 if succeeded, a -1 if any exceptions happened.
	 */
	public int connect(String host, String user, String password)
	{
		int ret = -1;

		try
		{
			Class.forName(DRIVER_CLASS_NAME);
			System.out.printf("Connecting at host %s with user='%s', password='%s'\n", host, user, password);
			this.c = DriverManager.getConnection(host, user, password);
			ret = 1;
		}
		catch (SQLException e)
		{
			System.out.printf("Could not connect with the following credentials: '%s', '%s', '%s'\n\n", host, user,
					password);
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			System.out.printf("Could not find class '%s'!", DRIVER_CLASS_NAME);
			e.printStackTrace();
		}
		System.out.printf("C = %s\n", c);
		return ret;
	}

	/***
	 * 
	 * @param username
	 *            The username of a user.
	 * @param password
	 *            The password of a user.
	 * @return A nonzero number representing if: this user doesn't exist or has an
	 *         incorrect password, is a normal user, or is an administrator.
	 */
	public int login(String username, String password)
	{
		System.out.printf("Results for logging in user '%s' with password '%s'...\n", username, password);
		int ret = 0;
		int usertype = -1; // user = 1, admin = 2, etc...
		int tempID = -1;

		Statement s = null;
		try
		{
			String query = String.format("SELECT * FROM %s WHERE username = '%s'", SQLC.TABLE_USERS, username);
			s = c.createStatement();

			System.out.printf("About to execute '%s'\n", query);
			ResultSet rs = s.executeQuery(query); // gets username and password from table of users
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = resultSetColumns(rs);

			System.out.printf("Got %d columns from that query!\n", cols);

			// if user doesn't exist
			if (cols == 0)
			{
				System.out.printf("User with name '%s' does not exist!\n", username);
				return USERNAME_NOT_FOUND;
			}

			rs.beforeFirst();
			rs.next();

			// verify user has entered the right password
			if (rs.getString(SQLC.PASSWORD_COLUMN_NAME.s()).compareTo((password)) == 0)
			{
				System.out.printf("Welcome, user '%s'!\n", username);
				ret = rs.getInt(SQLC.USERTYPE_COLUMN_NAME.s()); // get user type
				tempID = rs.getInt(SQLC.USER_ID_COLUMN_NAME.s()); //get user id for later

				switch (usertype)
				{
				case NORMAL_USER:
					break;
				case ADMINISTRATOR:
					break;
				}
				
			}
			else
			{
				// wrong password, right user...
				ret = PASSWORD_INCORRECT;
			}

			ArrayList<String> formattedResults = Util.formatResultSet(rs);

			Util.printList(formattedResults);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		if (ret > 0) // if user is valid, modify the current DAO object for use later.
		{
			this.USER_TYPE = ret;
			this.USER_NAME = username;
			this.USER_PASS = password;
			this.USER_ID = tempID;
			
			
		}

		return ret;

	}

	public ArrayList<String> getCategories()
	{
		ArrayList<String> ret = new ArrayList<>();
		
		Statement s;
		try
		{
			s = this.c.createStatement();
			
			String query = String.format("SELECT * FROM %s",SQLC.TABLE_CATEGORIES);
			
			ResultSet rs = s.executeQuery(query);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		
		return ret;
	}
	
	
	public static ArrayList<ArrayList<Object>> resultSetToList(ResultSet rs)
	{
		ResultSetMetaData rsmd = null;
		ArrayList<ArrayList<Object>> ret = new ArrayList<>();
		ArrayList<Object> tempRow;

		try
		{

			rsmd = rs.getMetaData();

			// System.out.printf("Width of cols are %d\n",rsmd.getColumnCount());

			int col = 0;
			while (rs.next())
			{
				// System.out.printf("Col %d\n",col);
				// System.out.println("rs.getString(1) = "+rs.getString(1));
				// System.out.println("rs.getObject(1) = "+rs.getObject(1));

				tempRow = new ArrayList<>();

				if (rsmd.getColumnCount() > 1)
				{
					for (int i = 1; i < rsmd.getColumnCount(); i++)
					{
						// System.out.printf("about to add %s\n",rs.getObject(i));
						tempRow.add((Object) rs.getString(i));
					}
				}

				tempRow.add((Object) rs.getString(rsmd.getColumnCount())); // FIXME figure out why we need this...fixes
																			// last col not appearing...

				// rs.geto
				ret.add(tempRow);
				// System.out.println("added this row to ret array: "+tempRow.toString());
				col++;
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		// System.out.println("Returning this: "+ret.toString());

		return ret;
	}

	public static ArrayList<ArrayList<Object>> resultSetMetadataToList(ResultSet rs)
	{
		ResultSetMetaData rsmd = null;
		ArrayList<ArrayList<Object>> ret = new ArrayList<>();

		try
		{
			rsmd = rs.getMetaData();
			ArrayList<Object> tempRow = new ArrayList<>();
			ArrayList<Object> tempRow2 = new ArrayList<>();

			// add metadata for headers to ret array
			for (int i = 1; i < rsmd.getColumnCount(); i++)
			{
				tempRow.add(rsmd.getColumnName(i));
				tempRow2.add(rsmd.getColumnTypeName(i));
			}

			tempRow.add((Object) rsmd.getColumnName(rsmd.getColumnCount())); // FIXME figure out why we need
																				// this...fixes last col not
																				// appearing...
			tempRow2.add((Object) rsmd.getColumnName(rsmd.getColumnCount())); // FIXME figure out why we need
																				// this...fixes last col not
																				// appearing...
			ret.add(tempRow);
			ret.add(tempRow2);

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return ret;
	}

	/***
	 * 
	 * @param rs
	 *            A ResultSet object. It will record the current position that the
	 *            ResultSet is in and will reset it back to that position once done
	 *            iterating.
	 * @return The number of columns in the resultSet.
	 */
	public static int resultSetColumns(ResultSet rs)
	{

		int cols = 0;
		int position = -1;

		try
		{
			position = rs.getRow(); // record our position
			rs.beforeFirst(); // goto 0

			while (rs.next())
			{
				cols++;
			}

			rs.absolute(position); // reset our position
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		try
		{
			rs.absolute(position); // go back to original position
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return cols;
	}



	/***
	 * Inserts a ticket.
	 */
	public void submitTicket(String name, String shortDesc, String longDesc, Integer category, Integer severity,
			Date startDate, Date endDate)
	{
		
		SimpleDateFormat sdf = new SimpleDateFormat(SQLC.SQL_DATE_FORMAT.s());
		String query = "";

		String sDate = sdf.format(startDate);
		java.sql.Date ed = (java.sql.Date) endDate;
		
		try
		{
			Statement s = c.createStatement();
			
			query = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s) ",
					SQLC.TABLE_TICKETS, SQLC.USER_ID_COLUMN_NAME, SQLC.SHORT_DESC_COLUMN_NAME,
					SQLC.LONG_DESC_COLUMN_NAME, SQLC.CATEGORY_ID_COLUMN_NAME, SQLC.SEVERITY_COLUMN_NAME, SQLC.START_DATE_COLUMN_NAME);
								  
			query += String.format("VALUES (%d, '%s', '%s', %d, %d, STR_TO_DATE('%s','%s'));",
								  this.USER_ID,  shortDesc,   longDesc,   category,   severity,  sDate, SQLC.SQL_STR_TO_DATE_FORMAT.s());
			
			System.out.printf("About to insert a record with the following SQL statement:\n '%s'\n",query);
			
			s.execute(query);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
