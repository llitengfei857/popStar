package com.pop.star.logic;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pop.star.Scene;

/**
 * 星星基类
 * 
 * @author 李腾飞
 * 
 */
public abstract class Star extends Actor {

	private int color;

	private int i;

	private int j;

	private Scene scene;

	public Star(Scene scene, int x, int y, int w, int h, int i, int j, int color) {
		setX(x);
		setY(y);
		setWidth(w);
		setHeight(h);
		setI(i);
		setJ(j);
		setStarColor(color);
		setScene(scene);
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public int getStarColor() {
		return color;
	}

	public void setStarColor(int color) {
		this.color = color;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		update();
	}

	public abstract void update();

	public abstract void touch();

}
