package resource_server.Exceptions;

public class FailedToCloseSessionException extends Exception
{
	public FailedToCloseSessionException()
	{
		
	}
	
	public FailedToCloseSessionException(String message)
	{
		super(message);
	}
	
	public FailedToCloseSessionException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public FailedToCloseSessionException(Throwable cause)
	{
		super(cause);
	}
}
