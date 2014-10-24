package resource_server.Enums;

public enum CommandCode
{
	Client_Disconnect,
	Client_GetHeldResourcesList,
	Client_GetResource,
	Client_GetResourcesList,
	Client_GetSessionsList,
	Client_ReleaseAllResources,
	Client_ReleaseResource,
	Server_Bye,
	Server_HeldResourcesList,
	Server_ResourceIsAlreadyHeld,
	Server_ResourceIsHeld,
	Server_ResourceIsReleased,
	Server_ResourceIsRequested,
	Server_ResourceNotFound,
	Server_ResourcesAreReleased,
	Server_ResourcesList,
	Server_SessionsList,
	Server_UnknownCommand,
	Server_Welcome,
	Unknown
}
