/*******************************************************************************
 * Copyright (c) 2013 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package ch.ahoegger.contentassist.internal;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import ch.ahoegger.contentassist.Activator;

/**
 * <h3>{@link ProposalStore}</h3>
 * 
 * @author aho
 * @since 3.10.0 11.02.2014
 */
public class ProposalStore {

  private ProposalStore() {

  }

  public static List<ICompletionProposal> loadProposals() {
    SAXParserFactory spf = SAXParserFactory.newInstance();

    // Create the XMLReader to be used to parse the document.
    XMLReader reader = null;
    try {
      // find file
      URL contentAssistXml = Platform.getInstallLocation().getDataArea("contentAssist.xml");
      SAXParser parser = spf.newSAXParser();
      reader = parser.getXMLReader();
      // Specify the error handler and the content handler.
      reader.setErrorHandler(new P_ParseErrorHandler());
      P_ContentHandler handler = new P_ContentHandler();
      reader.setContentHandler(handler);

      // Use the XMLReader to parse the entire file.
      InputStream in = null;
      try {
        in = contentAssistXml.openStream();
        InputSource is = new InputSource(in);
        reader.parse(is);
        return handler.getProposals();
      }
      finally {
        if (in != null) {
          in.close();
        }
      }
    }
    catch (Exception e) {
      Activator.logError(e);
    }
    return Collections.emptyList();
  }

  private static class P_ContentHandler extends DefaultHandler {

    public static enum Element {
      Name,
      Code
    }

    private Element m_currentElement;

    List<ICompletionProposal> m_proposals = new ArrayList<ICompletionProposal>();
    private SnippetProposal m_currentProp = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      if ("item".equalsIgnoreCase(qName)) {
        m_currentProp = new SnippetProposal();
      }
      else if (m_currentProp != null && "name".equalsIgnoreCase(qName)) {
        m_currentElement = Element.Name;
      }
      else if (m_currentProp != null && "code".equalsIgnoreCase(qName)) {
        m_currentElement = Element.Code;
      }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      if (m_currentProp != null && m_currentElement != null) {
        switch (m_currentElement) {
          case Name:
            m_currentProp.setName("zz_" + new String(ch, start, length));
            break;
          case Code:
            m_currentProp.setValue(new String(ch, start, length));

            break;
        }
      }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      if ("item".equalsIgnoreCase(qName)) {
        if (m_currentProp.getValue() != null && m_currentProp.getName() != null) {
          m_proposals.add(m_currentProp);
        }
        m_currentProp = null;
      }
      else if ("code".equalsIgnoreCase(qName) || "name".equalsIgnoreCase(qName)) {
        m_currentElement = null;
      }
    }

    public List<ICompletionProposal> getProposals() {
      return m_proposals;
    }
  }

  private static class P_ParseErrorHandler implements ErrorHandler {

    @Override
    public void warning(SAXParseException exception) throws SAXException {
      Activator.logWarning(exception);
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
      Activator.logError(exception);
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
      Activator.logError(exception);
    }

  }
}
