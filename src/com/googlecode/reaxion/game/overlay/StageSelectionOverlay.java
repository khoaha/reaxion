package com.googlecode.reaxion.game.overlay;

import java.awt.Point;

import com.googlecode.reaxion.game.model.stage.Checkerboard;
import com.googlecode.reaxion.game.model.stage.Flipside;
import com.googlecode.reaxion.game.model.stage.FlowerField;
import com.googlecode.reaxion.game.model.stage.MikoLake;
import com.googlecode.reaxion.game.model.stage.SeasRepose;
import com.googlecode.reaxion.game.model.stage.TwilightKingdom;
import com.googlecode.reaxion.game.model.stage.WorldsEdge;
import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.input.KeyInput;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapText;

/**
 * {@code StageSelectionOverlay} extends the functionality of {@code GridOverlay} in
 * order to create a stage selection menu with grid elements. An image of the stage is 
 * displayed on the left of the overlay, which corresponds to the currently selected 
 * stage grid item on the right of the overlay. The name of the currently selected stage
 * is displayed under the stage preview image.
 * 
 * @author Brian
 */

public class StageSelectionOverlay extends GridOverlay {

	private String baseURL = "../../resources/stages/renders/";

	private Node container;

	private String[] stageNames = { FlowerField.name, WorldsEdge.name,
			MikoLake.name, Flipside.name, TwilightKingdom.name,
			SeasRepose.name, Checkerboard.name };
	
	private Node[] previewBoxes;
	private BitmapText[] stageTitles;
	private Quad[][] stageGrid;
	private Quad cursor;
	
	private Point[][] stageGridLayout;

	private int currentRow, currentColumn;
	private int fontSize;
	private int stageGridRows, stageGridColumns;

	public StageSelectionOverlay() {
		super();
		init();
	}

	/**
	 * This method initializes both visible and background elements of {@code StageSelectionOverlay}.
	 */
	private void init() {
		container = new Node("container");
		
		// Settings Initialization
		currentRow = 0;
		currentColumn = 0;
		fontSize = 32;

		screenWidth = 800;
		screenHeight = 600;
		
		stageGridRows = 3;
		stageGridColumns = 3;

		stageGridLayout = createVerticallyCenteredGrid(stageGridRows, stageGridColumns, screenWidth - 250,
				64, 48, 10, 10);
		
		// Visual Element Initialization
		createPreviewBoxes();
		createStageTitles();
		createStageGrid();
		
		cursor = getImage(baseURL + "stageselect.png");
		cursor.setLocalTranslation(stageGridLayout[currentRow][currentColumn].x,
				stageGridLayout[currentRow][currentColumn].y, 0);
		
		// Visual Element Attachment
		container.attachChild(cursor);
		container.attachChild(previewBoxes[currentRow * stageGridColumns + currentColumn]);
		container.attachChild(stageTitles[currentRow * stageGridColumns + currentColumn]);

		container.updateRenderState();
		container.setLocalScale((float) DisplaySystem.getDisplaySystem()
				.getHeight()
				/ screenHeight);

		attachChild(container);
	}

	/**
	 * Returns the file URL of a stage image. The {@code isPreview} parameter is used to discern between
	 * preview images and grid item images.
	 * 
	 * @param stageName
	 * @param isPreview
	 * @return File URL
	 */
	private String getImageURL(String stageName, boolean isPreview) {
		String str = stageName.replace("'", "").replace(" ", "-").toLowerCase();
		return baseURL + str + (isPreview ? "_preview" : "_griditem") + ".png";
	}

	/**
	 * Creates all of the preview boxes and stores them in an {@code Array}.
	 */
	private void createPreviewBoxes() {
		previewBoxes = new Node[stageNames.length];

		for (int i = 0; i < stageNames.length; i++)
			previewBoxes[i] = createPreviewBox(stageNames[i]);
	}

	/**
	 * Creates an individual preview box.
	 * 
	 * @param name
	 * @return Preview box for the stage specified by the {@code name} parameter
	 */
	private Node createPreviewBox(String name) {
		Node previewBox = new Node("previewBox_" + name);

		Quad image = getImage(getImageURL(name, true));

		Quad border = new Quad("border_preview_" + name, image.getWidth() + 20, image
				.getHeight() + 16);
		border.setSolidColor(ColorRGBA.white);

		previewBox.attachChild(border);
		previewBox.attachChild(image);
		previewBox.setLocalTranslation(border.getWidth() / 2 + 40,
				screenHeight / 2 + 30, 0);

		return previewBox;
	}
	
	/**
	 * Creates all of the stage titles and stores them in an {@code Array}.
	 */
	private void createStageTitles() {
		stageTitles = new BitmapText[stageNames.length];
		
		for (int i = 0; i < stageNames.length; i++)
			stageTitles[i] = createStageTitle(stageNames[i]);
	}
	
	/**
	 * Creates an individual stage title. Text, name, color, size, and local translation are defined.
	 * 
	 * @param name
	 * @return {@code BitmapText} object with text set to {@code name}
	 */
	private BitmapText createStageTitle(String name) {
		BitmapText stage = new BitmapText(FontUtils.neuropol, false);
		stage.setText(name);
		stage.setName("stageTitle_" + name);
		stage.setDefaultColor(ColorRGBA.white);
		stage.setSize(fontSize);
		stage.update();
		stage.setLocalTranslation(210 - stage.getLineWidth() / 2, screenHeight / 2 - 256 / 2, 0);
		
		return stage;
	}

	/**
	 * Creates the navigable grid of stages.
	 */
	private void createStageGrid() {
		stageGrid = new Quad[stageGridRows][stageGridColumns];
		for (int i = 0; i < stageNames.length; i++) {
			int r = i / stageGridRows;
			int c = i % stageGridColumns;

			stageGrid[r][c] = getImage(getImageURL(stageNames[i], false));
			stageGrid[r][c].setName("stageGridItem_" + stageNames[i]);

			stageGrid[r][c].setLocalTranslation(
					stageGridLayout[r][c].x,
					stageGridLayout[r][c].y, 0);
			container.attachChild(stageGrid[r][c]);
		}
	}

	/**
	 * Handles arrow key input caught by {@code StageSelectionState}. The arrow key input is interpreted and displayed
	 * as a change in the selected stage grid item.
	 * 
	 * @param key
	 */
	public void updateDisplay(int key) {
		int lastRow = currentRow;
		int lastColumn = currentColumn;
		
		/*
		 * lastItem, uneven, and onLastRow are use to handle cases when the grid is uneven.
		 * If the user presses up on the top row, the last row is uneven, and the grid item selected is in a 
		 * column that does not exist on the last row, then the last item in the last row is selected.
		 * If the user presses down on the second to last row, the last row is uneven, and the grid item selected
		 * is in a column that does not exits on the last row, then the last item in the last row is selected.
		 * Wrap around on the last row is also handled correctly.
		 */
		int lastItem = 	stageNames.length - (stageGridRows * (stageGridColumns - 1)) - 1;
		
		boolean uneven = lastItem != stageGridRows - 1;
		boolean onLastRow = currentRow == stageGridRows - 1;
		
		//Key Input Checking
		switch(key) {
		case KeyInput.KEY_UP:
			if (currentRow == 0) {
				if(uneven && currentColumn > lastItem)
					currentColumn = lastItem;
				currentRow = stageGridRows - 1;
			}
			else
				currentRow--;
			break;
			
		case KeyInput.KEY_DOWN:
			if (currentRow == stageGridRows - 1)
				currentRow = 0;				
			else {
				if (currentRow == stageGridRows - 2 && uneven && currentColumn > lastItem)
					currentColumn = lastItem;
				currentRow ++;
			}
			break;
			
		case KeyInput.KEY_LEFT:
			if (currentColumn == 0)
				currentColumn = onLastRow? lastItem : stageGridColumns - 1;
			else
				currentColumn--;
			break;
			
		case KeyInput.KEY_RIGHT:
			if (onLastRow && currentColumn == lastItem || currentColumn == stageGridColumns - 1)
				currentColumn = 0;
			else
				currentColumn++;
			break;
		}
		
		// Cursor location changed
		cursor.setLocalTranslation(stageGridLayout[currentRow][currentColumn].x,
				stageGridLayout[currentRow][currentColumn].y, 0);
		
		// Changes currently displayed preview box
		container.detachChild(previewBoxes[lastRow * stageGridColumns + lastColumn]);
		container.attachChild(previewBoxes[currentRow * stageGridColumns + currentColumn]);
		
		// Changes current displayed stage title
		container.detachChild(stageTitles[lastRow * stageGridColumns + lastColumn]);
		container.attachChild(stageTitles[currentRow * stageGridColumns + currentColumn]);

		container.updateRenderState();
	}

	/**
	 * Removes apostrophes and spaces in the currently selected stage name in order to generate a
	 * {@code String} corresponding to its class name. 
	 * 
	 * @return className
	 */
	public String getSelectedStageClass() {
		String str = stageNames[currentRow * stageGridColumns + currentColumn].replace("'", "");
		return str.replace(" ", "");
	}

	/**
	 * Returns the selected stage name.
	 * 
	 * @return stageName
	 */
	public String getSelectedStageName() {
		return stageNames[currentRow * stageGridColumns + currentColumn];
	}

	/**
	 * Returns the text font used in {@code StageSelectionOverlay}.
	 * 
	 * @return font
	 */
	public BitmapFont getTextFont() {
		return FontUtils.neuropol;
	}

}
