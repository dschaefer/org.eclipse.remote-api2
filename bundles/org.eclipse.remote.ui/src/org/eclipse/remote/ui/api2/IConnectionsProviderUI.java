package org.eclipse.remote.ui.api2;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;

/**
 * UI elements for connections providers.
 * 
 * @since 1.2
 */
public interface IConnectionsProviderUI {

	ImageDescriptor getIcon();

	IWizard getNewConnectionWizard();
	
}
