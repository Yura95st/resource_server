package resource_server.Exceptions;

public class CommandParameterNameNotFoundException extends Exception
{
	public CommandParameterNameNotFoundException()
	{
		
	}
	
	public CommandParameterNameNotFoundException(String message)
	{
		super(message);
	}
	
	public CommandParameterNameNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public CommandParameterNameNotFoundException(Throwable cause)
	{
		super(cause);
	}
}
