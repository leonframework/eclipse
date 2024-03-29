package io.leon.eclipseintegration.ui;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "io.leon.eclipseintegration.ui"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	public void initializeImageRegistry(ImageRegistry imageRegistry) {
		try
		{
		URL url = FileLocator.toFileURL(new URL(getBundle().getEntry("/")
				+ "icons/proposal16.gif"));
		imageRegistry.put("leonProposalIcon", ImageDescriptor.createFromURL(url));
		}
		catch (IOException e) {
			Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
					"Error reading leon icons", e);
			getDefault().getLog().log(status);
		}
	}
	
	public static Image getImage(String key)
	{
		return getDefault().getImageRegistry().get(key);
	}
}
