/*******************************************************************************
 * Copyright (c) 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.remote.internal.jsch.ui;

import org.eclipse.remote.core.IRemoteServices;
import org.eclipse.remote.ui.IRemoteUIServices;
import org.eclipse.remote.ui.IRemoteUIServicesFactory;

public class JSchUIServicesFactory implements IRemoteUIServicesFactory {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.remote.ui.IRemoteUIServicesFactory#getServices(org.eclipse.ptp.remote.core.IRemoteServices)
	 */
	public IRemoteUIServices getServices(IRemoteServices services) {
		return JSchUIServices.getInstance(services);
	}
}
