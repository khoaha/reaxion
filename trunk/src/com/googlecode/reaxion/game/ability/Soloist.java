package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.FastMath;

/**
 * Sacrifices tag switching for increased gauge.
 */
public class Soloist extends Ability {
	
	public Soloist() {
		super("Soloist");
	}
	
	@Override
	public void set(Character c) {
		System.out.println(c.model+" ");
		c.tagLock = true;
		c.gaugeRate *= 5f/3f;
	}
	
	@Override
	public boolean act(Character c, StageGameState b) {
		c.tagLock = true;
		return false;
	}
	
}