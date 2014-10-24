package resource_server.SessionsManager;

import java.util.List;

import resource_server.Exceptions.FailedToCloseSessionException;
import resource_server.Exceptions.SessionDoesNotExistException;
import resource_server.Exceptions.SessionIsNotActiveException;
import resource_server.Models.ICommand;
import resource_server.Models.ISession;

public interface ISessionsManager
{
	/**
	 * Closes the specific session.
	 *
	 * @param session
	 *            the session
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

	/**
	 * Sends command to the specific session.
	 *
	 * @param sessionId
	 *            the session id
	 * @param command
	 *            the command
	 * @throws SessionDoesNotExistException
	 * @throws SessionIsNotActiveException 
	 */
	void sendCommandToSession(int sessionId, ICommand command)
			throws SessionDoesNotExistException, SessionIsNotActiveException;
}
