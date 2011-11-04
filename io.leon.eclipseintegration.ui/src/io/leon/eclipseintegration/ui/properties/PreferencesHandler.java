package io.leon.eclipseintegration.ui.properties;

import io.leon.eclipseintegration.ui.Activator;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.prefs.BackingStoreException;

public class PreferencesHandler {

	private IEclipsePreferences projectPreferences;
	private LeonPreferences leonPreferences;

	public static String CONFIGURATION_FILE = "configurationFile";

	public PreferencesHandler(IProject project) {
		projectPreferences = new ProjectScope(project)
				.getNode(Activator.PLUGIN_ID);

		leonPreferences = new LeonPreferences();
		leonPreferences.setWebConfigurationFile(projectPreferences.get(CONFIGURATION_FILE, File.separator + "config.js"));
	}

	public LeonPreferences getPreferences() {
		return leonPreferences;
	}

	public void save() {
		projectPreferences.put(CONFIGURATION_FILE, leonPreferences.getConfigurationFile());

		try {
			projectPreferences.flush();
		} catch (BackingStoreException e) {
			Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
					"Error saving preferences", e);
			Activator.getDefault().getLog().log(status);
			MessageDialog.openError(Display.getDefault().getActiveShell(),
					"Error creating Leon project", e.getMessage());
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2009 Weigle Wilczek GmbH, Martinstr. 42-44, 73728 Esslingen,
 * Germany. All rights reserved.
 * 
 * WeigleWilczek has intellectual property rights relating to technology
 * embodied in this artefact. This artefact and the product to which it pertains
 * are distributed under licenses restricting their use, copying, distribution,
 * and decompilation. No part of the product or of this document may be
 * reproduced in any form by any means without prior written authorization of
 * WeigleWilczek and its licensors, if any.
 * 
 * DOCUMENTATION IS PROVIDED "AS IS" AND ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESEN- TATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * DISCLAIMED, EXCEPT TO THE EXTENT THAT SUCH DISCLAIMERS ARE HELD TO BE LEGALLY
 * INVALID.
 ******************************************************************************/
