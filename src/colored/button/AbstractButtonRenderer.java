/*******************************************************************************
 * Copyright (c) 2012 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation
 *******************************************************************************/
package colored.button;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import colored.button.util.AdvancedPath;
import colored.button.util.SWTGraphicUtil;

/**
 * This class is an abstract button renderer used for default, red, orange, green and purple themes
 */
public abstract class AbstractButtonRenderer implements ButtonRenderer
{

	private static final int RADIUS_VALUE = 10;
	private static final Color DISABLED_FONT_COLOR = SWTGraphicUtil.getColorSafely(119, 119, 119);
	private static final Color DISABLED_SECOND_BACKGROUND_COLOR = SWTGraphicUtil.getColorSafely(220, 220, 220);
	private static final Color DISABLED_FIRST_BACKGROUND_COLOR = SWTGraphicUtil.getColorSafely(237, 237, 237);

	private ButtonConfiguration normal, hover, disabled, selected, onclick;
	private GC gc;
	private ButtonConfiguration configuration;
	private int gapOnClic;
	protected OButton parent;
	private Image imageUp;
	private Image imageDown;
	private Image imageLeft;
	private Image imageRight;
	private Image disabledImage;
	private static final int MARGIN = 5;
	private static final int GAP_ON_CLIC = 2;

	/**
	 * Constructor
	 */
	protected AbstractButtonRenderer()
	{
		initButtonConfiguration();
	}

	private void initButtonConfiguration()
	{
		this.normal = createNormalConfiguration();
		this.hover = createHoverConfiguration();
		this.disabled = createDisabledConfiguration();
		this.selected = createSelectedConfiguration();
		this.onclick = createOnClickConfiguration();
	}

	/**
	 * @return the configuration when the button is not clicked, enabled, not selected, and the mouse is not hover
	 */
	protected ButtonConfiguration createNormalConfiguration()
	{
		final ButtonConfiguration configuration = new ButtonConfiguration();
		configuration.setCornerRadius(10);
		configuration.setFont(Display.getDefault().getSystemFont()).setFontColor(getFontColor());
		configuration.setGradientDirection(SWT.VERTICAL);
		configuration.setBackgroundColor(getFirstBackgroundColor());
		configuration.setSecondBackgroundColor(getSecondBackgroundColor());
		return configuration;
	}

	/**
	 * @return the font color
	 */
	protected abstract Color getFontColor();

	/**
	 * @return the first background color
	 */
	protected abstract Color getFirstBackgroundColor();

	/**
	 * @return the second background color
	 */
	protected abstract Color getSecondBackgroundColor();

	/**
	 * @return the configuration when the mouse is hover
	 */
	protected ButtonConfiguration createHoverConfiguration()
	{
		final ButtonConfiguration configuration = new ButtonConfiguration();
		configuration.setCornerRadius(10);
		configuration.setFont(Display.getDefault().getSystemFont()).setFontColor(getFontColor());
		configuration.setGradientDirection(SWT.VERTICAL);
		configuration.setBackgroundColor(getSecondBackgroundColor());
		configuration.setSecondBackgroundColor(getFirstBackgroundColor());
		return configuration;
	}

	/**
	 * @return the configuration when the button is disabled
	 */
	protected ButtonConfiguration createDisabledConfiguration()
	{
		final ButtonConfiguration configuration = new ButtonConfiguration();
		configuration.setCornerRadius(RADIUS_VALUE);
		configuration.setFont(Display.getDefault().getSystemFont()).setFontColor(DISABLED_FONT_COLOR);
		configuration.setGradientDirection(SWT.VERTICAL);
		configuration.setBackgroundColor(DISABLED_FIRST_BACKGROUND_COLOR);
		configuration.setSecondBackgroundColor(DISABLED_SECOND_BACKGROUND_COLOR);
		return configuration;
	}

	/**
	 * @return the configuration when the button is selected
	 */
	protected ButtonConfiguration createSelectedConfiguration()
	{
		return createHoverConfiguration();
	}

	/**
	 * @return the configuration when the button is clicked
	 */
	protected ButtonConfiguration createOnClickConfiguration()
	{
		return createHoverConfiguration();
	}

	/**
	 * @see com.cerner.button.ButtonRenderer#createDisabledImage()
	 */
	@Override
	public void createDisabledImage()
	{
		if ((this.disabledImage != null) && !this.disabledImage.isDisposed()) {
			this.disabledImage.dispose();
		}
		if ((this.parent == null) || (this.parent.getImage() == null)) {
			this.disabledImage = null;
		}
		else {
			this.disabledImage = new Image(this.parent.getDisplay(), this.parent.getImage(), SWT.IMAGE_DISABLE);
			this.parent.addListener(SWT.Dispose, e -> {
				if (!AbstractButtonRenderer.this.disabledImage.isDisposed()) {
					AbstractButtonRenderer.this.disabledImage.dispose();
				}
			});
		}
	}

	/**
	 * @see com.cerner.button.ButtonRenderer#dispose()
	 */
	@Override
	public void dispose()
	{}

	/**
	 * @see com.cerner.button.ButtonRenderer#drawButtonWhenMouseHover(org.eclipse.swt.graphics.GC, com.cerner.button.OButton)
	 */
	@Override
	public void drawButtonWhenMouseHover(final GC gc, final OButton parent)
	{
		this.gc = gc;
		this.configuration = this.hover;
		this.gapOnClic = 0;
		this.parent = parent;
		draw();
	}

	private void draw()
	{
		this.gc.setAdvanced(true);
		this.gc.setAntialias(SWT.ON);
		drawBackground();
		int xPosition = computeStartingPosition();
		xPosition += drawImage(xPosition);
		if (this.parent.getText() != null) {
			drawText(xPosition);
		}
	}

	private void drawBackground()
	{
		final AdvancedPath path = createClipping();
		this.gc.setClipping(path);
		this.gc.setForeground(this.configuration.getBackgroundColor());
		this.gc.setBackground(this.configuration.getSecondBackgroundColor());
		this.gc.fillGradientRectangle(0, this.gapOnClic, this.parent.getWidth(), this.parent.getHeight() - GAP_ON_CLIC,
			this.configuration.getGradientDirection() == SWT.VERTICAL);
		this.gc.setClipping((Rectangle) null);
		path.dispose();
	}

	private AdvancedPath createClipping()
	{
		final AdvancedPath path = new AdvancedPath(this.parent.getDisplay());
		path.addRoundRectangle(0, this.gapOnClic, this.parent.getWidth(), this.parent.getHeight() - GAP_ON_CLIC, this.configuration.getCornerRadius(),
			this.configuration.getCornerRadius());
		return path;
	}

	private int computeStartingPosition()
	{
		final int widthOfTextAndImage = computeSizeOfTextAndImages().x;
		switch (this.parent.alignment) {
		case SWT.CENTER:
			return (this.parent.getWidth() - widthOfTextAndImage) / 2;
		case SWT.RIGHT:
			return this.parent.getWidth() - widthOfTextAndImage - MARGIN;
		default:
			return MARGIN;
		}
	}

	private int drawImage(final int xPosition)
	{
		final Image image = extractImage();

		if (image == null) {
			return 0;
		}

		final int yPosition = (this.parent.getHeight() - image.getBounds().height - GAP_ON_CLIC) / 2;
		this.gc.drawImage(image, xPosition, yPosition + this.gapOnClic);
		return image.getBounds().width + MARGIN;
	}

	private Image extractImage()
	{
		if ((this.parent.getStyle() & SWT.ARROW) != 0) {
			if ((this.parent.getStyle() & SWT.DOWN) != 0) {
				return this.imageDown;
			}
			if ((this.parent.getStyle() & SWT.UP) != 0) {
				return this.imageUp;
			}
			if ((this.parent.getStyle() & SWT.LEFT) != 0) {
				return this.imageLeft;
			}
			if ((this.parent.getStyle() & SWT.RIGHT) != 0) {
				return this.imageRight;
			}
		}

		if (this.parent.getImage() == null) {
			return null;
		}

		final Image image;
		if (!this.parent.isEnabled()) {
			image = this.disabledImage;
		}
		else {
			image = this.parent.getImage();
		}

		return image;
	}

	private void drawText(final int xPosition)
	{
		this.gc.setFont(this.configuration.getFont());
		this.gc.setForeground(this.configuration.getFontColor());

		final Point textSize = this.gc.stringExtent(this.parent.getText());
		final int yPosition = (this.parent.getHeight() - textSize.y - GAP_ON_CLIC) / 2;

		this.gc.drawText(this.parent.getText(), xPosition, yPosition + this.gapOnClic, true);
	}

	/**
	 * @see com.cerner.button.ButtonRenderer#drawButtonWhenDisabled(org.eclipse.swt.graphics.GC, com.cerner.button.OButton)
	 */
	@Override
	public void drawButtonWhenDisabled(final GC gc, final OButton parent)
	{
		this.gc = gc;
		this.configuration = this.disabled;
		this.gapOnClic = 0;
		this.parent = parent;
		draw();
	}

	/**
	 * @see com.cerner.button.ButtonRenderer#drawButtonWhenSelected(org.eclipse.swt.graphics.GC, com.cerner.button.OButton)
	 */
	@Override
	public void drawButtonWhenSelected(final GC gc, final OButton parent)
	{
		this.gc = gc;
		this.configuration = this.selected;
		this.gapOnClic = 0;
		this.parent = parent;
		draw();
	}

	/**
	 * @see com.cerner.button.ButtonRenderer#drawButton(org.eclipse.swt.graphics.GC, com.cerner.button.OButton)
	 */
	@Override
	public void drawButton(final GC gc, final OButton parent)
	{
		this.gc = gc;
		this.configuration = this.normal;
		this.gapOnClic = 0;
		this.parent = parent;
		draw();
	}

	/**
	 * @see com.cerner.button.ButtonRenderer#drawButtonWhenClicked(org.eclipse.swt.graphics.GC, com.cerner.button.OButton)
	 */
	@Override
	public void drawButtonWhenClicked(final GC gc, final OButton parent)
	{
		this.gc = gc;
		this.configuration = this.onclick;
		this.gapOnClic = GAP_ON_CLIC;
		this.parent = parent;
		draw();
	}

	/**
	 * @see com.cerner.button.ButtonRenderer#computeSize(com.cerner.button.OButton, int, int, boolean)
	 */
	@Override
	public Point computeSize(final OButton button, final int wHint, final int hHint, final boolean changed)
	{
		this.parent = button;
		final Point sizeOfTextAndImages = computeSizeOfTextAndImages();
		return new Point((2 * MARGIN) + sizeOfTextAndImages.x, (2 * MARGIN) + sizeOfTextAndImages.y + GAP_ON_CLIC);
	}

	private Point computeSizeOfTextAndImages()
	{
		int width = 0, height = 0;
		final boolean textNotEmpty = (this.parent.getText() != null) && !this.parent.getText().equals("");

		if (textNotEmpty) {
			final GC gc = new GC(this.parent);
			if (this.configuration == null) {
				gc.setFont(this.parent.getFont());
			}
			else {
				gc.setFont(this.configuration.getFont());
			}

			final Point extent = gc.stringExtent(this.parent.getText());
			gc.dispose();
			width += extent.x;
			height = extent.y;
		}

		final Point imageSize = new Point(-1, -1);
		computeImageSize(extractImage(), imageSize);

		if (imageSize.x != -1) {
			width += imageSize.x;
			height = Math.max(imageSize.y, height);
			if (textNotEmpty) {
				width += MARGIN;
			}
		}
		return new Point(width, height);
	}

	private void computeImageSize(final Image image, final Point imageSize)
	{
		if (image == null) {
			return;
		}
		final Rectangle imageBounds = image.getBounds();
		imageSize.x = Math.max(imageBounds.width, imageSize.x);
		imageSize.y = Math.max(imageBounds.height, imageSize.y);
	}

}
