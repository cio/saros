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

package de.fu_berlin.inf.dpp.intellij;

import com.intellij.openapi.actionSystem.KeyboardShortcut;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.project.Project;
import de.fu_berlin.inf.dpp.core.Saros;
import de.fu_berlin.inf.dpp.intellij.util.DateFormatFileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.jetbrains.annotations.NotNull;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;

/**
 * Component that is initalized when a project is loaded.
 * It initializes the logging, shortcuts and the {@link Saros} object.
 */
public class SarosComponent
    implements com.intellij.openapi.components.ProjectComponent {

    public SarosComponent(final Project project) {
        //Configures console logger
        URL log4jProperties = getClass().getResource("/log4j.properties");
        PropertyConfigurator.configure(log4jProperties);

        //We configure the file logger in code, because we can not access the
        //logPath from log4j.properties
        configureFileLogger();

        Keymap keymap = KeymapManager.getInstance().getActiveKeymap();
        keymap.addShortcut("ActivateSarosToolWindow", new KeyboardShortcut(
            KeyStroke.getKeyStroke(KeyEvent.VK_F11,
                java.awt.event.InputEvent.ALT_DOWN_MASK), null
        ));

        Saros.create(project);
    }

    private void configureFileLogger() {
        String file = "'" + PathManager.getLogPath()
            + "/SarosLogs/Saros_'yyyy-MM-dd_HH-mm-ss'.log'";
        try {
            PatternLayout layout = new PatternLayout("%d %-5p [%c{1}] %m%n");
            DateFormatFileAppender appender = new DateFormatFileAppender(file,
                layout);
            appender.setName("DateFormatAppender");
            Logger.getRootLogger().addAppender(appender);
        } catch (IOException e) {
            com.intellij.openapi.diagnostic.Logger
                .getInstance(SarosComponent.class)
                .error("Could not instantiate logger for path " + file);
        }
    }

    @Override
    public void initComponent() {
        //NOP
    }

    @Override
    public void disposeComponent() {
        //NOP
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "Saros";
    }

    @Override
    public void projectOpened() {
        //TODO: Update project
    }

    @Override
    public void projectClosed() {
        //TODO: Update project
    }
}
