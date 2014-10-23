package resource_server.Models;

import java.io.IOException;

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
}
