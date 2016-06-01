package com.pop.star.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.pop.star.DataValues;
import com.pop.star.DialogHanlder;
import com.pop.star.MainGame;
import com.pop.star.Scene;
import com.pop.star.resource.GameResourceManager;
import com.pop.star.resource.SoundManager;

public class MenuScreen implements Screen {

	private Image imageBackground;

	private Image imageTitle;

	private ImageButton buttonBegionGame;

	private ImageButton buttonExit;

	private CheckBox buttonContinue;

	private CheckBox buttonSetting;

	private TextureAtlas atlas;

	private Stage stage;

	private int width, height;

	public MenuScreen() {
		init();
		DataValues.getInstance().setHammerNumber(10);
	}

	private void init() {
		atlas = GameResourceManager.getInstance().getTextureAltas("ui.pack");
		for (Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		width = MainGame.instance.getWidth();
		height = MainGame.instance.getHeight();
		stage = new Stage(new FillViewport(width, height));
		Gdx.input.setInputProcessor(stage);
		imageBackground = new Image(atlas.findRegion("back"));
		imageTitle = new Image(atlas.findRegion("title"));

		ImageButtonStyle buttonStyle = new ImageButtonStyle();
		buttonStyle.up = new TextureRegionDrawable(
				atlas.findRegion("menu_start"));
		buttonStyle.down = new TextureRegionDrawable(
				atlas.findRegion("menu_start"));
		buttonBegionGame = new ImageButton(buttonStyle);

		buttonStyle = new ImageButtonStyle();
		buttonStyle.up = new TextureRegionDrawable(atlas.findRegion("exit1"));
		buttonStyle.down = new TextureRegionDrawable(atlas.findRegion("exit2"));
		buttonExit = new ImageButton(buttonStyle);

		CheckBoxStyle boxStyle = new CheckBoxStyle();
		boxStyle.checkboxOff = new TextureRegionDrawable(
				atlas.findRegion("sound_on"));
		boxStyle.checkboxOn = new TextureRegionDrawable(
				atlas.findRegion("sound_off"));
		boxStyle.font = GameResourceManager.getInstance().getBitmapFont();
		buttonSetting = new CheckBox("", boxStyle);

		boxStyle = new CheckBoxStyle();
		boxStyle.checkboxOff = new TextureRegionDrawable(
				atlas.findRegion("menu_resume"));
		boxStyle.checkboxOn = new TextureRegionDrawable(
				atlas.findRegion("menu_resume_disabled"));
		boxStyle.font = GameResourceManager.getInstance().getBitmapFont();
		buttonContinue = new CheckBox("", boxStyle);

		LabelStyle style = new LabelStyle();
		style.font = GameResourceManager.getInstance().getBitmapFont();

		addToActor();

		if (DataValues.getInstance().isSave()) {
			buttonContinue.setChecked(false);
			buttonContinue.setTouchable(Touchable.enabled);
		} else {
			buttonContinue.setChecked(true);
			buttonContinue.setTouchable(Touchable.disabled);
		}

		buttonContinue.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (buttonContinue.isChecked()) {
					SoundManager.playButtonSelect();
					MainGame.instance.setScreen(new Scene(true));
				} else {
					stage.addActor(new Dialog("没有查询您有保存的数据", null));
				}
			}
		});

		buttonBegionGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				SoundManager.playButtonSelect();
				MainGame.instance.setScreen(new Scene(false));
			}
		});

		if (DataValues.getInstance().isMusicOn() == true) {
			buttonSetting.setChecked(false);
		} else {
			buttonSetting.setChecked(true);
		}

		buttonSetting.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (buttonSetting.isChecked()) {
					DataValues.getInstance().setMusicOn(false);
					DataValues.getInstance().setSoundOn(false);
					SoundManager.stopMusic();
					stage.addActor(new Toast("关闭声音"));
				} else {
					DataValues.getInstance().setMusicOn(true);
					DataValues.getInstance().setSoundOn(true);
					stage.addActor(new Toast("开启声音"));
				}
			}
		});

		buttonExit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				SoundManager.playButtonSelect();
				stage.addActor(new Dialog("是否要退出游戏？", new DialogHanlder() {

					@Override
					public void buttonOK() {
						Gdx.app.exit();
					}

					@Override
					public void buttonBack() {

					}
				}));
			}
		});

	}

	private void addToActor() {
		buttonBegionGame.setPosition(97, 400);
		buttonContinue.setPosition(97, 300);
		buttonSetting.setPosition(97, 200);
		imageTitle.setPosition(width / 2 - imageTitle.getWidth() / 2, 730);
		buttonExit.setPosition(10, 720);
		stage.addActor(imageBackground);
		// stage.addActor(imageTitle);
		stage.addActor(buttonBegionGame);
		stage.addActor(buttonSetting);
		stage.addActor(buttonExit);
		stage.addActor(buttonContinue);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float deltaTime) {
		stage.draw();
		stage.act();

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
