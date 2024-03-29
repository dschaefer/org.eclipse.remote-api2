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
package org.eclipse.remote.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.remote.core.IRemoteServices;
import org.eclipse.remote.core.RemoteServices;
import org.eclipse.remote.internal.ui.RemoteUIPlugin;
import org.eclipse.remote.internal.ui.RemoteUIServicesDescriptor;
import org.eclipse.remote.internal.ui.messages.Messages;
import org.eclipse.ui.PlatformUI;

/**
 * Main entry point for accessing remote UI services.
 * 
 * @since 7.0
 */
public class RemoteUIServices {
	private static final String EXTENSION_POINT_ID = "remoteUIServices"; //$NON-NLS-1$

	private static Map<String, RemoteUIServicesDescriptor> fRemoteUIServices = null;
	private static Map<String, IRemoteServices> fRemoteServices = new HashMap<String, IRemoteServices>();

	/**
	 * Look up a remote service provider and ensure it is initialized. The method will use the supplied container's progress
	 * service, or, if null, the platform progress service, in order to allow the initialization to be canceled.
	 * 
	 * @param id
	 *            id of service to locate
	 * @param context
	 *            context with progress service, or null to use the platform progress service
	 * @return remote service or null if the service can't be located or the progress monitor was canceled
	 * @since 5.0
	 */
	public static IRemoteServices getRemoteServices(final String id, IRunnableContext context) {
		IRemoteServices service = fRemoteServices.get(id);
		if (service == null) {
			final IRemoteServices[] remoteService = new IRemoteServices[1];
			IRunnableWithProgress runnable = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask(Messages.RemoteUIServices_Configuring_remote_services, 10);
					remoteService[0] = RemoteServices.getRemoteServices(id, monitor);
				}
			};
			try {
				if (context != null) {
					context.run(true, false, runnable);
				} else {
					PlatformUI.getWorkbench().getProgressService().busyCursorWhile(runnable);
				}
			} catch (InvocationTargetException e) {
				// Ignored
			} catch (InterruptedException e) {
				// cancelled
			}
			service = remoteService[0];
			if (service != null) {
				fRemoteServices.put(id, service);
			}
		}
		return service;
	}

	/**
	 * Helper method to find UI services that correspond to a particular remote services implementation
	 * 
	 * @param services
	 * @return remote UI services
	 */
	public static IRemoteUIServices getRemoteUIServices(IRemoteServices services) {
		if (fRemoteUIServices == null) {
			fRemoteUIServices = retrieveRemoteUIServices();
		}

		/*
		 * Find the UI services corresponding to services.
		 */
		RemoteUIServicesDescriptor descriptor = fRemoteUIServices.get(services.getId());
		if (descriptor != null) {
			return descriptor.getUIServices(services);
		}
		return null;
	}

	/**
	 * Find and load all remoteUIServices plugins.
	 */
	private static Map<String, RemoteUIServicesDescriptor> retrieveRemoteUIServices() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint(RemoteUIPlugin.getUniqueIdentifier(), EXTENSION_POINT_ID);
		final IExtension[] extensions = extensionPoint.getExtensions();

		Map<String, RemoteUIServicesDescriptor> services = new HashMap<String, RemoteUIServicesDescriptor>(5);

		for (IExtension ext : extensions) {
			final IConfigurationElement[] elements = ext.getConfigurationElements();

			for (IConfigurationElement ce : elements) {
				RemoteUIServicesDescriptor descriptor = new RemoteUIServicesDescriptor(ce);
				services.put(descriptor.getId(), descriptor);
			}
		}

		return services;
	}
}
