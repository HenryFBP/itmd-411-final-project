import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class GetID {

	 public static void main(String[] args)throws Exception {
 
	    	 try {
	    		Connection connect = null;
	    		Statement statement = null;
	    		ResultSet resultSet = null;
	    		String response = "";
	             // This will load the MySQL driver, each DB has its own driver
	             Class.forName("com.mysql.jdbc.Driver").newInstance();
	             // Setup the connection with the DB
	             connect = (Connection) DriverManager
	                     .getConnection("jdbc:mysql://www.papademas.net/inventory?"
	                             + "user=dbTest&password=dbTest");
	             // Statements allow to issue SQL queries to the database
	             response = "Insert into jpapaInventory(cost) values ('"+900+"');";
	             statement = (Statement) connect.createStatement();
	             statement.executeUpdate(response,Statement.RETURN_GENERATED_KEYS);

	             resultSet = statement.getGeneratedKeys();
	             int id = 0;
	             if (resultSet.next())
	             {
	                id = resultSet.getInt(1);
	             }  
	             System.out.println("ID inserted = " + id);
	             // Result set gets the result of the SQL query
	             resultSet = statement
	                     .executeQuery("select cost from jpapaInventory");
	         
	             // ResultSet is initially before the first data set
	             while (resultSet.next()) {
				          /* column data may be retrieved via name
			              e.g. resultSet.getString("name");
			              or via the column number which starts at 1
				          e.g. resultSet.getString(1); */
	                 response = resultSet.getString(1);  //retrieve cost
	                 
	                System.out.println("Cost= " + response);
	             }
	         }
	         catch(Exception e) {
	             e.printStackTrace();
	         }

	    } 
}
