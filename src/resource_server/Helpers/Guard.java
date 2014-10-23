package resource_server.Helpers;

public class Guard
{
	public static void isMoreOrEqualToZero(int number, String objectName)
	{
		if (number < 0)
		{
			throw new IllegalArgumentException(String.format(
				"Argument can't be less, than 0: %1$s", objectName));
		}
	}
	
	public static void isNotNull(Object object, String objectName)
	{
		if (object == null)
		{
			throw new IllegalArgumentException(String.format(
				"Argument can't be null: %1$s", objectName));
		}
	}
}
