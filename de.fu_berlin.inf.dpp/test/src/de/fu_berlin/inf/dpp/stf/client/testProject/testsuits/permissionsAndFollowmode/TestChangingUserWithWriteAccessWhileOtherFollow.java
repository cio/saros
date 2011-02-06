package de.fu_berlin.inf.dpp.stf.client.testProject.testsuits.permissionsAndFollowmode;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.RemoteException;

import org.eclipse.core.runtime.CoreException;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fu_berlin.inf.dpp.stf.client.testProject.testsuits.STFTest;

public class TestChangingUserWithWriteAccessWhileOtherFollow extends STFTest {

    /**
     * Preconditions:
     * <ol>
     * <li>Alice (Host, Write Access)</li>
     * <li>Bob (Read-Only Access)</li>
     * <li>Carl (Read-Only Access)</li>
     * <li>Dave (Read-Only Access)</li>
     * <li>All read-only users enable followmode</li>
     * </ol>
     * 
     * @throws AccessException
     * @throws RemoteException
     * @throws InterruptedException
     */
    @BeforeClass
    public static void runBeforeClass() throws RemoteException,
        InterruptedException {
        initTesters(TypeOfTester.ALICE, TypeOfTester.BOB, TypeOfTester.CARL,
            TypeOfTester.DAVE);
        setUpWorkbenchs();
        setUpSaros();
        setUpSessionByDefault(alice, bob, carl, dave);
        alice.followedBy(bob, carl, dave);
    }

    /**
     * Steps:
     * <ol>
     * <li>alice grants carl exclusive write access.</li>
     * <li>read-only users are in follow mode.</li>
     * <li>carl opens a file and edit it.</li>
     * <li>read-only users leave follow mode after they saw the opened file.</li>
     * <li>carl continue to edit the opened file, but doesn't save</li>
     * </ol>
     * 
     * Result:
     * <ol>
     * <li></li>
     * <li></li>
     * <li>read-only users saw the opened file and the dirty flag of the file,</li>
     * <li></li>
     * <li></li>
     * <li>Edited file is opened and not saved at every user with read-only
     * access</li>
     * </ol>
     * 
     * TODO: Tt exists still some bugs in saros by granding write access, so you
     * may get exception by perform this test.
     * 
     * @throws CoreException
     * @throws IOException
     * @throws InterruptedException
     * 
     * 
     */

    @Test
    public void testChangingWriteAccessWhileOtherFollow() throws IOException,
        CoreException, InterruptedException {

        /*
         * After new release 10.10.28 all read-only users is automatically in
         * follow mode(are the read-only users really in follow mode???) when
         * host grants someone exclusive write access. So the following three
         * line have to comment out, otherwise you should get timeoutException.
         */
        // alice.bot.waitUntilFollowed(carl.getBaseJid());
        // bob.bot.waitUntilFollowed(carl.getBaseJid());
        // dave.bot.waitUntilFollowed(carl.getBaseJid());

        carl.editor.setTextInJavaEditorWithoutSave(CP1, PROJECT1, PKG1, CLS1);
        String dirtyClsContentOfCarl = carl.editor.getTextOfJavaEditor(
            PROJECT1, PKG1, CLS1);

        alice.editor.waitUntilJavaEditorContentSame(dirtyClsContentOfCarl,
            PROJECT1, PKG1, CLS1);
        assertTrue(alice.editor.isJavaEditorActive(CLS1));
        assertTrue(alice.editor.isClassDirty(PROJECT1, PKG1, CLS1,
            ID_JAVA_EDITOR));

        bob.editor.waitUntilJavaEditorContentSame(dirtyClsContentOfCarl,
            PROJECT1, PKG1, CLS1);
        assertTrue(bob.editor.isJavaEditorActive(CLS1));
        assertTrue(bob.editor
            .isClassDirty(PROJECT1, PKG1, CLS1, ID_JAVA_EDITOR));

        dave.editor.waitUntilJavaEditorContentSame(dirtyClsContentOfCarl,
            PROJECT1, PKG1, CLS1);
        assertTrue(dave.editor.isJavaEditorActive(CLS1));
        assertTrue(dave.editor.isClassDirty(PROJECT1, PKG1, CLS1,
            ID_JAVA_EDITOR));

        carl.stopFollowedBy(alice, bob, dave);
        carl.editor.setTextInJavaEditorWithoutSave(CP1_CHANGE, PROJECT1, PKG1,
            CLS1);
        carl.editor.closeJavaEditorWithSave(CLS1);
        String dirtyClsChangeContentOfCarl = carl.editor.getTextOfJavaEditor(
            PROJECT1, PKG1, CLS1);

        assertTrue(alice.editor.isJavaEditorActive(CLS1));
        /*
         * TODO alice can still see the changes maded by carl, although she
         * already leave follow mode. There is a bug here (see Bug 3094186)and
         * it should be fixed, so that asserts that the following condition is
         * false
         * 
         * 
         * With Saros Version 10.10.29.DEVEL.
         */
        // assertFalse(alice.bot.getTextOfJavaEditor(PROJECT, PKG, CLS).equals(
        // dirtyClsChangeContentOfCarl));

        assertTrue(bob.editor.isJavaEditorActive(CLS1));

        /*
         * TODO bob can still see the changes maded by carl, although he already
         * leave follow mode. There is a bug here (see Bug 3094186) and it
         * should be fixed, so that asserts that the following condition is
         * false.
         * 
         * With Saros Version 10.10.29.DEVEL.
         */
        // assertFalse(bob.bot.getTextOfJavaEditor(PROJECT, PKG, CLS).equals(
        // dirtyClsChangeContentOfCarl));

        assertTrue(dave.editor.isJavaEditorActive(CLS1));

        /*
         * TODO dave can still see the changes , although he already leave
         * follow mode. There is a bug here (see Bug 3094186) and it should be
         * fixed, so that asserts that the following condition is false.
         * 
         * With Saros Version 10.10.29.DEVEL.
         */
        // assertFalse(dave.bot.getTextOfJavaEditor(PROJECT, PKG, CLS).equals(
        // dirtyClsChangeContentOfCarl));

    }
}