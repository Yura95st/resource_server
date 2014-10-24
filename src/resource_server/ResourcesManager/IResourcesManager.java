package resource_server.ResourcesManager;

import java.util.List;

import resource_server.Exceptions.ResourceIsAlreadyHeldException;
import resource_server.Exceptions.ResourceNotFoundException;
import resource_server.Models.IResource;

public interface IResourcesManager
{

	/**
	 * Gets the identifier of the session, that is holding the specific
	 * resource.
	 *
	 * @param resource
	 *            the resource
	 * @return the session identifier
	 * @throws ResourceNotFoundException 
	 */
	int getHeldResourceSessionId(IResource resource) throws ResourceNotFoundException;
	
	/**
	 * Gets the resource by it's name.
	 *
	 * @param resourceName
	 *            the resource name
	 * @return the resource
	 * @throws ResourceNotFoundException
	 */
	IResource getResource(String resourceName) throws ResourceNotFoundException;

	/**
	 * Gets the resources.
	 *
	 * @return the resources
	 */
	List<IResource> getResources();

	/**
	 * Gets the resources, that are held by the session with specific
	 * identifier.
	 *
	 * @param sessionId
	 *            the session identifier
	 * @return the resources
	 */
	List<IResource> getSessionResources(int sessionId);

	/**
	 * Marks the resource as held by the session with specific identifier.
	 *
	 * @param resource
	 *            the resource
	 * @param sessionId
	 *            the session identifier
	 * @throws ResourceNotFoundException
	 * @throws ResourceIsAlreadyHeldException
	 */
	void holdResource(IResource resource, int sessionId)
			throws ResourceNotFoundException, ResourceIsAlreadyHeldException;

	/**
	 * Checks if resource is free.
	 *
	 * @param resource
	 *            the resource
	 * @return true, if resource is free
	 * @throws ResourceNotFoundException
	 */
	boolean isResourceFree(IResource resource) throws ResourceNotFoundException;
	
	/**
	 * Release resource.
	 *
	 * @param resource
	 *            the resource
	 * @throws ResourceNotFoundException
	 */
	void releaseResource(IResource resource) throws ResourceNotFoundException;

	/**
	 * Releases all resources, that were held by the session with specific
	 * identifier.
	 *
	 * @param sessionId
	 *            the session identifier
	 */
	void releaseSessionResources(int sessionId);
}