package io.leon.eclipseintegration.ui.leonwizard;

import io.leon.eclipseintegration.ui.Activator;
import io.leon.eclipseintegration.ui.natures.LeonNature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;


public class LeonWizard extends BasicNewProjectResourceWizard implements
		INewWizard {

	@Override
	public void initializeDefaultPageImageDescriptor() {
		ImageDescriptor desc = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/leon_wizard_banner.png");
		setDefaultPageImageDescriptor(desc);
	}

	@Override
	public boolean performFinish() {
		super.performFinish();

		IProject project = getNewProject();

		// create web folder structure and config files
		if (project != null) {
			InputStream is = null;
			String errorMessage = "Error extracting leon project content";

			try {
				File projectFolder = project.getLocation().toFile();
				is = getClass().getResourceAsStream(
						"leonproject.zip");
				new UnZipper().upzip(is, new File(projectFolder.getAbsolutePath()));
				
				errorMessage = "Error closing file stream";
				is.close();
			} catch (IOException e) {
				Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
						errorMessage, e);
				Activator.getDefault().getLog().log(status);
				MessageDialog.openError(Display.getDefault().getActiveShell(),
						"Error creating Leon project", errorMessage);
			} finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					Status status = new Status(Status.ERROR,
							Activator.PLUGIN_ID, "Error closing file stream", e);
					Activator.getDefault().getLog().log(status);
				}
			}

			// add leon project nature
			try {
				IProjectDescription description = project.getDescription();
				String[] natures = description.getNatureIds();
				String[] newNatures = new String[natures.length + 1];
				System.arraycopy(natures, 0, newNatures, 0, natures.length);

				newNatures[natures.length] = LeonNature.NATURE_ID;

				description.setNatureIds(newNatures);
				project.setDescription(description, null);
			} catch (CoreException e) {
				Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
						"Error getting project description", e);
				Activator.getDefault().getLog().log(status);
			}

			// refresh navigator
			try {
				project.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
						"Error refreshing project", e);
				Activator.getDefault().getLog().log(status);
			}
		}

		return true;
	}
}
