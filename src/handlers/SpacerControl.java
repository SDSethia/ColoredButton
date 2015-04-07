package handlers;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * The SpacerControl is a placeholder class for the ToolControl which provides space between toolbars
 * <a href="http://www.eclipse.org/forums/index.php/t/469015/">Eclipse</a> recommends the
 * usage of toolcontrol with stretch tag added to the toolcontrol.
 * 
 * @author Snehadeep Sethia
 */
public class SpacerControl
{
	/**
	 * @param parent
	 *        of this control
	 */
	@PostConstruct
	public static void initComposite(final Composite parent)
	{
		Composite body = new Composite(parent, SWT.NONE);
		body.setLayout(new GridLayout());
	}
}
