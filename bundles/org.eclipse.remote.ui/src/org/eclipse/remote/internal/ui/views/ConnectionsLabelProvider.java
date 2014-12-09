package org.eclipse.remote.internal.ui.views;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.remote.core.api2.IConnection;
import org.eclipse.remote.internal.ui.RemoteUIImages;
import org.eclipse.remote.ui.api2.IConnectionsProviderUI;
import org.eclipse.swt.graphics.Image;

public class ConnectionsLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		if (element instanceof IConnection) {
			IConnection connection = (IConnection) element;
			ImageRegistry registry = RemoteUIImages.getImageRegistry();
			String key = connection.getProviderId() + (connection.isOpen() ? ".open" : ".closed");
			Image image = registry.get(key);
			if (image != null)
				return image;

			IConnectionsProviderUI ui = (IConnectionsProviderUI) connection.getAdapter(IConnectionsProviderUI.class);
			if (ui != null) {
				ImageDescriptor imageDesc = ui.getIcon();
				if (imageDesc != null) {
					// TODO Do overlay and store that
					registry.put(key, image);
					image = registry.get(key);
					return image;
				}

				return null;
			} else {
				// TODO Do a default one
			}
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof IConnection) {
			IConnection connection = (IConnection) element;
			// TODO replace status with icon
			return connection.getName() + " (" + (connection.isOpen() ? "open" : "closed") + ")";
		}
		return super.getText(element);
	}

}
