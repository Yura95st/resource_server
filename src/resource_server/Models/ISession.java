package resource_server.Models;

import java.io.IOException;

import resource_server.Exceptions.SessionIsNotActiveException;

public interface ISession extends Runnable
{
	/**
	 * Closes the session.
	 *
	 * @throws IOException
	 */
	void close() throws IOException;
	
	/**
	 * Gets the identifier.
	 *
	 * @return the identifier
	 */
	int getId();

	/**
	 * Sends command.
	 *
	 * @param command
	 *            the command
	 * @throws SessionIsNotActiveException 
	 */
	void sendCommand(ICommand command) throws SessionIsNotActiveException;
}
