package com.googlecode.reaxion.game.model.stage;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.googlecode.reaxion.game.audio.BackgroundMusic;
import com.googlecode.reaxion.game.model.Model;
import com.googlecode.reaxion.game.model.prop.Dust;
import com.googlecode.reaxion.game.state.StageGameState;
import com.googlecode.reaxion.game.util.LoadingQueue;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;

/**
 * Battles end not with a bang but with a whimper in death's other kingdom.
 */
public class TwilightKingdom extends Stage {

	public static final String NAME = "Twilight Kingdom";

	private static final String filename = "stages/twilight_kingdom-ground";

	private static int r = 100;

	private Model sky;

	public TwilightKingdom() {
		super(filename, NAME);
		bgm = new BackgroundMusic[] { BackgroundMusic.MITSU_NO_YOAKE };
		bgmOdds = new float[] {1};
	}

	public void loadComponents(StageGameState b) {
		sky = LoadingQueue.quickLoad(new Model("stages/twilight_kingdom-sky"), b);
		b.removeModel(sky);
		model.attachChild(sky.model);
	}

	@Override
	public void act(StageGameState b) {
		Vector3f playerPos = b.getPlayer().model.getLocalTranslation();

		// make the sky move with the player
		sky.model.setLocalTranslation(playerPos);

		// make dust
		Dust dust = (Dust) LoadingQueue.quickLoad(new Dust(), b);
		float theta = 2 * FastMath.PI * FastMath.nextRandomFloat();
		dust.model.setLocalTranslation(24 * FastMath.cos(theta) + playerPos.x,
				FastMath.nextRandomFloat()*16 + 2,
				24 * FastMath.sin(theta) + playerPos.z);
		dust.setVelocity(new Vector3f(FastMath.nextRandomFloat()*.1f - .05f,
				FastMath.nextRandomFloat()*.05f - .1f,
				FastMath.nextRandomFloat()*.1f + .25f));
		b.getRootNode().updateRenderState();
	}

	@Override
	public Point2D.Float snapToBounds(Point2D.Float center) {
		// introduce offset as correct in case center is already out of bounds
		Point2D.Float offset = new Point2D.Float(0, 0);

		// find the distance from origin
		float dist = FastMath.sqrt(FastMath.pow(center.x, 2)
				+ FastMath.pow(center.y, 2));
		// find the angle from origin
		float angle = FastMath.atan2(center.y, center.x);

		if (dist >= r) {
			offset.x = (dist - r + .01f) * FastMath.cos(angle);
			offset.y = (dist - r + .01f) * FastMath.sin(angle);
		}

		return offset;
	}

	@Override
	public Point2D.Float[] bound(Point2D.Float center, float radius) {
		ArrayList<Point2D.Float> p = new ArrayList<Point2D.Float>();

		// find the distance from origin
		float dist = FastMath.sqrt(FastMath.pow(center.x, 2)
				+ FastMath.pow(center.y, 2));

		// check if outside the circle
		if (dist + radius > r) {

			// find the angle from origin
			float angle = FastMath.atan2(center.y, center.x);

			// determine collisions from that distance
			if (dist - radius == r || dist + radius == r) {
				// add the one point of collision
				p.add(new Point2D.Float(r * FastMath.cos(angle), r
						* FastMath.sin(angle)));
			} else {
				// add the two points of collision
				float ptY = FastMath.sqrt(FastMath.pow(radius, 2)
						- FastMath.pow(r - dist, 2));
				float minor = FastMath.atan2(ptY, r - dist);
				p.add(new Point2D.Float(r * FastMath.cos(angle) + radius
						* FastMath.cos(angle + minor), r * FastMath.sin(angle)
						+ radius * FastMath.sin(angle + minor)));
				p.add(new Point2D.Float(r * FastMath.cos(angle) + radius
						* FastMath.cos(angle - minor), r * FastMath.sin(angle)
						+ radius * FastMath.sin(angle - minor)));
			}

		}

		return (p.toArray(new Point2D.Float[0]));
	}

	@Override
	public LightState createLights() {
		// Set up lighting all around
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer()
				.createLightState();
		lightState.setEnabled(true);
		lightState.setGlobalAmbient(new ColorRGBA(1f, 1f, 1f, 1f));

		for (float a = 0; a < 2 * FastMath.PI; a += 2 * FastMath.PI / 8) {
			PointLight p = new PointLight();
			p.setDiffuse(new ColorRGBA(.5f, .5f, .5f, .5f));
			p.setAmbient(new ColorRGBA(.25f, .25f, .25f, .25f));
			p.setLocation(new Vector3f(600 * FastMath.cos(a), 0, 600 * FastMath
					.sin(a)));
			p.setEnabled(true);
			lightState.attach(p);
		}

		DirectionalLight dl = new DirectionalLight();
		dl.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		dl.setAmbient(new ColorRGBA(.5f, .5f, .5f, .5f));
		dl.setDirection(new Vector3f(0, -1, 0));
		dl.setEnabled(true);

		lightState.attach(dl);

		return lightState;
	}
}
