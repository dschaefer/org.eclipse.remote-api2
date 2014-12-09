/*******************************************************************************
 * Copyright (c) 2014 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Doug Schaefer
 *******************************************************************************/
package org.eclipse.remote.ui.dialogs;

import org.eclipse.jface.wizard.Wizard;

/**
 * @since 1.2
 */
public class NewRemoteConnectionWizard extends Wizard {

	private final NewRemoteConnectionTypePage typePage;

	public NewRemoteConnectionWizard() {
		setWindowTitle("Launch Target Type");
		typePage = new NewRemoteConnectionTypePage();
		setForcePreviousAndNextButtons(true);
	}

	@Override
	public void addPages() {
		addPage(typePage);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	@Override
	public boolean canFinish() {
		// Need to move onto the new target wizard
		return false;
	}

}
