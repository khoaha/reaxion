package com.googlecode.reaxion.game.model.attackobject;

import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;

public class DarkSpike extends AttackObject {
	
	public static final String filename = "dark-spike";
	protected static final int span = 260;
	protected static final float dpf = 13;
	
	private final int upTime = 4;
	private final int downTime = 248;
	
	public DarkSpike(Model m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	public DarkSpike(Model[] m) {
    	super(filename, dpf, m);
    	flinch = true;
    	lifespan = span;
    }
	
	@Override
	public void hit(StageGameState b, Character other) {
		finish(b);
    }
	
	@ Override
    public void act(StageGameState b) {
		if (lifeCount < upTime)
			velocity = new Vector3f(0, (float)4/upTime, 0);
		else if (lifeCount >= downTime)
			velocity = new Vector3f(0, (float)-4/(lifespan - downTime), 0);
		else
			velocity = new Vector3f();
		
    	super.act(b);
    }
	
}
