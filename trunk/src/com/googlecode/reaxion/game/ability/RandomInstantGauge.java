package com.googlecode.reaxion.game.ability;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.BattleGameState;
import com.jme.math.FastMath;

/**
 * Introduces random chance of automatically filling gauge if gauge is
 * already above the lower bound.
 */
public class RandomInstantGauge extends Ability {
	
	private static final float chance = .0002f;
	
	public RandomInstantGauge() {
		super("Evasive Start");
	}
	
	@Override
	public boolean act(Character c, BattleGameState b) {
		if (c.gauge > c.minGauge && FastMath.nextRandomFloat() <= chance) {
			System.out.println(c.model+" filled the gauge instantly!");
			c.gauge = c.maxGauge;
			activate(c, b);
		}
		return false;
	}
	
}