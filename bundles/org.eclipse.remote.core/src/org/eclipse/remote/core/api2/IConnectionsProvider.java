package org.eclipse.remote.core.api2;

import java.util.Collection;

/**
 * A provider of connections.
 * 
 * @since 1.1
 */
public interface IConnectionsProvider extends IConnectionsProviderDescriptor {

	/**
	 * Return all the connections managed by this provider.
	 * 
	 * @return connections
	 */
	Collection<IConnection> getConnections();
	
	/**
	 * Return the connection with the given name.
	 * 
	 * @param name
	 * @return connection
	 */
	IConnection getConnection(String name);

}
