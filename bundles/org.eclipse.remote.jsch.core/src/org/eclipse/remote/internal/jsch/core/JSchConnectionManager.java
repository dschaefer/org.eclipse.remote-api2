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
package org.eclipse.remote.internal.jsch.core;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.osgi.util.NLS;
import org.eclipse.remote.core.AbstractRemoteConnectionManager;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.IRemoteServices;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.eclipse.remote.internal.jsch.core.messages.Messages;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class JSchConnectionManager extends AbstractRemoteConnectionManager {
	private Map<String, JSchConnection> fConnections;

	/**
	 * @since 4.0
	 */
	public JSchConnectionManager(IRemoteServices services) {
		super(services);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.remote.core.IRemoteConnectionManager#getConnection(java
	 * .lang.String)
	 */
	@Override
	public IRemoteConnection getConnection(String name) {
		loadConnections();
		return fConnections.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.remote.core.IRemoteConnectionManager#getConnection(java
	 * .net.URI)
	 */
	/**
	 * @since 4.0
	 */
	@Override
	public IRemoteConnection getConnection(URI uri) {
		String connName = JSchFileSystem.getConnectionNameFor(uri);
		if (connName != null) {
			return getConnection(connName);
		}
		return null;
	}

	public JSchConnection createConnection(String name) {
		return new JSchConnection(name, getRemoteServices());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.remote.core.IRemoteConnectionManager#getConnections()
	 */
	@Override
	public List<IRemoteConnection> getConnections() {
		loadConnections();
		List<IRemoteConnection> conns = new ArrayList<IRemoteConnection>();
		conns.addAll(fConnections.values());
		return conns;
	}

	private synchronized void loadConnections() {
		if (fConnections == null) {
			fConnections = Collections.synchronizedMap(new HashMap<String, JSchConnection>());
			IEclipsePreferences root = InstanceScope.INSTANCE.getNode(Activator.getUniqueIdentifier());
			Preferences connections = root.node(JSchConnectionAttributes.CONNECTIONS_KEY);
			try {
				for (String name : connections.childrenNames()) {
					JSchConnection connection = new JSchConnection(name, getRemoteServices());
					fConnections.put(name, connection);
				}
			} catch (BackingStoreException e) {
				Activator.log(e.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.remote.core.IRemoteConnectionManager#newConnection(java
	 * .lang.String, java.util.Map)
	 */
	/**
	 * @since 5.0
	 */
	@Override
	public IRemoteConnectionWorkingCopy newConnection(String name) throws RemoteConnectionException {
		if (getConnection(name) != null) {
			throw new RemoteConnectionException(NLS.bind(Messages.JSchConnectionManager_connection_with_name_exists, name));
		}
		return createConnection(name).getWorkingCopy();
	}

	public void add(JSchConnection conn) {
		if (!fConnections.containsKey(conn.getName())) {
			fConnections.put(conn.getName(), conn);
		}
	}

	public void remove(JSchConnection conn) {
		if (fConnections.containsKey(conn.getName())) {
			fConnections.remove(conn.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.remote.core.IRemoteConnectionManager#removeConnection
	 * (org.eclipse.remote.core.IRemoteConnection)
	 */
	@Override
	public void removeConnection(IRemoteConnection conn) throws RemoteConnectionException {
		if (!(conn instanceof JSchConnection)) {
			throw new RemoteConnectionException(Messages.JSchConnectionManager_invalidConnectionType);
		}
		if (conn.isOpen()) {
			throw new RemoteConnectionException(Messages.JSchConnectionManager_cannotRemoveOpenConnection);
		}
		((JSchConnection) conn).getInfo().remove();
		fConnections.remove(conn.getName());
	}
}
