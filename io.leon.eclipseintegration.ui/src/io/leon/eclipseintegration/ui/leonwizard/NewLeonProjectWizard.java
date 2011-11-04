package io.leon.eclipseintegration.ui.leonwizard;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class NewLeonProjectWizard extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		LeonWizard wizard = new LeonWizard();
		WizardDialog dialog = new WizardDialog(HandlerUtil.getActiveShell(event), wizard);
		
		wizard.initializeDefaultPageImageDescriptor();
		wizard.init(PlatformUI.getWorkbench(), null);
		
		dialog.open();
		
		return null;
	}

}
