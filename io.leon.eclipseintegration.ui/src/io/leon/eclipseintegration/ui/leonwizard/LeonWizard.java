package io.leon.eclipseintegration.ui.leonwizard;

import io.leon.eclipseintegration.ui.Activator;
import io.leon.eclipseintegration.ui.natures.LeonNature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.wst.jsdt.core.JavaScriptCore;

public class LeonWizard extends AbstractNewProjectWizard implements
		INewWizard {

	private static final String java_nature = JavaCore.NATURE_ID;
	private static final String java_builder = JavaCore.BUILDER_ID;
	
	private static final String javascript_nature = JavaScriptCore.NATURE_ID;
	private static final String javascript_builder = JavaScriptCore.BUILDER_ID;

	public LeonWizard() {
	}

	@Override
	public void initializeDefaultPageImageDescriptor() {
		ImageDescriptor desc = Activator.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, "icons/leon_wizard_banner.png");
		setDefaultPageImageDescriptor(desc);
	}

	@Override
	public void addPages() {
		mainPage = new WizardNewLeonProjectCreationPage();
		addPage(mainPage);
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
				is = getClass().getResourceAsStream("leonproject.zip");
				new UnZipper().upzip(is,
						new File(projectFolder.getAbsolutePath()));

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

			 addJavaNature(project);
			 addJavaScriptNature(project);

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

	private void addJavaNature(IProject project) {
		try {
			if (!project.hasNature(java_nature)) {
				IProjectDescription description = project.getDescription();
				String[] prevNatures = description.getNatureIds();
				String[] newNatures = new String[prevNatures.length + 1];
				System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
				newNatures[prevNatures.length] = java_nature;
				description.setNatureIds(newNatures);
				project.setDescription(description, null);
			}
			
			if(!project.hasBuildConfig(javascript_builder))
			{
				IProjectDescription description = project.getDescription();
				IBuildConfiguration[] prevBuilders = project.getBuildConfigs();
				String[] newBuilders = new String[prevBuilders.length + 1];
				for (int i = 0; i < prevBuilders.length; i++) {
					 IBuildConfiguration configuration = prevBuilders[i];
					 newBuilders[i] =  configuration.getName();
				}
				newBuilders[prevBuilders.length] = java_builder;
				description.setBuildConfigs(newBuilders);
				project.setDescription(description, null);
			}
			
			
			IJavaProject javaProject = JavaCore.create(project);
			IClasspathEntry[] prevEntries = javaProject.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[prevEntries.length + 1];
			System.arraycopy(prevEntries, 0, newEntries, 0, prevEntries.length);
			IClasspathEntry entry = JavaRuntime.getDefaultJREContainerEntry();
			newEntries[prevEntries.length] = entry;
			javaProject.setRawClasspath(newEntries, null);
		} catch (CoreException e) {
			Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
					"Error adding java nature", e);
			Activator.getDefault().getLog().log(status);
		}
	}

	private void addJavaScriptNature(IProject project) {
		try {
			if (!project.hasNature(javascript_nature)) {
				IProjectDescription description = project.getDescription();
				String[] prevNatures = description.getNatureIds();
				String[] newNatures = new String[prevNatures.length + 1];
				System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
				newNatures[prevNatures.length] = javascript_nature;
				description.setNatureIds(newNatures);
				project.setDescription(description, null);
			}
			
			if(!project.hasBuildConfig(javascript_builder))
			{
				IProjectDescription description = project.getDescription();
				IBuildConfiguration[] prevBuilders = project.getBuildConfigs();
				String[] newBuilders = new String[prevBuilders.length + 1];
				for (int i = 0; i < prevBuilders.length; i++) {
					 IBuildConfiguration configuration = prevBuilders[i];
					 newBuilders[i] =  configuration.getName();
				}
				newBuilders[prevBuilders.length] = javascript_builder;
				description.setBuildConfigs(newBuilders);
				project.setDescription(description, null);
			}
			
			unzipJsdtSettings(project);
		} catch (CoreException e) {
			Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
					"Error adding java script nature", e);
			Activator.getDefault().getLog().log(status);
		}
	}
	
	private void unzipJsdtSettings(IProject project)
	{
		InputStream is = null;
		String errorMessage = "Error writing jsdt settings";

		try {
			File settingsFolder = project.getLocation().append(".settings").toFile();
			if(!settingsFolder.exists())
			{
				settingsFolder.mkdirs();
			}
			
			is = getClass().getResourceAsStream("jsdtsettings.zip");
			new UnZipper().upzip(is,
					new File(settingsFolder.getAbsolutePath()));

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
	}
	
}
