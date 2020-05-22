/*
   Copyright (C) 2013 Red Hat, Inc.

This file is part of IcedTea.

IcedTea is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License as published by
the Free Software Foundation, version 2.

IcedTea is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with IcedTea; see the file COPYING.  If not, write to
the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version.
 */

package net.adoptopenjdk.icedteaweb.xmlparser;

import net.adoptopenjdk.icedteaweb.logging.Logger;
import net.adoptopenjdk.icedteaweb.logging.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import static net.adoptopenjdk.icedteaweb.xmlparser.ParserType.NORMAL;

/**
 * A gateway to the actual implementation of the parsers.
 *
 * Used by net.sourceforge.jnlp.Parser
 */
public class XMLParser {

    private final static Logger LOG = LoggerFactory.getLogger(XMLParser.class);

    public static final String CODEBASE = "codebase";

    /**
     * Parses input from an InputStream and returns a Node representing the
     * root of the parse tree.
     *
     * @param input the {@link InputStream} containing the XML
     * @return a {@link XmlNode} representing the root of the parsed XML
     * @throws ParseException if parsing fails
     */
    public final XmlNode getRootNode(final InputStream input) throws ParseException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            final Reader xmlReader = XMLSanitizer.sanitizeXml(preprocessXml(new XmlStreamReader(input)));
            final InputStream inputStreamWrapper = new InputStream() {
                @Override
                public int read() throws IOException {
                    return xmlReader.read();
                }
            };
            Document doc = dBuilder.parse(inputStreamWrapper);
            return new XmlNode2(doc.getDocumentElement());
        } catch (Exception ex) {
            throw new ParseException("Invalid XML document syntax.", ex);
        }
    }

    protected Reader preprocessXml(final Reader original) throws ParseException {
        LOG.info("Using XMLParser");
        ParseException.setUsed(NORMAL);
        return original;
    }
}
