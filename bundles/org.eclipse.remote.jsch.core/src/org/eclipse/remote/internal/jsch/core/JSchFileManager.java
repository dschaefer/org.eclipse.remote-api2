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

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.remote.core.IRemoteFileManager;

public class JSchFileManager implements IRemoteFileManager {
	private final JSchConnection fConnection;

	public JSchFileManager(JSchConnection connection) {
		fConnection = connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.remote.core.IRemoteFileManager#getDirectorySeparator()
	 */
	/**
	 * @since 4.0
	 */
	@Override
	public String getDirectorySeparator() {
		return "/"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.remote.core.IRemoteFileManager#getResource(java.lang.
	 * String)
	 */
	@Override
	public IFileStore getResource(String pathStr) {
		IPath path = new Path(pathStr);
		if (!path.isAbsolute()) {
			path = new Path(fConnection.getWorkingDirectory()).append(path);
		}
		return JschFileStore.getInstance(JSchFileSystem.getURIFor(fConnection.getName(), path.toString()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.remote.core.IRemoteFileManager#toPath(java.net.URI)
	 */
	@Override
	public String toPath(URI uri) {
		return uri.getPath();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.remote.core.IRemoteFileManager#toURI(org.eclipse.core
	 * .runtime.IPath)
	 */
	@Override
	public URI toURI(IPath path) {
		try {
			return JSchFileSystem.getURIFor(fConnection.getName(), path.toString());
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.remote.core.IRemoteFileManager#toURI(java.lang.String)
	 */
	@Override
	public URI toURI(String path) {
		return toURI(new Path(path));
	}
}
