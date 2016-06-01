package com.pop.star.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.pop.star.resource.GameResourceManager;

/**
 * 提示文字
 * 
 * @author 李腾飞
 * 
 */
public class Toast extends Group {

	/**
	 * 
	 * @param str
	 *            显示的字
	 * @param y
	 *            只改Y坐标
	 */
	public Toast(String str, int y) {
		LabelStyle style = new LabelStyle();
		style.font = GameResourceManager.getInstance().getBitmapFont();
		Label label = new Label(str, style);
		label.setColor(Color.BLUE);
		label.setPosition(480 / 2 - label.getWidth() / 2, y);
		addActor(label);
		addAction(Actions.sequence(Actions.fadeOut(1.3f),
				Actions.removeActor(this)));
	}

	/**
	 * 默认
	 * 
	 * @param str
	 */
	public Toast(String str) {
		this(str, 100);
	}

}
