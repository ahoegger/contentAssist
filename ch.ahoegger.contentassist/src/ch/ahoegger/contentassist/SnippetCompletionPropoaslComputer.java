package ch.ahoegger.contentassist;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

import ch.ahoegger.contentassist.internal.ProposalStore;

public class SnippetCompletionPropoaslComputer implements IJavaCompletionProposalComputer {

  public SnippetCompletionPropoaslComputer() {
  }

  @Override
  public void sessionStarted() {
  }

  @Override
  public String getErrorMessage() {
    return null;
  }

  @Override
  public void sessionEnded() {
  }

  @Override
  public List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context, IProgressMonitor monitor) {
    return ProposalStore.loadProposals();
  }

  @Override
  public List<IContextInformation> computeContextInformation(ContentAssistInvocationContext context, IProgressMonitor monitor) {
    return null;
  }

}
