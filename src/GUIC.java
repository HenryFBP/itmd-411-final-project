/***
 * GUI Constants for laziness and uniformity.
 */
public enum GUIC
{
	MUST_ENTER_DATE("You must enter a date here."),
	NOT_LOGGED_IN("Not currently logged in."),
	CURRENTLY_LOGGED_IN_TEXT("Currently logged in as:"),
	;
	
	
	
	private String constants;

	private GUIC(String s)
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
