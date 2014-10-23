package resource_server.Models;

import resource_server.Enums.CommandCode;
import resource_server.Enums.CommandParameterName;
import resource_server.Exceptions.CommandParameterNameNotFoundException;

public interface ICommand
{
	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	CommandCode getCode();

	/**
	 * Gets the parameter value.
	 *
	 * @param name the name
	 * @return the parameter value
	 * @throws CommandParameterNameNotFoundException
	 */
	String getParameterValue(CommandParameterName name)
		throws CommandParameterNameNotFoundException;

	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	void setCode(CommandCode code);
	
	/**
	 * Sets the parameter.
	 *
	 * @param name the name
	 * @param value the value
	 */
	void setParameter(CommandParameterName name, String value);

	/**
	 * To XML.
	 *
	 * @return the XMLstring
	 */
	String toXML();
}
