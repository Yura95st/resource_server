package resource_server.Client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import resource_server.Helpers.ExceptionHelper;

public class Client
{
	public static void main(String[] args)
	{
		Client client = new Client("127.0.0.1", 27015);

		try
		{
			client.start();
		}
		catch (Exception e)
		{
			System.err.println(ExceptionHelper.getFullExceptionMessage(e));
		}
	}
	
	private String host;
	
	private int port;
	
	public Client(String host, int port)
	{
		this.host = host;
		this.port = port;
	}
	
	public void start() throws UnknownHostException, IOException
	{
		Socket socket = new Socket(this.host, this.port);
		
		ClientInputHandler inputHandler = new ClientInputHandler(socket);
		
		ClientOutputHandler outputHandler = new ClientOutputHandler(socket);

		new Thread(inputHandler).start();

		outputHandler.run();
	}
}