package org.eclipse.remote.internal.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteServices;
import org.eclipse.remote.core.api2.IConnection;
import org.eclipse.remote.core.api2.IConnectionsProvider;
import org.eclipse.remote.core.api2.IConnectionsProviderDescriptor;

public class ProxyConnectionsProvider extends PlatformObject implements IConnectionsProvider {

	private final IConnectionsProviderDescriptor desc;
	private final IRemoteServices remoteServices;

	public ProxyConnectionsProvider(IConnectionsProviderDescriptor desc, IRemoteServices remoteServices) {
		this.desc = desc;
		this.remoteServices = remoteServices;
	}

	@Override
	public String getId() {
		return desc.getId();
	}

	@Override
	public String getName() {
		return desc.getName();
	}
	
	@Override
	public boolean autoPopulated() {
		return desc.autoPopulated();
	}

	@Override
	public Collection<IConnection> getConnections() {
		List<IConnection> connections = new ArrayList<>(1);
		for (IRemoteConnection remoteConn : remoteServices.getConnectionManager().getConnections()) {
			if (remoteConn instanceof IConnection)
				connections.add((IConnection) remoteConn);
		}
		return connections;
	}

	@Override
	public IConnection getConnection(String name) {
		IRemoteConnection remoteConn = remoteServices.getConnectionManager().getConnection(name);
		if (remoteConn instanceof IConnection)
			return (IConnection) remoteConn;
		else
			return null;
	}

}
