package handlers;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;

public class SwitchHandler
{
	@Inject
	private IEclipseContext context;

	@PostConstruct
	private void init()
	{
		context.set("SWITCH", true);
	}

	/**
	 * Toggles the patient list visibility, called each time the Patient Handler button is click
	 */
	@Execute
	public void execute()
	{
		final boolean visibility = (boolean) context.get("SWITCH");
		context.set("SWITCH", !visibility);
	}

}
