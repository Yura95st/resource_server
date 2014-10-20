package resource_server.Exceptions;

public class SessionAlreadyExistsException extends Exception
{
	public SessionAlreadyExistsException()
	{
		
	}
	
	public SessionAlreadyExistsException(String message)
	{
		super(message);
	}
	
	public SessionAlreadyExistsException(String message,
			Throwable cause)
	{
		super(message, cause);
	}
	
	public SessionAlreadyExistsException(Throwable cause)
	{
		super(cause);
	}
}
