/* FileLog.java
 Copyright (C) 2011, 2013 Red Hat, Inc.

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
package net.sourceforge.jnlp.util.logging;

import net.adoptopenjdk.icedteaweb.IcedTeaWebConstants;
import net.adoptopenjdk.icedteaweb.logging.Logger;
import net.adoptopenjdk.icedteaweb.logging.LoggerFactory;
import net.adoptopenjdk.icedteaweb.os.OsUtil;
import net.sourceforge.jnlp.util.docprovider.TextsProvider;
import net.sourceforge.jnlp.util.logging.filelogs.LogBasedFileLog;
import net.sourceforge.jnlp.util.logging.filelogs.WriterBasedFileLog;
import net.sourceforge.jnlp.util.logging.headers.Header;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is utility and factory around file logs.
 */
public final class FileLog  {

    private final static Logger LOG = LoggerFactory.getLogger(FileLog.class);

    public static Header getHeadlineHeader() {
        return new Header(OutputControllerLevel.WARNING_ALL);
    }

    private static String getColon() {
        if (OsUtil.isWindows()) {
            return "_";
        } else {
            return ":";
        }
    }
    
    private static final class SingleStreamLoggerImpl implements SingleStreamLogger {

        public SingleStreamLoggerImpl() {
        }

        @Override
        public void log(String s) {
            //dummy
        }

        @Override
        public void close() {
            //dummy
        }
    }

    private static final SimpleDateFormat fileLogNameFormatter = new SimpleDateFormat("yyyy-MM-dd_HH" + getColon() + "mm" + getColon() + "ss.S");
    /**"Tue Nov 19 09:43:50 CET 2013"*/

    private static final String defaultloggerName = TextsProvider.ITW + " file-logger";


    public static SingleStreamLogger createFileLog() {
        return createFileLog("javantx");
    }
    
    public static SingleStreamLogger createAppFileLog() {
        return createFileLog("clienta");
    }
    
    private static SingleStreamLogger createFileLog(String id) {
        SingleStreamLogger s;
        try {
            if (LogConfig.getLogConfig().isLegacyLogBasedFileLog()) {
                s = new LogBasedFileLog(defaultloggerName, getFileName(id), false);
            } else {
                s = new WriterBasedFileLog(getFileName(id), false);
            }
        } catch (Exception ex) {
            LOG.error(IcedTeaWebConstants.DEFAULT_ERROR_MESSAGE, ex);
            //we do not wont to block whole logging just because initialization error in "new FileLog()"
            s = new SingleStreamLoggerImpl();
        }
        return s;
    }

    private static String getFileName(String id) {
        String s = LogConfig.getLogConfig().getIcedteaLogDir() + "itw-" + id + "-" + getStamp() + ".log";
        LOG.debug("Attempting to log into: {}", s);
        return s;
    }
    
  
    private static String getStamp() {
        return fileLogNameFormatter.format(new Date());
    }

}
