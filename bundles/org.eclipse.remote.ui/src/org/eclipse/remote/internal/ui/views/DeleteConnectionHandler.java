package org.eclipse.remote.internal.ui.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.remote.core.api2.IConnection;
import org.eclipse.remote.core.api2.IConnectionsManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class DeleteConnectionHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if (selection != null && selection instanceof IStructuredSelection) {
			// Get the managable connections from the selection
			List<IConnection> connections = new ArrayList<IConnection>();
			@SuppressWarnings("unchecked")
			Iterator<Object> i = ((IStructuredSelection) selection).iterator();
			while (i.hasNext()) {
				Object obj = i.next();
				if (obj instanceof IConnection && ((IConnection) obj).getAdapter(IConnectionsManager.class) != null) {
					connections.add((IConnection) obj);
				}
			}

			// Confirm the delete
			MessageBox confirmDialog = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
			String message = "Delete connection";
			for (IConnection connection : connections) {
				message += " " + connection.getName(); //$NON-NLS-1
			}
			message += "?"; //$NON-NLS-1
			confirmDialog.setMessage(message);

			// Make it so
			if (confirmDialog.open() == SWT.OK) {
				for (IConnection connection : connections) {
					IConnectionsManager manager = (IConnectionsManager) connection.getAdapter(IConnectionsManager.class);
					if (manager != null) {
						manager.removeConnection(connection);
					}
				}
			}
		}
		return Status.OK_STATUS;
	}

}
