package org.eclipse.remote.core.api2;

import java.util.Properties;

import org.eclipse.remote.core.exception.RemoteConnectionException;

/**
 * Factory provided by the connections provider to manage connections.
 * 
 * @since 1.1
 */
public interface IConnectionsManager {

	/**
	 * Called by the connections service to load a connection
	 * from persistence store.
	 * 
	 * @param properties
	 * @throws RemoteConnectionException 
	 */
	void loadConnection(String name, Properties properties) throws RemoteConnectionException;

	/**
	 * Remove the connection.
	 * 
	 * @param connection
	 */
	void removeConnection(IConnection connection);

}
