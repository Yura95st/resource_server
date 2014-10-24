package resource_server.Models;

import org.junit.Assert;
import org.junit.Test;

public class ResourceTests
{
	public void equals_DifferentResourceObjectsWithSameName_ReturnsTrue()
	{
		String resourceName = "resource_name";
		
		IResource resourceOne = new Resource(resourceName);
		IResource resourceTwo = new Resource(resourceName);
		
		Assert.assertTrue(resourceOne.equals(resourceTwo));
	}

	@Test
	public void getName_ReturnsValidName()
	{
		String resourceName = "resource_name";
		
		IResource recource = new Resource(resourceName);
		
		Assert.assertEquals(resourceName, recource.getName());
	}
}
