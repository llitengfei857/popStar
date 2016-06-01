package com.pop.star.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pop.star.DialogHanlder;
import com.pop.star.resource.GameResourceManager;
import com.pop.star.resource.SoundManager;

/**
 * 对话框
 * 
 * @author 李腾飞
 * 
 */
public class Dialog extends Group {

	private Image imageBack;

	private ImageButton buttonOk, buttonBack;

	private TextField textFeild;

	private TextureAtlas atlas;

	private String str;

	public Dialog(String str, final DialogHanlder handler) {
		this.str = str;
		atlas = GameResourceManager.getInstance().getTextureAltas("ui.pack");
		setSize(480, 800);
		init();

		buttonOk.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				SoundManager.playButtonSelect();
				if (handler != null) {
					handler.buttonOK();
				}
				remove();
			}
		});

		buttonBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				SoundManager.playButtonSelect();
				if (handler != null) {
					handler.buttonBack();
				}
				remove();
			}
		});

	}

	private void init() {
		imageBack = new Image(atlas.findRegion("dialog"));
		ImageButtonStyle buttonStyle = new ImageButtonStyle();
		buttonStyle.up = new TextureRegionDrawable(
				atlas.findRegion("buttonOK1"));
		buttonStyle.up = new TextureRegionDrawable(
				atlas.findRegion("buttonOK2"));
		buttonOk = new ImageButton(buttonStyle);

		buttonStyle = new ImageButtonStyle();
		buttonStyle.up = new TextureRegionDrawable(
				atlas.findRegion("buttonBack1"));
		buttonStyle.up = new TextureRegionDrawable(
				atlas.findRegion("buttonBack2"));
		buttonBack = new ImageButton(buttonStyle);

		TextFieldStyle fieldStyle = new TextFieldStyle();
		fieldStyle.font = GameResourceManager.getInstance().getBitmapFont();
		fieldStyle.cursor = new TextureRegionDrawable(
				atlas.findRegion("cursor"));
		fieldStyle.fontColor = new Color(1, 1, 1, 1);
		textFeild = new TextField(str, fieldStyle);
		textFeild.setTouchable(Touchable.disabled);

		imageBack.setPosition(20, 300);
		buttonOk.setPosition(35, 305);
		buttonBack.setPosition(280, 305);
		// textFeild.setPosition(100, 300);
		textFeild.setBounds(80, 400, 380, 100);

		addActor(imageBack);
		addActor(buttonOk);
		addActor(buttonBack);
		addActor(textFeild);

	}

}
