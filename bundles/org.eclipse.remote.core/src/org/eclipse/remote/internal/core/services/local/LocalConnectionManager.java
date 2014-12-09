/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.remote.internal.core.services.local;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.remote.core.AbstractRemoteConnectionManager;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.eclipse.remote.internal.core.messages.Messages;

public class LocalConnectionManager extends AbstractRemoteConnectionManager {
	private final LocalConnection fLocalConnection;

	public LocalConnectionManager(LocalServices services) {
		super(services);
		fLocalConnection = new LocalConnection(services);
	}

	public LocalConnection getLocalConnection() {
		return fLocalConnection;
	}

	@Override
	public IRemoteConnection getConnection(String name) {
		if (name.equals(fLocalConnection.getName())) {
			return fLocalConnection;
		}
		return null;
	}

	@Override
	public IRemoteConnection getConnection(URI uri) {
		if (uri.getScheme().equals(EFS.getLocalFileSystem().getScheme())) {
			return fLocalConnection;
		}
		return null;
	}

	@Override
	public List<IRemoteConnection> getConnections() {
		List<IRemoteConnection> list = new ArrayList<IRemoteConnection>();
		list.add(fLocalConnection);
		return list;
	}

	@Override
	public IRemoteConnectionWorkingCopy newConnection(String name) throws RemoteConnectionException {
		throw new RemoteConnectionException(Messages.Unable_to_create_new_local_connections);
	}

	@Override
	public void removeConnection(IRemoteConnection connection) {
		// Nothing
	}
}
