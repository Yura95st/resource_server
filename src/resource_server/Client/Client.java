package resource_server.Client;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import resource_server.CommandHandlers.ClientOutputHandler;
import resource_server.CommandHandlers.ClientInputHandler;
import resource_server.Helpers.ExceptionHelper;

public class Client implements Closeable
{
	public static void main(String[] args)
	{
		try (Client client = new Client("127.0.0.1", 27015))
		{
			client.start();
		}
		catch (Exception e)
		{
			System.err.println(ExceptionHelper
					.getFullExceptionMessage(e));
		}
	}
	
	private String host;
	
	private int port;
	
	private Socket socket;
	
	public Client(String host, int port)
	{
		this.host = host;
		this.port = port;
	}
	
	@Override
	public void close() throws IOException
	{
		this.socket.close();
	}
	
	public void start() throws UnknownHostException, IOException
	{
		this.socket = new Socket(this.host, this.port);
		
		ClientInputHandler inputHandler = new ClientInputHandler(
			this.socket);
		
		ClientOutputHandler outputHandler = new ClientOutputHandler(
			this.socket);

		new Thread(inputHandler).start();

		outputHandler.run();
	}
}