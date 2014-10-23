package resource_server.SessionsManager;

import java.net.Socket;

import resource_server.Exceptions.FailedToOpenSessionFromSocketException;
import resource_server.Exceptions.SessionAlreadyExistsException;
import resource_server.Models.ISession;

public interface IServerSessionsManager extends ISessionsManager
{
	void openSession(ISession session) throws SessionAlreadyExistsException;
	
	void openSessionFromSocket(Socket socket)
		throws SessionAlreadyExistsException,
		FailedToOpenSessionFromSocketException;
}
