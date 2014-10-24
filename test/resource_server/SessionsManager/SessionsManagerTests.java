package resource_server.SessionsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import resource_server.Exceptions.FailedToCloseSessionException;
import resource_server.Exceptions.SessionAlreadyExistsException;
import resource_server.Exceptions.SessionDoesNotExistException;
import resource_server.Exceptions.SessionIsNotActiveException;
import resource_server.Models.Command;
import resource_server.Models.ICommand;
import resource_server.Models.ISession;
import resource_server.Models.Session;

public class SessionsManagerTests
{
	private ICommand command;

	private ISession mockedSession;
	
	private IServerSessionsManager sessionsManager;

	@Test(expected = FailedToCloseSessionException.class)
	public void closeSession_IOExceptionOccured_ThrowsFailedToCloseSessionException()
		throws Exception
	{
		this.sessionsManager.openSession(this.mockedSession);

		Mockito.doThrow(IOException.class).when(this.mockedSession).close();

		this.sessionsManager.closeSession(this.mockedSession);
	}

	@Test(expected = SessionDoesNotExistException.class)
	public void closeSession_SessionDoesNotExist_ThrowsSessionDoesNotExistException()
		throws Exception
	{
		this.sessionsManager.closeSession(this.mockedSession);
	}

	@Test(expected = IllegalArgumentException.class)
	public void closeSession_SessionIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		this.sessionsManager.closeSession(null);
	}

	@Test
	public void closeSession_SessionSuccessfullyClosed() throws Exception
	{
		this.sessionsManager.openSession(this.mockedSession);

		this.sessionsManager.closeSession(this.mockedSession);

		Mockito.verify(this.mockedSession).close();

		Assert.assertFalse(this.sessionsManager.getOpenedSessions().contains(
			this.mockedSession));
	}

	@Test
	public void getOpenedSessions_ReturnsValidSessionsList() throws Exception
	{
		List<ISession> sessionsList = new ArrayList<ISession>() {
			{
				this.add(SessionsManagerTests.this.mockedSession);
			}
		};
		
		for (ISession session : sessionsList)
		{
			this.sessionsManager.openSession(session);
		}

		Assert.assertEquals(sessionsList,
			this.sessionsManager.getOpenedSessions());
	}

	@Test(expected = SessionAlreadyExistsException.class)
	public void openSession_SessionAlreadyExists_ThrowsSessionAlreadyExistsException()
		throws Exception
	{
		this.sessionsManager.openSession(this.mockedSession);

		this.sessionsManager.openSession(this.mockedSession);
	}

	@Test(expected = IllegalArgumentException.class)
	public void openSession_SessionIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		this.sessionsManager.openSession(null);
	}
	
	@Test
	public void openSession_SessionSuccessfullyOpened() throws Exception
	{
		Assert.assertFalse(this.sessionsManager.getOpenedSessions().contains(
			this.mockedSession));

		this.sessionsManager.openSession(this.mockedSession);
		
		Mockito.verify(this.mockedSession).run();

		Assert.assertTrue(this.sessionsManager.getOpenedSessions().contains(
			this.mockedSession));
	}

	@Test(expected = IllegalArgumentException.class)
	public void sendCommandToSession_CommandIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		this.sessionsManager.sendCommandToSession(this.mockedSession.getId(),
			null);
	}

	@Test(expected = SessionDoesNotExistException.class)
	public void sendCommandToSession_NonexistentSessionId_ThrowsSessionDoesNotExistException()
		throws Exception
	{
		int sessionId = this.mockedSession.getId() + 1;

		this.sessionsManager.sendCommandToSession(sessionId, this.command);
	}

	@Test(expected = SessionIsNotActiveException.class)
	public void sendCommandToSession_SessionIsNotActive_ThrowsSessionIsNotActiveException()
		throws Exception
	{
		Mockito.doThrow(SessionIsNotActiveException.class)
				.when(this.mockedSession).sendCommand(this.command);

		this.sessionsManager.openSession(this.mockedSession);

		this.sessionsManager.sendCommandToSession(this.mockedSession.getId(),
			this.command);
	}
	
	@Test
	public void sendCommandToSession_SuccessfullySentCommandToSession()
		throws Exception
	{
		this.sessionsManager.openSession(this.mockedSession);
		
		this.sessionsManager.sendCommandToSession(this.mockedSession.getId(),
			this.command);
		
		Mockito.verify(this.mockedSession).sendCommand(this.command);
	}

	@Before
	public void setUp() throws Exception
	{
		this.mockedSession = Mockito.mock(Session.class);

		Mockito.when(this.mockedSession.getId()).thenReturn(1);
		
		this.sessionsManager = new SessionsManager();

		this.command = new Command();
	}
}