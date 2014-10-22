package resource_server.Models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import resource_server.Enums.CommandCode;
import resource_server.Enums.CommandParameterName;
import resource_server.Helpers.ExceptionHelper;
import resource_server.Helpers.Guard;
import resource_server.SessionsManager.ISessionsManager;

public class Session implements ISession
{
	private int id;
	
	private BufferedReader input;
	
	private boolean isActive;
	
	private PrintWriter output;

	private ISessionsManager sessionsManager;
	
	private Socket socket;
	
	public Session(int id, Socket socket, ISessionsManager sessionsManager)
			throws IOException
	{
		this.id = id;
		this.socket = socket;
		this.sessionsManager = sessionsManager;
		
		this.input = new BufferedReader(new InputStreamReader(
			this.socket.getInputStream()));
		
		this.output = new PrintWriter(new BufferedWriter(
			new OutputStreamWriter(this.socket.getOutputStream())), true);
		
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
				
				default:
				{
					System.out.println(String.format(
						"Unknown command from server: %1$s", inputFromClient));
					break;
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(String.format(
				"Unknown format of the command from server: %1$s",
				inputFromClient));
		}
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
				this.sessionsManager.closeSession(this);
			}
			catch (Exception e)
			{
				System.err.println(ExceptionHelper.getFullExceptionMessage(e));
			}
		}
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

	private void sendSessionsList()
	{
		ICommand command = new Command();

		command.setCode(CommandCode.Server_SessionsList);

		StringBuilder stringBuilder = new StringBuilder();

		for (ISession session : this.sessionsManager.getOpenedSessions())
		{
			stringBuilder.append(String.format("Session #%1$d; ",
				session.getId()));
			stringBuilder.append(System.getProperty("line.separator"));
		}

		String parameterValue = stringBuilder.toString().trim();

		command.setParameter(CommandParameterName.SessionsList, parameterValue);

		this.sendCommand(command);
	}
	
	private void sendWelcomeCommand()
	{
		ICommand command = new Command();

		command.setCode(CommandCode.Server_Welcome);

		this.sendCommand(command);
	}
}
