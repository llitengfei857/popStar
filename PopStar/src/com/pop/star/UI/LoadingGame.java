package com.pop.star.UI;

import com.badlogic.gdx.Screen;
import com.pop.star.MainGame;
import com.pop.star.resource.GameResourceManager;

/**
 * 登录
 * 
 * @author 李腾飞
 * 
 */
public class LoadingGame implements Screen {

	@Override
	public void render(float deltaTime) {
		if (GameResourceManager.getInstance().updateAssetManager()) {
			MainGame.instance.setScreen(new MenuScreen());
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

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
