package com.pop.star;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.pop.star.UI.Dialog;
import com.pop.star.UI.MenuScreen;
import com.pop.star.UI.Toast;
import com.pop.star.logic.BombStar;
import com.pop.star.logic.ConnectStar;
import com.pop.star.logic.DestroyLogic;
import com.pop.star.logic.NormalStar;
import com.pop.star.logic.Star;
import com.pop.star.resource.GameResourceManager;
import com.pop.star.resource.SoundManager;

/**
 * 游戏主类
 * 
 * @author 李腾飞
 * 
 */
public class Scene implements Screen, InputProcessor {

	public static final int BOMB_STAR = 6;

	public static final int CONNECT_STAR = 7;

	private int screenWidth, screenHeight;

	private Stage stageUILayer;

	private Stage stageMapLayer;

	private Stage stageEffectLayer;

	private TextureAtlas atlas;

	private InputMultiplexer multiplexer;

	private Image imageBackground;

	private ImageButtonStyle buttonStyle;

	private ImageButton buttonExit;

	private Image imagePropsHammer;

	private Label labelScore, labelLevelScore, labelHammer, labelLevel;

	private Star[][] starList = new Star[10][10];

	private ParticleEffect[] effectStar;

	/**
	 * 保存要消除的方块（点击每一个星星的时候）
	 */
	private ArrayList<Star> saveCheckStar = new ArrayList<Star>();

	private ArrayList<Star> saveBombStar = new ArrayList<Star>();

	private DestroyLogic destroyLogic;

	/**
	 * 当前消除了几个相同的颜色的块
	 */
	private int starSame;

	/**
	 * 最后剩余的块
	 */
	private int lastRemainingBlocks;

	/**
	 * 当前关卡结束
	 */
	private boolean hasEndGame;

	// 是否动画延迟1秒才能点击
	private boolean hasCheckInputHandler;

	private boolean hasCheckHammer;

	public Scene(boolean isContinue) {
		screenWidth = MainGame.instance.getWidth();
		screenHeight = MainGame.instance.getHeight();
		stageUILayer = new Stage(new FillViewport(screenWidth, screenHeight));
		stageMapLayer = new Stage(new FillViewport(screenWidth, screenHeight));
		stageEffectLayer = new Stage(
				new FillViewport(screenWidth, screenHeight));
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(this);
		multiplexer.addProcessor(stageMapLayer);
		multiplexer.addProcessor(stageUILayer);
		multiplexer.addProcessor(stageEffectLayer);
		Gdx.input.setInputProcessor(multiplexer);
		hasCheckInputHandler = false;
		destroyLogic = new DestroyLogic(this);
		initResource();
		if (isContinue == true) {
			// 继续游戏
			// DataValues.getInstance().setAllScore(0);
		} else {
			// 新开始
			DataValues.getInstance().setCurLevel(1);
			DataValues.getInstance().setAllScore(0);
			DataValues.getInstance().setSave(false);
		}
		initGameResource();
		initListener();
	}

	public void nextLevel() {
		DataValues.getInstance().setSave(true);
		DataValues dv = DataValues.getInstance();
		dv.setCurLevel(dv.getCurLevel() + 1);
		initGameResource();
		setLastRemainingBlocks(0);
	}

	private void initGameResource() {
		// 动画延迟1秒才能点击
		final Timer t = new Timer();
		t.scheduleTask(new Task() {

			@Override
			public void run() {
				hasCheckInputHandler = true;
				t.clear();
			}
		}, 1f);

		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < 10; i++) {
				int color = MathUtils.random(1, 5);
				Star star = new NormalStar(this, i * 48, 800, 48, 48, i, j,
						color);
				// (东西下落性)bounceOut (有回弹性)swingOut (有回弹性2)elasticOut
				star.addAction(Actions.moveTo(i * 48, j * 48, 1,
						Interpolation.bounceOut));
				star.removeAction(new MoveToAction());
				starList[i][j] = star;
				starList[i][j].setName("color:" + color + "i:" + i + "j:" + j);
				stageMapLayer.addActor((NormalStar) starList[i][j]);
			}
		}

		int boobStar = MathUtils.random(1, 2);
		for (int b = 0; b < boobStar; b++) {
			boolean created = false;
			while (!created) {
				int iStar = MathUtils.random(0, 9);
				int jStar = MathUtils.random(0, 9);
				if (!(starList[iStar][jStar] instanceof BombStar)) {
					stageMapLayer.getRoot().removeActor(starList[iStar][jStar]);
					starList[iStar][jStar] = null;
					// 加载bomb
					Star bombStar = new BombStar(this, iStar * 48, 800, 48, 48,
							iStar, jStar, BOMB_STAR);
					starList[iStar][jStar] = bombStar;
					bombStar.addAction(Actions.moveTo(iStar * 48, jStar * 48,
							1, Interpolation.bounceOut));
					bombStar.removeAction(new MoveToAction());
					stageMapLayer.getRoot().addActor(bombStar);
					created = true;
				}
			}

		}

		int connect = MathUtils.random(1, 2);
		for (int i = 0; i < connect; i++) {

			boolean created = false;
			while (!created) {
				int iStar = MathUtils.random(0, 9);
				int jStar = MathUtils.random(0, 9);
				if (!(starList[iStar][jStar] instanceof ConnectStar)) {
					stageMapLayer.getRoot().removeActor(starList[iStar][jStar]);
					starList[iStar][jStar] = null;
					// 加载bomb
					Star connectStar = new ConnectStar(this, iStar * 48, 800,
							48, 48, iStar, jStar, CONNECT_STAR);
					starList[iStar][jStar] = connectStar;
					connectStar.addAction(Actions.moveTo(iStar * 48,
							jStar * 48, 1, Interpolation.bounceOut));
					connectStar.removeAction(new MoveToAction());
					stageMapLayer.getRoot().addActor(connectStar);
					created = true;
				}
			}

		}

		setHasCheckHammer(false);
		setHasEndGame(false);
	}

	private void initListener() {
		imagePropsHammer.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (DataValues.getInstance().getHammerNumber() > 0) {
					setHasCheckHammer(true);
					stageEffectLayer.addActor(new Toast("请要点击要消除的块",
							(int) (imagePropsHammer.getY() - 20)));
				}
			}
		});

		buttonExit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				SoundManager.playButtonSelect();
				multiplexer.removeProcessor(Scene.this);
				stageMapLayer.addActor(new Dialog("是否要退出游戏",
						new DialogHanlder() {

							@Override
							public void buttonOK() {
								DataValues.getInstance().setCurLevel(
										DataValues.getInstance().getCurLevel());
								SoundManager.stopMusic();
								MainGame.instance.setScreen(new MenuScreen());
							}

							@Override
							public void buttonBack() {
								multiplexer.addProcessor(Scene.this);
							}
						}));

			}
		});
	}

	private void initResource() {
		atlas = GameResourceManager.getInstance().getTextureAltas("ui.pack");
		for (Texture t : atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		imageBackground = new Image(atlas.findRegion("back"));
		imagePropsHammer = new Image(atlas.findRegion("hammer"));

		buttonStyle = new ImageButtonStyle();
		buttonStyle.up = new TextureRegionDrawable(atlas.findRegion("exit1"));
		buttonStyle.down = new TextureRegionDrawable(atlas.findRegion("exit2"));
		buttonExit = new ImageButton(buttonStyle);

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = GameResourceManager.getInstance().getBitmapFont();
		labelScore = new Label("", labelStyle);
		labelLevelScore = new Label("", labelStyle);
		labelHammer = new Label("", labelStyle);
		labelLevel = new Label("", labelStyle);
		effectStar = new ParticleEffect[5];
		effectStar[0] = GameResourceManager.getInstance().getParticleEffect(
				"particle/star/blue.p");
		effectStar[1] = GameResourceManager.getInstance().getParticleEffect(
				"particle/star/yellow.p");
		effectStar[2] = GameResourceManager.getInstance().getParticleEffect(
				"particle/star/purple.p");
		effectStar[3] = GameResourceManager.getInstance().getParticleEffect(
				"particle/star/red.p");
		effectStar[4] = GameResourceManager.getInstance().getParticleEffect(
				"particle/star/green.p");
		buttonExit.setPosition(10, 720);
		labelScore.setPosition(80, 660);
		labelLevelScore.setPosition(250, 660);
		imagePropsHammer.setPosition(250, 560);
		labelHammer.setPosition(300, 580);
		labelLevel.setPosition(80, 580);

		stageUILayer.addActor(imageBackground);
		stageUILayer.addActor(buttonExit);
		stageUILayer.addActor(labelLevelScore);
		stageUILayer.addActor(labelScore);
		stageUILayer.addActor(imagePropsHammer);
		stageUILayer.addActor(labelHammer);
		stageUILayer.addActor(labelLevel);

		SoundManager.playMusicBackGround();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		stageUILayer.act();
		stageUILayer.draw();
		stageMapLayer.act();
		stageMapLayer.draw();
		stageEffectLayer.act();
		stageEffectLayer.draw();

		labelScore.setText("分数:" + DataValues.getInstance().getAllScore());
		labelLevelScore.setText("目标分数:"
				+ DataValues.getInstance().getLevelScore(
						DataValues.getInstance().getCurLevel()));
		labelHammer
				.setText("小星星:" + DataValues.getInstance().getHammerNumber());
		labelLevel.setText("关卡:" + DataValues.getInstance().getCurLevel());
		// 粒子
		stageEffectLayer.getBatch().begin();
		for (int i = 0; i < effectStar.length; i++) {
			effectStar[i].draw(stageEffectLayer.getBatch());
			effectStar[i].update(Gdx.graphics.getDeltaTime());
		}
		stageEffectLayer.getBatch().end();

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		Gdx.app.log("exit Game", "clear all resource");
		stageMapLayer.dispose();
		stageUILayer.dispose();
		multiplexer.clear();

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector2 oldPostion = new Vector2(screenX, screenY);
		Vector2 newPostion = stageMapLayer.screenToStageCoordinates(oldPostion);
		if (newPostion.x >= 0 && newPostion.x <= 480 && newPostion.y >= 0
				&& newPostion.y <= 480
				&& (!isHasEndGame() && (hasCheckInputHandler == true))) {
			int iStar = (int) newPostion.x / 48;
			int jStar = (int) newPostion.y / 48;
			if (starList[iStar][jStar] != null) {
				if (starList[iStar][jStar] instanceof NormalStar) {
					((NormalStar) starList[iStar][jStar]).touch();
				} else if (starList[iStar][jStar] instanceof BombStar) {
					((BombStar) starList[iStar][jStar]).touch();
				}
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public Star[][] getStarList() {
		return starList;
	}

	public ArrayList<Star> getSaveCheckStar() {
		return saveCheckStar;
	}

	public ArrayList<Star> getSaveBombStar() {
		return saveBombStar;
	}

	public int getStarSame() {
		return starSame;
	}

	public void setStarSame(int starSame) {
		this.starSame = starSame;
	}

	public Stage getStage() {
		return stageMapLayer;
	}

	public DestroyLogic getDestroyLogic() {
		return destroyLogic;
	}

	public boolean isHasEndGame() {
		return hasEndGame;
	}

	public void setHasEndGame(boolean hasEndGame) {
		this.hasEndGame = hasEndGame;
	}

	public ParticleEffect getEffectStar(int index) {
		return effectStar[index];
	}

	public int getLastRemainingBlocks() {
		return lastRemainingBlocks;
	}

	public boolean isHasCheckHammer() {
		return hasCheckHammer;
	}

	public void setHasCheckHammer(boolean hasCheckHammer) {
		this.hasCheckHammer = hasCheckHammer;
	}

	/**
	 * 记住当前的关卡的剩余块
	 * 
	 * @param lastRemainingBlocks
	 */
	public void setLastRemainingBlocks(int lastRemainingBlocks) {
		this.lastRemainingBlocks = lastRemainingBlocks;
	}

}
