package com.googlecode.reaxion.game.overlay;

import java.awt.Point;

import com.googlecode.reaxion.game.util.FontUtils;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
import com.jmex.angelfont.BitmapText;

public class CharacterSelectionOverlay extends GridOverlay {

	private static final String baseURL = "../../resources/icons/characterselect/";
	private static final String cursorURL = "../../resources/cursors/";

	private final int numchars = 8;
	
	private String[] charNames;
	private Quad[] p1Fill;
	private Quad[] p2Fill;
	private Quad[] opFill;
	private Quad p1c;
	private Quad p2c;
	private BitmapText[] p1Display;
	private BitmapText[] p2Display;
	private BitmapText[] opDisplay;
	private BitmapText menu;

	private int tblL = 6;
	private int tblW = 4;
	private int round = 0;
	
	private ColorRGBA textColor;
	private ColorRGBA boxColor;
	private ColorRGBA selTextColor;
	private ColorRGBA selBoxColor;

	private int[] currentIndex = new int[2];
	private int[] selectedChars = new int[3];
	
	private int[][] takenPos = new int[tblL][tblW];



	public CharacterSelectionOverlay() {
		super(800, 600, true);


		
		// create a container Node for scaling
		container = new Node("container");
		attachChild(container);

		// White
		textColor = new ColorRGBA(1, 1, 1, 1);
		// Dark Gray
		boxColor = new ColorRGBA(.25f, .25f, .25f, 1);
		selTextColor = new ColorRGBA(0, 1, 0, 1);
		selBoxColor = textColor ;//new ColorRGBA(0, .67f, .67f, 1);

		p1Fill = new Quad[numchars];
		p2Fill = new Quad[numchars];
		opFill = new Quad[numchars];

		charNames = new String[numchars];
		charNames[0] = "Khoa";
		charNames[1] = "Cy";
		charNames[2] = "Nilay";
		charNames[3] = "Monica";
		charNames[4] = "Austin";
		charNames[5] = "Brian";
		charNames[6] = "Andrew";
		charNames[7] = "Shine";
		p1Display = new BitmapText[numchars];
		p2Display = new BitmapText[numchars];
		opDisplay = new BitmapText[numchars];
		for (int i = 0; i < numchars; i++) {
			p1Display[i] = new BitmapText(FontUtils.neuropol, false);
			p2Display[i] = new BitmapText(FontUtils.neuropol, false);
			opDisplay[i] = new BitmapText(FontUtils.neuropol, false);
			p1Display[i].setText(charNames[i]);
			p2Display[i].setText(charNames[i]);
			opDisplay[i].setText(charNames[i]);
		}
		menu = new BitmapText(FontUtils.neuropol, false);
		menu.setText("Character Select. Use arrow keys to move, space to choose, and enter to play.");

		for (int i = 0; i < 3; i++)
			selectedChars[i] = 0;

		initGUI();

		container.setLocalScale((float) DisplaySystem.getDisplaySystem()
				.getHeight() / 600);
		

		
	}

	public void updateDisplay(int dir) {
		int[] lastIndex = new int[2];
		lastIndex[0] = currentIndex[0];
		lastIndex[1] = currentIndex[1];
		if (dir == 1) {

			if (currentIndex[0] == 0)
				return;
			else
				currentIndex[0]--;
		} else {
			if (dir == 2) {
				if (currentIndex[1] == tblL-1 || takenPos[currentIndex[1]+1][currentIndex[0]] == 123)
					return;
				else
					currentIndex[1]++;

			} else if (dir == 3) {
				if (currentIndex[0] == tblW-1 || takenPos[currentIndex[1]][currentIndex[0]+1] == 123)
					return;
				else
					currentIndex[0]++;

			} else {
				if (currentIndex[1] == 0)
					return;
				else
					currentIndex[1]--;

			}
		}
		/*
		 * System.out.println(lastIndex[0] + " " + lastIndex[1]);
		 * System.out.println(currentIndex[0] + " " + currentIndex[1]);
		 */

		//p1Display[selectedChars[0]].setDefaultColor(selBoxColor);
		//p2Display[selectedChars[1]].setDefaultColor(selBoxColor);
		//opDisplay[selectedChars[2]].setDefaultColor(selBoxColor);

		int selCur = takenPos[currentIndex[1]][currentIndex[0]];
		
		p1Display[selCur].setDefaultColor(selTextColor);
		
		int selBef = takenPos[lastIndex[1]][lastIndex[0]];
		
		p1Display[selBef].setDefaultColor(textColor);
		
		/*
		if (currentIndex[0] == 0) {
			p1Display[currentIndex[1]].setDefaultColor(selTextColor);
			p1Display[currentIndex[1]].update();
		} else if (currentIndex[0] == 1) {
			p2Display[currentIndex[1]].setDefaultColor(selTextColor);
			p2Display[currentIndex[1]].update();
		} else if (currentIndex[0] == 2) {
			opDisplay[currentIndex[1]].setDefaultColor(selTextColor);
			opDisplay[currentIndex[1]].update();
		}
		if (lastIndex[0] == 0) {
			p1Display[lastIndex[1]].setDefaultColor(textColor);
			if (lastIndex[1] == selectedChars[0])
				p1Display[lastIndex[1]].setDefaultColor(selBoxColor);
			p1Display[lastIndex[1]].update();
		} else if (lastIndex[0] == 1) {
			p2Display[lastIndex[1]].setDefaultColor(textColor);
			if (lastIndex[1] == selectedChars[1])
				p2Display[lastIndex[1]].setDefaultColor(selBoxColor);
			p2Display[lastIndex[1]].update();
		} else if (lastIndex[0] == 2) {
			opDisplay[lastIndex[1]].setDefaultColor(textColor);
			if (lastIndex[1] == selectedChars[2])
				opDisplay[lastIndex[1]].setDefaultColor(selBoxColor);
			opDisplay[lastIndex[1]].update();
		}*/

	}

	public void updateSel() {
		int picked = 0;
		int ctr = 0;

		/*for(int i = 0; i < tblL; i++)
			for(int j = 0; j < tblW; j++)
			{
				if(i == currentIndex[0] && j == currentIndex[1])
					picked = ctr;
				else
					ctr++;
			}*/
		picked = takenPos[currentIndex[1]][currentIndex[0]];
		System.out.println(picked);
			switch(round)
			{
				case 0:
					selectedChars[0] = picked;
					round ++;
					p1c.setLocalTranslation(p1Fill[picked].getLocalTranslation());
					container.attachChild(p1c);
					this.updateRenderState();
					break;
				case 1:
					selectedChars[1] = picked;
					round ++;
					p2c.setLocalTranslation(p1Fill[picked].getLocalTranslation());
					container.attachChild(p2c);
					this.updateRenderState();
					break;
				case 2:
					selectedChars[2] = picked;
					round ++;
					break;
				default:
					break;
		}
		
		
		/*int last = selectedChars[currentIndex[0]];
		selectedChars[currentIndex[0]] = currentIndex[1];
		if (currentIndex[0] == 0) {
			p1Display[last].setDefaultColor(textColor);
			p1Display[last].update();
		} else if (currentIndex[0] == 1) {
			p2Display[last].setDefaultColor(textColor);
			p2Display[last].update();
		} else if (currentIndex[0] == 2) {
			opDisplay[last].setDefaultColor(textColor);
			opDisplay[last].update();
		}*/
	}

	public void initGUI() {

		for(int i = 0; i < tblL; i++)
			for(int j = 0; j < tblW; j++)
				takenPos[i][j] = 123;
		
		String s = cursorURL + "p1.png";
		p1c = getImage(s);
		s = cursorURL + "p2.png";
		p2c = getImage(s);
		
    	String [] charLoc = new String[numchars];
    	Quad [] pix = new Quad[numchars];
    	for(int j = 0; j<numchars; j++)
    	{
    		charLoc[j] = baseURL+charNames[j].toLowerCase()+".png";
    		p1Fill[j] = getImage(charLoc[j]);
    		p1Fill[j].setLocalScale(35f/64f);
    		p2Fill[j] = getImage(charLoc[j]);
    		p2Fill[j].setLocalScale(35f/64f);
    		opFill[j] = getImage(charLoc[j]);
    		opFill[j].setLocalScale(35f/64f);
    	}
    	
    	
    	Point[][] pos = createHorizontallyCenteredGrid(tblW, tblL, 400, 70, 70, 10, 40);
		
    	int cntr = 0;
		for (int i = 0; i < tblW; i++) 
			for (int j = 0; j < tblL; j++){
			
				
				
			//p1Fill[i] = pix[i];
			//p1Fill[i].setLocalTranslation(new Vector3f(pos[i][j].y,
				//	pos[i][j].x, 0));
				if(cntr >= p1Fill.length)
					break;
				p1Fill[cntr].setLocalTranslation(new Vector3f(-425 + pos[i][j].x,
						pos[i][j].y, 0));
				p1Display[cntr].setLocalTranslation(new Vector3f(-460 + pos[i][j].x,
						pos[i][j].y - 35, 0));
				takenPos[j][i] = cntr;
				container.attachChild(p1Fill[cntr]);
				cntr++;

			

			/*//p2Fill[i] = pix[i];
			p2Fill[i].setLocalTranslation(new Vector3f(-100 + 185 + 90 * i,
					280 -10, 0));
			container.attachChild(p2Fill[i]);

			//opFill[i] = pix[i];
			opFill[i].setLocalTranslation(new Vector3f(-100 + 185 + 90 * i,
					150 -  10, 0));
			container.attachChild(opFill[i]);*/
		}

		menu.setLocalTranslation(new Vector3f(-22 + 38, 550, 0));
		menu.setSize(18);
		menu.update();
		container.attachChild(menu);

		// the following lines can be removed when brian is created.
		/*
		BitmapText warning = new BitmapText(FontUtils.neuropol, false);
		warning.setSize(18);
		warning.setDefaultColor(textColor);
		warning.setLocalTranslation(-22 + 78, 410, 0);
		warning
				.setText("Note: do not choose brian until his model has been created.");
		warning.update();
		container.attachChild(warning);
		*/

		BitmapText[] labels = new BitmapText[3];
		String[] temp = { "Player 1", "Player 2", "Opponent" };
		for (int i = 0; i < 3; i++) {
			labels[i] = new BitmapText(FontUtils.neuropol, false);
			labels[i].setSize(17);
			labels[i].setDefaultColor(textColor);
			labels[i].setLocalTranslation(-62 + 112, 405 + 50 - 130*i, 0);
			labels[i].setText(temp[i]);
			labels[i].update();
			//container.attachChild(labels[i]);
		}

		for (int i = 0; i < p1Display.length; i++) {
			p1Display[i].setSize(16);
			p1Display[i].setDefaultColor(i == 0? selTextColor : textColor);
			p1Display[i].setText(charNames[i]);
			//p1Display[i].setLocalTranslation(new Vector3f(-130 + 185 + 90 * i,
				//	360, 0));
			p1Display[i].update();
			container.attachChild(p1Display[i]);

			p2Display[i].setSize(16);
			p2Display[i].setDefaultColor(i == 0 ? selBoxColor : textColor);
			p2Display[i].setText(charNames[i]);
			//p2Display[i].setLocalTranslation(new Vector3f(-130 + 185 + 90 * i,
			//		230, 0));
			p2Display[i].update();
			container.attachChild(p2Display[i]);

			opDisplay[i].setSize(16);
			opDisplay[i].setDefaultColor(i == 0 ? selBoxColor : textColor);
			opDisplay[i].setText(charNames[i]);
			//opDisplay[i].setLocalTranslation(new Vector3f(-130 + 185 + 90 * i,
					//100, 0));
			opDisplay[i].update();
			container.attachChild(opDisplay[i]);

		}
		this.updateRenderState();
	}

	public String[] getSelectedChars() {
		String[] temp = new String[selectedChars.length];
		for (int i = 0; i < selectedChars.length; i++)
			temp[i] = charNames[selectedChars[i]];
		return temp;
	}

}