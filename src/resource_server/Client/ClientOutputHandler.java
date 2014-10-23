package resource_server.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import resource_server.Enums.ClientUserCommand;
import resource_server.Enums.CommandCode;
import resource_server.Enums.CommandParameterName;
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
	
	private void handleInput(String input)
	{
		Guard.isNotNull(input, "input");

		ICommand command = new Command();
		
		if (this.socket.isClosed()
			|| input.equalsIgnoreCase(ClientUserCommand.Quit.toString()))
		{
			this.active = false;
			return;
		}
		else if (input.equalsIgnoreCase(ClientUserCommand.Help.toString()))
		{
			this.printHelp();
			return;
		}
		else if (input
				.equalsIgnoreCase(ClientUserCommand.Disconnect.toString()))
		{
			command.setCode(CommandCode.Client_Disconnect);
		}
		else if (input.equalsIgnoreCase(ClientUserCommand.GetSessionsList
				.toString()))
		{
			command.setCode(CommandCode.Client_GetSessionsList);
		}
		else if (input.equalsIgnoreCase(ClientUserCommand.GetResourcesList
				.toString()))
		{
			command.setCode(CommandCode.Client_GetResourcesList);
		}
		else if (input.equalsIgnoreCase(ClientUserCommand.ReleaseAllResources
			.toString()))
		{
			command.setCode(CommandCode.Client_ReleaseAllResources);
		}
		else if (input.toLowerCase().startsWith(
			ClientUserCommand.GetResource.toString()))
		{
			int index = ClientUserCommand.GetResource.toString().length() + 1;

			if (index < input.length())
			{
				String resourceName = input.substring(index);
				
				command.setCode(CommandCode.Client_GetResource);
				command.setParameter(CommandParameterName.ResourceName,
					resourceName);
			}
			else
			{
				System.out.println(String.format(
					"Resource name can't be empty in '%1$s <resource_name>'.",
					ClientUserCommand.GetResource.toString()));
				return;
			}
		}
		else if (input.toLowerCase().startsWith(
			ClientUserCommand.ReleaseResource.toString()))
		{
			int index = ClientUserCommand.ReleaseResource.toString().length() + 1;

			if (index < input.length())
			{
				String resourceName = input.substring(index);
				
				command.setCode(CommandCode.Client_ReleaseResource);
				command.setParameter(CommandParameterName.ResourceName,
					resourceName);
			}
			else
			{
				System.out.println(String.format(
					"Resource name can't be empty in '%1$s <resource_name>'.",
					ClientUserCommand.ReleaseResource.toString()));
				return;
			}
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
		
		stringBuilder.append(ClientUserCommand.Disconnect.toString()
			+ " To disconnect from server.");
		stringBuilder.append(System.getProperty("line.separator"));
		
		stringBuilder.append(ClientUserCommand.GetResource.toString()
			+ " <resource_name> To get the resource from server.");
		stringBuilder.append(System.getProperty("line.separator"));
		
		stringBuilder.append(ClientUserCommand.GetResourcesList.toString()
			+ " To get the list of server resources.");
		stringBuilder.append(System.getProperty("line.separator"));
		
		stringBuilder.append(ClientUserCommand.GetSessionsList.toString()
			+ " To get the list of all opened sessions on the server.");
		stringBuilder.append(System.getProperty("line.separator"));
		
		stringBuilder.append(ClientUserCommand.ReleaseAllResources.toString()
			+ " To release all resources, that are held.");
		stringBuilder.append(System.getProperty("line.separator"));
		
		stringBuilder.append(ClientUserCommand.ReleaseResource.toString()
			+ " <resource_name> To release resource.");
		stringBuilder.append(System.getProperty("line.separator"));
		
		stringBuilder.append(ClientUserCommand.Quit.toString() + " To exit");

		System.out.println(stringBuilder.toString());
	}
}
