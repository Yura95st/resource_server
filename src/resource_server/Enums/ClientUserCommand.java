package resource_server.Enums;

public enum ClientUserCommand
{
	Disconnect("disconnect"),
	GetResource("get_resource"),
	GetResourcesList("get_resources_list"),
	GetHeldResourcesList("get_held_resources_list"),
	GetSessionsList("get_sessions_list"),
	Help("help"),
	Quit("quit"),
	ReleaseAllResources("release_all_resources"),
	ReleaseResource("release_resource");
	
	private final String text;

	private ClientUserCommand(final String text)
	{
		this.text = text;
	}

	@Override
	public String toString()
	{
		return this.text;
	}
}