package resource_server.ResourcesManager;

import java.util.List;

import resource_server.Exceptions.ResourceIsAlreadyHeldException;
import resource_server.Exceptions.ResourceNotFoundException;
import resource_server.Models.IResource;

public interface IResourcesManager
{
	IResource getResource(String resourceName) throws ResourceNotFoundException;
	
	List<IResource> getResources();

	List<IResource> getSessionResources(int sessionId);

	void holdResource(IResource resource, int sessionId) throws ResourceNotFoundException, ResourceIsAlreadyHeldException;

	boolean isResourceFree(IResource resource) throws ResourceNotFoundException;

	void releaseResource(IResource resource) throws ResourceNotFoundException;
	
	void releaseSessionResources(int sessionId);
}