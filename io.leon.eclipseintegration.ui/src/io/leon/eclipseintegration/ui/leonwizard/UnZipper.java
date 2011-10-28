package io.leon.eclipseintegration.ui.leonwizard;

import io.leon.eclipseintegration.ui.Activator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.runtime.Status;

public class UnZipper {
	private final int BUFFER = 2048;

	public void upzip(InputStream is, File destPath) {
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		ZipInputStream zis = null;
		FileOutputStream fos = null;
		try {
			zis = new ZipInputStream(bis = new BufferedInputStream(is));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				int count;
				byte data[] = new byte[BUFFER];
				// write the files to the disk
				if (!entry.isDirectory()) {
					File currentFile = new File(destPath.getAbsolutePath()
							+ File.separator + entry.getName());
					File path = currentFile.getParentFile();
					if (!path.isDirectory()) {
						path.mkdirs();
					}

					fos = new FileOutputStream(currentFile);
					bos = new BufferedOutputStream(fos, BUFFER);
					while ((count = zis.read(data, 0, BUFFER)) != -1) {
						bos.write(data, 0, count);
					}
					bos.flush();
					bos.close();
				}
			}
			zis.close();

		} catch (IOException e) {
			Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
					"Error extracting leon project content", e);
			Activator.getDefault().getLog().log(status);
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
						"Error closing buffered input stream", e);
				Activator.getDefault().getLog().log(status);
			}
			try {
				if (zis != null) {
					zis.close();
				}
			} catch (IOException e) {
				Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
						"Error closing zip input stream", e);
				Activator.getDefault().getLog().log(status);
			}
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
						"Error closing file output stream", e);
				Activator.getDefault().getLog().log(status);
			}
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				Status status = new Status(Status.ERROR, Activator.PLUGIN_ID,
						"Error closing buffered output stream", e);
				Activator.getDefault().getLog().log(status);
			}
		}
	}
}
