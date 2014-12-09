package org.eclipse.remote.core.api2;

import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.remote.core.exception.RemoteConnectionException;

/**
 * Represents a connection to a compute node. This interface provides
 * some core information about the connection and the node it connects
 * to. The connection can also provide services that accessible via
 * the IAdaptable interface.
 * 
 * Adapt to the IConnectionsManager interface to delete the connection
 * if that is supported by the connection type.
 * 
 * @since 1.1
 */
public interface IConnection extends IAdaptable {

	// common attributes
	static final String USERNAME = "connection.username";
	static final String ADDRESS = "connection.address";
	static final String PORT = "connection.port";

	// common properties
	final static String OS_NAME_PROPERTY = "os.name"; //$NON-NLS-1$
	final static String OS_VERSION_PROPERTY = "os.version"; //$NON-NLS-1$
	final static String OS_ARCH_PROPERTY = "os.arch"; //$NON-NLS-1$
	final static String FILE_SEPARATOR_PROPERTY = "file.separator"; //$NON-NLS-1$
	final static String PATH_SEPARATOR_PROPERTY = "path.separator"; //$NON-NLS-1$
	final static String LINE_SEPARATOR_PROPERTY = "line.separator"; //$NON-NLS-1$
	final static String USER_HOME_PROPERTY = "user.home"; //$NON-NLS-1$

	/**
	 * The name of the connection. Connections are uniquely identified
	 * by the id of the provider for this connection and this name.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Return the id of the provider for this connection.
	 * 
	 * @return proovider
	 */
	String getProviderId();
	
	/**
	 * Attributes for this connection. These are persisted with the connection.
	 * 
	 * @return
	 */
	Map<String, String> getAttributes();

	/**
	 * A read-only property describing some aspect of the connection.
	 * 
	 * @param key
	 * @return
	 */
	String getProperty(String key);

	/**
	 * Open the connection.
	 * 
	 * @param monitor
	 * @throws RemoteConnectionException
	 */
	void open(IProgressMonitor monitor) throws RemoteConnectionException;

	/**
	 * Close the connection.
	 */
	void close();

	/**
	 * Is the connection open
	 * @return true if connection is open
	 */
	boolean isOpen();

}
