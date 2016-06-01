package com.pop.star.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.pop.star.DataValues;
import com.pop.star.resource.GameResourceManager;
import com.pop.star.resource.SoundManager;

/**
 * 结束特效
 * 
 * @author 李腾飞
 * 
 */
public class LevelEndEffect extends Group {

	private Label labelCurScore;

	private Label labelOverplusScore;

	private Label labelOver;

	private Timer timer;

	/**
	 * @param curScore
	 *            当前的积分
	 * @param overplusScore
	 *            最后剩余的块
	 * @param handler
	 */
	public LevelEndEffect(int curScore, int overplusScore,
			final EndLevelHandler handler) {

		LabelStyle style = new LabelStyle();
		style.font = GameResourceManager.getInstance().getBitmapFont();

		labelCurScore = new Label("当前获得分数：" + curScore, style);
		labelOverplusScore = new Label("当前剩余的块：" + overplusScore, style);
		labelOver = new Label("游戏结束,没有达到目标分数", style);
		labelCurScore.setColor(Color.BLACK);
		labelOverplusScore.setColor(Color.BLACK);
		labelOver.setColor(Color.BLACK);

		labelOver.setPosition(480 / 2 - labelOver.getWidth() / 2, 260);
		labelCurScore.setPosition(480 / 2 - labelCurScore.getWidth() / 2, 200);
		labelOverplusScore.setPosition(
				480 / 2 - labelOverplusScore.getWidth() / 2, 230);

		if (curScore >= DataValues.getInstance().getLevelScore(
				DataValues.getInstance().getCurLevel())) {
			addActor(labelCurScore);
			addActor(labelOverplusScore);
			SoundManager.playSoundWin();
		} else {
			SoundManager.playSoundLose();
			addActor(labelOver);
		}

		addAction(Actions.sequence(Actions.delay(1),
				Actions.moveBy(0, 700, 3, Interpolation.fade)));

		timer = new Timer();
		timer.scheduleTask(new Task() {

			@Override
			public void run() {
				timer.clear();
				removeAction(new DelayAction());
				removeAction(new MoveByAction());
				remove();
				if (handler != null) {
					handler.handler();
				}
			}
		}, 4);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

}
