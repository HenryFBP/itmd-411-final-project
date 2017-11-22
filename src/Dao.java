import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.sql.*;



public class Dao
{
	

	private static final String TABLE_USERS = "users";
	private static final String TABLE_TICKETS = "tickets";

	private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	
	public static final int USERNAME_NOT_FOUND =  -0x0001;
	public static final int PASSWORD_INCORRECT =  -0x0002;
	
	
	public static final int NORMAL_USER =  		0x0001;
	public static final int ADMINISTRATOR =  	0x0002;
	
	private static Connection c = null;
	private Integer USER_TYPE = null;
	private String USER_NAME = null;
	
	private static String stringLine(String duplicate, int length)
	{
		String ret = "";
		
		for (int i = 0; i < length ; i++)
		{
			ret += duplicate;
		}
		return ret;
	}
	
	public static void printResultSet(ArrayList<String> ar)
	{
		for(int i = 0; i < ar.size(); i++)
		{
			System.out.println(ar.get(i));
		}
		return;
	}
	
	public static int getLargestColumn(ResultSet rs)
	{
		int largest = -1;
		ResultSetMetaData rsmd = null;
		try
		{
			rsmd = rs.getMetaData();
			
			
			for (int i = 1; i < rsmd.getColumnCount(); i++)
			{
				if(rsmd.getColumnDisplaySize(i) > largest)
				{
					largest = rsmd.getColumnDisplaySize(i);
				}
			}
			
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return largest;

		
	}
	
	
	public Dao() //default constructor
	{
		
	}
	
	public Dao(String host, String user, String password) //nondefault constructor
	{
		this.connect(host, user, password);
	}
	
	/***
	 * Tries to connect to user@host using password as a password.
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
			c = DriverManager.getConnection(host, user, password);
			ret = 1;
		}
		catch (SQLException e)
		{
			System.out.printf("Could not connect with the following credentials: '%s', '%s', '%s'\n\n",host,user,password);
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
//		catch (ClassNotFoundException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		finally
		{
			return ret;
		}
	}

	/***
	 * Note:  the reason for this existing is me not wanting to store the professor's host, login, and password
	 * 		  as a hardcoded string on a public git repo. Instead, it will be stored in a .gitignore'd file and read in later.
	 * @param path A path to a text file containing a newline-separated list of host, login, password.
	 * @return A list of logins in the following format:<br>
	 * <code>
	 * {<br>
	 * &nbsp;{"localhost:3306","user","pass"},<br>
	 * &nbsp;{"129.999.0.1:3604","admin","nimda"},<br>
	 * &nbsp;{"goggles.ru:3604","hithere","ello"}<br>
	 * }<br>
	 * </code>
	 */
	public static ArrayList<ArrayList<String>> getLoginsFromFile(String path)
	{
		ArrayList<ArrayList<String>> ret = new ArrayList<>();
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(new File(path)));
			
			String line = null;
			String words[] = null;
			ArrayList<String> lineArr = null;
			
			while((line = br.readLine()) != null)
			{
				lineArr = new ArrayList<>();
				words = line.split(",");
				for(String word : words)
				{
					lineArr.add(word);
				}
				
				if(lineArr.size() > 0)
				{
					System.out.printf("Adding this: '%s'\n",lineArr);
					ret.add(lineArr);
				}
			}
			
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return ret;
	}

	/***
	 * 
	 * @param username The username of a user.
	 * @param password The password of a user.
	 * @return A nonzero number representing if: 	this user doesn't exist or has an incorrect password,
	 * 												is a normal user,
	 * 												or is an administrator.
	 */
	public static int login(String username, String password)
	{
		System.out.printf("Results for logging in user '%s' with password '%s'...\n",username,password);
		int ret = 0;
		
		Statement s;
		try
		{
			String query = String.format("SELECT * FROM %s WHERE username = '%s'",TABLE_USERS,username);
			s = c.createStatement();
			
			System.out.printf("About to execute '%s'\n",query);
			ResultSet rs = s.executeQuery(query); //gets username and password from table of users
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = resultSetColumns(rs);
			
			System.out.printf("Got %d columns from that query!\n",cols);
						
			if(cols == 0) //if user doesn't exist
			{
				System.out.printf("User with name '%s' does not exist!\n",username);
				return USERNAME_NOT_FOUND;
			}
			
			ArrayList<String> formattedResults = formatResultSet(rs);
			
			printResultSet(formattedResults);
			
			//verify user has entered the right password
			
			
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
			
			
//			System.out.printf("Width of cols are %d\n",rsmd.getColumnCount());
			
			int col = 0;
			while(rs.next())
			{
//				System.out.printf("Col %d\n",col);
//				System.out.println("rs.getString(1) = "+rs.getString(1));
//				System.out.println("rs.getObject(1) = "+rs.getObject(1));

				tempRow = new ArrayList<>();
				
				if(rsmd.getColumnCount() > 1)
				{
					for (int i = 1; i < rsmd.getColumnCount(); i++)
					{
//						System.out.printf("about to add %s\n",rs.getObject(i));
						tempRow.add((Object)rs.getString(i));
					}
				}
				
				tempRow.add((Object)rs.getString(rsmd.getColumnCount())); // FIXME figure out why we need this...fixes last col not appearing...

				
				
//				rs.geto
				ret.add(tempRow);
//				System.out.println("added this row to ret array: "+tempRow.toString());
				col++;
			}
			
			
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
//		System.out.println("Returning this: "+ret.toString());
		
		
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
			
			//add metadata for headers to ret array
			for (int i = 1; i < rsmd.getColumnCount(); i++)
			{
				tempRow.add(rsmd.getColumnName(i));
				tempRow2.add(rsmd.getColumnTypeName(i));
			}
			
			tempRow.add((Object)rsmd.getColumnName(rsmd.getColumnCount()));		// FIXME figure out why we need this...fixes last col not appearing...
			tempRow2.add((Object)rsmd.getColumnName(rsmd.getColumnCount()));		// FIXME figure out why we need this...fixes last col not appearing...
			ret.add(tempRow);
			ret.add(tempRow2);
			
			
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		
		
		return ret;
	}
	
	public static int resultSetColumns(ResultSet rs)
	{
		
		int cols = 0;
		int position = -1;
		
		
		try
		{
			position = rs.getRow(); //record our position
			rs.beforeFirst(); //goto 0

			while(rs.next())
			{
				cols++;
			}
			
			rs.absolute(position); //reset our position
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return cols;
	}
	
	public static ArrayList<String> formatResultSet(ResultSet rs) 
	{
		int largestColumn = getLargestColumn(rs);
		String stringFormat = 	"%" + 13 + "s |";
		String stringFormat2 =  "%" + 15 + "s |";
		
		ArrayList<String> ret = new ArrayList<>();

		ResultSetMetaData rsmd;
		try
		{
			rsmd = rs.getMetaData();
			ret.add(String.format(stringFormat2, "Column name:"));
			ret.add(String.format(stringFormat2, "Column type:"));
			
			
//			System.out.println("metadata:");
			for(int i = 1; i < rsmd.getColumnCount(); i++)
			{
//				printf("i = %2d, current thing = %10s, %10s\n",
//						i,rsmd.getColumnName(i),rsmd.getColumnTypeName(i));
				ret.set(0,
						ret.get(0) + String.format(stringFormat,rsmd.getColumnName(i)) //tack on a name
						);
				
				ret.set(1,
						ret.get(1) + String.format(stringFormat,rsmd.getColumnTypeName(i)) //tack on a type
						);
			}
			
//			System.out.println("length of my lines should be " + ret.get(0).length());
			ret.add(stringLine("-",ret.get(0).length()));
			ret.add(0, stringLine("_",ret.get(0).length()));
			
			int numCols = 0;
			
			while(rs.next())
			{
				String oneColumn = String.format(stringFormat2,"");
				for(int i = 1; i < rsmd.getColumnCount(); i++)
				{
//					System.out.println("HI. ONECOLUMN = "+oneColumn);
					oneColumn += String.format(stringFormat, rs.getString(i));
				}
				
				ret.add(oneColumn);
				numCols++;
			}
			
			if(numCols == 0) //our resultSet is empty....
			{
				ret.add("    Empty resultset...");
			}

			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		
		
		return ret;
	}
}
