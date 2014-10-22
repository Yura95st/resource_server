package resource_server.Models;

import resource_server.Enums.CommandCode;
import resource_server.Enums.CommandParameterName;
import resource_server.Exceptions.CommandParameterNameNotFoundException;

public interface ICommand
{
	CommandCode getCode();
	
	String getParameterValue(CommandParameterName name)
			throws CommandParameterNameNotFoundException;
	
	void setCode(CommandCode code);

	void setParameter(CommandParameterName name, String value);
	
	String toXML();
}
