package handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import colored.button.AnyColorButtonRenderer;
import colored.button.OButton;
import colored.button.RedButtonRenderer;

public class OButtonHandler
{
	private OButton button;

	@Inject
	private void init(final Composite parent)
	{
		button = new OButton(parent, SWT.PUSH);
		button.setLayout(new GridLayout());
		button.setText("OButton (1)");
		button.setButtonRenderer(AnyColorButtonRenderer.getInstance(111, 111, 111));

	}

	@Optional
	@Inject
	private void changeButtonStyle(@Named("SWITCH") final boolean switchContext)
	{
		if (switchContext) {
			button.setButtonRenderer(AnyColorButtonRenderer.getInstance(222, 111, 111));
			button.pack();
		}
		else {
			button.setButtonRenderer(RedButtonRenderer.getInstance());
			button.pack();
		}

	}

}
