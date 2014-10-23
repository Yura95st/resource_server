package resource_server.SessionsManager;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import resource_server.Exceptions.FailedToCloseSessionException;
import resource_server.Exceptions.FailedToOpenSessionFromSocketException;
import resource_server.Exceptions.SessionAlreadyExistsException;
import resource_server.Exceptions.SessionDoesNotExistException;
import resource_server.Helpers.Guard;
import resource_server.Models.ISession;
import resource_server.Models.Session;

public class SessionsManager implements IServerSessionsManager
{
	private static SessionsManager instance = null;

	public static SessionsManager getInstance()
	{
		if (SessionsManager.instance == null)
		{
			SessionsManager.instance = new SessionsManager();
		}

		return SessionsManager.instance;
	}
	
	private int nextSessionId;
	
	private HashMap<Integer, ISession> sessions;

	protected SessionsManager()
	{
		this.sessions = new HashMap<Integer, ISession>();
		this.nextSessionId = 0;
	}
	
	@Override
	public synchronized void closeSession(ISession session)
			throws SessionDoesNotExistException, FailedToCloseSessionException
	{
		Guard.isNotNull(session, "session");
		
		if (!this.sessions.containsKey(session.getId()))
		{
			throw new SessionDoesNotExistException(String.format(
				"Session with id: %1$d does not exist.", session.getId()));
		}

		try
		{
			session.close();
		}
		catch (IOException e)
		{
			throw new FailedToCloseSessionException(String.format(
				"Failed to close session with id: %1$d.", session.getId()), e);
		}
		
		this.sessions.remove(session.getId());
		
		System.out.println(String.format(
			"Session with id: %1$d has been closed.", session.getId()));
	}

	@Override
	public List<ISession> getOpenedSessions()
	{
		return new ArrayList<ISession>(this.sessions.values());
	}

	@Override
	public void openSession(ISession session)
			throws SessionAlreadyExistsException
	{
		Guard.isNotNull(session, "session");
		
		if (this.sessions.containsKey(session.getId()))
		{
			throw new SessionAlreadyExistsException(String.format(
				"Session with id: %1$d already exists.", session.getId()));
		}

		this.sessions.put(session.getId(), session);
		
		new Thread(session).start();

		System.out.println(String.format("Session with id: %1$d is opened.",
			session.getId()));
	}

	@Override
	public void openSessionFromSocket(Socket socket)
			throws SessionAlreadyExistsException,
		FailedToOpenSessionFromSocketException
	{
		Guard.isNotNull(socket, "socket");
		
		if (this.sessions.containsValue(socket))
		{
			throw new SessionAlreadyExistsException(String.format(
				"Session with socket: %1$s already exists.", socket));
		}

		try
		{
			Session session = new Session(this.getNextSessionId(), socket);

			this.openSession(session);
		}
		catch (IOException e)
		{
			throw new FailedToOpenSessionFromSocketException(String.format(
				"Failed to open session from socket: %1$s.", socket), e);
		}
	}
	
	private int getNextSessionId()
	{
		return this.nextSessionId++;
	}
	
}
