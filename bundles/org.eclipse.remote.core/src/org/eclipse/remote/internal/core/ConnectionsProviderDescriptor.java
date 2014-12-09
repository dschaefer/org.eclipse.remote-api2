package org.eclipse.remote.internal.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.core.runtime.Status;
import org.eclipse.remote.core.IRemoteServices;
import org.eclipse.remote.core.RemoteServices;
import org.eclipse.remote.core.api2.IConnectionsProvider;
import org.eclipse.remote.core.api2.IConnectionsProviderDescriptor;
import org.eclipse.remote.core.api2.IConnectionsProviderFactory;

public class ConnectionsProviderDescriptor extends PlatformObject implements IConnectionsProviderDescriptor {

	private final ConnectionsService connectionsService;
	private final String id;
	private final String name;
	private final boolean autoPopulated;
	private IConnectionsProvider provider;
	private IConfigurationElement element;

	public ConnectionsProviderDescriptor(ConnectionsService connectionsService, IConfigurationElement element) {
		this.connectionsService = connectionsService;
		this.id = element.getAttribute("id");
		this.name = element.getAttribute("name");
		String autoPopulatedStr = element.getAttribute("autoPopulated");
		this.autoPopulated = autoPopulatedStr != null ? Boolean.valueOf(autoPopulatedStr) : false;
		if ("connectionsProvider".equals(element.getName())) {
			this.element = element;
		}
	}

	private void loadProvider() {
		if (provider == null) {
			if (element != null) {
				try {
					IConnectionsProviderFactory factory = (IConnectionsProviderFactory) element.createExecutableExtension("class");
					provider = factory.getProvider(this);
					connectionsService.providerLoaded(provider);
					element = null; // release the element
				} catch (CoreException e) {
					RemoteCorePlugin.log(e.getStatus());
				}
			} else {
				// Try the old remote services
				IRemoteServices remoteServices = RemoteServices.getRemoteServices(id);
				if (remoteServices != null) {
					provider = new ProxyConnectionsProvider(this, remoteServices);
				} else {
					// Nope, just use the null one.
					provider = new NullConnectionsProvider(this);
					RemoteCorePlugin.log(new Status(IStatus.WARNING, RemoteCorePlugin.getUniqueIdentifier(),
							"Factory not provided for " + id));
				}
			}
			
			// Tell the services we're loaded
			connectionsService.providerLoaded(provider);
		}
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (IConnectionsProvider.class.equals(adapter)) {
			loadProvider();
			return provider;
		}

		// Try the super class first to avoid loading the provider
		Object obj = super.getAdapter(adapter);
		if (obj != null)
			return obj;

		// Nope, try adapting the provider
		loadProvider();
		if (provider != null) {
			obj = provider.getAdapter(adapter);
			if (obj != null) 
				return obj;
		}

		// Nothing
		return null;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean autoPopulated() {
		return autoPopulated;
	}

}
