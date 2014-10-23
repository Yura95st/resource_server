package resource_server.SessionsManager;

import java.util.List;

import resource_server.Exceptions.FailedToCloseSessionException;
import resource_server.Exceptions.SessionDoesNotExistException;
import resource_server.Models.ISession;

public interface ISessionsManager
{
	/**
	 * Closes the specific session.
	 *
	 * @param session the session
	 * @throws SessionDoesNotExistException
	 * @throws FailedToCloseSessionException
	 */
	void closeSession(ISession session) throws SessionDoesNotExistException,
		FailedToCloseSessionException;

	/**
	 * Gets the list of opened sessions.
	 *
	 * @return the opened sessions
	 */
	List<ISession> getOpenedSessions();
}
