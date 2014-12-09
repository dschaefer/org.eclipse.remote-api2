package org.eclipse.remote.core.api2;

/**
 * @since 1.1
 */
public interface IConnectionsProviderFactory {

	IConnectionsProvider getProvider(IConnectionsProviderDescriptor descriptor);

}
