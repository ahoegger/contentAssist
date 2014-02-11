package ch.ahoegger.contentassist;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.ahoegger.contentassist.internal.log.SdkLogManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "ch.ahoegger.contentassist"; //$NON-NLS-1$

  // The shared instance
  private static Activator plugin;

  private static SdkLogManager logManager;

  /**
   * The constructor
   */
  public Activator() {
  }

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
    logManager = new SdkLogManager(this);
    plugin = this;
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }

  public static Image getImage(String relativePath) {
    Image image = getDefault().getImageRegistry().get(relativePath);
    if (image == null) {
      ImageDescriptor desc = imageDescriptorFromPlugin(PLUGIN_ID, relativePath);
      if (desc != null) {
        getDefault().getImageRegistry().put(relativePath, desc.createImage());
      }
    }
    return getDefault().getImageRegistry().get(relativePath);
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static Activator getDefault() {
    return plugin;
  }

  public static void logInfo(Throwable t) {
    logManager.logInfo(t);
  }

  public static void logInfo(String message) {
    logManager.logInfo(message);
  }

  public static void logInfo(String message, Throwable t) {
    logManager.logInfo(message, t);
  }

  public static void logWarning(String message) {
    logManager.logWarning(message);
  }

  public static void logWarning(Throwable t) {
    logManager.logWarning(t);
  }

  public static void logWarning(String message, Throwable t) {
    logManager.logWarning(message, t);
  }

  public static void logError(Throwable t) {
    logManager.logError(t);
  }

  public static void logError(String message) {
    logManager.logError(message);
  }

  public static void logError(String message, Throwable t) {
    logManager.logError(message, t);
  }

  /**
   * @see SdkLogManager#log(int, String, Throwable)
   */
  public static void log(int level, Throwable t) {
    logManager.log(level, t);
  }

  /**
   * @see SdkLogManager#log(int, String, Throwable)
   */
  public static void log(int level, String message) {
    logManager.log(level, message);
  }

  /**
   * @see SdkLogManager#log(int, String, Throwable)
   */
  public static void log(int level, String message, Throwable t) {
    logManager.log(level, message, t);
  }

  public static void log(IStatus status) {
    logManager.log(status);
  }

}
