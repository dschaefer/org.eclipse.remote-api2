package org.eclipse.remote.core.api2;

import org.eclipse.core.runtime.IAdaptable;


/**
 * This is a descriptor of a provider. The main objective of the descriptor
 * is to avoid loading the provider class until it is needed.
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * 
 * @since 1.1
 */
public interface IConnectionsProviderDescriptor extends IAdaptable {

	/**
	 * The id of the connection provider.
	 * 
	 * @return id
	 */
	String getId();

	/**
	 * A user friendly and localized name of the provider.
	 * 
	 * @return name
	 */
	String getName();

	/**
	 * Are connections of this type auto populated? If so, UI should not
	 * be presented to add or remove connections.
	 * 
	 * @return auto populated
	 */
	boolean autoPopulated();
}
