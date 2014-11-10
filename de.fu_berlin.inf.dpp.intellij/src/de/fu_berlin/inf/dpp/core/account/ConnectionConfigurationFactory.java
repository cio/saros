package de.fu_berlin.inf.dpp.core.account;

import org.jivesoftware.smack.ConnectionConfiguration;

public class ConnectionConfigurationFactory {
    public static ConnectionConfiguration createConnectionConfiguration(String domain,
                                                                        String server, int port, boolean useTLS, boolean useSASL) {
        //FIXME: Copy from de.fu_berlin.inf.dpp.communication.connection.ConnectionHandler
        //minus ProxyInfo
        ConnectionConfiguration connectionConfiguration = null;

        if (server.length() == 0)
            connectionConfiguration = new ConnectionConfiguration(domain);
        else
            connectionConfiguration = new ConnectionConfiguration(server, port,
                domain);

        connectionConfiguration.setSASLAuthenticationEnabled(useSASL);

        if (!useTLS)
            connectionConfiguration
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        connectionConfiguration.setReconnectionAllowed(false);

        return connectionConfiguration;
    }
}
