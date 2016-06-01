package com.pop.star.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pop.star.DataValues;
import com.pop.star.MainGame;
import com.pop.star.Scene;
import com.pop.star.UI.MenuScreen;
import com.pop.star.logic.DestroyLogic.DestroyHandler;
import com.pop.star.resource.GameResourceManager;
import com.pop.star.resource.SoundManager;

/**
 * 普通星星
 * 
 * @author 李腾飞
 * 
 */
public class NormalStar extends Star {

	private TextureRegion region;

	public NormalStar(Scene scene, int x, int y, int w, int h, int i, int j,
			int color) {
		super(scene, x, y, w, h, i, j, color);
		region = GameResourceManager.getInstance().getTextureAltas("game.pack")
				.findRegion("star" + color);
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
	public void touch() {
		if (getScene().isHasCheckHammer() == true) {
			DataValues.getInstance().setHammerNumber(
					DataValues.getInstance().getHammerNumber() - 1);
			// 是道具小锤子，只消除一个
			getScene().setHasCheckHammer(false);
			getScene().getDestroyLogic().destroyOneStar(getI(), getJ());
		} else {
			// 计算剩余的块
			getScene().getDestroyLogic().checkHasTheSame(getI(), getJ(),
					getStarColor());
		}
		if (getScene().getStarList()[getI()][getJ()] != null
				&& getScene().getStarSame() >= 2) {
			getScene().getEffectStar(getStarColor() - 1).setPosition(getX(),
					getY());
			getScene().getEffectStar(getStarColor() - 1).start();
			SoundManager.playSoundDestroyStar();
		}
		// 排序删星星
		getScene().getDestroyLogic().sortStar(getI(), getJ(), getStarColor());
		if (getScene().getDestroyLogic().isAllSameBlocksClear()) {
			getScene().setHasEndGame(true);
			getScene().getDestroyLogic().destroyRemainingBlocks(
					new DestroyHandler() {

						@Override
						public void handler() {
							getScene().getStage().clear();
							// 延迟N秒到下一关
							getScene().getStage().addActor(
									new LevelEndEffect(DataValues.getInstance()
											.getAllScore(), getScene()
											.getLastRemainingBlocks(),
											new EndLevelHandler() {
												@Override
												public void handler() {
													DataValues dv = DataValues
															.getInstance();
													int level = dv
															.getCurLevel();
													if (dv.getAllScore() >= dv
															.getLevelScore(level)) {
														getScene().nextLevel();
													} else {
														MainGame.instance
																.setScreen(new MenuScreen());
													}

												}
											}));
						}
					});

		}
	}

	@Override
	public void update() {

	}

}
