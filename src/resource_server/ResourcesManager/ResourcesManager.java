package resource_server.ResourcesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import resource_server.Exceptions.ResourceIsAlreadyHeldException;
import resource_server.Exceptions.ResourceNotFoundException;
import resource_server.Helpers.Guard;
import resource_server.Models.IResource;
import resource_server.Models.Resource;

public class ResourcesManager implements IResourcesManager
{
	private static ResourcesManager instance = null;
	
	public static ResourcesManager getInstance()
	{
		if (ResourcesManager.instance == null)
		{
			ResourcesManager.instance = new ResourcesManager();
		}

		return ResourcesManager.instance;
	}
	
	Map<String, Integer> heldResources;
	
	Map<String, IResource> resorces;
	
	protected ResourcesManager()
	{
		this.resorces = new HashMap<String, IResource>();
		
		this.heldResources = new HashMap<String, Integer>();
		
		this.initResources();
	}
	
	private void checkResourceName(String name)
			throws ResourceNotFoundException
	{
		if (!this.resorces.containsKey(name))
		{
			throw new ResourceNotFoundException(String.format(
				"Resource with name '%1$s' not found.", name));
		}
	}
	
	@Override
	public IResource getResource(String resourceName)
		throws ResourceNotFoundException
	{
		Guard.isNotNull(resourceName, "resourceName");

		this.checkResourceName(resourceName);

		return this.resorces.get(resourceName);
	}
	
	@Override
	public List<IResource> getResources()
	{
		return new ArrayList<IResource>(this.resorces.values());
	}
	
	@Override
	public List<IResource> getSessionResources(int sessionId)
	{
		Guard.isMoreOrEqualToZero(sessionId, "sessionId");

		List<IResource> sessionResources = new ArrayList<IResource>();

		for (Entry<String, Integer> entry : this.heldResources.entrySet())
		{
			if (entry.getValue() == sessionId)
			{
				sessionResources.add(this.resorces.get(entry.getKey()));
			}
		}

		return sessionResources;
	}
	
	@Override
	public void holdResource(IResource resource, int sessionId)
		throws ResourceNotFoundException, ResourceIsAlreadyHeldException
	{
		if (!this.isResourceFree(resource))
		{
			throw new ResourceIsAlreadyHeldException(String.format(
				"Resource with name '%1$s' is already held.",
				resource.getName()));
		}

		this.heldResources.put(resource.getName(), sessionId);
	}

	private void initResources()
	{
		int resourcesCount = 10;
		
		for (int i = 0; i < resourcesCount; i++)
		{
			IResource resource = new Resource(String.format("Resource_%1$d", i));
			
			this.resorces.put(resource.getName(), resource);
		}
	}

	@Override
	public boolean isResourceFree(IResource resource)
		throws ResourceNotFoundException
	{
		Guard.isNotNull(resource, "resource");

		String resourceName = resource.getName();

		this.checkResourceName(resourceName);

		return !this.heldResources.containsKey(resourceName);
	}
	
	@Override
	public void releaseResource(IResource resource)
		throws ResourceNotFoundException
	{
		Guard.isNotNull(resource, "resource");

		String resourceName = resource.getName();

		this.checkResourceName(resourceName);

		this.heldResources.remove(resourceName);
	}

	@Override
	public void releaseSessionResources(int sessionId)
	{
		List<IResource> sessionResources = this.getSessionResources(sessionId);

		for (IResource resource : sessionResources)
		{
			try
			{
				this.releaseResource(resource);
			}
			catch (ResourceNotFoundException e)
			{
				continue;
			}
		}
	}
}
