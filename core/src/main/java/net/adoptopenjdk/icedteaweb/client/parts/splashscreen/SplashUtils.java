/* SplashUtils.java
Copyright (C) 2012 Red Hat, Inc.

This file is part of IcedTea.

IcedTea is free software; you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software
Foundation, version 2.

IcedTea is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
IcedTea; see the file COPYING. If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is making a
combined work based on this library. Thus, the terms and conditions of the GNU
General Public License cover the whole combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent modules, and
to copy and distribute the resulting executable under terms of your choice,
provided that you also meet, for each linked independent module, the terms and
conditions of the license of that module. An independent module is a module
which is not derived from or based on this library. If you modify this library,
you may extend this exception to your version of the library, but you are not
obligated to do so. If you do not wish to do so, delete this exception
statement from your version. */
package net.adoptopenjdk.icedteaweb.client.parts.splashscreen;

import net.adoptopenjdk.icedteaweb.IcedTeaWebConstants;
import net.adoptopenjdk.icedteaweb.client.parts.splashscreen.impls.DefaultSplashScreen2012;
import net.adoptopenjdk.icedteaweb.logging.Logger;
import net.adoptopenjdk.icedteaweb.logging.LoggerFactory;
import net.sourceforge.jnlp.runtime.Boot;
import net.sourceforge.jnlp.runtime.JNLPRuntime;

import static net.adoptopenjdk.icedteaweb.IcedTeaWebConstants.ICEDTEA_WEB_SPLASH;
import static net.adoptopenjdk.icedteaweb.IcedTeaWebConstants.NO_SPLASH;

public class SplashUtils {
    private static final Logger LOG = LoggerFactory.getLogger(SplashUtils.class);

    /**
     * Returns a splash or null if splash is suppressed by {@link IcedTeaWebConstants#ICEDTEA_WEB_SPLASH} environment variable.
     *
     * Warning - splash should have receive width and height without borders.
     * Javaws window HAVE border. This must be calculated prior calling this method
     */
    static SplashPanel getSplashScreen() {
        SplashPanel splashPanel;

        if (NO_SPLASH.equalsIgnoreCase(getSplashEnvironmentVariable())) {
            return null;
        }

        splashPanel = new DefaultSplashScreen2012();

        splashPanel.setVersion(Boot.version);
        return splashPanel;
    }

    private static String getSplashEnvironmentVariable() {
        try {
            // the command line arg -Xnosplash overrules the env var
            if (!JNLPRuntime.isShowWebSplash()) {
                return NO_SPLASH;
            }
            return System.getenv(ICEDTEA_WEB_SPLASH);
        } catch (Exception ex) {
            LOG.error("Problem reading environment variable for splash screen.", ex);
            return null;
        }
    }
}
