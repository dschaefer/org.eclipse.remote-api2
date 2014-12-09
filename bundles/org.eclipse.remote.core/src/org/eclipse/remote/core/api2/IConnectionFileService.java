package org.eclipse.remote.core.api2;

import org.eclipse.remote.core.IRemoteFileManager;

/**
 * @since 1.1
 */
public interface IConnectionFileService extends IRemoteFileManager {

	/**
	 * The connection for this service.
	 * 
	 * @return connection
	 */
	IConnection getConnection();

}
