package de.fu_berlin.inf.dpp.stf.client.testProject.testsuits.invitation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fu_berlin.inf.dpp.stf.client.testProject.helpers.STFTest;

public class TestShareProjectUsingExistingProject extends STFTest {

    /**
     * Preconditions:
     * <ol>
     * <li>Alice (Host, Driver)</li>
     * <li>Bob (Observer)</li>
     * </ol>
     * 
     * @throws RemoteException
     */

    @BeforeClass
    public static void runBeforeClass() throws RemoteException {
        initTesters(TypeOfTester.ALICE, TypeOfTester.BOB);
        setUpWorkbenchs();
        setUpSaros();
    }

    @AfterClass
    public static void runAfterClass() {
        //
    }

    @Before
    public void runBeforeEveryTest() throws RemoteException {
        alice.pEV.newJavaProjectWithClass(PROJECT1, PKG1, CLS1);
        bob.pEV.newJavaProjectWithClass(PROJECT1, PKG1, CLS2);
    }

    @After
    public void runAfterEveryTest() throws RemoteException,
        InterruptedException {
        alice.leaveSessionHostFirstDone(bob);
        deleteProjectsByActiveTesters();

    }

    @Test
    public void shareProjectUsingExistingProject() throws RemoteException {
        assertFalse(bob.pEV.isClassExist(PROJECT1, PKG1, CLS1));
        assertTrue(bob.pEV.isClassExist(PROJECT1, PKG1, CLS2));
        alice.buildSessionDoneSequentially(PROJECT1,
            TypeOfShareProject.SHARE_PROJECT,
            TypeOfCreateProject.EXIST_PROJECT, bob);
        assertTrue(bob.pEV.isClassExist(PROJECT1, PKG1, CLS1));
        assertFalse(bob.pEV.isClassExist(PROJECT1, PKG1, CLS2));
    }

    @Test
    public void shareProjectUsingExistProjectWithCopyAfterCancelLocalChange()
        throws RemoteException {
        assertFalse(bob.pEV.isClassExist(PROJECT1, PKG1, CLS1));
        assertTrue(bob.pEV.isClassExist(PROJECT1, PKG1, CLS2));

        alice
            .buildSessionDoneSequentially(
                PROJECT1,
                TypeOfShareProject.SHARE_PROJECT,
                TypeOfCreateProject.EXIST_PROJECT_WITH_COPY_AFTER_CANCEL_LOCAL_CHANGE,
                bob);
        assertTrue(bob.pEV.isWIndowSessionInvitationActive());
        bob.pEV
            .confirmSecondPageOfWizardSessionInvitationUsingExistProjectWithCopy(PROJECT1);

        assertTrue(bob.pEV.isProjectExist(PROJECT1));
        assertTrue(bob.pEV.isClassExist(PROJECT1, PKG1, CLS2));
        assertTrue(bob.pEV.isProjectExist(PROJECT1_NEXT));
        assertTrue(bob.pEV.isClassExist(PROJECT1_NEXT, PKG1, CLS1));
        bob.pEV.deleteProject(PROJECT1_NEXT);
    }

    @Test
    public void testShareProjectUsingExistingProjectWithCopy()
        throws RemoteException {
        alice.buildSessionDoneSequentially(PROJECT1,
            TypeOfShareProject.SHARE_PROJECT,
            TypeOfCreateProject.EXIST_PROJECT_WITH_COPY, bob);
        assertTrue(bob.pEV.isProjectExist(PROJECT1));
        assertTrue(bob.pEV.isClassExist(PROJECT1, PKG1, CLS2));
        assertTrue(bob.pEV.isProjectExist(PROJECT1_NEXT));
        assertTrue(bob.pEV.isClassExist(PROJECT1_NEXT, PKG1, CLS1));
        bob.pEV.deleteProject(PROJECT1_NEXT);

    }
}