package de.fu_berlin.inf.dpp.concurrent.jupiter.test.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;

import de.fu_berlin.inf.dpp.User;
import de.fu_berlin.inf.dpp.activities.business.TextEditActivity;
import de.fu_berlin.inf.dpp.concurrent.jupiter.Operation;
import de.fu_berlin.inf.dpp.net.JID;

/**
 * this class represent a document object for testing.
 * 
 * @author troll
 * @author oezbek
 */
public class Document {

    /**
     * Listener for jupiter document actions.
     * 
     * @author orieger
     * 
     */
    public interface JupiterDocumentListener {

        public void documentAction(JID jid);

        public String getID();
    }

    private static final Logger log = Logger
        .getLogger(Document.class.getName());

    /** document state. */
    private StringBuffer doc;

    /**
     * constructor to init doc.
     * 
     * @param initState
     *            start document state.
     */
    public Document(String initState) {
        doc = new StringBuffer(initState);
    }

    /**
     * return string representation of current doc state.
     * 
     * @return string of current doc state.
     */
    public String getDocument() {
        return doc.toString();
    }

    @Override
    public String toString() {
        return doc.toString();
    }

    /**
     * Execute Operation on document state.
     * 
     * @param op
     */
    public void execOperation(Operation op) {
        User dummy = JupiterTestCase.createUserMock("dummy");

        List<TextEditActivity> activities = op.toTextEdit(new Path("dummy"),
            dummy);

        for (TextEditActivity activity : activities) {

            int start = activity.getOffset();
            int end = start + activity.getReplacedText().length();
            String is = doc.toString().substring(start, end);

            if (!is.equals(activity.getReplacedText())) {
                log.warn("Text should be '" + activity.getReplacedText()
                    + "' is '" + is + "'");
                throw new RuntimeException("Text should be '"
                    + activity.getReplacedText() + "' is '" + is + "'");
            }

            doc.replace(start, end, activity.getText());
        }
    }
}
