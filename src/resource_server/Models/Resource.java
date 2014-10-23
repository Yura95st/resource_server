package resource_server.Models;

import resource_server.Helpers.Guard;

public class Resource implements IResource
{
	private String name;
	
	public Resource(String name)
	{
		Guard.isNotNull(name, "name");
		
		this.name = name;
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
		Resource other = (Resource) obj;
		if (this.name == null)
		{
			if (other.name != null)
			{
				return false;
			}
		}
		else if (!this.name.equals(other.name))
		{
			return false;
		}
		return true;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
			+ ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}
}
