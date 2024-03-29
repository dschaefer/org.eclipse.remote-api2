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

import org.eclipse.remote.core.api2.IConnectionsService;
import org.eclipse.remote.internal.ui.RemoteUIPlugin;
import org.eclipse.ui.navigator.CommonNavigator;

public class ConnectionsView extends CommonNavigator {

	@Override
	protected Object getInitialInput() {
		return RemoteUIPlugin.getService(IConnectionsService.class);
	}
	
}
