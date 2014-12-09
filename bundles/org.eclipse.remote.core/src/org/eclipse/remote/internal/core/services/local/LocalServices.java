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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.remote.core.AbstractRemoteServices;
import org.eclipse.remote.core.IRemoteConnectionManager;
import org.eclipse.remote.core.IRemoteServicesDescriptor;

public class LocalServices extends AbstractRemoteServices {
	public static final String LocalServicesId = "org.eclipse.remote.LocalServices"; //$NON-NLS-1$

	private final LocalConnectionManager fConnMgr = new LocalConnectionManager(this);

	public LocalServices(IRemoteServicesDescriptor descriptor) {
		super(descriptor);
	}

	@Override
	public int getCapabilities() {
		return 0;
	}

	@Override
	public IRemoteConnectionManager getConnectionManager() {
		return fConnMgr;
	}

	@Override
	public boolean initialize(IProgressMonitor monitor) {
		return true;
	}

}
