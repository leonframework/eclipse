package io.leon.eclipseintegration.ui.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.wst.jsdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.wst.jsdt.ui.text.java.IJavaCompletionProposalComputer;

public class LeonJavaScriptCompletionProposalComputer implements IJavaCompletionProposalComputer {

	public LeonJavaScriptCompletionProposalComputer() {
	}

	@Override
	public void sessionStarted() {
	}

	@Override
	public List<?> computeCompletionProposals(
			ContentAssistInvocationContext context, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return new ArrayList<ICompletionProposal>();
	}

	@Override
	public List<?> computeContextInformation(
			ContentAssistInvocationContext context, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return new ArrayList<ICompletionProposal>();
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public void sessionEnded() {
	}

}
