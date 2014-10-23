package resource_server.ResourcesManager;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import resource_server.Exceptions.ResourceIsAlreadyHeldException;
import resource_server.Exceptions.ResourceNotFoundException;
import resource_server.Models.IResource;
import resource_server.Models.Resource;

public class ResourcesManagerTests
{
	private IResourcesManager resourcesManager;
	
	@Test(expected = ResourceNotFoundException.class)
	public void getResource_NonexistentResourceName_ThrowsResourceNotFoundException()
			throws Exception
	{
		this.resourcesManager.getResource("nonexistent_resource_name");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getResource_ResourceNameIsNull_ThrowsIllegalArgumentException()
			throws Exception
	{
		this.resourcesManager.getResource(null);
	}

	@Test
	public void getResource_ReturnsValidResource() throws Exception
	{
		IResource resource = this.resourcesManager.getResources().get(0);

		Assert.assertEquals(resource,
			this.resourcesManager.getResource(resource.getName()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getSessionResources_InvalidSessionId_ThrowsIllegalArgumentException()
	{
		this.resourcesManager.getSessionResources(-1);
	}

	@Test
	public void getSessionResources_ReturnsSessionResources() throws Exception
	{
		int sessionId = 0;
		IResource holdedResource = this.resourcesManager.getResources().get(0);
		
		this.resourcesManager.holdResource(holdedResource, sessionId);
		
		Assert.assertEquals(new ArrayList<IResource>() {
			{
				this.add(holdedResource);
			}
		}, this.resourcesManager.getSessionResources(sessionId));
	}
	
	@Test
	public void getSessionResources_SessionHasNoResources_ReturnsEmptyList()
	{
		Assert.assertEquals(0, this.resourcesManager.getSessionResources(2)
			.size());
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void holdResource_NonexistentResource_ThrowsResourceNotFoundException()
			throws Exception
	{
		int sessionId = 0;
		IResource resource = new Resource("nonexistent_resource_name");
		
		this.resourcesManager.holdResource(resource, sessionId);
	}

	@Test(expected = ResourceIsAlreadyHeldException.class)
	public void holdResource_ResourceIsAlreadyHeld_ThrowsResourceIsAlreadyHeldException()
			throws Exception
	{
		int sessionId = 0;
		IResource holdedResource = this.resourcesManager.getResources().get(0);
		
		this.resourcesManager.holdResource(holdedResource, sessionId);

		this.resourcesManager.holdResource(holdedResource, 1);
	}

	@Test
	public void holdResource_SuccessfullyHoldsTheResource() throws Exception
	{
		int sessionId = 0;
		IResource holdedResource = this.resourcesManager.getResources().get(0);
		
		this.resourcesManager.holdResource(holdedResource, sessionId);

		Assert.assertFalse(this.resourcesManager.isResourceFree(holdedResource));

		Assert.assertTrue(this.resourcesManager.getSessionResources(sessionId)
				.contains(holdedResource));
	}

	@Test(expected = ResourceNotFoundException.class)
	public void isResourceFree_NonexistentResource_ThrowsResourceNotFoundException()
			throws Exception
	{
		IResource resource = new Resource("nonexistent_resource_name");
		
		this.resourcesManager.isResourceFree(resource);
	}

	@Test
	public void isResourceFree_ResourceIsFree_ReturnsTrue() throws Exception
	{
		IResource resource = this.resourcesManager.getResources().get(0);
		
		Assert.assertTrue(this.resourcesManager.isResourceFree(resource));
	}

	@Test
	public void isResourceFree_ResourceIsHeld_ReturnsFalse() throws Exception
	{
		int sessionId = 0;
		IResource holdedResource = this.resourcesManager.getResources().get(0);
		
		this.resourcesManager.holdResource(holdedResource, sessionId);
		
		Assert.assertFalse(this.resourcesManager.isResourceFree(holdedResource));
	}

	@Test(expected = ResourceNotFoundException.class)
	public void releaseResource_NonexistentResource_ThrowsResourceNotFoundException()
			throws Exception
	{
		IResource resource = new Resource("nonexistent_resource_name");
		
		this.resourcesManager.releaseResource(resource);
	}

	@Test
	public void releaseResource_ResourceIsFree_DoNothing() throws Exception
	{
		IResource resource = this.resourcesManager.getResources().get(0);

		Assert.assertTrue(this.resourcesManager.isResourceFree(resource));
		
		this.resourcesManager.releaseResource(resource);
	}

	@Test
	public void releaseResource_SuccessfullyReleasesTheResource()
			throws Exception
	{
		int sessionId = 0;
		IResource holdedResource = this.resourcesManager.getResources().get(0);
		
		this.resourcesManager.holdResource(holdedResource, sessionId);
		
		this.resourcesManager.releaseResource(holdedResource);

		Assert.assertTrue(this.resourcesManager.isResourceFree(holdedResource));
	}

	@Test(expected = IllegalArgumentException.class)
	public void releaseSessionResources_InvalidSessionId_ThrowsIllegalArgumentException()
	{
		this.resourcesManager.releaseSessionResources(-1);
	}

	@Test
	public void releaseSessionResources_ResourcesAreSuccessfullyReleased()
			throws Exception
	{
		int sessionId = 0;
		IResource holdedResource = this.resourcesManager.getResources().get(0);
		
		this.resourcesManager.holdResource(holdedResource, sessionId);

		this.resourcesManager.releaseSessionResources(sessionId);

		Assert.assertEquals(0,
			this.resourcesManager.getSessionResources(sessionId).size());
	}

	@Test
	public void releaseSessionResources_SessionHasNoResources_DoNothing()
	{
		int sessionId = 0;
		
		Assert.assertEquals(0,
			this.resourcesManager.getSessionResources(sessionId).size());

		this.resourcesManager.releaseSessionResources(sessionId);
	}

	@Before
	public void setUp() throws Exception
	{
		this.resourcesManager = new ResourcesManager();
	}
}
