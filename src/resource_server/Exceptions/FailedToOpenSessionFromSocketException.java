package resource_server.Exceptions;

public class FailedToOpenSessionFromSocketException extends Exception
{
	public FailedToOpenSessionFromSocketException()
	{

	}

	public FailedToOpenSessionFromSocketException(String message)
	{
		super(message);
	}

	public FailedToOpenSessionFromSocketException(String message,
		Throwable cause)
	{
		super(message, cause);
	}

	public FailedToOpenSessionFromSocketException(Throwable cause)
	{
		super(cause);
	}
}
