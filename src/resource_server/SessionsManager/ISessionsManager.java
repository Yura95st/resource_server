package resource_server.SessionsManager;

import java.util.List;

import resource_server.Exceptions.FailedToCloseSessionException;
import resource_server.Exceptions.SessionDoesNotExistException;
import resource_server.Models.ISession;

public interface ISessionsManager
{
	void closeSession(ISession session) throws SessionDoesNotExistException,
		FailedToCloseSessionException;

	List<ISession> getOpenedSessions();
}
