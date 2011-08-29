package de.fu_berlin.inf.dpp.stf.client.util;

import static de.fu_berlin.inf.dpp.stf.shared.Constants.ACCEPT;
import static de.fu_berlin.inf.dpp.stf.shared.Constants.NODE_SAROS;
import static de.fu_berlin.inf.dpp.stf.shared.Constants.SHELL_SESSION_INVITATION;
import static de.fu_berlin.inf.dpp.stf.shared.Constants.VIEW_SAROS;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;

import de.fu_berlin.inf.dpp.net.JID;
import de.fu_berlin.inf.dpp.stf.client.tester.AbstractTester;
import de.fu_berlin.inf.dpp.stf.shared.Constants.TypeOfCreateProject;

public class Util {
    /**
     * Closes the following views:
     * <ol>
     * <li>Problems</li>
     * <li>Javadoc</li>
     * <li>Declaration</li>
     * <li>Task List</li>
     * <li>Outline</li>
     * </ol>
     * 
     * @param tester
     *            the remote tester e.g ALICE
     **/
    public static void closeUnnecessaryViews(AbstractTester tester)
        throws RemoteException {
        if (tester.remoteBot().isViewOpen("Problems"))
            tester.remoteBot().view("Problems").close();

        if (tester.remoteBot().isViewOpen("Javadoc"))
            tester.remoteBot().view("Javadoc").close();

        if (tester.remoteBot().isViewOpen("Declaration"))
            tester.remoteBot().view("Declaration").close();

        if (tester.remoteBot().isViewOpen("Task List"))
            tester.remoteBot().view("Task List").close();

        if (tester.remoteBot().isViewOpen("Outline"))
            tester.remoteBot().view("Outline").close();
    }

    /**
     * Opens the <b>Saros</b> view
     * 
     * @param tester
     *            the remote tester e.g. ALICE
     **/

    public static void openSarosView(AbstractTester tester)
        throws RemoteException {
        if (!tester.remoteBot().isViewOpen(VIEW_SAROS)) {
            tester.superBot().menuBar().window()
                .showViewWithName(NODE_SAROS, VIEW_SAROS);
        }
    }

    /**
     * A convenient function to quickly build a session with a project and a
     * file. The project is created by this method so it <b>must not</b> exist
     * before. The invitiees are invited concurrently.
     * 
     * @NOTE there is no guarantee that the project and its files are already
     *       shared after this method returns
     * @param projectName
     *            the name of the project
     * @param path
     *            the path of the file e.g. foo/bar/readme.txt
     * @param content
     *            the content of the file
     * @param inviter
     *            e.g. ALICE
     * @param invitees
     *            e.g. BOB, CARL
     * @throws IllegalStateException
     *             if the inviter or one of the invitee is not connected or is
     *             already in a session
     * @throws Exception
     *             for any other (internal) failure
     */
    public static void setUpSessionWithProjectAndFile(String projectName,
        String path, String content, AbstractTester inviter,
        AbstractTester... invitees) throws Exception {

        assertStates(true, false, inviter, invitees);

        inviter.superBot().internal().createProject(projectName);
        inviter.superBot().internal().createFile(projectName, path, content);
        buildSessionConcurrently(projectName, TypeOfCreateProject.NEW_PROJECT,
            inviter, invitees);
    }

    /**
     * A convenient function to quickly build a session with a java project and
     * a class. The project is created by this method so it <b>must not</b>
     * exist before. The invitiees are invited concurrently.
     * 
     * @NOTE there is no guarantee that the project and its files are already
     *       shared after this method returns
     * @param projectName
     *            the name of the project
     * @param packageName
     *            the name of the package
     * @param className
     *            the name of the class without .java or .class suffix
     * @param inviter
     *            e.g. ALICE
     * @param invitees
     *            e.g. BOB, CARL
     * @throws IllegalStateException
     *             if the inviter or one of the invitee is not connected or is
     *             already in a session
     * @throws Exception
     *             for any other (internal) failure
     */
    public static void setUpSessionWithJavaProjectAndClass(String projectName,
        String packageName, String className, AbstractTester inviter,
        AbstractTester... invitees) throws Exception {

        assertStates(true, false, inviter, invitees);

        inviter.superBot().internal().createJavaProject(projectName);
        inviter.superBot().internal()
            .createJavaClass(projectName, packageName, className);

        buildSessionConcurrently(projectName, TypeOfCreateProject.NEW_PROJECT,
            inviter, invitees);
    }

    public static void setUpSessionWithJavaProjects(
        Map<String, List<String>> projectsPkgsClasses, AbstractTester inviter,
        AbstractTester... invitees) {

        throw new UnsupportedOperationException("not yet implemented");
        // List<String> createdProjects = new ArrayList<String>();
        //
        // for (Iterator<String> i = projectsPkgsClasses.keySet().iterator(); i
        // .hasNext();) {
        // String key = i.next();
        // if (!createdProjects.contains(key)) {
        // createdProjects.add(key);
        // inviter.superBot().views().packageExplorerView().tree().newC()
        // .javaProject(key);
        // List<String> pkgAndclass = projectsPkgsClasses.get(key);
        // inviter.superBot().views().packageExplorerView()
        // .selectPkg(key, pkgAndclass.get(0)).newC()
        // .cls(key, pkgAndclass.get(0), pkgAndclass.get(1));
        // }
        // }

        // buildSessionConcurrently(projectName,
        // TypeOfCreateProject.NEW_PROJECT,
        // inviter, invitees);
    }

    /**
     * Creates a project with an empty file for every tester in his workspace.
     * The project and the file <b>must not</b> exist.
     * 
     * @param projectName
     *            the name of the project
     * @param path
     *            the path of the file e.g. foo/bar/readme.txt
     * @param testers
     *            e.g. ALICE, CARL
     * @throws Exception
     *             if a (internal) failure occurs
     */

    public static void createProjectWithEmptyFile(String projectName,
        String path, AbstractTester... testers) throws Exception {

        for (AbstractTester tester : testers) {
            tester.superBot().internal().createProject(projectName);
            tester.superBot().internal().createFile(projectName, path, "");
        }
    }

    /**
     * Adds a project to the current session. This is done sequentially, so the
     * project is send to the invitees one after another.
     * 
     * @NOTE Adding a project that is already shared or does not exist results
     *       in unexpected behavior.
     * @NOTE there is no guarantee that the project and its files are already
     *       shared after this method returns
     * @param projectName
     *            the name of the project
     * @param projectType
     *            the type of project that should be used on the invitee side
     *            e.g new, use existing ...
     * @param inviter
     *            e.g. ALICE
     * @param invitees
     *            e.g. BOB, CARL
     * @throws IllegalStateException
     *             if the inviter or one of the invitee is not connected or is
     *             not in a session
     * @throws Exception
     *             for any other (internal) failure
     */
    public static void addProjectToSessionSequentially(String projectName,
        TypeOfCreateProject projectType, AbstractTester inviter,
        AbstractTester... invitees) throws Exception {

        assertStates(true, true, inviter, invitees);

        inviter.superBot().menuBar().saros().addProjects(projectName);

        for (AbstractTester invitee : invitees) {
            invitee.superBot().confirmShellAddProjectUsingWhichProject(
                projectName, projectType);
        }
    }

    /**
     * Establish a Saros session with the given invitees. Every invitee is
     * invited one bye one.
     * 
     * @NOTE Establishing session with a project that is already shared or does
     *       not exist results in unexpected behavior.
     * @NOTE there is no guarantee that the project and its files are already
     *       shared after this method returns
     * @param projectName
     *            the name of the project to share
     * @param projectType
     *            the type of project that should be used on the invitee side
     *            e.g new, use existing ...
     * @param inviter
     *            e.g. ALICE
     * @param invitees
     *            e.g. BOB, CARL
     * @throws IllegalStateException
     *             if the inviter or one of the invitee is not connected or is
     *             already in a session
     * @throws Exception
     *             for any other (internal) failure
     */
    public static void buildSessionSequentially(String projectName,
        TypeOfCreateProject projectType, AbstractTester inviter,
        AbstractTester... invitees) throws Exception {

        assertStates(true, false, inviter, invitees);

        JID[] inviteesJID = getJID(invitees);

        inviter.superBot().menuBar().saros()
            .shareProjects(projectName, inviteesJID);

        for (AbstractTester invitee : invitees) {
            invitee.remoteBot().shell(SHELL_SESSION_INVITATION).confirm(ACCEPT);
            invitee.superBot().confirmShellAddProjectUsingWhichProject(
                projectName, projectType);
        }
    }

    /**
     * Establish a Saros session with the given invitees. All invitees are
     * invited simultaneously.
     * 
     * @NOTE Establishing session with a project that is already shared or does
     *       not exist results in unexpected behavior.
     * @NOTE there is no guarantee that the project and its files are already
     *       shared after this method returns
     * @param projectName
     *            the name of the project to share
     * @param projectType
     *            the type of project that should be used on the invitee side
     *            e.g new, use existing ...
     * @param inviter
     *            e.g. ALICE
     * @param invitees
     *            e.g. BOB, CARL
     * @throws IllegalStateException
     *             if the inviter or one of the invitee is not connected or is
     *             already in a session
     * @throws Exception
     *             for any other (internal) failure
     */

    public static void buildSessionConcurrently(final String projectName,
        final TypeOfCreateProject projectType, AbstractTester inviter,
        AbstractTester... invitees) throws Exception {

        assertStates(true, false, inviter, invitees);

        inviter.superBot().menuBar().saros()
            .shareProjects(projectName, Util.getJID(invitees));

        List<Callable<Void>> joinSessionTasks = new ArrayList<Callable<Void>>();
        for (final AbstractTester invitee : invitees) {
            joinSessionTasks.add(new Callable<Void>() {
                public Void call() throws Exception {
                    invitee.superBot()
                        .confirmShellSessionInvitationAndShellAddProject(
                            projectName, projectType);
                    return null;
                }
            });
        }

        workAll(joinSessionTasks);
    }

    /**
     * Activates the Follow mode feature. Activating Follow mode when the
     * followed participant has no open editors results in failure of this
     * method.
     * 
     * @param followedParticipant
     *            the participant to follow e.g ALICE
     * @param participants
     *            the list of participants who want to activate Follow mode e.g
     *            BOB, CARL
     * @throws TimeoutException
     *             if the follow participant has no editor open or the editor
     *             activity was not received yet
     * @throws IllegalStateException
     *             if the followed participant or one of the participants is not
     *             connected or not in a session
     * @throws Exception
     *             for any other (internal) failure
     */
    public static void activateFollowMode(
        final AbstractTester followedParticipant,
        AbstractTester... participants) throws Exception {

        assertStates(true, true, followedParticipant, participants);

        for (AbstractTester tester : participants)
            tester.superBot().views().sarosView()
                .selectParticipant(followedParticipant.getJID())
                .followParticipant();
    }

    /**
     * Adds buddies to the contact list of the tester. All buddies will have the
     * tester added to their contact list as well.
     * 
     * @param tester
     *            the tester who wants to add buddies to his contact list e.g
     *            ALICE
     * @param buddies
     *            the buddies to add, e.g BOB, CARL
     * @throws IllegalStateException
     *             if the tester or one of the buddies is not connected
     * @throws Exception
     *             for any other (internal) failure
     * 
     */
    public static void addBuddiesToContactList(AbstractTester tester,
        AbstractTester... buddies) throws Exception {

        assertStates(true, null, tester, buddies);

        for (AbstractTester peer : buddies) {
            if (!tester.superBot().views().sarosView().hasBuddy(peer.getJID())) {
                tester.superBot().views().sarosView()
                    .addNewBuddy(peer.getJID());
                peer.superBot().confirmShellRequestOfSubscriptionReceived();
            }
        }
    }

    /**
     * Removes the given buddies from the contact list of the tester. All
     * buddies will have the tester removed from their contact list as well.
     * 
     * @param tester
     *            the tester who wants to add buddies to his contact list e.g
     *            ALICE
     * @param buddies
     *            the buddies to add, e.g BOB, CARL
     * @throws IllegalStateException
     *             if the tester or one of the buddies is not connected
     * @throws Exception
     *             for any other (internal) failure
     * 
     */
    public static void removeBuddiesFromContactList(AbstractTester tester,
        AbstractTester... buddies) throws Exception {

        assertStates(true, null, tester, buddies);

        for (AbstractTester deletedBuddy : buddies) {
            if (!tester.superBot().views().sarosView()
                .hasBuddy(deletedBuddy.getJID()))
                continue;
            tester.superBot().views().sarosView()
                .selectBuddy(deletedBuddy.getJID()).delete();
            deletedBuddy.superBot().confirmShellRemovelOfSubscription();
        }

    }

    /**
     * Adds buddies to the current session.
     * 
     * @NOTE there is no guarantee that the project and its files are already
     *       shared after this method returns
     * @param projectName
     *            the name of the project which <b>must</b> be shared in the
     *            current session
     * @param projectType
     *            the type of project that should be used on the invitee side
     *            e.g new, use existing ...
     * @param inviter
     *            the test who must be host of the current session
     * @param invitees
     *            the buddies you want to invite to your session
     * @throws IllegalStateException
     *             if the inviter or one of the invitees is not connected, one
     *             of the invitee is already in a session or the inviter is not
     *             host
     * @throws Exception
     *             for any other (internal) failure
     */
    public static void inviteBuddies(final String projectName,
        final TypeOfCreateProject projectType, AbstractTester inviter,
        AbstractTester... invitees) throws Exception {

        assertStates(true, null, inviter, invitees);

        if (!inviter.superBot().views().sarosView().isInSession())
            throw new IllegalStateException(inviter + " is not in a session");

        if (!inviter.superBot().views().sarosView().isHost())
            throw new IllegalStateException(inviter
                + " is not host of the current session");

        for (AbstractTester invitee : invitees) {
            if (invitee.superBot().views().sarosView().isInSession())
                throw new IllegalStateException(invitee
                    + " is already in a session");
        }

        inviter.superBot().menuBar().saros()
            .addBuddies(Util.getBaseJID(invitees));

        List<Callable<Void>> joinSessionTasks = new ArrayList<Callable<Void>>();

        for (final AbstractTester tester : invitees) {
            joinSessionTasks.add(new Callable<Void>() {
                public Void call() throws Exception {
                    tester.remoteBot().shell(SHELL_SESSION_INVITATION)
                        .confirm(ACCEPT);
                    tester.superBot().confirmShellAddProjectUsingWhichProject(
                        projectName, projectType);
                    return null;
                }
            });
        }

        Util.workAll(joinSessionTasks);
    }

    /**
     * Returns the base part of the JID from the tester.
     * 
     * @param tester
     *            a list of tester
     * @return the base part JIDs of the tester in the same order as in the
     *         tester list
     */
    public static String[] getBaseJID(AbstractTester... tester) {
        String[] peerBaseJIDs = new String[tester.length];
        for (int i = 0; i < tester.length; i++) {
            peerBaseJIDs[i] = tester[i].getBaseJid();
        }
        return peerBaseJIDs;
    }

    /**
     * Returns the JID from the tester.
     * 
     * @param tester
     *            a list of tester
     * @return the JIDs of the tester in the same order as in the tester list
     */

    public static JID[] getJID(AbstractTester... tester) {
        JID[] peerBaseJIDs = new JID[tester.length];
        for (int i = 0; i < tester.length; i++) {
            peerBaseJIDs[i] = tester[i].getJID();
        }
        return peerBaseJIDs;
    }

    /**
     * Grants write access to the given participants
     * 
     * @param host
     *            the host of the current session, e.g ALICE
     * @param participants
     *            the participants to grant write access to, e.g BOB, CARL
     * @throws IllegalStateException
     *             if the host or one of the participants is not connected or is
     *             not in a session or the host is not host of the current
     *             session host
     * @throws Exception
     *             for any other (internal) failure
     */
    public static void grantWriteAccess(AbstractTester host,
        AbstractTester... participants) throws Exception {

        assertStates(true, true, host, participants);

        if (!host.superBot().views().sarosView().isHost())
            throw new IllegalStateException(host
                + " is not host of the current session");

        for (AbstractTester tester : participants) {
            if (tester.superBot().views().sarosView().isInSession()
                && host.superBot().views().sarosView()
                    .selectParticipant(tester.getJID()).hasReadOnlyAccess()) {
                host.superBot().views().sarosView()
                    .selectParticipant(tester.getJID()).grantWriteAccess();
            }
        }
    }

    /**
     * Stops the follow mode feature for all given participants.
     * 
     * @param participants
     *            a list of participants where follow mode should been stopped
     * @throws IllegalStateException
     *             if tone of the participants is not connected or not in a
     *             session
     * @throws Exception
     *             for any other (internal) failure
     */

    public static void stopFollowModeSequentially(
        AbstractTester... participants) throws Exception {

        assertStates(true, true, participants);

        for (AbstractTester tester : participants) {
            if (tester.superBot().views().sarosView().isInSession()
                && tester.superBot().views().sarosView().isFollowing()) {
                JID followedBuddyJID = tester.superBot().views().sarosView()
                    .getFollowedBuddy();
                tester.superBot().views().sarosView()
                    .selectParticipant(followedBuddyJID).stopFollowing();
            }
        }
    }

    /**
     * Stops the follow mode feature. This is done concurrently for all given
     * participants.
     * 
     * @param participants
     *            a list of participants where follow mode should been stopped
     * @throws IllegalStateException
     *             if tone of the participants is not connected or not in a
     *             session
     * @throws Exception
     *             for any other (internal) failure
     */
    public static void stopFollowModeConcurrently(
        AbstractTester... participants) throws Exception {

        assertStates(true, true, participants);

        List<Callable<Void>> stopFollowTasks = new ArrayList<Callable<Void>>();
        for (int i = 0; i < participants.length; i++) {
            final AbstractTester tester = participants[i];
            stopFollowTasks.add(new Callable<Void>() {
                public Void call() throws Exception {
                    JID followedBuddyJID = tester.superBot().views()
                        .sarosView().getFollowedBuddy();
                    tester.superBot().views().sarosView()
                        .selectParticipant(followedBuddyJID).stopFollowing();
                    return null;
                }
            });
        }
        workAll(stopFollowTasks);
    }

    /**
     * Rebuilds a session if necessary with the given participants. If the
     * project does not exists on the inviter side an empty non Java Project
     * will be created. Participants that are already (in a different) session
     * will not be invited.
     * 
     * @NOTE there is no guarantee that the project and its files are already
     *       shared after this method returns
     * @NOTE calling this method with always different project names during a
     *       session will result in unexpected behavior
     * 
     * @param projectName
     *            the name of the project
     * @param inviter
     *            e.g. ALICE
     * @param invitees
     *            e.g. BOB, CARL
     * @throws IllegalStateException
     *             if the host or one of the participants is not connected
     * @throws Exception
     *             for any other (internal) failure
     */

    public static void reBuildSession(String projectName,
        AbstractTester inviter, AbstractTester... invitees) throws Exception {

        assertStates(true, null, inviter, invitees);

        if (!inviter.superBot().internal().existsResource(projectName))
            inviter.superBot().internal().createProject(projectName);

        for (AbstractTester tester : invitees)
            if (!tester.superBot().views().sarosView().isInSession())
                tester.superBot().views().sarosView()
                    .connectWith(tester.getJID(), tester.getPassword());

        if (!inviter.superBot().views().sarosView().isInSession()) {

            for (AbstractTester tester : invitees) {
                buildSessionSequentially(projectName,
                    TypeOfCreateProject.EXIST_PROJECT, inviter, tester);
            }
        }
    }

    /**
     * Converts a class path of and Eclipse JDT project to its real path.<br/>
     * E.g project name = foo, pkg = my.foo, className = HelloWorld will be
     * converted to foo/src/my/foo/HelloWorld.java
     * 
     * @param projectName
     *            the name of an Eclipse project
     * @param pkg
     *            an java package name
     * @param className
     *            a class name
     * @return the path to that class
     */
    public static String classPathToFilePath(String projectName, String pkg,
        String className) {
        return projectName + "/src/" + pkg.replace('.', '/') + "/" + className
            + ".java";
    }

    /**
     * Invokes all callable tasks in the list and returns when all tasks
     * completed.
     * 
     * @param <T>
     * @param tasks
     *            a list containing callable tasks
     * @return a list with the results of all tasks
     */

    public static <T> List<T> workAll(List<Callable<T>> tasks) {
        if (System.getProperty("os.name").matches("Mac OS X.*"))
            // the menubar is only active on Mac OS on the Window that has the
            // current focus
            return workAll(tasks, 1);
        else
            return workAll(tasks, tasks.size());
    }

    /**
     * Invokes all callable tasks in the list and returns when all tasks
     * completed.
     * 
     * @param <T>
     * @param tasks
     *            a list containing callable tasks
     * @param numberOfThreads
     *            the number of threads to use to execute the tasks
     * @return a list with the results of all tasks
     */
    public static <T> List<T> workAll(List<Callable<T>> tasks,
        int numberOfThreads) {

        ExecutorService pool = Executors.newFixedThreadPool(numberOfThreads);

        try {

            List<T> result = new ArrayList<T>();

            for (Future<T> future : pool.invokeAll(tasks))
                result.add(future.get());

            return result;

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            pool.shutdown();
        }
    }

    private static void assertStates(Boolean isConnected, Boolean isInSession,
        AbstractTester tester, AbstractTester... testers) throws Exception {
        AbstractTester[] t = new AbstractTester[testers.length + 1];
        System.arraycopy(testers, 0, t, 0, testers.length);
        t[t.length - 1] = tester;

        assertStates(isConnected, isInSession, t);
    }

    private static void assertStates(Boolean isConnected, Boolean isInSession,
        AbstractTester... testers) throws Exception {
        for (AbstractTester tester : testers) {
            if (isConnected != null
                && isConnected != tester.superBot().views().sarosView()
                    .isConnected())
                throw new IllegalStateException(tester + " is "
                    + (isConnected ? "not connected" : "connected"));

            if (isInSession != null
                && isInSession != tester.superBot().views().sarosView()
                    .isInSession())
                throw new IllegalStateException(tester + " is "
                    + (isInSession ? "not" : "already") + " in a session");

        }
    }

}
