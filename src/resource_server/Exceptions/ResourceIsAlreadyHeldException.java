package resource_server.Exceptions;

public class ResourceIsAlreadyHeldException extends Exception
{
	public ResourceIsAlreadyHeldException()
	{
		
	}
	
	public ResourceIsAlreadyHeldException(String message)
	{
		super(message);
	}
	
	public ResourceIsAlreadyHeldException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public ResourceIsAlreadyHeldException(Throwable cause)
	{
		super(cause);
	}
}
