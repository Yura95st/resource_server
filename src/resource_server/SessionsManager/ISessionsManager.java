package resource_server.SessionsManager;

import java.util.List;

import resource_server.ISession;
import resource_server.Exceptions.FailedToCloseSessionException;
import resource_server.Exceptions.SessionDoesNotExistException;

public interface ISessionsManager
{
	void closeSession(ISession session) throws SessionDoesNotExistException,
		FailedToCloseSessionException;

	List<ISession> getOpenedSessions();
}