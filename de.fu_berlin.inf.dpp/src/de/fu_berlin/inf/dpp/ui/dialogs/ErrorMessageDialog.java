package de.fu_berlin.inf.dpp.ui.dialogs;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;

import de.fu_berlin.inf.dpp.ui.Messages;
import de.fu_berlin.inf.dpp.ui.util.SWTUtils;

/**
 * Eclipse Dialog to show Exception Messages.
 * 
 * @author rdjemili
 * @author chjacob
 * 
 */
public class ErrorMessageDialog {

    private static final Logger log = Logger.getLogger(ErrorMessageDialog.class
        .getName());

    /**
     * show error message dialog.
     * 
     * @param exception
     */
    public static void showErrorMessage(final Exception exception) {
        SWTUtils.runSafeSWTSync(log, new Runnable() {
            @Override
            public void run() {
                MessageDialog.openError(SWTUtils.getShell(),
                    exception.toString(), exception.getMessage());
            }
        });
    }

    /**
     * Show error message for the given exception with the given window title
     */
    public static void showErrorMessage(final String windowTitle,
        final Exception exception) {
        SWTUtils.runSafeSWTSync(log, new Runnable() {
            @Override
            public void run() {
                MessageDialog.openError(SWTUtils.getShell(), windowTitle,
                    exception.toString());
            }
        });
    }

    /**
     * Opens a modal dialog which displays the given error message to the user.
     * 
     * @param exceptionMessage
     *            The message to show the user or null (in which case
     *            "An unspecified error occurred" is printed, which is not very
     *            desirable)
     */
    public static void showErrorMessage(String exceptionMessage) {

        if ((exceptionMessage == null) || exceptionMessage.trim().length() == 0) {
            exceptionMessage = Messages.ErrorMessageDialog_error_unspecified;
        }
        final String error = exceptionMessage;

        SWTUtils.runSafeSWTSync(log, new Runnable() {
            @Override
            public void run() {
                MessageDialog.openError(SWTUtils.getShell(),
                    Messages.ErrorMessageDialog_error_plugin, error);
            }
        });
    }
}
