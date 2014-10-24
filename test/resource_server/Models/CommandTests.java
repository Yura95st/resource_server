package resource_server.Models;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import resource_server.Enums.CommandCode;
import resource_server.Enums.CommandParameterName;
import resource_server.Exceptions.CommandParameterNameNotFoundException;
import resource_server.Exceptions.FailedToParseCommandFromXMLException;

public class CommandTests
{
	private ICommand command;
	
	CommandParameterName parameterName = CommandParameterName.SessionsList;

	String parameterValue = "parameter_value";
	
	@Test
	public void getCode_ReturnsValidCommandCode()
	{
		CommandCode code = CommandCode.Client_Disconnect;

		ICommand newCommand = new Command();

		newCommand.setCode(code);

		Assert.assertEquals(code, newCommand.getCode());
	}
	
	@Test(expected = CommandParameterNameNotFoundException.class)
	public void getParameterValue_NonexistentParameterName_ThrowsCommandParameterNameNotFoundException()
		throws Exception

	{
		this.command.getParameterValue(CommandParameterName.ResourcesList);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getParameterValue_ParameterNameIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		this.command.getParameterValue(null);
	}

	@Test
	public void getParameterValue_ReturnsValidParameterValue() throws Exception
	{
		Assert.assertEquals(this.parameterValue,
			this.command.getParameterValue(this.parameterName));
	}
	
	@Test(expected = FailedToParseCommandFromXMLException.class)
	public void parseXML_InvalidXMLString_ThrowsFailedToParseCommandFromXMLException()
			throws Exception
	{
		Command.parseXML("invalid_xml_string");
	}
	
	@Test
	public void parseXML_ReturnsValidCommand() throws Exception
	{
		Map<CommandParameterName, String> parametersMap = new HashMap<CommandParameterName, String>() {
			{
				this.put(CommandParameterName.ResourceName, "resource_name");
				this.put(CommandParameterName.SessionId, "session_id");
			}
		};
		
		ICommand testCommand = new Command();
		
		testCommand.setCode(CommandCode.Client_Disconnect);
		
		for (Entry<CommandParameterName, String> entry : parametersMap
				.entrySet())
		{
			testCommand.setParameter(entry.getKey(), entry.getValue());
		}
		
		ICommand parseCommand = Command.parseXML(testCommand.toXML());
		
		Assert.assertEquals(testCommand.getCode(), parseCommand.getCode());
		
		for (Entry<CommandParameterName, String> entry : parametersMap
				.entrySet())
		{
			CommandParameterName key = entry.getKey();
			
			Assert.assertEquals(testCommand.getParameterValue(key),
				parseCommand.getParameterValue(key));
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void parseXML_XMLStringIsNull_ThrowsIllegalArgumentException()
			throws Exception
	{
		Command.parseXML(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void setCode_CommandCodeIsNull_ThrowsIllegalArgumentException()
	{
		this.command.setCode(null);
	}
	
	@Test
	public void setCode_SetsNewCommandCode()
	{
		CommandCode newCommandCode = CommandCode.Server_Bye;

		Assert.assertNotEquals(newCommandCode, this.command.getCode());

		this.command.setCode(newCommandCode);

		Assert.assertEquals(newCommandCode, this.command.getCode());
	}
	
	@Test
	public void setParameter_NewParameter_CreatesNewParameter()
		throws Exception
	{
		CommandParameterName name = CommandParameterName.ResourcesList;
		String value = "new_parameter_value";
		
		this.command.setParameter(name, value);
		
		Assert.assertEquals(value, this.command.getParameterValue(name));
	}
	
	@Test
	public void setParameter_ParameterAlreadyExists_UpdatesParameterValue()
		throws Exception
	{
		String value = "new_parameter_value";
		
		Assert.assertNotEquals(value,
			this.command.getParameterValue(this.parameterName));
		
		this.command.setParameter(this.parameterName, value);
		
		Assert.assertEquals(value,
			this.command.getParameterValue(this.parameterName));
	}

	@Test(expected = IllegalArgumentException.class)
	public void setParameter_ParameterNameIsNull_ThrowsIllegalArgumentException()
	{
		this.command.setParameter(null, "parameter_value");
	}

	@Test(expected = IllegalArgumentException.class)
	public void setParameter_ParameterValueIsNull_ThrowsIllegalArgumentException()
	{
		this.command.setParameter(this.parameterName, null);
	}
	
	@Before
	public void setUp() throws Exception
	{
		this.command = new Command();

		this.command.setCode(CommandCode.Server_Welcome);
		
		this.command.setParameter(this.parameterName, this.parameterValue);
	}
	
	@Test
	public void toXML_ReturnsValidXMLString() throws Exception
	{
		String xmlString = this.command.toXML();
		
		ICommand testCommand = Command.parseXML(xmlString);
		
		Assert.assertEquals(this.command, testCommand);
	}
}
