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

import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension2;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension3;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension4;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.text.edits.ReplaceEdit;

import ch.ahoegger.contentassist.Activator;

/**
 * <h3>{@link SnippetProposal}</h3>
 * 
 * @author aho
 * @since 3.10.0 11.02.2014
 */
public class SnippetProposal implements IJavaCompletionProposal, ICompletionProposalExtension, ICompletionProposalExtension2, ICompletionProposalExtension3, ICompletionProposalExtension4 {

  private String m_name;
  private String m_value;

  public SnippetProposal() {
  }

  public void setName(String name) {
    m_name = name;
  }

  public String getName() {
    return m_name;
  }

  public void setValue(String value) {
    m_value = value;
  }

  public String getValue() {
    return m_value;
  }

  @Override
  public Point getSelection(IDocument document) {
    return null;
  }

  @Override
  public void apply(IDocument document) {
  }

  @Override
  public String getAdditionalProposalInfo() {
    return null;
  }

  @Override
  public String getDisplayString() {
    return getName();
  }

  @Override
  public Image getImage() {
    return Activator.getImage("resources/icons/eclipse_scout.gif");
  }

  @Override
  public IContextInformation getContextInformation() {
    return null;
  }

  @Override
  public boolean isAutoInsertable() {
    return false;
  }

  @Override
  public IInformationControlCreator getInformationControlCreator() {
    return null;
  }

  @Override
  public CharSequence getPrefixCompletionText(IDocument document, int completionOffset) {
    return null;
  }

  @Override
  public int getPrefixCompletionStart(IDocument document, int completionOffset) {
    return 0;
  }

  @Override
  public void apply(ITextViewer viewer, char trigger, int stateMask, int offset) {
    try {
      IDocument document = viewer.getDocument();
      Point wordRange = findTriggerWordRange(document, offset);
      if (wordRange != null) {

        ReplaceEdit edit = new ReplaceEdit(wordRange.x, wordRange.y - wordRange.x, getValue());
        edit.apply(document);
      }
    }
    catch (BadLocationException e) {
      Activator.logError(e);
    }

  }

  @Override
  public void selected(ITextViewer viewer, boolean smartToggle) {
  }

  @Override
  public void unselected(ITextViewer viewer) {
  }

  @Override
  public boolean validate(IDocument document, int offset, DocumentEvent event) {
    return false;
  }

  @Override
  public void apply(IDocument document, char trigger, int offset) {
  }

  @Override
  public boolean isValidFor(IDocument document, int offset) {
    return false;
  }

  @Override
  public char[] getTriggerCharacters() {
    return null;
  }

  @Override
  public int getContextInformationPosition() {
    return 0;
  }

  @Override
  public int getRelevance() {
    return 0;
  }

  protected Point findTriggerWordRange(IDocument document, int offset) throws BadLocationException {
    IRegion lineRange = document.getLineInformationOfOffset(offset);
    // find start
    int startOffest = -1;
    int index = offset - 1;

    while (index > 0 && index > lineRange.getOffset() && isValidChar(document.getChar(index))) {
      index--;
    }
    startOffest = index;

    // find end
    int endOffset = -1;
    index = offset;
    while ((document.getLength() > index && index < (lineRange.getOffset() + lineRange.getLength())) && isValidChar(document.getChar(index))) {
      index++;
    }
    endOffset = index;
    if (startOffest > -1 && endOffset > -1 && startOffest <= endOffset) {
      return new Point(startOffest, endOffset);
    }
    return null;
  }

  private boolean isValidChar(char c) {
    // 0-9
    if (c >= 48 && c <= 57) {
      return true;
    }
    // A-Z
    if (c >= 65 && c <= 90) {
      return true;
    }
    // a-z
    if (c >= 97 && c <= 122) {
      return true;
    }
    // special characters
    switch (c) {
      case '-':
      case '_':
        return true;
    }
    return false;
  }
}
