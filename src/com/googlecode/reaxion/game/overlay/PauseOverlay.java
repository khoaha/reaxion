package com.googlecode.reaxion.game.overlay;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;

/**
 * Facilitates the creation of and maintains in-battle pausing.
 * @author Khoa Ha
 *
 */
public class PauseOverlay extends Overlay {
	
	private static final String baseURL = "../../resources/gui/";
	
	private Quad pauseText;
	
	public PauseOverlay() {
		super();
        
        // create a container Node for scaling
        container = new Node("container");
        attachChild(container);
        
        // create pauseText
        pauseText = getImage(baseURL + "pause.png");
		pauseText.setLocalTranslation(new Vector3f(width/2, height/2, 0));
		pauseText.setLocalScale((float) DisplaySystem.getDisplaySystem().getHeight()/600);
    }
	
	/**
	 * Called when pausing to create the overlays
	 */
	public void pause() {
		container.attachChild(pauseText);
		updateRenderState();
	}
	
	/**
	 * Called when unpausing to remove the overlays
	 */
	public void unpause() {
		container.detachChild(pauseText);
		updateRenderState();
	}	
	
}
