package resource_server.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import resource_server.Helpers.ExceptionHelper;

public class Client implements Closeable
{
	public static void main(String[] args)
	{
		try (Client client = new Client("127.0.0.1", 27015))
		{
			client.start();
		}
		catch (Exception exception)
		{
			System.err.println(ExceptionHelper
				.getFullExceptionMessage(exception));
		}
	}

	private BufferedReader bufferedReader;

	private String host;

	private int port;

	private PrintWriter printWriter;

	private Socket socket;

	public Client(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	@Override
	public void close() throws IOException
	{
		this.bufferedReader.close();

		this.printWriter.close();

		this.socket.close();
	}

	private void listen() throws IOException
	{
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
			System.in));

		while (true)
		{
			String fromUser = stdIn.readLine();

			if (fromUser != null)
			{
				this.printWriter.println(fromUser);
			}

			String fromServer = this.bufferedReader.readLine();

			if (fromServer == null)
			{
				System.out.println("Disconnected from server.");

				break;
			}

			System.out.println("Server: " + fromServer);

			//			if (fromServer.equals("Bye"))
			//			{
			//				break;
			//			}
		}
	}

	public void start() throws UnknownHostException, IOException
	{
		this.socket = new Socket(this.host, this.port);

		this.bufferedReader = new BufferedReader(new InputStreamReader(
			this.socket.getInputStream()));

		this.printWriter = new PrintWriter(new BufferedWriter(
			new OutputStreamWriter(this.socket.getOutputStream())), true);

		this.listen();
	}
}