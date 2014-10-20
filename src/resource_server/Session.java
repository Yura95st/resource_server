package resource_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import resource_server.SessionsManager.ISessionsManager;

public class Session implements ISession
{
	private BufferedReader bufferedReader;

	private int id;

	private PrintWriter printWriter;

	private ISessionsManager sessionsManager;
	
	private Socket socket;

	public Session(int id, Socket socket, ISessionsManager sessionsManager)
		throws IOException
	{
		this.id = id;
		this.socket = socket;
		this.sessionsManager = sessionsManager;

		this.bufferedReader = new BufferedReader(new InputStreamReader(
			this.socket.getInputStream()));

		this.printWriter = new PrintWriter(new BufferedWriter(
			new OutputStreamWriter(this.socket.getOutputStream())), true);
	}

	@Override
	public void close() throws IOException
	{
		this.socket.close();

		System.out.println("Socket has been closed: " + this.socket);
	}

	@Override
	public int getId()
	{
		return this.id;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				String str = this.bufferedReader.readLine();

				if (str == null)
				{
					break;
				}

				System.out.println(String.format("Client #%1$d: %2$s", this.id,
					str));

				StringBuilder stringBuilder = new StringBuilder();

				if (str.equals("GET_ALL_SESSIONS"))
				{
					for (ISession session : this.sessionsManager
							.getOpenedSessions())
					{
						stringBuilder.append(String.format("Session #%1$d; ",
							session.getId()));
					}
				}
				else
				{
					stringBuilder.append(String.format(
						"I got your message: \"%1$s\"", str));
				}

				String message = stringBuilder.toString();
				
				this.send(message);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				this.sessionsManager.closeSession(this);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void send(String message)
	{
		this.printWriter.println(message);
	}
}
