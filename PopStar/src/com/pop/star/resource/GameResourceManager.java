package com.pop.star.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameResourceManager extends ResourceManager {

	private static GameResourceManager instance = new GameResourceManager();

	private BitmapFont font;

	private GameResourceManager() {
		super();
	}

	public static GameResourceManager getInstance() {
		return instance;
	}

	@Override
	public void loadResource() {
		load("ui.pack", TextureAtlas.class);
		load("game.pack", TextureAtlas.class);
		load("particle/star/blue.p", ParticleEffect.class);
		load("particle/star/green.p", ParticleEffect.class);
		load("particle/star/purple.p", ParticleEffect.class);
		load("particle/star/red.p", ParticleEffect.class);
		load("particle/star/yellow.p", ParticleEffect.class);

		load("sound/backGround.mp3", Music.class);
		load("sound/menuback.mp3", Music.class);
		load("sound/destroyStar.mp3", Sound.class);
		load("sound/lose.mp3", Sound.class);
		load("sound/win.mp3", Sound.class);
		load("sound/select.mp3", Sound.class);
		load("sound/bomb.mp3", Sound.class);

		// font
		Texture fntTexture = new Texture(Gdx.files.internal("font/font.png"));
		fntTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = new BitmapFont(Gdx.files.internal("font/font.fnt"),
				new TextureRegion(fntTexture), false);
	}

	public BitmapFont getBitmapFont() {
		return font;
	}

}
