package resource_server.Enums;

public enum CommandCode
{
	Client_Disconnect,
	Client_GetResource,
	Client_GetResourcesList,
	Client_GetSessionsList,
	Client_ReleaseAllResources,
	Client_ReleaseResource,
	Server_Bye,
	Server_ResourcesAreReleased,
	Server_ResourceIsAlreadyHeld,
	Server_ResourceIsHeld,
	Server_ResourceIsReleased,
	Server_ResourceNotFound,
	Server_ResourcesList,
	Server_SessionsList,
	Server_UnknownCommand,
	Server_Welcome,
	Unknown
}
