package resource_server.Models;

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

	CommandParameterName parameterName = CommandParameterName.Foo;
	
	String parameterValue = "parameter_value";

	@Test
	public void getCode_ReturnsValidCommandCode()
	{
		CommandCode code = CommandCode.Client_Disconnect;
		
		ICommand newCommand = new Command(code);
		
		Assert.assertEquals(code, newCommand.getCode());
	}

	@Test(expected = CommandParameterNameNotFoundException.class)
	public void getParameterValue_NonexistentParameterName_ThrowsCommandParameterNameNotFoundException()
			throws Exception
	
	{
		this.command.getParameterValue(CommandParameterName.Bar);
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

	@Test(expected = IllegalArgumentException.class)
	public void parseXML_XMLStringIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		Command.parseXML(null);
	}

	@Test
	public void setParameter_NewParameter_CreatesNewParameter()
			throws Exception
	{
		CommandParameterName name = CommandParameterName.Bar;
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
		this.command = new Command(CommandCode.Unknown);

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