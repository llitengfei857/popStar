package com.pop.star;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Logger;
import com.pop.star.UI.LoadingGame;
import com.pop.star.resource.GameResourceManager;
import com.pop.star.resource.SoundManager;

public class MainGame extends Game {

	public static MainGame instance;

	private int width, height;

	public MainGame() {
		super();
	}

	@Override
	public void create() {
		instance = this;
		width = 480;
		height = 800;
		GameResourceManager.getInstance().loadResource();
		Gdx.app.setLogLevel(Logger.DEBUG);
		setScreen(new LoadingGame());
		DataValues.getInstance().setMusicOn(true);
		DataValues.getInstance().setSoundOn(true);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		GameResourceManager.getInstance().unloadResource();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void resume() {
		super.resume();
		Gdx.app.log("游戏", "返回游戏");
	}

	@Override
	public void pause() {
		super.pause();
		Gdx.app.log("游戏", "暂停游戏");
		SoundManager.stopMusic();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
