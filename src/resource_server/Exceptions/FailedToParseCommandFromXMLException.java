package resource_server.Exceptions;

public class FailedToParseCommandFromXMLException extends Exception
{
	public FailedToParseCommandFromXMLException()
	{
		
	}
	
	public FailedToParseCommandFromXMLException(String message)
	{
		super(message);
	}
	
	public FailedToParseCommandFromXMLException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public FailedToParseCommandFromXMLException(Throwable cause)
	{
		super(cause);
	}
}
