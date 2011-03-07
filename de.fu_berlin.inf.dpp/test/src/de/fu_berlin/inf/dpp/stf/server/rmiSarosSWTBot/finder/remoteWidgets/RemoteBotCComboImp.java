package de.fu_berlin.inf.dpp.stf.server.rmiSarosSWTBot.finder.remoteWidgets;

import java.rmi.RemoteException;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotCCombo;

public class RemoteBotCComboImp extends AbstractRmoteWidget implements
    RemoteBotCCombo {

    private static transient RemoteBotCComboImp self;

    private SWTBotCCombo widget;

    /**
     * {@link RemoteBotCComboImp} is a singleton, but inheritance is possible.
     */
    public static RemoteBotCComboImp getInstance() {
        if (self != null)
            return self;
        self = new RemoteBotCComboImp();
        return self;
    }

    public RemoteBotCCombo setWidget(SWTBotCCombo ccomb) {
        this.widget = ccomb;
        return this;
    }

    /**************************************************************
     * 
     * exported functions
     * 
     **************************************************************/

    /**********************************************
     * 
     * finders
     * 
     **********************************************/
    public RemoteBotMenu contextMenu(String text) throws RemoteException {
        return stfBotMenu.setWidget(widget.contextMenu(text));
    }

    /**********************************************
     * 
     * actions
     * 
     **********************************************/

    public void setSelection(int indexOfSelection) throws RemoteException {
        widget.setSelection(indexOfSelection);
    }

    public String selection() throws RemoteException {
        return widget.selection();
    }

    public int selectionIndex() throws RemoteException {
        return widget.selectionIndex();
    }

    public void setSelection(String text) throws RemoteException {
        widget.setSelection(text);
    }

    public void setText(String text) throws RemoteException {
        widget.setText(text);
    }

    public void setFocus() throws RemoteException {
        widget.setFocus();
    }

    /**********************************************
     * 
     * states
     * 
     **********************************************/
    public boolean isEnabled() throws RemoteException {
        return widget.isEnabled();
    }

    public boolean isVisible() throws RemoteException {
        return widget.isVisible();
    }

    public boolean isActive() throws RemoteException {
        return widget.isActive();
    }

    public String getText() throws RemoteException {
        return widget.getText();
    }

    public String getToolTipText() throws RemoteException {
        return widget.getText();
    }

    public int itemCount() throws RemoteException {
        return widget.itemCount();
    }

    public String[] items() throws RemoteException {
        return widget.items();
    }

    public int textLimit() throws RemoteException {
        return widget.textLimit();
    }

}