package resource_server.Models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import resource_server.Enums.CommandCode;
import resource_server.Enums.CommandParameterName;
import resource_server.Exceptions.ResourceIsAlreadyHeldException;
import resource_server.Exceptions.ResourceNotFoundException;
import resource_server.Helpers.ExceptionHelper;
import resource_server.Helpers.Guard;
import resource_server.ResourcesManager.IResourcesManager;
import resource_server.ResourcesManager.ResourcesManager;
import resource_server.SessionsManager.ISessionsManager;
import resource_server.SessionsManager.SessionsManager;

public class Session implements ISession
{
	private int id;
	
	private BufferedReader input;
	
	private boolean isActive;
	
	private PrintWriter output;
	
	private IResourcesManager resourcesManager;
	
	private ISessionsManager sessionsManager;
	
	private Socket socket;
	
	public Session(int id, Socket socket) throws IOException
	{
		Guard.isMoreOrEqualToZero(id, "id");
		Guard.isNotNull(socket, "socket");
		
		this.id = id;
		this.socket = socket;
		
		this.input = new BufferedReader(new InputStreamReader(
			this.socket.getInputStream()));
		
		this.output = new PrintWriter(new BufferedWriter(
			new OutputStreamWriter(this.socket.getOutputStream())), true);
		
		this.sessionsManager = SessionsManager.getInstance();
		
		this.resourcesManager = ResourcesManager.getInstance();
		
		this.isActive = false;
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
		this.isActive = true;
		
		this.sendWelcomeCommand();
		
		try
		{
			while (this.isActive)
			{
				String inputFromClient = this.input.readLine();
				
				if (inputFromClient == null)
				{
					break;
				}
				
				this.handleInputFromClient(inputFromClient);
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
				this.resourcesManager.releaseSessionResources(this.id);

				this.sessionsManager.closeSession(this);
			}
			catch (Exception e)
			{
				System.err.println(ExceptionHelper.getFullExceptionMessage(e));
			}
		}
	}
	
	private String getResourcesStringFromList(List<IResource> resourcesList)
	{
		StringBuilder stringBuilder = new StringBuilder();
		
		for (IResource resource : resourcesList)
		{
			stringBuilder.append(resource.getName());
			
			// Divide names with \t character to be able to split them later
			stringBuilder.append('\t');
		}
		
		String resourcesString = stringBuilder.toString().trim();

		return resourcesString;
	}
	
	private void handleInputFromClient(String inputFromClient)
	{
		Guard.isNotNull(inputFromClient, "inputFromClient");

		System.out.println(String.format("Client #%1$d: %2$s", this.id,
			inputFromClient));
		
		try
		{
			ICommand command = Command.parseXML(inputFromClient);
			
			switch (command.getCode())
			{
				case Client_Disconnect:
				{
					this.sendBye();
					this.isActive = false;
					break;
				}
				
				case Client_GetSessionsList:
				{
					this.sendSessionsList();
					break;
				}
				
				case Client_GetResourcesList:
				{
					this.sendResourcesList();
					break;
				}

				case Client_GetHeldResourcesList:
				{
					this.sendHeldResourcesList();
					break;
				}
				
				case Client_GetResource:
				{
					this.tryToHoldResource(command
							.getParameterValue(CommandParameterName.ResourceName));
					break;
				}
				
				case Client_ReleaseResource:
				{
					this.releaseResource(command
							.getParameterValue(CommandParameterName.ResourceName));
					break;
				}
				
				case Client_ReleaseAllResources:
				{
					this.releaseAllResources();
					break;
				}
				
				default:
				{
					this.sendUnknownCommand();
					break;
				}
			}
		}
		catch (Exception e)
		{
			this.sendUnknownCommand();
		}
	}
	
	private void releaseAllResources()
	{
		ICommand command = new Command();
		
		CommandCode code = CommandCode.Server_ResourcesAreReleased;
		
		this.resourcesManager.releaseSessionResources(this.id);
		
		command.setCode(code);
		
		this.sendCommand(command);
	}
	
	private void releaseResource(String resourceName)
	{
		ICommand command = new Command();
		
		command.setParameter(CommandParameterName.ResourceName, resourceName);
		
		CommandCode code = CommandCode.Server_ResourceIsReleased;
		
		try
		{
			IResource resource = this.resourcesManager
					.getResource(resourceName);
			
			this.resourcesManager.releaseResource(resource);
		}
		catch (ResourceNotFoundException e)
		{
			code = CommandCode.Server_ResourceNotFound;
		}
		
		command.setCode(code);
		
		this.sendCommand(command);
	}
	
	private void sendBye()
	{
		ICommand command = new Command();
		
		command.setCode(CommandCode.Server_Bye);
		
		this.sendCommand(command);
	}
	
	private void sendCommand(ICommand command)
	{
		this.output.println(command.toXML());
	}

	private void sendHeldResourcesList()
	{
		ICommand command = new Command();
		
		command.setCode(CommandCode.Server_HeldResourcesList);

		List<IResource> resourcesList = this.resourcesManager
				.getSessionResources(this.id);
		
		command.setParameter(CommandParameterName.ResourcesList,
			this.getResourcesStringFromList(resourcesList));
		
		this.sendCommand(command);
	}

	private void sendResourcesList()
	{
		ICommand command = new Command();
		
		command.setCode(CommandCode.Server_ResourcesList);

		List<IResource> resourcesList = this.resourcesManager.getResources();
		
		command.setParameter(CommandParameterName.ResourcesList,
			this.getResourcesStringFromList(resourcesList));
		
		this.sendCommand(command);
	}

	private void sendSessionsList()
	{
		ICommand command = new Command();
		
		command.setCode(CommandCode.Server_SessionsList);
		
		StringBuilder stringBuilder = new StringBuilder();
		
		for (ISession session : this.sessionsManager.getOpenedSessions())
		{
			stringBuilder
					.append(String.format("Session #%1$d", session.getId()));
			
			// Divide session names with \t character to be able to split them
			// later
			stringBuilder.append('\t');
		}
		
		String parameterValue = stringBuilder.toString().trim();
		
		command.setParameter(CommandParameterName.SessionsList, parameterValue);
		
		this.sendCommand(command);
	}
	
	private void sendUnknownCommand()
	{
		ICommand command = new Command();
		
		command.setCode(CommandCode.Server_UnknownCommand);
		
		this.sendCommand(command);
	}
	
	private void sendWelcomeCommand()
	{
		ICommand command = new Command();
		
		command.setCode(CommandCode.Server_Welcome);
		
		this.sendCommand(command);
	}
	
	private void tryToHoldResource(String resourceName)
	{
		ICommand command = new Command();
		
		command.setParameter(CommandParameterName.ResourceName, resourceName);
		
		CommandCode code = CommandCode.Server_ResourceIsHeld;
		
		try
		{
			IResource resource = this.resourcesManager
					.getResource(resourceName);
			
			this.resourcesManager.holdResource(resource, this.id);
		}
		catch (ResourceNotFoundException e)
		{
			code = CommandCode.Server_ResourceNotFound;
		}
		catch (ResourceIsAlreadyHeldException e)
		{
			code = CommandCode.Server_ResourceIsAlreadyHeld;
		}
		
		command.setCode(code);
		
		this.sendCommand(command);
	}
}
