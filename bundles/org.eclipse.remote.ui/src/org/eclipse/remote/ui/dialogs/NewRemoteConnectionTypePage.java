package org.eclipse.remote.ui.dialogs;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.remote.core.IRemoteServices;
import org.eclipse.remote.core.IRemoteServicesDescriptor;
import org.eclipse.remote.core.RemoteServices;
import org.eclipse.remote.core.api2.IConnectionsProviderDescriptor;
import org.eclipse.remote.core.api2.IConnectionsService;
import org.eclipse.remote.internal.ui.RemoteUIImages;
import org.eclipse.remote.internal.ui.RemoteUIPlugin;
import org.eclipse.remote.ui.IRemoteUIConnectionWizard;
import org.eclipse.remote.ui.IRemoteUIServices;
import org.eclipse.remote.ui.RemoteUIServices;
import org.eclipse.remote.ui.api2.IConnectionsProviderUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * @since 1.2
 */
public class NewRemoteConnectionTypePage extends WizardPage {

	private Table table;

	public NewRemoteConnectionTypePage() {
		super("NewLaunchTargetTypePage");
		setTitle("Remote Connection Type");
		setDescription("Select type of remote connection to create.");
	}

	@Override
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());

		table = new Table(comp, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(data);

		setPageComplete(false);
		IConnectionsService connectionsService = RemoteUIPlugin.getService(IConnectionsService.class);
		for (IConnectionsProviderDescriptor provider : connectionsService.getConnectionsProviders()) {
			if (!provider.autoPopulated()) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setData(provider);
				item.setText(provider.getName());
				IConnectionsProviderUI ui = (IConnectionsProviderUI) provider.getAdapter(IConnectionsProviderUI.class);
				if (ui != null) {
					ImageRegistry registry = RemoteUIImages.getImageRegistry();
					String key = provider.getId() + ".type";
					Image image = registry.get(key);
					if (image == null) {
						ImageDescriptor imageDesc = ui.getIcon();
						if (imageDesc != null) {
							registry.put(key, image);
							image = registry.get(key);
						}
					}
					if (image == null)
						item.setImage(image);
				}
			}
		}
		if (table.getItemCount() > 0) {
			table.select(0);
			setPageComplete(true);
		}
		
		setControl(comp);
	}

	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete();
	}

	@Override
	public IWizardPage getNextPage() {
		Object data = table.getSelection()[0].getData();
		if (data instanceof IConnectionsProviderDescriptor) {
			IConnectionsProviderDescriptor desc = (IConnectionsProviderDescriptor) data;
			IConnectionsProviderUI ui = (IConnectionsProviderUI) desc.getAdapter(IConnectionsProviderUI.class);
			IWizard nextWizard = null;
			if (ui != null) {
				nextWizard = ui.getNewConnectionWizard();
			} else {
				IRemoteServices services = RemoteServices.getRemoteServices(desc.getId());
				IRemoteUIServices uiServices = RemoteUIServices.getRemoteUIServices(services);
				IRemoteUIConnectionWizard connectionWizard = uiServices.getUIConnectionManager().getConnectionWizard(getShell());
				if (connectionWizard instanceof IWizard) {
					nextWizard = (IWizard)connectionWizard;
				}
			}
			
			if (nextWizard != null) {
				nextWizard.addPages();
				if (nextWizard.getPageCount() > 0) {
					return nextWizard.getPages()[0];
				}
			}
		}
		
		return super.getNextPage();
	}

}
