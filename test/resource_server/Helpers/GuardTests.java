package resource_server.Helpers;

import org.junit.Test;

public class GuardTests
{
	@Test(expected = IllegalArgumentException.class)
	public void isMoreOrEqualToZero_NumberIsLessThanZero_ThrowsIllegalArgumentException()
	{
		int number = -1;
		Guard.isMoreOrEqualToZero(number, "number");
	}

	@Test
	public void isMoreOrEqualToZero_NumberisMoreOrEqualToZero_DoesNotThrowAnyException()
	{
		int number = 0;
		
		Guard.isMoreOrEqualToZero(number, "number");
		
		number = 1;
		
		Guard.isMoreOrEqualToZero(number, "number");
	}
	
	@Test
	public void isNotNull_ObjectIsNotNull_DoesNotThrowAnyException()
	{
		Object tempObject = new Object();

		Guard.isNotNull(tempObject, "tempObject");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void isNotNull_ObjectIsNull_ThrowsIllegalArgumentException()
	{
		Object tempObject = null;

		Guard.isNotNull(tempObject, "tempObject");
	}
}
