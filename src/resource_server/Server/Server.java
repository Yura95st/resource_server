package resource_server.Server;

import java.io.Closeable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import resource_server.Helpers.ExceptionHelper;
import resource_server.Models.ISession;
import resource_server.SessionsManager.IServerSessionsManager;
import resource_server.SessionsManager.SessionsManager;

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
	
	private IServerSessionsManager sessionsManager;
	
	public Server(String host, int port)
	{
		this.host = host;
		
		this.port = port;
		
		this.sessionsManager = SessionsManager.getInstance();
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

			if (this.serverSocket != null)
			{
				this.serverSocket.close();
			}
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