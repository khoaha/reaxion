package com.googlecode.reaxion.game;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

/**
 * Input Handler for the allows player to move it forward, backward, left, and right.
 * Attacks and other inputs pertaining to the player will also go here.
 * @author Khoa
 *
 */
public class PlayerInput extends InputHandler {
	
	/**
	 * Min and max levels of jump strength, as fractions of player's jump,
	 * dictated by how long the player holds the key
	 */
	private final int[] jumpLevels = {2, 4};
	private int jumpCount = 0;

	private BattleGameState state;
	private MajorCharacter player;
	private Camera camera;
	
	private Boolean forthOn = false;
	private Boolean leftOn = false;
	private Boolean jumpOn = false;
	
	protected Class[] attacks;
	
    /**
     * Supply the node to control and the api that will handle input creation.
     * @param b the current BattleGameState
     * @param q the array of classes of attacks
     */
    public PlayerInput(BattleGameState b) {
    	state = b;
    	attacks = state.getPlayerAttacks();
    	player = state.getPlayer();
    	camera = state.getCamera();
        setKeyBindings();
    }

    /**
     * Creates the keyboard object, allowing us to obtain the values of a keyboard as keys are
     * pressed. It then sets the actions to be triggered based on if certain keys are pressed (numpad).
     * @author Khoa
     */
    private void setKeyBindings() {
        KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

        keyboard.set("forth", KeyInput.KEY_NUMPAD8);
        keyboard.set("back", KeyInput.KEY_NUMPAD2);
        keyboard.set("right", KeyInput.KEY_NUMPAD6);
        keyboard.set("left", KeyInput.KEY_NUMPAD4);
        keyboard.set("jump", KeyInput.KEY_NUMPAD0);
        keyboard.set("attackHold", KeyInput.KEY_Z);
        keyboard.set("attack1", KeyInput.KEY_X);
        keyboard.set("attack2", KeyInput.KEY_C);
        keyboard.set("attack3", KeyInput.KEY_V);
        keyboard.set("switch", KeyInput.KEY_SPACE);
    }
    
    /**
     * Must be called during {@code update()} by the {@code GameState}. Checks the current state of
     * input commands, preserving priority by the order in which they are pressed. Sets player's unit
     * vector accordingly. (Since theta = 0 is along the Z axis,)
     * @author Khoa
     */
    public void checkKeys() {
    	// switch players
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("switch", false)) {
    		state.tagSwitch();
    		// reassign player
        	player = state.getPlayer();
        	attacks = state.getPlayerAttacks();
    	}
    	
    	// check priority key order
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("forth", false))
    		forthOn = true;
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("back", false))
    		forthOn = false;
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("right", false))
    		leftOn = false;
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("left", false))
    		leftOn = true;
    	
    	// create unit vector and check for priority releases
    	float unitX = 0f;
    	float unitY = 0f;
    	float unitZ = 0f;
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("forth", true)) {
    		if (forthOn)
    			unitX = -1f;
    	} else {
    		forthOn = false;
    	}
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("back", true)) {
    		if (!forthOn)
    			unitX = 1f;
    	} else {
    		forthOn = true;
    	}
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("right", true)) {
    		if (!leftOn)
    			unitZ = 1f;
    	} else {
    		leftOn = true;
    	}
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("left", true)) {
    		if (leftOn)
    			unitZ = -1f;
    	} else {
    		leftOn = false;
    	}
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("jump", true)) {
    		if (!player.jumpLock && !player.flinching && player.model.getWorldTranslation().y <= 0) {
    			if (!jumpOn) {
    				jumpOn = true;
    				jumpCount = jumpLevels[1] - jumpLevels[0];
    				player.gravVel = player.jump * jumpLevels[0]/jumpLevels[1];
    			}
    		} else if (jumpCount > 0) {
    			player.gravVel += player.jump/jumpLevels[1];
    			jumpCount--;
    		}
    	} else {
    		jumpOn = false;
    	}
    	
    	// check attacks
    	
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("attack1", false)) {
    		if (KeyBindingManager.getKeyBindingManager().isValidCommand("attackHold", true))
    			executeAttack(3);
    		else
    			executeAttack(0);
    	}
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("attack2", false)) {
    		if (KeyBindingManager.getKeyBindingManager().isValidCommand("attackHold", true))
    			executeAttack(4);
    		else
    			executeAttack(1);
    	}
    	if (KeyBindingManager.getKeyBindingManager().isValidCommand("attack3", false)) {
    		if (KeyBindingManager.getKeyBindingManager().isValidCommand("attackHold", true))
    			executeAttack(5);
    		else
    			executeAttack(2);
    	}
    	
    	// normalize vector
    	if (Math.abs(unitX) + Math.abs(unitY) + Math.abs(unitZ) > 1) {
    		float hyp = (float) Math.sqrt(Math.pow(unitX, 2) + Math.pow(unitY, 2) + Math.pow(unitZ, 2));
    		unitX /= hyp;
    		unitY /= hyp;
    		unitZ /= hyp;
    	}
    	
    	// calculate new angle in XZ plane
    	Vector3f p1;
    	Vector3f p2;
    	if (state.cameraMode == "free") {
    		p1 = camera.getLocation();
    		p2 = player.getTrackPoint();
    	} else {
    		p1 = player.getTrackPoint();
    		p2 = state.getTarget().getTrackPoint();
    	}
    	float angle = FastMath.atan2(p1.x-p2.x, p1.z-p2.z);
    	
    	
    	// rotate XZ components
    	float nUnitX = unitX*FastMath.sin(angle) + unitZ*FastMath.cos(angle);
    	float nUnitZ = unitX*FastMath.cos(angle) - unitZ*FastMath.sin(angle);
    	
    	// assign vector to player
    	if (!player.moveLock && !player.flinching)
    		player.setVelocity(new Vector3f(nUnitX, unitY, nUnitZ).mult(player.speed));
    }
    
    /**
     * Execute attack index in parameter
     */
    private void executeAttack(int ind) {
    	if (!player.flinching && player.currentAttack == null) {
			try {
				if (attacks[ind] != null)
					attacks[ind].getConstructors()[1].newInstance(new AttackData(player, state.getTarget()));
			} catch (Exception e) {
				System.out.println("Fatal error: Attack array parameter was not an Attack.");
				e.printStackTrace();
			}
		}
    }
}