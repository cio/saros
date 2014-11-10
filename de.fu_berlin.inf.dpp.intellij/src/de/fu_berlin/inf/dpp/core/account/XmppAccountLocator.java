package de.fu_berlin.inf.dpp.core.account;

import de.fu_berlin.inf.dpp.account.XMPPAccount;

import java.util.List;

public class XmppAccountLocator {
    /**
     * Searches for user in account store
     */
    public static XMPPAccount locateAccount(String user, List<XMPPAccount> allAccounts) {
        int index = user.indexOf('@');
        String server = null;
        if (index > -1) {
            String[] pair = user.split("@");
            user = pair[0];
            server = pair[1];
        }

        for (XMPPAccount account : allAccounts) {
            if (server == null) {
                if (user.equalsIgnoreCase(account.getUsername())) {
                    return account;
                }
            } else {
                if (server.equalsIgnoreCase(account.getServer()) && user
                    .equalsIgnoreCase(account.getUsername())) {
                    return account;
                }
            }

            if (user.startsWith(account.getUsername())) {
                return account;
            }
        }

        return null;
    }
}
