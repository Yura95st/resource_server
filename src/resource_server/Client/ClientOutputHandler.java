package resource_server.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
	
	private Map<ClientUserCommand, String> helpMap;
	
	private PrintWriter output;

	private Socket socket;
	
	public ClientOutputHandler(Socket socket) throws IOException
	{
		Guard.isNotNull(socket, "socket");

		this.socket = socket;
		
		this.output = new PrintWriter(new BufferedWriter(
			new OutputStreamWriter(this.socket.getOutputStream())), true);
		
		this.active = false;
		
		this.helpMap = new TreeMap<ClientUserCommand, String>() {
			{
				this.put(ClientUserCommand.Disconnect,
						"To disconnect from server.");
				this.put(ClientUserCommand.GetResource,
						"To get the resource from server.");
				this.put(ClientUserCommand.GetHeldResourcesList,
						"To get the list of held resources.");
				this.put(ClientUserCommand.GetResourcesList,
						"To get the list of server resources.");
				this.put(ClientUserCommand.GetSessionsList,
						"To get the list of all opened sessions on the server.");
				this.put(ClientUserCommand.ReleaseAllResources,
						"To release all resources, that are held.");
				this.put(ClientUserCommand.ReleaseResource,
						"To release resource.");
				this.put(ClientUserCommand.Quit, "To exit.");
			}
		};
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
				if (!this.socket.isClosed())
				{
					this.socket.shutdownInput();
					
					this.socket.close();
				}
			}
			catch (IOException e)
			{
				System.err.println(ExceptionHelper.getFullExceptionMessage(e));
			}
		}
	}
	
	private void handleInput(String input) throws IOException
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
		else if (input.equalsIgnoreCase(ClientUserCommand.GetHeldResourcesList
			.toString()))
		{
			command.setCode(CommandCode.Client_GetHeldResourcesList);
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

		int maxLength = 0;

		for (ClientUserCommand key : this.helpMap.keySet())
		{
			int length = key.toString().length();

			maxLength = maxLength < length ? length : maxLength;
		}

		String separator = "";

		for (int i = 0; i < maxLength + 20; i++)
		{
			separator += " ";
		}

		stringBuilder.append("Here are the list of available commands:");

		for (Entry<ClientUserCommand, String> entry : this.helpMap.entrySet())
		{
			ClientUserCommand key = entry.getKey();
			
			int length = key.toString().length();
			
			stringBuilder.append(System.getProperty("line.separator"));
			stringBuilder.append(key.toString());
			
			if (key == ClientUserCommand.GetResource || key == ClientUserCommand.ReleaseResource)
			{
				String parameter = " <resource_name>";
				stringBuilder.append(parameter);
				
				length += parameter.length();
			}
			
			stringBuilder.append(separator.substring(length));
			stringBuilder.append(entry.getValue());
		}

		System.out.println(stringBuilder.toString().trim());
	}
}
