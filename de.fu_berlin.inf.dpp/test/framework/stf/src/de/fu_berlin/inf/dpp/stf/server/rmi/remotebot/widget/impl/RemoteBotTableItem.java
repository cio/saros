package de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.impl;

import java.rmi.RemoteException;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;

import de.fu_berlin.inf.dpp.stf.server.StfRemoteObject;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.impl.RemoteWorkbenchBot;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotMenu;
import de.fu_berlin.inf.dpp.stf.server.rmi.remotebot.widget.IRemoteBotTableItem;

public final class RemoteBotTableItem extends StfRemoteObject implements
    IRemoteBotTableItem {

    private static final RemoteBotTableItem INSTANCE = new RemoteBotTableItem();

    private SWTBotTableItem widget;

    public static RemoteBotTableItem getInstance() {
        return INSTANCE;
    }

    public IRemoteBotTableItem setWidget(SWTBotTableItem tableItem) {
        this.widget = tableItem;
        return this;
    }

    public IRemoteBotMenu contextMenu(String text) throws RemoteException {
        return RemoteBotMenu.getInstance().setWidget(widget.contextMenu(text));
    }

    public void select() throws RemoteException {
        widget.select();
    }

    public void check() throws RemoteException {
        widget.check();
    }

    public void uncheck() throws RemoteException {
        widget.uncheck();
    }

    public void toggleCheck() throws RemoteException {
        widget.toggleCheck();
    }

    public void click() throws RemoteException {
        widget.click();
    }

    public void clickAndWait() throws RemoteException {
        waitUntilIsEnabled();
        click();
    }

    public void setFocus() throws RemoteException {
        widget.setFocus();
    }

    public boolean existsContextMenu(String contextName) throws RemoteException {

        try {
            widget.contextMenu(contextName);
            return true;
        } catch (WidgetNotFoundException e) {
            return false;
        }
    }

    public boolean isEnabled() throws RemoteException {
        return widget.isEnabled();
    }

    public boolean isVisible() throws RemoteException {
        return widget.isVisible();
    }

    public boolean isActive() throws RemoteException {
        return widget.isActive();
    }

    public boolean isChecked() throws RemoteException {
        return widget.isChecked();
    }

    public boolean isGrayed() throws RemoteException {
        return widget.isGrayed();
    }

    public String getText(int index) throws RemoteException {
        return widget.getText(index);
    }

    public String getText() throws RemoteException {
        return widget.getText();
    }

    public String getToolTipText() throws RemoteException {
        return widget.getText();
    }

    public void waitUntilIsEnabled() throws RemoteException {
        RemoteWorkbenchBot.getInstance().waitUntil(
            Conditions.widgetIsEnabled(widget));
    }
}
