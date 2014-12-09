package org.eclipse.remote.core.api2;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import org.eclipse.remote.core.IRemoteConnectionChangeListener;

/**
 * OSGi service to access the Connections framework.
 * 
 * @noextend This interface is not intended to be extended by clients.
 * @since 1.1
 */
public interface IConnectionsService {

	/**
	 * Return all connections known to the system.
	 * 
	 * @return collection of connections
	 */
	Collection<IConnection> getConnections();

	/**
	 * Return the specified connection. Connections are uniquely identified
	 * by the id of their provider and their name.
	 * 
	 * @param providerId
	 * @param name
	 * @return connection
	 */
	IConnection getConnection(String providerId, String name);

	/**
	 * Called by provider to save the connection.
	 * 
	 * @param connection
	 * @throws IOException 
	 */
	void saveConnection(IConnection connection, Properties connectionProperties) throws IOException;
	
	/**
	 * Return all connections providers.
	 * 
	 * @return connections providers
	 */
	Collection<IConnectionsProviderDescriptor> getConnectionsProviders();

	/**
	 * Return the connections provider with the given id.
	 * 
	 * @param id id of connections provider
	 * @return connections provider
	 */
	IConnectionsProviderDescriptor getConnectionsProvider(String id);

	/**
	 * Add connection change event listener.
	 * 
	 * @param listener
	 */
	void addListener(IRemoteConnectionChangeListener listener);

	/**
	 * Remove connection change event listener.
	 * 
	 * @param listener
	 */
	void removeListener(IRemoteConnectionChangeListener listener);

}
