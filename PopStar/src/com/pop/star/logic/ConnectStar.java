package com.pop.star.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pop.star.Scene;
import com.pop.star.resource.GameResourceManager;

/**
 * 连接其它星星的道具
 * 
 * @author 李腾飞
 * 
 */
public class ConnectStar extends Star {

	private TextureRegion region;

	public ConnectStar(Scene scene, int x, int y, int w, int h, int i, int j,
			int color) {
		super(scene, x, y, w, h, i, j, color);
		region = GameResourceManager.getInstance().getTextureAltas("game.pack")
				.findRegion("star" + 7);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (region != null) {
			batch.draw(region, getX(), getY(), getWidth() / 2, getHeight() / 2,
					getWidth(), getHeight(), 1, 1, 0);
		}
	}

	@Override
	public void update() {

	}

	@Override
	public void touch() {

	}

}
