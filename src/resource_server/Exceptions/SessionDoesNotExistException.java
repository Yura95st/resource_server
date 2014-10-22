package resource_server.Exceptions;

public class SessionDoesNotExistException extends Exception
{
	public SessionDoesNotExistException()
	{

	}

	public SessionDoesNotExistException(String message)
	{
		super(message);
	}

	public SessionDoesNotExistException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SessionDoesNotExistException(Throwable cause)
	{
		super(cause);
	}
}
