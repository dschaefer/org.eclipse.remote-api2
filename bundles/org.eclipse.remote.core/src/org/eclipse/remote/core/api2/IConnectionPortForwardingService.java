package org.eclipse.remote.core.api2;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.remote.core.exception.RemoteConnectionException;

/**
 * Service for managing port forwarding accross the connection.
 * 
 * @since 1.1
 */
public interface IConnectionPortForwardingService {

	/**
	 * The connection for this service.
	 * 
	 * @return connection
	 */
	IConnection getConnection();
	
	/**
	 * Forward local port localPort to remote port fwdPort on remote machine fwdAddress. If this IRemoteConnection is not to
	 * fwdAddress, the port will be routed via the connection machine to fwdAddress.
	 * 
	 * @param localPort
	 *            local port to forward
	 * @param fwdAddress
	 *            address of remote machine
	 * @param fwdPort
	 *            remote port on remote machine
	 * @throws RemoteConnectionException
	 */
	public void forwardLocalPort(int localPort, String fwdAddress, int fwdPort) throws RemoteConnectionException;

	/**
	 * Forward a local port to remote port fwdPort on remote machine fwdAddress. The local port is chosen dynamically and returned
	 * by the method. If this IRemoteConnection is not to fwdAddress, the port will be routed via the connection machine to
	 * fwdAddress.
	 * 
	 * @param fwdAddress
	 * @param fwdPort
	 * @param monitor
	 * @return local port number
	 * @throws RemoteConnectionException
	 */
	public int forwardLocalPort(String fwdAddress, int fwdPort, IProgressMonitor monitor) throws RemoteConnectionException;

	/**
	 * Forward remote port remotePort to port fwdPort on machine fwdAddress. When a connection is made to remotePort on the remote
	 * machine, it is forwarded via this IRemoteConnection to fwdPort on machine fwdAddress.
	 * 
	 * @param remotePort
	 *            remote port to forward
	 * @param fwdAddress
	 *            address of recipient machine
	 * @param fwdPort
	 *            port on recipient machine
	 * @throws RemoteConnectionException
	 */
	public void forwardRemotePort(int remotePort, String fwdAddress, int fwdPort) throws RemoteConnectionException;

	/**
	 * Forward a remote port to port fwdPort on remote machine fwdAddress. The remote port is chosen dynamically and returned by the
	 * method. When a connection is made to this port on the remote machine, it is forwarded via this IRemoteConnection to fwdPort
	 * on machine fwdAddress.
	 * 
	 * If fwdAddress is the empty string ("") then the fwdPort will be bound to any address on all interfaces. Note that this
	 * requires enabling the GatewayPort sshd option on some systems.
	 * 
	 * @param fwdAddress
	 * @param fwdPort
	 * @param monitor
	 * @return remote port number
	 * @throws RemoteConnectionException
	 */
	public int forwardRemotePort(String fwdAddress, int fwdPort, IProgressMonitor monitor) throws RemoteConnectionException;

}
