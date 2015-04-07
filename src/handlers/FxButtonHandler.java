package handlers;

import javafx.embed.swt.FXCanvas;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class FxButtonHandler
{

	private Button button;

	@Inject
	private void init(final Composite parent)
	{
		final FXCanvas canvas = new FXCanvas(parent, SWT.NONE);
		button = new Button();
		button.setText("FxButton (1)");
		button.setMinSize(500, Button.USE_PREF_SIZE);
		button.setMinWidth(Button.USE_PREF_SIZE);
		button.setStyle("-fx-background-color:#4e6c7a,linear-gradient(#86a5b5, #6b8b9a);");
		final Scene scene = new Scene(button);
		canvas.setScene(scene);

	}

	@Optional
	@Inject
	private void changeButtonStyle(@Named("SWITCH") final boolean switchContext)
	{

		if (switchContext) {
			button.setStyle("-fx-background-color:#4e6c7a,linear-gradient(#86a5b5, #6b8b9a);");
		}
		else {
			button.setStyle("-fx-text-fill:#ffffff;-fx-background-color:#545453,linear-gradient(#c67a6e 0%, #a92b1d 40%, #af463a 60%, #d7aca7 100%);");
		}

	}
}
