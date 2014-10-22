package resource_server.CommandHandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import resource_server.Enums.CommandCode;
import resource_server.Helpers.ExceptionHelper;
import resource_server.Helpers.Guard;
import resource_server.Models.Command;
import resource_server.Models.ICommand;

public class ClientOutputHandler implements Runnable
{
	private boolean active;
	
	private PrintWriter output;
	
	private Socket socket;
	
	public ClientOutputHandler(Socket socket) throws IOException
	{
		Guard.isNotNull(socket, "socket");

		this.socket = socket;
		
		this.output = new PrintWriter(new BufferedWriter(
			new OutputStreamWriter(this.socket.getOutputStream())), true);
		
		this.active = false;
	}
	
	private void handleInput(String input)
	{
		Guard.isNotNull(input, "input");
		
		if (this.socket.isClosed() || input.equalsIgnoreCase("quit")
			|| input.equalsIgnoreCase("q"))
		{
			this.active = false;
		}
		else if (input.equalsIgnoreCase("disconnect"))
		{
			ICommand command = new Command(CommandCode.Client_Disconnect);
			
			this.output.println(command.toXML());
		}
		else if (input.equalsIgnoreCase("get_all_sessions"))
		{
			this.output.println(input);
		}
		else
		{
			System.out.println(String.format("Unknown command: %1$s", input));
		}
	}
	
	@Override
	public void run()
	{
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
			System.in));
		
		this.active = true;
		
		try
		{
			while (this.active)
			{
				String inputString = stdIn.readLine();

				this.handleInput(inputString);
			}
		}
		catch (IOException e)
		{
			System.err.println(ExceptionHelper.getFullExceptionMessage(e));
		}
		finally
		{
			try
			{
				this.output.close();

				this.socket.close();
			}
			catch (IOException e)
			{
				System.err.println(ExceptionHelper.getFullExceptionMessage(e));
			}
		}
	}
}
