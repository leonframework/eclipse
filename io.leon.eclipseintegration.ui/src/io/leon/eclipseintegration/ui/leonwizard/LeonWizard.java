package io.leon.eclipseintegration.ui.leonwizard;

import io.leon.eclipseintegration.ui.Activator;
import io.leon.eclipseintegration.ui.natures.LeonNature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
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
			BufferedReader bis = null;
			FileReader fis = null;
			BufferedWriter bos = null;
			FileWriter fos = null;

			String errorMessage = "Error creating web folder";

			try {
				File projectFolder = project.getLocation().toFile();

				File webFolder = new File(projectFolder.getAbsolutePath()
						+ File.separator + "www");
				File configJs = new File(projectFolder.getAbsolutePath()
						+ File.separator + "config.js");
				File indexHtml = new File(webFolder.getAbsolutePath()
						+ File.separator + "index.html");

				if (!webFolder.exists()) {
					webFolder.mkdir();
				}

				if (webFolder.exists() && !indexHtml.exists()) {
					indexHtml.createNewFile();
				}

				if (!configJs.exists()) {
					errorMessage = "Error creating leon config file";

					configJs.createNewFile();

					errorMessage = "Error reading leon config file from plugin";

					URL configResource = getClass().getResource("config.js");
					URL configFile = FileLocator.toFileURL(configResource);

					bis = new BufferedReader(fis = new FileReader(
							configFile.getFile()));
//					long length = configFile.getFile().length();
					
					StringBuffer buf  = new StringBuffer();
		            String zeile      = new String();

		            while((zeile = bis.readLine()) != null)
		            {
		                buf.append(zeile);
		                buf.append("\n");
		            }

					errorMessage = "Error writing leon config file content";

					bos = new BufferedWriter(fos = new FileWriter(
							configJs));
					bos.write(buf.toString());
					bos.flush();
				}
			} catch (IOException e) {
				Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
						errorMessage, e);
				Activator.getDefault().getLog().log(status);
				MessageDialog.openError(Display.getDefault().getActiveShell(),
						"Error creating Leon project", errorMessage);
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (IOException e) {
					Status status = new Status(Status.ERROR,
							Activator.PLUGIN_ID, "Error closing file stream", e);
					Activator.getDefault().getLog().log(status);
				}
				try {
					if (bis != null) {
						bis.close();
					}
				} catch (IOException e) {
					Status status = new Status(Status.ERROR,
							Activator.PLUGIN_ID, "Error closing file stream", e);
					Activator.getDefault().getLog().log(status);
				}
				try {
					if (fos != null) {
						fos.close();
					}
				} catch (IOException e) {
					Status status = new Status(Status.ERROR,
							Activator.PLUGIN_ID, "Error closing file stream", e);
					Activator.getDefault().getLog().log(status);
				}
				try {
					if (bos != null) {
						bos.close();
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
