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

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.remote.core.IRemoteFileManager;
import org.eclipse.remote.core.api2.IConnection;
import org.eclipse.remote.core.api2.IConnectionFileService;

public class LocalFileManager implements IRemoteFileManager, IConnectionFileService {
	private final IConnection connection;

	public LocalFileManager(IConnection connection) {
		this.connection = connection;
	}

	@Override
	public IConnection getConnection() {
		return connection;
	}
	
	@Override
	public String getDirectorySeparator() {
		return System.getProperty("file.separator", "/"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public IFileStore getResource(String path) {
		return EFS.getLocalFileSystem().getStore(new Path(path));
	}

	@Override
	public String toPath(URI uri) {
		return URIUtil.toPath(uri).toString();
	}

	@Override
	public URI toURI(IPath path) {
		return URIUtil.toURI(path);
	}

	@Override
	public URI toURI(String path) {
		return URIUtil.toURI(path);
	}
}
