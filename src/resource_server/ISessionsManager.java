package resource_server;

import java.net.Socket;
import java.util.List;

import resource_server.Exceptions.FailedToCloseSessionException;
import resource_server.Exceptions.FailedToOpenSessionFromSocketException;
import resource_server.Exceptions.SessionAlreadyExistsException;
import resource_server.Exceptions.SessionDoesNotExistException;

public interface ISessionsManager
{
	void closeSession(ISession session) throws SessionDoesNotExistException,
		FailedToCloseSessionException;

	List<ISession> getOpenedSessions();
	
	void openSession(ISession session) throws SessionAlreadyExistsException;

	void openSessionFromSocket(Socket socket)
			throws SessionAlreadyExistsException,
			FailedToOpenSessionFromSocketException;
}
