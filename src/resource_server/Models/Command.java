package resource_server.Models;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import resource_server.Enums.CommandCode;
import resource_server.Enums.CommandParameterName;
import resource_server.Exceptions.CommandParameterNameNotFoundException;
import resource_server.Exceptions.FailedToParseCommandFromXMLException;
import resource_server.Helpers.Guard;

public class Command implements ICommand
{
	public static ICommand parseXML(String xmlString)
		throws FailedToParseCommandFromXMLException
	{
		Guard.isNotNull(xmlString, "xmlString");
		
		try
		{
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlString));

			Document doc = db.parse(is);

			Node commandCodeNode = doc.getElementsByTagName("code").item(0);

			ICommand command = new Command(CommandCode.valueOf(commandCodeNode
				.getTextContent()));

			NodeList parametersNodeList = doc.getElementsByTagName("parameter");

			for (int i = 0, count = parametersNodeList.getLength(); i < count; i++)
			{
				Node parameterNode = parametersNodeList.item(0);

				NodeList childNodes = parameterNode.getChildNodes();

				CommandParameterName parameterName = CommandParameterName
						.valueOf(childNodes.item(0).getTextContent());
				String parameterValue = childNodes.item(1).getTextContent();

				command.setParameter(parameterName, parameterValue);
			}

			return command;
		}
		catch (Exception e)
		{
			throw new FailedToParseCommandFromXMLException(String.format(
				"Failed to parse Command from XML: %1$s", xmlString), e);
		}
	}
	
	CommandCode code;
	
	Map<CommandParameterName, String> parameters;
	
	public Command(CommandCode code)
	{
		this.code = code;

		this.parameters = new HashMap<CommandParameterName, String>();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		Command other = (Command) obj;
		if (this.code != other.code)
		{
			return false;
		}
		if (this.parameters == null)
		{
			if (other.parameters != null)
			{
				return false;
			}
		}
		else if (!this.parameters.equals(other.parameters))
		{
			return false;
		}
		return true;
	}

	@Override
	public CommandCode getCode()
	{
		return this.code;
	}
	
	@Override
	public String getParameterValue(CommandParameterName name)
			throws CommandParameterNameNotFoundException
	{
		Guard.isNotNull(name, "name");

		if (!this.parameters.containsKey(name))
		{
			throw new CommandParameterNameNotFoundException(String.format(
				"Command parameter with name: %1$s was not found.", name));
		}

		return this.parameters.get(name);
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
			+ ((this.code == null) ? 0 : this.code.hashCode());
		result = prime * result
				+ ((this.parameters == null) ? 0 : this.parameters.hashCode());
		return result;
	}
	
	@Override
	public void setParameter(CommandParameterName name, String value)
	{
		Guard.isNotNull(name, "name");
		Guard.isNotNull(value, "value");

		this.parameters.put(name, value);
	}
	
	@Override
	public String toXML()
	{
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("<command>");
		
		stringBuilder.append(String.format("<code>%1$s</code>", this.code));
		
		stringBuilder.append("<parameters>");
		
		for (Entry<CommandParameterName, String> entry : this.parameters
				.entrySet())
		{
			stringBuilder.append("<parameter>");
			stringBuilder.append(String.format("<name>%1$s</name>",
				entry.getKey()));
			stringBuilder.append(String.format("<value>%1$s</value>",
				entry.getValue()));
			stringBuilder.append("</parameter>");
		}
		
		stringBuilder.append("</parameters>");
		stringBuilder.append("</command>");
		
		String xmlString = stringBuilder.toString().trim();
		
		return xmlString;
	}
}
