package resource_server.Exceptions;

public class SessionIsNotActiveException extends Exception
{
	public SessionIsNotActiveException()
	{
		
	}
	
	public SessionIsNotActiveException(String message)
	{
		super(message);
	}
	
	public SessionIsNotActiveException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public SessionIsNotActiveException(Throwable cause)
	{
		super(cause);
	}
}
