package resource_server.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import resource_server.Enums.CommandParameterName;
import resource_server.Helpers.ExceptionHelper;
import resource_server.Helpers.Guard;
import resource_server.Models.Command;
import resource_server.Models.ICommand;

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
				String inputFromServer = this.input.readLine();

				if (inputFromServer == null)
				{
					break;
				}
				
				this.handleInputFromServer(inputFromServer);
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

				if (!this.socket.isClosed())
				{
					this.socket.close();
				}
			}
			catch (IOException e)
			{
				System.err.println(ExceptionHelper.getFullExceptionMessage(e));
			}
			
			System.out.println("Connection lost. Press 'Enter' to exit...");
		}
	}
	
	private void handleInputFromServer(String inputFromServer)
	{
		Guard.isNotNull(inputFromServer, "inputFromServer");
		
		try
		{
			ICommand command = Command.parseXML(inputFromServer);
			
			switch (command.getCode())
			{
				case Server_Welcome:
				{
					System.out.println("Server: Welcome!");
					break;
				}

				case Server_Bye:
				{
					System.out.println("Server: Bye! I hope to see You later.");
					break;
				}

				case Server_SessionsList:
				{
					this.printSessionsList(command
						.getParameterValue(CommandParameterName.SessionsList));
					break;
				}

				case Server_ResourcesList:
				{
					this.printResourcesList(command
						.getParameterValue(CommandParameterName.ResourcesList));
					break;
				}

				case Server_ResourceIsAlreadyHeld:
				{
					System.out
							.println(String
									.format(
										"Server: Resource '%1$s' is already held.",
										command.getParameterValue(CommandParameterName.ResourceName)));
					break;
				}

				case Server_ResourceIsReleased:
				{
					System.out
							.println(String
									.format(
										"Server: Resource '%1$s' successfully released.",
										command.getParameterValue(CommandParameterName.ResourceName)));
					break;
				}

				case Server_ResourceIsHeld:
				{
					System.out
							.println(String
									.format(
										"Server: Resource '%1$s' successfully held.",
										command.getParameterValue(CommandParameterName.ResourceName)));
					break;
				}

				case Server_ResourcesAreReleased:
				{
					System.out
							.println("Server: All resources successfully released.");
					break;
				}

				case Server_ResourceNotFound:
				{
					System.out
							.println(String.format(
								"Server: Resource '%1$s' not found.",
								command.getParameterValue(CommandParameterName.ResourceName)));
					break;
				}

				case Server_UnknownCommand:
				{
					System.out.println("Server: Unknown command.");
					break;
				}
				
				default:
				{
					System.out.println(String.format(
						"Unknown command from server: %1$s", inputFromServer));
					break;
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(String.format(
				"Unknown format of the command from server: %1$s",
				inputFromServer));
		}
	}

	private void printResourcesList(String resourcesString)
	{
		System.out.println("Server: Here are the list of resources:");

		int i = 1;

		for (String resourceName : resourcesString.split("\t"))
		{
			System.out
					.println(String.format("\t%1$d. %2$s", i++, resourceName));
		}
	}
	
	private void printSessionsList(String sessionsString)
	{
		System.out.println("Server: Here are the list of opened sessions:");
		
		int i = 1;

		for (String sessionName : sessionsString.split("\t"))
		{
			System.out.println(String.format("\t%1$d. %2$s", i++, sessionName));
		}
	}
}
