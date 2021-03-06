/*
 *
 *  DPP - Serious Distributed Pair Programming
 *  (c) Freie Universität Berlin - Fachbereich Mathematik und Informatik - 2010
 *  (c) NFQ (www.nfq.com) - 2014
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 1, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * /
 */

package de.fu_berlin.inf.dpp.intellij.ui.wizards.pages;

import de.fu_berlin.inf.dpp.intellij.ui.wizards.Wizard;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

/**
 * Default navigation panel with a progress bar and back, next, cancel buttons.
 */

public class NavigationPanel extends JPanel {
    public enum Position {
        FIRST, MIDDLE, LAST
    }

    public static final String NEXT_ACTION = "next";
    public static final String BACK_ACTION = "back";
    public static final String CANCEL_ACTION = "cancel";

    public static final String TITLE_NEXT = "Next>>>";
    public static final String TITLE_BACK = "<<<Back";
    public static final String TITLE_CANCEL = "Cancel";
    public static final String TITLE_FINISH = "Finish";

    private JButton backButton;
    private JButton nextButton;
    private JButton cancelButton;

    public NavigationPanel() {
        backButton = new JButton(TITLE_BACK);
        nextButton = new JButton(TITLE_NEXT);
        cancelButton = new JButton(TITLE_CANCEL);
        createUIComponents();
    }

    private void createUIComponents() {
        setLayout(new BorderLayout());

        Box box = new Box(BoxLayout.X_AXIS);

        add(new JSeparator(), BorderLayout.NORTH);

        box.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));

        backButton.setActionCommand(BACK_ACTION);
        box.add(backButton);
        box.add(Box.createHorizontalStrut(10));

        nextButton.setActionCommand(NEXT_ACTION);
        box.add(nextButton);

        cancelButton.setActionCommand(CANCEL_ACTION);
        box.add(Box.createHorizontalStrut(10));
        box.add(cancelButton);

        add(box, BorderLayout.EAST);
    }

    /**
     * Adds action listener to all buttons.
     *
     * @param actionListener action listener
     */
    public void addActionListener(ActionListener actionListener) {
        backButton.addActionListener(actionListener);
        nextButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);
    }

    /**
     * Methods changes enable status of back and next buttons according to
     * position in the page list (see {@link Wizard.WizardPageModel}.
     *
     * @param position          page position in the page list
     * @param backButtonEnabled
     * @param nextButtonEnabled
     */
    public void setPosition(Position position, boolean backButtonEnabled,
        boolean nextButtonEnabled) {
        switch (position) {
        case FIRST:
            backButton.setEnabled(false);

            nextButton.setEnabled(nextButtonEnabled);
            break;
        case MIDDLE:
            backButton.setEnabled(backButtonEnabled);

            nextButton.setEnabled(nextButtonEnabled);
            break;
        case LAST:
            backButton.setEnabled(backButtonEnabled);

            nextButton.setEnabled(nextButtonEnabled);
            nextButton.setText(TITLE_FINISH);
            nextButton.repaint();
            break;
        default:
            backButton.setEnabled(false);
            nextButton.setEnabled(false);
        }
    }

    public void disableNextButton() {
        nextButton.setEnabled(false);
    }

    public void enableNextButton() {
        nextButton.setEnabled(true);
    }

    public void setNextButtonText(String nextButtonTitle) {
        nextButton.setText(nextButtonTitle);
    }
}
