/*******************************************************************************
 * Copyright (c) 2008,2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.remote.ui.dialogs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.internal.ui.messages.Messages;
import org.eclipse.remote.ui.widgets.RemoteResourceBrowserWidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Generic file/directory browser for remote resources.
 * 
 * A directory browser (DIRECTORY_BROWSER) only allows selection of directories.
 * A file browser (FILE_BROWSER) allows selection of files and directories, but the ok button is only enabled when a file is
 * selected.
 * A resource browser (FILE_BROWSER|DIRECTORY_BROWSER) allows selection of files and directories. This is the default.
 * 
 * To select multiple resources, set the style to SWT.MULTI.
 * 
 * @author greg
 * 
 */
public class RemoteResourceBrowser extends Dialog implements IRunnableContext {
	public static final String EMPTY_STRING = ""; //$NON-NLS-1$
	public static final int FILE_BROWSER = 0x01;
	public static final int DIRECTORY_BROWSER = 0x02;

	private final static int widthHint = 400;

	private Button fOkButton;
	private RemoteResourceBrowserWidget fResourceBrowserWidget;
	private ProgressMonitorPart fProgressMonitor;

	private int fBrowserType;
	private String fDialogTitle;

	private boolean fShowHiddenCheckbox = true;
	private boolean fShowNewFolderButton = true;
	private boolean fShowConnections = false;
	private boolean fShowLocalSelection = false;
	private String fInitialPath;
	private IRemoteConnection fConnection;
	private int fBrowserStyle = SWT.SINGLE;

	public RemoteResourceBrowser(Shell parent, int style) {
		super(parent);
		setShellStyle(SWT.RESIZE | getShellStyle());
		if (style != SWT.NONE) {
			fBrowserStyle = style;
		}
		setTitle(Messages.RemoteResourceBrowser_resourceTitle);
		setType(FILE_BROWSER | DIRECTORY_BROWSER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createButton(org.eclipse.swt.widgets
	 * .Composite, int, java.lang.String, boolean)
	 */
	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		Button button = super.createButton(parent, id, label, defaultButton);
		if (id == IDialogConstants.OK_ID) {
			fOkButton = button;
		}
		return button;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		if (fDialogTitle == null) {
			if (fBrowserType == DIRECTORY_BROWSER) {
				setTitle(Messages.RemoteResourceBrowser_directoryTitle);
			} else if (fBrowserType == FILE_BROWSER) {
				setTitle(Messages.RemoteResourceBrowser_fileTitle);
			}
		} else {
			setTitle(fDialogTitle);
		}
		if (fInitialPath != null) {
			fResourceBrowserWidget.setInitialPath(fInitialPath);
		}
		if (fConnection != null) {
			fResourceBrowserWidget.setConnection(fConnection);
		}
		updateDialog();
		return contents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite main = (Composite) super.createDialogArea(parent);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = widthHint;
		main.setLayoutData(gd);
		main.setLayout(new GridLayout(1, true));

		int options = 0;
		if (fConnection == null || fShowConnections) {
			options |= RemoteResourceBrowserWidget.SHOW_CONNECTIONS;
		}
		if (fBrowserType == DIRECTORY_BROWSER) {
			options |= RemoteResourceBrowserWidget.DIRECTORY_BROWSER;
		} else if (fBrowserType == FILE_BROWSER) {
			options |= RemoteResourceBrowserWidget.FILE_BROWSER;
		} else {
			options |= RemoteResourceBrowserWidget.FILE_BROWSER | RemoteResourceBrowserWidget.DIRECTORY_BROWSER;
		}
		if (fShowHiddenCheckbox) {
			options |= RemoteResourceBrowserWidget.SHOW_HIDDEN_CHECKBOX;
		}
		if (fShowNewFolderButton) {
			options |= RemoteResourceBrowserWidget.SHOW_NEW_FOLDER_BUTTON;
		}
		if (fShowLocalSelection) {
			options |= RemoteResourceBrowserWidget.SHOW_LOCAL_SELECTION;
		}
		fResourceBrowserWidget = new RemoteResourceBrowserWidget(main, fBrowserStyle, options);
		fResourceBrowserWidget.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateDialog();
			}
		});
		fResourceBrowserWidget.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				getShell().setDefaultButton(null); // allow text widget to receive SWT.DefaultSelection event
			}

			@Override
			public void focusLost(FocusEvent e) {
				getShell().setDefaultButton(fOkButton);
			}
		});
		fResourceBrowserWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		if (fConnection != null) {
			fResourceBrowserWidget.setConnection(fConnection);
		}

		Composite monitorComposite = new Composite(main, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		monitorComposite.setLayout(layout);
		monitorComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fProgressMonitor = new ProgressMonitorPart(monitorComposite, new GridLayout(), true);
		GridData gridData = new GridData(SWT.FILL, SWT.TOP, true, true);
		fProgressMonitor.setLayoutData(gridData);
		monitorComposite.setVisible(false);

		fResourceBrowserWidget.setRunnableContext(this);

		return main;
	}

	/**
	 * Get the connection that was selected
	 * 
	 * @return selected connection
	 */
	public IRemoteConnection getConnection() {
		if (fResourceBrowserWidget != null) {
			return fResourceBrowserWidget.getConnection();
		}
		return null;
	}

	/**
	 * Get the resources that was selected.
	 * 
	 * @return selected resource or null if no resource is selected
	 */
	public IFileStore getResource() {
		if (fResourceBrowserWidget != null) {
			return fResourceBrowserWidget.getResource();
		}
		return null;
	}

	/**
	 * Get the resources that were selected.
	 * 
	 * @return selected resources
	 */
	public List<IFileStore> getResources() {
		if (fResourceBrowserWidget != null) {
			return fResourceBrowserWidget.getResources();
		}
		return new ArrayList<IFileStore>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.operation.IRunnableContext#run(boolean, boolean, org.eclipse.jface.operation.IRunnableWithProgress)
	 */
	@Override
	public void run(boolean fork, boolean cancelable, IRunnableWithProgress runnable) throws InvocationTargetException,
			InterruptedException {
		fProgressMonitor.attachToCancelComponent(null);
		fProgressMonitor.getParent().setVisible(true);
		try {
			ModalContext.run(runnable, fork, fProgressMonitor, getShell().getDisplay());
		} finally {
			fProgressMonitor.getParent().setVisible(false);
			fProgressMonitor.removeFromCancelComponent(null);
		}
	}

	/**
	 * Set the initial connection for the browser.
	 * 
	 * @param connection
	 */
	public void setConnection(IRemoteConnection connection) {
		fConnection = connection;
	}

	/**
	 * Set the initial path to start browsing. This will be set in the browser
	 * text field, and in a future version should expand the browser to this
	 * location if it exists.
	 * 
	 * @param path
	 */
	public void setInitialPath(String path) {
		fInitialPath = path;
	}

	/**
	 * Set the fDialogTitle of the dialog.
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		fDialogTitle = title;
		if (fDialogTitle == null) {
			fDialogTitle = ""; //$NON-NLS-1$
		}
		Shell shell = getShell();
		if ((shell != null) && !shell.isDisposed()) {
			shell.setText(fDialogTitle);
		}
	}

	/**
	 * Set the type of browser. Can be either a file browser (allows selection
	 * of files), a directory browser (allows selection of directories), or
	 * a resource browser (allows selection of files and directories).
	 */
	public void setType(int type) {
		fBrowserType = type;
	}

	/**
	 * Show available connections on browser if possible (default disabled).
	 * 
	 * @param enable
	 */
	public void showConnections(boolean enable) {
		fShowConnections = enable;
	}

	/**
	 * Enable a checkbox to show hidden files (default enabled)
	 * 
	 * @param showHidden
	 */
	public void showHiddenCheckbox(boolean showHidden) {
		fShowHiddenCheckbox = showHidden;
	}

	/**
	 * Enable selection of local files
	 * 
	 * @param showNewFolderButton
	 */
	public void showLocalSelection(boolean showLocalSelection) {
		fShowLocalSelection = showLocalSelection;
	}

	/**
	 * Enable a button to create new folders (default enabled)
	 * 
	 * @param showNewFolderButton
	 */
	public void showNewFolderButton(boolean showNewFolderButton) {
		fShowNewFolderButton = showNewFolderButton;
	}

	private void updateDialog() {
		if (fOkButton != null) {
			IFileStore resource = getResource();
			boolean enabled = getConnection() != null && resource != null;
			if (enabled && fBrowserType == FILE_BROWSER) {
				enabled &= !resource.fetchInfo().isDirectory();
			}
			fOkButton.setEnabled(enabled);
		}
	}
}
