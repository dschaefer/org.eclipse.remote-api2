package org.eclipse.remote.internal.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.remote.core.IRemoteConnectionChangeListener;
import org.eclipse.remote.core.api2.IConnection;
import org.eclipse.remote.core.api2.IConnectionsManager;
import org.eclipse.remote.core.api2.IConnectionsProvider;
import org.eclipse.remote.core.api2.IConnectionsProviderDescriptor;
import org.eclipse.remote.core.api2.IConnectionsService;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.eclipse.remote.internal.core.services.local.LocalServices;

public class ConnectionsService implements IConnectionsService {
	private Map<String, IConnectionsProviderDescriptor> providerDescs;
	private Set<IConnectionsProvider> loadedProviders = new HashSet<>();

	private void init() {
		// Load up the provider descriptors
		if (providerDescs == null) {
			providerDescs = new HashMap<>();
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry.getExtensionPoint(RemoteCorePlugin.getUniqueIdentifier(),
					RemoteServicesImpl.REMOTE_SERVICES_EXTENSION_POINT_ID);
			for (IExtension ext : extensionPoint.getExtensions()) {
				for (IConfigurationElement element : ext.getConfigurationElements()) {
					IConnectionsProviderDescriptor desc =  new ConnectionsProviderDescriptor(this, element);
					providerDescs.put(desc.getId(), desc);
				}
			}
		}

		// Load up the connections
		IPath stateLoc = RemoteCorePlugin.getDefault().getStateLocation();
		for (IConnectionsProviderDescriptor providerDesc : providerDescs.values()) {
			IPath providerLoc = stateLoc.append(providerDesc.getId());
			File providerDir = providerLoc.toFile();
			if (providerDir.exists()) {
				IConnectionsManager manager = (IConnectionsManager) providerDesc.getAdapter(IConnectionsManager.class);
				if (manager != null) {
					for (File file : providerDir.listFiles()) {
						Properties props = new Properties();
						try {
							props.load(new FileReader(file));
							manager.loadConnection(file.getName(), props);
						} catch (IOException e) {
							RemoteCorePlugin.log(e);
						} catch (RemoteConnectionException e) {
							RemoteCorePlugin.log(e.getStatus());
						}
					}
				}
			}
		}

		// Force load the local provider
		IConnectionsProviderDescriptor localDesc = providerDescs.get(LocalServices.LocalServicesId);
		if (localDesc != null)
			localDesc.getAdapter(IConnectionsProvider.class);
	}

	void providerLoaded(IConnectionsProvider provider) {
		loadedProviders.add(provider);
	}

	@Override
	public void saveConnection(IConnection connection, Properties connectionProperties) throws IOException {
		String providerId = connection.getProviderId();
		String name = connection.getName();

		IPath stateLoc = RemoteCorePlugin.getDefault().getStateLocation();
		IPath providerLoc = stateLoc.append(providerId);
		File providerDir = providerLoc.toFile();
		providerDir.mkdirs();
		File connectionFile = new File(providerDir, name);
		connectionProperties.store(new FileOutputStream(connectionFile), name);
	}

	@Override
	public Collection<IConnectionsProviderDescriptor> getConnectionsProviders() {
		init();
		return providerDescs.values();
	}

	@Override
	public IConnectionsProviderDescriptor getConnectionsProvider(String id) {
		init();
		return providerDescs.get(id);
	}

	@Override
	public Collection<IConnection> getConnections() {
		init();
		List<IConnection> connections = new ArrayList<>();
		for (IConnectionsProvider provider : loadedProviders) {
			connections.addAll(provider.getConnections());
		}
		return connections;
	}

	@Override
	public IConnection getConnection(String providerId, String name) {
		init();
		IConnectionsProviderDescriptor desc = getConnectionsProvider(providerId);
		if (desc == null)
			return null;

		IConnectionsProvider provider = (IConnectionsProvider) desc.getAdapter(IConnectionsProvider.class);
		if (provider == null)
			return null;

		return provider.getConnection(name);
	}

	@Override
	public void addListener(IRemoteConnectionChangeListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeListener(IRemoteConnectionChangeListener listener) {
		// TODO Auto-generated method stub

	}
}
