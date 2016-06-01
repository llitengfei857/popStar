package com.pop.star.resource;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public abstract class ResourceManager {

	private AssetManager assetManager;

	public ResourceManager() {
		assetManager = new AssetManager();
	}

	public final AssetManager getAssetManager() {
		return assetManager;
	}

	public final boolean updateAssetManager() {
		return assetManager.update();
	}

	public final float getProgress() {
		return assetManager.getProgress();
	}

	public final Texture getTexture(String path) {
		return assetManager.get(path, Texture.class);
	}

	public final BitmapFont getBitMapFont(String path) {
		return assetManager.get(path, BitmapFont.class);
	}

	public final TextureAtlas getTextureAltas(String path) {
		return assetManager.get(path, TextureAtlas.class);
	}

	public final Sound getSound(String path) {
		return assetManager.get(path, Sound.class);
	}

	public final Music getMusic(String path) {
		return assetManager.get(path, Music.class);
	}

	public final ParticleEffect getParticleEffect(String path) {
		return assetManager.get(path, ParticleEffect.class);
	}

	public final void unloadResource() {
		assetManager.clear();
		assetManager.dispose();
	}

	public abstract void loadResource();

	protected final <T> void load(String fileName, Class<T> type) {
		assetManager.load(fileName, type);
	}

}
