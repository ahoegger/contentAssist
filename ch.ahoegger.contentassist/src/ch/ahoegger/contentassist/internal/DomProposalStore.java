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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.ahoegger.contentassist.Activator;

/**
 * <h3>{@link DomProposalStore}</h3>
 * 
 * @author aho
 * @since 3.10.0 11.02.2014
 */
public class DomProposalStore {

  private DomProposalStore() {

  }

  public static Set<? extends ICompletionProposal> loadProposals() {

    Set<SnippetProposal> result = new TreeSet<SnippetProposal>();
    InputStream in = null;
    try {
      URL contentAssistXml = Platform.getInstallLocation().getDataArea("contentAssist.xml");
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      in = contentAssistXml.openStream();
      Document doc = dBuilder.parse(in);

      NodeList nList = doc.getElementsByTagName("item");
      for (int i = 0; i < nList.getLength(); i++) {
        SnippetProposal prop = new SnippetProposal();
        Node nNode = nList.item(i);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          Element eElement = (Element) nNode;
          NodeList nameList = eElement.getElementsByTagName("name");
          if (nameList == null) {
            continue;
          }
          Element nameElement = (Element) nameList.item(0);
          if (nameElement == null) {
            continue;
          }
          prop.setName(nameElement.getTextContent());
          NodeList codeList = eElement.getElementsByTagName("code");
          if (codeList == null) {
            continue;
          }
          Element codeElement = (Element) codeList.item(0);
          if (codeElement == null) {
            continue;
          }
          prop.setValue(codeElement.getTextContent());
          result.add(prop);
        }
      }
    }
    catch (Exception e) {
      Activator.logError(e);
    }
    finally {
      if (in != null) {
        try {
          in.close();
        }
        catch (IOException e) {
          // void
        }
      }
    }

    return result;
  }
}
