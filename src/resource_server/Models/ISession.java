package resource_server.Models;

import java.io.IOException;

public interface ISession extends Runnable
{
	void close() throws IOException;
	
	int getId();
}
