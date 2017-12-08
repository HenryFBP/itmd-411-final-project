import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

/***
 * 
 * @author henryfbp
 *	A utility class containing lots of different useful functions.
 */
public class Util
{
	
	public static final String NORTH = SpringLayout.NORTH;
	public static final String EAST = SpringLayout.EAST;
	public static final String SOUTH = SpringLayout.SOUTH;
	public static final String WEST = SpringLayout.WEST;
	
	public static void printf(String s, Object... args)
	{
		System.out.printf(s, args);
	}

	public static void print(String s)
	{
		System.out.print(s);
	}

	public static void println(String s)
	{
		System.out.println(s);
	}

	/***
	 * @param duplicate The string to repeat.
	 * @param length How many times should we repeat?
	 * @return The duplicated string.
	 */
	private static String stringLine(String duplicate, int length)
	{
		String ret = "";
	
		for (int i = 0; i < length; i++)
		{
			ret += duplicate;
		}
		return ret;
	}

	public static String oppositeDirection(String direction)
	{
		switch (direction.toUpperCase().charAt(0))
		{
		case 'N':
			return SOUTH;
		case 'E':
			return WEST;
		case 'S':
			return NORTH;
		case 'W':
			return EAST;
		default:
			return null;
		}
	
	}

	/***
	 * @param ar The list to print.
	 */
	public static void printList(ArrayList<String> ar)
	{
		for (int i = 0; i < ar.size(); i++)
		{
			System.out.println(ar.get(i));
		}
		return;
	}
	
	/***
	 * 
	 * @param rs The resultSet to be formatted.
	 * @return  A pretty, human-readable list of strings.
	 */
	public static ArrayList<String> formatResultSet(ResultSet rs)
	{
		int pos = -1;
		try
		{
			pos = rs.getRow(); // record current place
			rs.beforeFirst(); // set place to zero

		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}

		int largestColumn = getLargestColumn(rs);
		String stringFormat = "%" + 13 + "s |";
		String stringFormat2 = "%" + 15 + "s |";

		ArrayList<String> ret = new ArrayList<>();

		ResultSetMetaData rsmd;
		try
		{
			rsmd = rs.getMetaData();
			ret.add(String.format(stringFormat2, "Column name:"));
			ret.add(String.format(stringFormat2, "Column type:"));

			// System.out.println("metadata:");
			for (int i = 1; i < rsmd.getColumnCount(); i++)
			{
				// printf("i = %2d, current thing = %10s, %10s\n",
				// i,rsmd.getColumnName(i),rsmd.getColumnTypeName(i));
				ret.set(0, ret.get(0) + String.format(stringFormat, rsmd.getColumnName(i)) // tack on a name
				);

				ret.set(1, ret.get(1) + String.format(stringFormat, rsmd.getColumnTypeName(i)) // tack on a type
				);
			}

			// System.out.println("length of my lines should be " + ret.get(0).length());
			ret.add(stringLine("-", ret.get(0).length()));
			ret.add(0, stringLine("_", ret.get(0).length()));

			int numCols = 0;

			while (rs.next())
			{
				String oneColumn = String.format(stringFormat2, "");
				for (int i = 1; i < rsmd.getColumnCount(); i++)
				{
					// System.out.println("HI. ONECOLUMN = "+oneColumn);
					oneColumn += String.format(stringFormat, rs.getString(i));
				}

				ret.add(oneColumn);
				numCols++;
			}

			if (numCols == 0) // our resultSet is empty....
			{
				ret.add("    Empty resultset...");
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		try
		{
			rs.absolute(pos); // reset to position it was at
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return ret;
	}
	
	
	/***
	 * 
	 * @param rs The resultSet you want to find the largest column in.
	 * @return The column that has the largest amount of characters in it.
	 */
	public static int getLargestColumn(ResultSet rs)
	{
		int largest = -1;
		ResultSetMetaData rsmd = null;
		try
		{
			rsmd = rs.getMetaData();

			for (int i = 1; i < rsmd.getColumnCount(); i++)
			{
				if (rsmd.getColumnDisplaySize(i) > largest)
				{
					largest = rsmd.getColumnDisplaySize(i);
				}
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return largest;

	}
	
	

	/***
	 * Note: the reason for this existing is me not wanting to store the professor's
	 * host, login, and password as a hardcoded string on a public git repo.
	 * Instead, it will be stored in a .gitignore'd file and read in later.
	 * 
	 * @param path
	 *            A path to a text file containing a newline-separated list of host,
	 *            login, password.
	 * @return A list of logins in the following format:<br>
	 *         <code>
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

			while ((line = br.readLine()) != null)
			{
				lineArr = new ArrayList<>();
				words = line.split(",");
				for (String word : words)
				{
					lineArr.add(word);
				}

				if (lineArr.size() > 0)
				{
					System.out.printf("Adding this: '%s'\n", lineArr);
					ret.add(lineArr);
				}
			}

		}
		catch (FileNotFoundException e)
		{
			printf("File '%s' not found. Creating it with default login info...", path);

			try
			{
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
				
				bw.write(SQLC.DEFAULT_LOGIN_DATA+"");
				
				bw.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			
			
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return ret;
	}

	/***
	 * Makes all buttons inside of a JPanel disabled/unclickable.
	 * @param buttonContainer	The container that has the buttons inside of it
	 * @param excludedButtons 	Names of buttons to not be disabled.
	 * @param enableAll 	 	Enable all buttons instead of disabling?
	 */
	public static void disableButtons(JPanel buttonContainer, String[] excludedButtons, Boolean enableAll)
	{
		ArrayList<String> excludedButtonsAR = new ArrayList<>(Arrays.asList(excludedButtons));
		
		ArrayList<JButton> menuButtons = getButtonsFromJPanel(buttonContainer);
		SpringLayout tempSL = (SpringLayout) buttonContainer.getLayout();
		
		for(int i = 0; i < menuButtons.size(); i++)
		{
			JButton tempButton = menuButtons.get(i);
			
			tempButton.setEnabled(false); //assume it should be disabled
			
			if(enableAll || excludedButtonsAR.contains(tempButton.getName()))
			{
				tempButton.setEnabled(true);
			}
		}
	}
	
	/***
	 *  Disable all buttons EXCEPT those specified in the excludedButtons list.
	 * @param buttonContainer The container of buttons.
	 * @param excludedButtons Button names to not be disabled.
	 */
	public static void disableButtons(JPanel buttonContainer, String[] excludedButtons)
	{
		disableButtons(buttonContainer, excludedButtons, false);
	}
	
	public void disableButtons(JPanel buttonContainer)
	{
		disableButtons(buttonContainer, new String[] {});
	}
	
	public static void enableButtons(JPanel buttonContainer)
	{
		disableButtons(buttonContainer, new String[] {}, true);
	}
	
	

/***
 * Populates a comboBox with a list gotten from an SQL table.
 * The SQL table should look like this:
 * <pre>
 * <code>
 * +----+-------------+
 * | <b>id</b> |     str     |
 * +----+-------------+
 * | 1  |    bad      |
 * | 2  |    good     |
 * | 3  |    great    |
 * | 4  |  excellent  |
 * +----+-------------+
 * </pre>
 * </code>
 * 
 * and produces this JComboBox:
 * <pre>
 * <code>
 * +------------------+
 * | 1 - bad          |
 * | 2 - good         |
 * | 3 - great        |
 * | 4 - excellent    |
 * +------------------+
 * </pre>
 * </code>
 * @param comboBox
 * @param tableName
 */
	public static void setupComboBox(JComboBox<String> comboBox, String tableName, Dao dao)
	{
		try
		{
			Statement s = dao.c.createStatement();
			
			String query = String.format("SELECT * FROM %s",tableName);
			
			printf("Grabbing category list from database with the following query: \n'%s'\n",query);
			
			ResultSet rs = s.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			
			comboBox.removeAll();
			comboBox.setModel(new DefaultComboBoxModel<>());
			
			while(rs.next())
			{
				String onecol = "";
				
				for(int i = 1; i <= rsmd.getColumnCount(); i++)
				{
					if(i != 1)
					{
						onecol += " - ";
					}
					
					onecol += rs.getString(i).toString();
				}
				
				printf("Adding string '%s' to comboBox!\n",onecol);
				comboBox.addItem(onecol);
			}
			
			
			
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setupCategories(JComboBox<String> comboBox, Dao dao)
	{
		setupComboBox(comboBox, SQLC.TABLE_CATEGORIES.ts(), dao);
	}
	
	public static void setupSeverities(JComboBox<String> comboBox, Dao dao)
	{
		setupComboBox(comboBox, SQLC.TABLE_SEVERITIES.ts(), dao);
	}
	
	


	public static Component getVisibleComponent(JPanel jp)
	{
		for (Component c : jp.getComponents())
		{
			if (c.isVisible())
			{
				return c;
			}
		}
		return null;
	}

	public static ArrayList<JButton> getButtonsFromJPanel(JPanel jp)
	{
		ArrayList<JButton> ret = new ArrayList<>();

		for (Component c : jp.getComponents())
		{
			if (c instanceof JButton)
			{
				ret.add((JButton) c);
			}
		}
		return ret;
	}
	
	public static ArrayList<Component> getComponentsFromJPanel(JPanel jp)
	{
		ArrayList<Component> ret = new ArrayList<>();
		
		for (Component c : jp.getComponents())
		{
			if (c instanceof Component)
			{
				ret.add((Component) c);
			}
		}
		return ret;
	}
	


	/***
	 * This grabs the comparator ('>', '=', '!='), and the two operands (name, id,
	 * date), ("henry", 1, 11/27/2017) and puts it into a query. It then uses the
	 * results of that query to format the table.
	 * 
	 * @param s
	 */
	public static void setupResultsView(String s)
	{
		
	}

	/***
	 * Sets tooltips for all components inside of a container.
	 * @param container				The container that has the components inside of it.
	 * @param excludedComponents	Names of components to not get tooltips.
	 * @param tooltip				The tooltip to be displayed.
	 */
	public static void setTooltips(JPanel container, String[] excludedComponents, String tooltip)
	{
		ArrayList<String> excludedComponentsAR = new ArrayList<>(Arrays.asList(excludedComponents));
		
		ArrayList<Component> components = Util.getComponentsFromJPanel(container);
		SpringLayout tempSL = (SpringLayout) container.getLayout();
		
		for (int i = 0; i < components.size(); i++)
		{
			if (!excludedComponentsAR.contains(components.get(i).getName()))
			{
				try
				{
					((JComponent) components.get(i)).setToolTipText(tooltip); // may not be able to be cast to JComponent...
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	/**
	 * @param buttonContainer
	 *            The container that has the buttons inside of it.
	 * 
	 *            With a bunch of buttons, they will be laid out using springLayout
	 *            offset by 10px from eachother.
	 */
	public static void formatButtons(JPanel buttonContainer)
	{
		// insert formatting for all buttons.
		ArrayList<JButton> menuButtons = Util.getButtonsFromJPanel(buttonContainer);
		SpringLayout tempSL = (SpringLayout) buttonContainer.getLayout();

		for (int i = 0; i < menuButtons.size(); i++)
		{
			JButton tempButton = menuButtons.get(i);
			Util.printf("menuButtons[%d] = %s\n", i, tempButton);

			tempSL.putConstraint(Util.WEST, tempButton, 10, Util.WEST, buttonContainer);
			tempSL.putConstraint(Util.EAST, tempButton, -10, Util.EAST, buttonContainer);

			if (i == 0) // first button aligns vertically w/ container
			{
				tempSL.putConstraint(Util.NORTH, tempButton, 10, Util.NORTH, buttonContainer);
			}
			else // all others align with south side of prev. button
			{
				JButton prevButton = menuButtons.get(i - 1);
				tempSL.putConstraint(Util.NORTH, tempButton, 10, Util.SOUTH, prevButton);
			}
		}
	}
	
	/**
	 * Return the int[] [row,col] of a JTable object.
	 * @param t The JTable object.
	 * @return The selected row and column.
	 */
	public static Integer[] getSelectedPos(JTable t)
	{
		Integer[] ret = {t.getSelectedRow(), t.getSelectedColumn()};
		
		return ret;
	}
	
	
	
}
