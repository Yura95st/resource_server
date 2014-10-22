package resource_server.Client;

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

		ICommand command = new Command();
		
		if (this.socket.isClosed() || input.equalsIgnoreCase("quit")
			|| input.equalsIgnoreCase("q"))
		{
			this.active = false;
			return;
		}
		else if (input.equalsIgnoreCase("help") || input.equalsIgnoreCase("h"))
		{
			this.printHelp();
			return;
		}
		else if (input.equalsIgnoreCase("disconnect"))
		{
			command.setCode(CommandCode.Client_Disconnect);
		}
		else if (input.equalsIgnoreCase("get_sessions_list"))
		{
			command.setCode(CommandCode.Client_GetSessionsList);
		}
		else
		{
			System.out.println(String.format("Unknown command: %1$s", input));
			return;
		}

		this.output.println(command.toXML());
	}
	
	private void printHelp()
	{
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("Here are the list of available commands:");
		stringBuilder.append(System.getProperty("line.separator"));
		stringBuilder
				.append("disconnect              To disconnect from server.");
		stringBuilder.append(System.getProperty("line.separator"));
		stringBuilder
				.append("get_sessions_list       To get the list of all opened sessions.");
		stringBuilder.append(System.getProperty("line.separator"));
		stringBuilder.append("quit                    To exit");

		System.out.println(stringBuilder.toString());
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
				
				if (!this.socket.isClosed())
				{
					this.socket.close();
				}
			}
			catch (IOException e)
			{
				System.err.println(ExceptionHelper.getFullExceptionMessage(e));
			}
		}
	}
}
