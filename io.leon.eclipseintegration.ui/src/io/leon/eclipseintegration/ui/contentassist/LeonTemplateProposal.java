package io.leon.eclipseintegration.ui.contentassist;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;

public class LeonTemplateProposal extends TemplateProposal {
	
	private final String documentation;

	public LeonTemplateProposal(Template template, String documentation, TemplateContext context,
			IRegion region, Image image) {
		super(template, context, region, image);
		this.documentation = documentation;
	}

	@Override
	public String getAdditionalProposalInfo() {
		StringBuffer buffer = new StringBuffer();
		
		if(documentation != null && documentation.length() > 0)
		{
			String htmlDocu = documentation.replaceAll("\\n", "<br/>");
			buffer.append(htmlDocu);
			buffer.append("<br/>");
//			buffer.append("<br/>");
		}
		
		// Add pattern to description
//		String formattedPattern = super.getAdditionalProposalInfo();
//		buffer.append(formattedPattern);
		return buffer.toString();
	}
}
