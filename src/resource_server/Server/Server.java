package resource_server.Server;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import resource_server.Session;
import resource_server.ISession;
import resource_server.ISessionsManager;
import resource_server.SessionsManager;
import resource_server.Exceptions.FailedToOpenSessionFromSocketException;
import resource_server.Exceptions.SessionAlreadyExistsException;
import resource_server.Helpers.ExceptionHelper;

public class Server implements Closeable
{
	public static void main(String[] args)
	{
		try (Server server = new Server("127.0.0.1", 27015))
		{
			server.start();
		}
		catch (Exception exception)
		{
			System.err.println(ExceptionHelper
				.getFullExceptionMessage(exception));
		}
	}

	private String host;

	private int port;

	private ServerSocket serverSocket;
	
	private ISessionsManager sessionsManager;

	public Server(String host, int port)
	{
		this.host = host;

		this.port = port;

		this.sessionsManager = new SessionsManager();
	}

	@Override
	public void close()
	{
		List<ISession> sessions = this.sessionsManager.getOpenedSessions();
		
		try
		{
			for (int i = 0, count = sessions.size(); i < count; i++)
			{
				ISession session = sessions.get(i);
				
				this.sessionsManager.closeSession(session);
			}
			
			this.serverSocket.close();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Server is stopped.");
	}

	public void start() throws Exception
	{
		this.serverSocket = new ServerSocket(this.port, 0,
			InetAddress.getByName(this.host));

		System.out.println(String.format("Server is started on %1$s:%2$d.",
			this.host, this.port));

		while (true)
		{
			Socket socket = this.serverSocket.accept();

			this.sessionsManager.openSessionFromSocket(socket);
		}
	}
}