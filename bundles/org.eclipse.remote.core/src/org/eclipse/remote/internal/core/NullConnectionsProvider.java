package org.eclipse.remote.internal.core;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.remote.core.api2.IConnection;
import org.eclipse.remote.core.api2.IConnectionsProvider;
import org.eclipse.remote.core.api2.IConnectionsProviderDescriptor;

public class NullConnectionsProvider extends PlatformObject implements IConnectionsProvider {

	private final IConnectionsProviderDescriptor desc;
	
	public NullConnectionsProvider(IConnectionsProviderDescriptor desc) {
		this.desc = desc;
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
	public Collection<IConnection> getConnections() {
		return Collections.emptyList();
	}

	@Override
	public IConnection getConnection(String name) {
		return null;
	}

	@Override
	public boolean autoPopulated() {
		return true;
	}

}
