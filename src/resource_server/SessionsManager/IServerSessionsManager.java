package resource_server.SessionsManager;

import java.net.Socket;

import resource_server.Exceptions.FailedToOpenSessionFromSocketException;
import resource_server.Exceptions.SessionAlreadyExistsException;
import resource_server.Models.ISession;

public interface IServerSessionsManager extends ISessionsManager
{
	
	/**
	 * Opens the specific session.
	 *
	 * @param session the session
	 * @throws SessionAlreadyExistsException the session already exists exception
	 */
	void openSession(ISession session) throws SessionAlreadyExistsException;
	
	/**
	 * Opens new session from socket.
	 *
	 * @param socket the socket
	 * @throws SessionAlreadyExistsException
	 * @throws FailedToOpenSessionFromSocketException
	 */
	void openSessionFromSocket(Socket socket)
		throws SessionAlreadyExistsException,
		FailedToOpenSessionFromSocketException;
}
