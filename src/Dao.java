import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class Dao
{
	
	
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
}
