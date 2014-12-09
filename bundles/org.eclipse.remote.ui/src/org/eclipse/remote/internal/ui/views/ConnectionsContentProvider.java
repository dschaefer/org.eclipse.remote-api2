/*******************************************************************************
 * Copyright (c) 2014 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Doug Schaefer (QNX) - Initial API and implementation
 *******************************************************************************/
package org.eclipse.remote.internal.ui.views;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.remote.core.IRemoteConnectionChangeEvent;
import org.eclipse.remote.core.IRemoteConnectionChangeListener;
import org.eclipse.remote.core.api2.IConnectionsService;

public class ConnectionsContentProvider implements ITreeContentProvider, IRemoteConnectionChangeListener {

	private IConnectionsService connectionsService;
	private Viewer viewer;

	public ConnectionsContentProvider() {
	}

	@Override
	public void connectionChanged(IRemoteConnectionChangeEvent event) {
		if (viewer != null) {
			viewer.getControl().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					viewer.refresh();
				}
			});
		}
	}

	@Override
	public void dispose() {
		connectionsService.removeListener(this);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;

		if (connectionsService != null) {
			connectionsService.removeListener(this);
		}

		if (newInput instanceof IConnectionsService) {
			connectionsService = (IConnectionsService) newInput;
			connectionsService.addListener(this);
		} else {
			connectionsService = null;
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return connectionsService.getConnections().toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		// connections don't have children by default
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return false;
	}

}
