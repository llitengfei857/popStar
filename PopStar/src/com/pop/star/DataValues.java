package com.pop.star;

public class DataValues {

	private static final DataValues instance = new DataValues();

	public static DataValues getInstance() {
		return instance;
	}

	/**
	 * 计算的所有总和
	 */
	private int allScore;

	/**
	 * 每关的关卡值
	 */
	private int levelScore[];

	/**
	 * 当前关卡
	 */
	private int curLevel;

	private int hammerNumber;

	/**
	 * 是否有继续游戏
	 */
	private boolean isSave;

	private boolean soundOn;

	private boolean musicOn;

	private DataValues() {
		levelScore = new int[] { 0, 1300, 2600, 3900, 5200, 6500, 7800, 9100,
				10400, 11700, 13000, 14300, 15600, 16900, 18200, 19500, 20800,
				22100, 23400, 24700, 26000 };
	}

	public int getAllScore() {
		return allScore;
	}

	public void setAllScore(int allScore) {
		this.allScore = allScore;
	}

	public int getLevelScore(int index) {
		return levelScore[index];
	}

	public void setLevelScore(int levelScore, int index) {
		this.levelScore[index] = levelScore;
	}

	public int getCurLevel() {
		return curLevel;
	}

	public void setCurLevel(int curLevel) {
		this.curLevel = curLevel;
	}

	/**
	 * 计算相同颜色块的分数
	 * 
	 * @param index
	 */
	public void calculationClearStar(int index) {
		if (index == 1) {
			return;
		}
		int score = index * (index * 5);
		setAllScore(score + getAllScore());
	}

	public boolean isSave() {
		return isSave;
	}

	public void setSave(boolean isSave) {
		this.isSave = isSave;
	}

	public boolean isSoundOn() {
		return soundOn;
	}

	public void setSoundOn(boolean soundOn) {
		this.soundOn = soundOn;
	}

	public boolean isMusicOn() {
		return musicOn;
	}

	public void setMusicOn(boolean musicOn) {
		this.musicOn = musicOn;
	}

	public int getHammerNumber() {
		return hammerNumber;
	}

	public void setHammerNumber(int hammerNumber) {
		this.hammerNumber = hammerNumber;
	}

}
