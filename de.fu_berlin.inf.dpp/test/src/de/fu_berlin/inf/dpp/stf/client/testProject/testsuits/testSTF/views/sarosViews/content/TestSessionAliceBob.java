package de.fu_berlin.inf.dpp.stf.client.testProject.testsuits.testSTF.views.sarosViews.content;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.fu_berlin.inf.dpp.stf.client.testProject.testsuits.STFTest;

public class TestSessionAliceBob extends STFTest {

    @BeforeClass
    public static void runBeforeClass() throws RemoteException,
        InterruptedException {
        initTesters(TypeOfTester.ALICE, TypeOfTester.BOB);
        setUpWorkbench();
        setUpSaros();
        setUpSessionWithAJavaProjectAndAClass(alice, bob);
    }

    @Before
    public void runBeforeEveryTest() throws RemoteException {
        reBuildSession(alice, bob);
    }

    @After
    public void runAfterEveryTest() throws RemoteException {
        resetWriteAccess(alice, bob);
        resetFollowModeSequentially(alice, bob);
    }

    @Test
    public void testSetFocusOnsarosView() throws RemoteException {
        log.trace("alice set focus on session view.");
        alice.bot().view(VIEW_SAROS).show();
        assertTrue(alice.bot().view(VIEW_SAROS).isActive());
        log.trace("alice close session view.");

        alice.bot().view(VIEW_SAROS).close();
        assertFalse(alice.bot().isViewOpen(VIEW_SAROS));
        log.trace("alice open session view again");
        alice.bot().openViewById(VIEW_SAROS_ID);
        assertTrue(alice.bot().isViewOpen(VIEW_SAROS));
        log.trace("alice focus on saros buddies view.");
    }

    @Test
    public void testRestrictToReadOnlyAccess() throws RemoteException {
        assertTrue(alice.superBot().views().sarosView()
            .selectParticipant(bob.getJID()).hasWriteAccess());
        assertFalse(alice.superBot().views().sarosView()
            .selectParticipant(bob.getJID()).hasReadOnlyAccess());
        alice.superBot().views().sarosView().selectParticipant(bob.getJID())
            .restrictToReadOnlyAccess();
        alice.superBot().views().sarosView().selectParticipant(bob.getJID())
            .waitUntilHasReadOnlyAccess();
        assertFalse(alice.superBot().views().sarosView()
            .selectParticipant(bob.getJID()).hasWriteAccess());
        assertTrue(alice.superBot().views().sarosView()
            .selectParticipant(bob.getJID()).hasReadOnlyAccess());
    }

    @Test
    public void testGrantWriteAccess() throws RemoteException {
        assertTrue(alice.superBot().views().sarosView()
            .selectParticipant(bob.getJID()).hasWriteAccess());
        alice.superBot().views().sarosView().selectParticipant(bob.getJID())
            .restrictToReadOnlyAccess();
        alice.superBot().views().sarosView().selectParticipant(bob.getJID())
            .waitUntilHasReadOnlyAccess();
        assertFalse(alice.superBot().views().sarosView()
            .selectParticipant(bob.getJID()).hasWriteAccess());
        alice.superBot().views().sarosView().selectParticipant(bob.getJID())
            .grantWriteAccess();
        alice.superBot().views().sarosView().selectParticipant(bob.getJID())
            .waitUntilHasWriteAccess();
        assertFalse(alice.superBot().views().sarosView()
            .selectParticipant(bob.getJID()).hasReadOnlyAccess());
    }

    @Test
    public void testFollowMode() throws RemoteException {
        assertFalse(alice.superBot().views().sarosView()
            .selectParticipant(bob.getJID()).isFollowing());
        assertFalse(bob.superBot().views().sarosView()
            .selectParticipant(alice.getJID()).isFollowing());

        bob.superBot().views().sarosView().selectParticipant(alice.getJID())
            .followParticipant();
        assertTrue(bob.superBot().views().sarosView()
            .selectParticipant(alice.getJID()).isFollowing());

        alice.superBot().views().packageExplorerView()
            .selectClass(PROJECT1, PKG1, CLS1).open();

        bob.bot().waitUntilEditorOpen(CLS1_SUFFIX);
        assertTrue(bob.bot().isEditorOpen(CLS1_SUFFIX));

        alice.superBot().views().sarosView().selectParticipant(bob.getJID())
            .followParticipant();
        assertTrue(alice.superBot().views().sarosView()
            .selectParticipant(bob.getJID()).isFollowing());

        bob.bot().editor(CLS1 + SUFFIX_JAVA).closeWithSave();
        alice.bot().waitUntilEditorClosed(CLS1_SUFFIX);
        assertFalse(alice.bot().isEditorOpen(CLS1_SUFFIX));
    }

    @Test
    public void testStopFollowing() throws RemoteException {
        assertFalse(alice.superBot().views().sarosView()
            .selectParticipant(bob.getJID()).isFollowing());
        assertFalse(bob.superBot().views().sarosView()
            .selectParticipant(alice.getJID()).isFollowing());

        bob.superBot().views().sarosView().selectParticipant(alice.getJID())
            .followParticipant();
        assertTrue(bob.superBot().views().sarosView()
            .selectParticipant(alice.getJID()).isFollowing());

        bob.superBot().views().sarosView().selectParticipant(alice.getJID())
            .stopFollowing();
        assertFalse(bob.superBot().views().sarosView()
            .selectParticipant(alice.getJID()).isFollowing());

    }

    /**
     * FIXME if this test would be performed more than one time on the same
     * saros-instance, you may get the TimeoutException.
     * 
     * under mac_os it work well, when the view window isn't too small.
     * 
     * 
     * @throws RemoteException
     */
    @Test
    public void jumpToSelectedBuddy() throws RemoteException {
        alice.superBot().views().packageExplorerView().tree().newC()
            .cls(PROJECT1, PKG1, CLS2);
        alice.bot().waitUntilEditorOpen(CLS2_SUFFIX);
        assertTrue(alice.bot().isEditorOpen(CLS2_SUFFIX));
        assertFalse(bob.bot().isEditorOpen(CLS2_SUFFIX));
        bob.bot().captureScreenshot(
            bob.bot().getPathToScreenShot() + "/vor_jump_to_position.png");
        bob.superBot().views().sarosView().selectParticipant(alice.getJID())
            .jumpToPositionOfSelectedBuddy();
        bob.bot().captureScreenshot(
            bob.bot().getPathToScreenShot() + "/after_jump_to_position.png");
        assertTrue(bob.bot().editor(CLS2_SUFFIX).isActive());

        alice.superBot().views().packageExplorerView()
            .selectClass(PROJECT1, PKG1, CLS1).open();
        alice.bot().editor(CLS1_SUFFIX).show();
        assertTrue(alice.bot().editor(CLS1_SUFFIX).isActive());
        assertFalse(bob.bot().isEditorOpen(CLS1_SUFFIX));
        bob.bot().sleep(500);
        bob.superBot().views().sarosView().selectParticipant(alice.getJID())
            .jumpToPositionOfSelectedBuddy();
        assertTrue(bob.bot().editor(CLS1_SUFFIX).isActive());
    }

    @Test
    @Ignore
    // FIXME dialog with error message "Xuggler not installed"
    public void sharedYourScreenWithSelectedUserGUI() throws RemoteException {
        // alice.mainMenu.setupSettingForScreensharing(1, 0, -1, -1);
        shareYourScreen(alice, bob);
        bob.bot().view(VIEW_REMOTE_SCREEN).waitUntilIsActive();
        assertTrue(bob.bot().view(VIEW_REMOTE_SCREEN).isActive());

    }

    @Test
    public void inconsistencyDetected() throws RemoteException {
        alice.superBot().views().packageExplorerView()
            .selectClass(PROJECT1, PKG1, CLS1).open();
        String editorTextOfAlice = alice.bot().editor(CLS1_SUFFIX).getText();
        alice.superBot().views().sarosView().selectParticipant(bob.getJID())
            .restrictToReadOnlyAccess();
        bob.superBot().views().packageExplorerView()
            .selectClass(PROJECT1, PKG1, CLS1).open();
        bob.bot().editor(CLS1_SUFFIX).setTextWithoutSave(CP1);
        String editorTextOfBob = bob.bot().editor(CLS1_SUFFIX).getText();
        assertFalse(editorTextOfAlice.equals(editorTextOfBob));
        bob.superBot().views().sarosView().waitUntilIsInconsistencyDetected();
        bob.superBot().views().sarosView().inconsistencyDetected();
        editorTextOfAlice = alice.bot().editor(CLS1_SUFFIX).getText();
        editorTextOfBob = bob.bot().editor(CLS1_SUFFIX).getText();
        assertTrue(editorTextOfAlice.equals(editorTextOfBob));
    }

    /**
     * alice(host) first leave the session then bob confirm the windonws
     * "Closing the Session".
     * 
     * @throws RemoteException
     * @throws InterruptedException
     */
    @Test
    public void leaveSessionProcessDonebyAllUsersWithHostFirstLeave()
        throws RemoteException, InterruptedException {
        assertTrue(alice.superBot().views().sarosView().isInSession());
        assertTrue(bob.superBot().views().sarosView().isInSession());
        leaveSessionHostFirst(alice);
        assertFalse(alice.superBot().views().sarosView().isInSession());
        assertFalse(bob.superBot().views().sarosView().isInSession());
    }

    /**
     * peer(bob) first leave the session then host(alice) leave.
     * 
     * @throws RemoteException
     * @throws InterruptedException
     */
    @Test
    public void leaveSessionProcessDonebyAllUsersWithPeersFirstLeave()
        throws RemoteException, InterruptedException {
        assertTrue(alice.superBot().views().sarosView().isInSession());
        assertTrue(bob.superBot().views().sarosView().isInSession());
        leaveSessionPeersFirst();
        assertFalse(alice.superBot().views().sarosView().isInSession());
        assertFalse(bob.superBot().views().sarosView().isInSession());

    }

    @Test
    public void testIsInSession() throws RemoteException, InterruptedException {
        assertTrue(alice.superBot().views().sarosView().isInSession());
        assertTrue(bob.superBot().views().sarosView().isInSession());
        leaveSessionHostFirst(alice);
        assertFalse(alice.superBot().views().sarosView().isInSession());
        assertFalse(bob.superBot().views().sarosView().isInSession());
    }

    @Test
    public void addProjects() throws RemoteException {
        alice.superBot().views().packageExplorerView().tree().newC()
            .javaProject(PROJECT2);
        alice.superBot().views().sarosView().selectSession()
            .addProjects(PROJECT2);
        bob.superBot().confirmShellAddProjects(PROJECT2,
            TypeOfCreateProject.NEW_PROJECT);

    }
}
