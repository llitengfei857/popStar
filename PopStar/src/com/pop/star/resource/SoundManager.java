package com.pop.star.resource;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.pop.star.DataValues;

public class SoundManager {

	private static Music music;

	public static void playMusicBackGround() {
		music = playMusic("sound/backGround.mp3", true);
	}

	public static void stopMusic() {
		if (music != null && music.isPlaying()) {
			music.stop();
		}
	}

	public static void playSoundDestroyStar() {
		playSound("sound/destroyStar.mp3");
	}

	public static void playSoundLose() {
		playSound("sound/lose.mp3");
	}

	public static void playSoundWin() {
		playSound("sound/win.mp3");
	}

	public static void playButtonSelect() {
		playSound("sound/select.mp3");
	}

	public static void playBomb() {
		playSound("sound/bomb.mp3");
	}

	private static void playSound(String path) {
		if (DataValues.getInstance().isSoundOn()) {
			Sound sound = GameResourceManager.getInstance().getSound(path);
			sound.play();
		}
	}

	private static Music playMusic(String path, boolean isLooping) {
		if (DataValues.getInstance().isMusicOn()
				&& DataValues.getInstance().isSoundOn()) {
			if (music != null && music.isPlaying()) {
				music.stop();
			}
			Music music = GameResourceManager.getInstance().getMusic(path);
			if (!music.isPlaying()) {
				music.setLooping(isLooping);
				music.play();
				return music;
			}
		}
		return null;
	}

	public static Music getMusic() {
		return music;
	}

}
