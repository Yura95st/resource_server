package resource_server.Helpers;

import org.junit.Assert;
import org.junit.Test;

public class ExceptionHelperTests
{

	@Test
	public void getFullExceptionMessage_ExceptionIsNull_ReturnEmptyString()
	{
		Assert.assertEquals("", ExceptionHelper.getFullExceptionMessage(null));
	}

	@Test
	public void getFullExceptionMessage_ExceptionMessageIsNull_ReturnEmptyString()
	{
		Exception exception = new Exception();

		Assert.assertEquals("",
			ExceptionHelper.getFullExceptionMessage(exception));
	}

	@Test
	public void getFullExceptionMessage_ReturnValidString()
	{
		Exception innerException = new Exception("Inner exception message");
		Exception exception = new Exception("Exception message", innerException);

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(exception.getMessage());
		stringBuilder.append(System.getProperty("line.separator"));
		stringBuilder.append(innerException.getMessage());

		Assert.assertEquals(stringBuilder.toString(),
			ExceptionHelper.getFullExceptionMessage(exception));
	}
}
