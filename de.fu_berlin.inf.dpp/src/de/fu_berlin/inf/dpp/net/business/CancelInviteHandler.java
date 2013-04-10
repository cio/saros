/**
 * 
 */
package de.fu_berlin.inf.dpp.net.business;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import de.fu_berlin.inf.dpp.annotations.Component;
import de.fu_berlin.inf.dpp.invitation.InvitationProcess;
import de.fu_berlin.inf.dpp.net.IReceiver;
import de.fu_berlin.inf.dpp.net.JID;
import de.fu_berlin.inf.dpp.net.internal.extensions.CancelInviteExtension;
import de.fu_berlin.inf.dpp.observables.InvitationProcessObservable;
import de.fu_berlin.inf.dpp.util.Utils;

@Component(module = "net")
public class CancelInviteHandler {

    private static final Logger log = Logger
        .getLogger(CancelInviteHandler.class.getName());

    private InvitationProcessObservable invitationProcesses;

    private PacketListener cancelInvitationExtensionListener = new PacketListener() {

        @Override
        public void processPacket(Packet packet) {
            CancelInviteExtension extension = CancelInviteExtension.PROVIDER
                .getPayload(packet);

            invitationCanceled(new JID(packet.getFrom()),
                extension.getInvitationID(), extension.getErrorMessage());
        }
    };

    public CancelInviteHandler(IReceiver receiver,
        InvitationProcessObservable invitationProcessObservable) {

        this.invitationProcesses = invitationProcessObservable;

        receiver.addPacketListener(cancelInvitationExtensionListener,
            CancelInviteExtension.PROVIDER.getPacketFilter());
    }

    public void invitationCanceled(JID sender, String invitationID,
        String errorMsg) {

        InvitationProcess invitationProcess = invitationProcesses
            .getInvitationProcess(sender, invitationID);

        if (invitationProcess == null) {
            log.warn("Inv[unkown user]: Received invitation cancel message for unknown invitation process. Ignoring...");
            return;
        }

        log.debug("Inv" + Utils.prefix(sender)
            + ": Received invitation cancel message");

        invitationProcess.remoteCancel(errorMsg);
    }
}