package colored.button;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import colored.button.util.SWTGraphicUtil;

/**
 * This is the red theme button renderer
 */
public class AnyColorButtonRenderer extends AbstractButtonRenderer
{

	private static AnyColorButtonRenderer instance;
	private static Color FIRST_BACKGROUND_COLOR;
	private static Color SECOND_BACKGROUND_COLOR;

	private AnyColorButtonRenderer()
	{
		super();
	}

	@Override
	protected Color getFontColor()
	{
		return Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	}

	@Override
	protected Color getFirstBackgroundColor()
	{
		return FIRST_BACKGROUND_COLOR;
	}

	@Override
	protected Color getSecondBackgroundColor()
	{
		return SECOND_BACKGROUND_COLOR;
	}

	private static void setButtonColor(final int r, final int g, final int b)
	{
		FIRST_BACKGROUND_COLOR = SWTGraphicUtil.getColorSafely(r, g, b);
		SECOND_BACKGROUND_COLOR = SWTGraphicUtil.getColorSafely(0, 0, 0);

	}

	public static AnyColorButtonRenderer getInstance(final int r, final int g, final int b)
	{
		if (instance == null) {
			setButtonColor(r, g, b);
			instance = new AnyColorButtonRenderer();
		}
		return instance;
	}
}