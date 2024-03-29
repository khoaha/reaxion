package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.ListFilter;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class SevenCard extends AttackObject {
	
	public static final String filename = "seven-card";
	protected static final int span = 240;
	protected static final float dpf = 7;
	
	private float dist = 3;
	
	private Character user;
	
	public boolean follow = true;
	
	public SevenCard(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	user = (Character)m;
    }
	
	public SevenCard(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    	user = (Character)m[m.length-1];
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		
    }
	
	@ Override
    public void act(StageGameState b) {
		
		changeDamage();
		
		// stay with character
		if (follow) {
			Vector3f rotation = user.rotationVector;
			Vector3f translation = rotation.normalize().mult(new Vector3f(dist, 0, dist));
			rotate(rotation);
			model.setLocalTranslation(user.model.getWorldTranslation().add(translation));
		}
        
        // check if a hit by another attack with linear approximation
    	Model[] collisions = getLinearModelCollisions(velocity, .5f, ListFilter.filterUsers(b.getModels(), users, true));
        for (Model c : collisions) {
        	if (c instanceof AttackObject) {
        		// kill this card
        		((AttackObject)c).hit(b, (Character)users.get(users.size()-1));
        		finish(b);
        		
        	// check for character hits
        	} else if (c instanceof Character) {
        		((Character)c).hit(b, this);
        		b.removeModel(this);
        	}
        }
        
        // actually move
        Vector3f loc = model.getLocalTranslation();
        loc.addLocal(velocity);
        model.setLocalTranslation(loc);
        
        //check lifespan
        if (lifeCount == lifespan)
        	finish(b);
        lifeCount++;
        
    }
	
	/**
	 * Change {@code damagePerFrame} based on user's HP.
	 */
	private void changeDamage() {
		int d1 = (int)(user.hp/100);
		int d2 = (int)((user.hp % 100)/10);
		int d3 = (int)(user.hp % 10);
		
		float mult = 0;
		// see if HP digits add to 7
		if (d1 + d2 + d3 == 7)
			mult += 4;
		// see if HP reversed is divisible by 7
		if (Integer.parseInt(new StringBuffer("" + (int)user.hp).reverse().toString()) % 7 == 0)
			mult += 4;
		// see if any digits are 7
		if (d1 == 7 || d2 == 7 || d3 == 7)
			mult += 4;
		
		damagePerFrame = dpf*Math.max(1, mult);
	}
	
}
