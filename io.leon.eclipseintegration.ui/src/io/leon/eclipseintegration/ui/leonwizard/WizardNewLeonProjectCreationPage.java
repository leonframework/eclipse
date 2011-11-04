package io.leon.eclipseintegration.ui.leonwizard;

import io.leon.eclipseintegration.ui.Activator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class WizardNewLeonProjectCreationPage extends
		WizardNewProjectCreationPage {


	public WizardNewLeonProjectCreationPage() {
		super("wizardNewLeonProjectCreationPage");
		setTitle("Leon Project");
		setDescription("Create a new Leon Project");
		setImageDescriptor(Activator.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, "icons/leon_wizard_banner.png"));
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);

		Composite area = (Composite) getControl();

		// group Web Page support
		// use default JVM
		// add web browser libs
		// use window as default super type

		createWorkingSetGroup(area, ((LeonWizard)getWizard()).getSelection(), new String[] { "org.eclipse.ui.resourceWorkingSetPage" });

		setControl(area);
	}
}
