package resource_server.CommandHandlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import resource_server.Helpers.ExceptionHelper;
import resource_server.Helpers.Guard;

public class ClientInputHandler implements Runnable
{
	private BufferedReader input;
	
	private Socket socket;
	
	public ClientInputHandler(Socket socket) throws IOException
	{
		Guard.isNotNull(socket, "socket");

		this.socket = socket;
		
		this.input = new BufferedReader(new InputStreamReader(
			this.socket.getInputStream()));
	}
	
	@Override
	public void run()
	{
		try
		{
			while (!this.socket.isClosed())
			{
				String fromServer = this.input.readLine();

				if (fromServer == null)
				{
					System.out.println("Connection lost.");
					break;
				}

				System.out.println("Server: " + fromServer);
			}
		}
		catch (Exception e)
		{
			System.err.println(ExceptionHelper.getFullExceptionMessage(e));
		}
		finally
		{
			try
			{
				this.input.close();

				this.socket.close();
			}
			catch (IOException e)
			{
				System.err.println(ExceptionHelper.getFullExceptionMessage(e));
			}
		}
	}
}
