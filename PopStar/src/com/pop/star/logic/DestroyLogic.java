package com.pop.star.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.pop.star.DataValues;
import com.pop.star.Scene;
import com.pop.star.resource.SoundManager;

/**
 * 消除逻辑
 * 
 * @author 李腾飞
 * 
 */
public class DestroyLogic {

	private Scene scene;

	public DestroyLogic(Scene scene) {
		this.scene = scene;
	}

	/**
	 * 检测是否有相同
	 * 
	 * @param iStar
	 * @param jStar
	 * @param curColor
	 */
	public void checkHasTheSame(int iStar, int jStar, int curColor) {
		scene.getSaveCheckStar().clear();
		scene.setStarSame(0);
		checkAllDirection(iStar, jStar, curColor);
	}

	/**
	 * 循环遍历
	 * 
	 * @param iStar
	 * @param jStar
	 * @param curColor
	 */
	private void checkAllDirection(int iStar, int jStar, int curColor) {
		checkSame(iStar + 1, jStar, curColor);
		checkSame(iStar - 1, jStar, curColor);
		checkSame(iStar, jStar - 1, curColor);
		checkSame(iStar, jStar + 1, curColor);
	}

	private void checkSame(int i, int j, int color) {
		// 检测上下左右是否会超出10
		if (i < 0 || i > 9 || j < 0 || j > 9
				|| scene.getStarList()[i][j] == null) {
			return;
		}
		// 如果有相同则不保存
		if (scene.getSaveCheckStar().contains(scene.getStarList()[i][j])) {
			return;
		}
		int needCheckColor = scene.getStarList()[i][j].getStarColor();
		if (needCheckColor == color || needCheckColor == Scene.CONNECT_STAR) {
			scene.setStarSame(scene.getStarSame() + 1);
			// 如果是普通的方块
			// if (scene.getStarList()[i][j] instanceof NormalStar) {
			scene.getSaveCheckStar().add(scene.getStarList()[i][j]);
			// }
			checkAllDirection(i, j, color);
		}

	}

	/**
	 * 删除星星并排序
	 * 
	 * @param iStar
	 * @param jStar
	 * @param curColor
	 */
	public void sortStar(int iStar, int jStar, int curColor) {
		DataValues.getInstance().calculationClearStar(scene.getStarSame());
		int[][] sort = new int[scene.getSaveCheckStar().size()][2];
		for (int i = 0; i < scene.getSaveCheckStar().size(); i++) {
			sort[i][0] = (int) scene.getSaveCheckStar().get(i).getI();
			sort[i][1] = (int) scene.getSaveCheckStar().get(i).getJ();
		}
		sortIntArray(sort, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 });
		for (int i = 0; i < sort.length; i++) {
			destroyStar(sort[i][0], sort[i][1]);
		}
	}

	/**
	 * 是否有剩余的块
	 * 
	 * @return
	 */
	public boolean isAllSameBlocksClear() {
		for (int iStar = 0; iStar < scene.getStarList().length; iStar++) {
			for (int jStar = 0; jStar < scene.getStarList()[iStar].length; jStar++) {
				if ((iStar < 9 && jStar < 9)
						&& scene.getStarList()[iStar][jStar] != null
						&& scene.getStarList()[iStar + 1][jStar] != null) {
					int preStarColor = scene.getStarList()[iStar][jStar]
							.getStarColor();
					int nextStarColor = scene.getStarList()[iStar + 1][jStar]
							.getStarColor();
					if (preStarColor == Scene.BOMB_STAR
							|| nextStarColor == Scene.BOMB_STAR) {
						return false;
					}
					if (preStarColor == nextStarColor) {
						return false;
					}

				}

				if ((iStar < 9 && jStar < 9)
						&& scene.getStarList()[iStar][jStar] != null
						&& scene.getStarList()[iStar][jStar + 1] != null) {
					int preStarColor = scene.getStarList()[iStar][jStar]
							.getStarColor();
					int nextStarColor = scene.getStarList()[iStar][jStar + 1]
							.getStarColor();
					if (preStarColor == Scene.BOMB_STAR
							|| nextStarColor == Scene.BOMB_STAR) {
						return false;
					}
					if (preStarColor == nextStarColor) {
						return false;
					}

				}
			}

		}

		return true;
	}

	/**
	 * 计算剩余块
	 */
	public ArrayList<Star> calculateRemainingBlocks() {
		ArrayList<Star> list = new ArrayList<Star>();
		list.clear();
		for (int j = 9; j >= 0; j--) {
			for (int i = 9; i >= 0; i--) {
				if (scene.getStarList()[i][j] != null) {
					list.add(scene.getStarList()[i][j]);
				}
			}
		}

		scene.setLastRemainingBlocks(list.size());
		return list;
	}

	/**
	 * 剩余的每个要做消除
	 * 
	 * @param handler
	 */
	public void destroyRemainingBlocks(final DestroyHandler handler) {
		final Timer timer = new Timer();
		timer.scheduleTask(new Task() {
			ArrayList<Star> list = calculateRemainingBlocks();
			Iterator<Star> ite = list.iterator();

			@Override
			public void run() {
				if (!ite.hasNext()) {
					handler.handler();
					timer.clear();
				} else {
					Star star = ite.next();
					if (star.getStarColor() <= 5) {
						scene.getEffectStar(star.getStarColor() - 1)
								.setPosition(star.getX(), star.getY());
						scene.getEffectStar(star.getStarColor() - 1).start();
					}
					scene.getStage().getRoot().removeActor(star);
					scene.getStarList()[star.getI()][star.getJ()] = null;
					ite.remove();
					SoundManager.playSoundDestroyStar();

				}
			}
		}, 0.09f, 0.3f);

	}

	public interface DestroyHandler {
		public void handler();
	}

	/**
	 * 消除一个星星
	 * 
	 * @param iStar
	 * @param jStar
	 */
	public void destroyOneStar(int iStar, int jStar) {
		destroyStar(iStar, jStar);
	}

	private void destroyStar(int iStar, int jStar) {
		if (scene.getStarList()[iStar][jStar] != null) {
			scene.getStage().getRoot()
					.removeActor((scene.getStarList()[iStar][jStar]));
			scene.getStarList()[iStar][jStar] = null;
			for (int j = jStar; j < 9; j++) {
				scene.getStarList()[iStar][j] = scene.getStarList()[iStar][j + 1];
				if (scene.getStarList()[iStar][j] != null) {
					(scene.getStarList()[iStar][j + 1]).addAction(Actions
							.moveBy(0, -48, 1, Interpolation.bounceOut));
					(scene.getStarList()[iStar][j]).setJ(j);
					(scene.getStarList()[iStar][j])
							.removeAction(new MoveByAction());
					scene.getStarList()[iStar][j + 1] = null;
				} else {
					scene.getStarList()[iStar][(j + 1)] = null;
				}
			}
		}
		// 游戏改横坐标
		if ((jStar == 0) && (scene.getStarList()[iStar][0] == null)
				&& (scene.getStarList()[iStar][1] == null)
				&& (scene.getStarList()[iStar][2] == null)
				&& (scene.getStarList()[iStar][3] == null)
				&& (scene.getStarList()[iStar][4] == null)
				&& (scene.getStarList()[iStar][5] == null)
				&& (scene.getStarList()[iStar][6] == null)
				&& (scene.getStarList()[iStar][7] == null)
				&& (scene.getStarList()[iStar][8] == null)
				&& (scene.getStarList()[iStar][9] == null)) {
			for (int iRow = iStar; iRow < 9; iRow++) {
				for (int jRow = 0; jRow < 10; jRow++) {
					scene.getStarList()[iRow][jRow] = scene.getStarList()[iRow + 1][jRow];
					if (scene.getStarList()[iRow][jRow] != null) {
						(scene.getStarList()[iRow + 1][jRow]).addAction(Actions
								.moveBy(-48, 0, 1, Interpolation.bounceOut));
						(scene.getStarList()[iRow][jRow]).setI(iRow);
						(scene.getStarList()[iRow][jRow])
								.removeAction(new MoveByAction());
						scene.getStarList()[iRow + 1][jRow] = null;
					} else {
						scene.getStarList()[iRow + 1][jRow] = null;
					}
				}
			}
		}

	}

	// 备用
	// private void destroyStar(int iStar, int jStar) {
	// if (scene.getStarList()[iStar][jStar] != null) {
	// scene.getStage()
	// .getRoot()
	// .removeActor(
	// ((NormalStar) scene.getStarList()[iStar][jStar]));
	// scene.getStarList()[iStar][jStar] = null;
	// for (int j = jStar; j < 9; j++) {
	// scene.getStarList()[iStar][j] = scene.getStarList()[iStar][j + 1];
	// if (scene.getStarList()[iStar][j] != null) {
	// ((NormalStar) scene.getStarList()[iStar][j + 1])
	// .addAction(Actions.moveBy(0, -48, 1,
	// Interpolation.bounceOut));
	// ((NormalStar) scene.getStarList()[iStar][j]).setJ(j);
	// ((NormalStar) scene.getStarList()[iStar][j])
	// .removeAction(new MoveByAction());
	// scene.getStarList()[iStar][j + 1] = null;
	// } else {
	// scene.getStarList()[iStar][(j + 1)] = null;
	// }
	// }
	// }
	// // 游戏改横坐标
	// if ((jStar == 0) && (scene.getStarList()[iStar][0] == null)
	// && (scene.getStarList()[iStar][1] == null)
	// && (scene.getStarList()[iStar][2] == null)
	// && (scene.getStarList()[iStar][3] == null)
	// && (scene.getStarList()[iStar][4] == null)
	// && (scene.getStarList()[iStar][5] == null)
	// && (scene.getStarList()[iStar][6] == null)
	// && (scene.getStarList()[iStar][7] == null)
	// && (scene.getStarList()[iStar][8] == null)
	// && (scene.getStarList()[iStar][9] == null)) {
	// for (int iRow = iStar; iRow < 9; iRow++) {
	// for (int jRow = 0; jRow < 10; jRow++) {
	// scene.getStarList()[iRow][jRow] = scene.getStarList()[iRow + 1][jRow];
	// if (scene.getStarList()[iRow][jRow] != null) {
	// ((NormalStar) scene.getStarList()[iRow + 1][jRow])
	// .addAction(Actions.moveBy(-48, 0, 1,
	// Interpolation.bounceOut));
	// ((NormalStar) scene.getStarList()[iRow][jRow])
	// .setI(iRow);
	// ((NormalStar) scene.getStarList()[iRow][jRow])
	// .removeAction(new MoveByAction());
	// scene.getStarList()[iRow + 1][jRow] = null;
	// } else {
	// scene.getStarList()[iRow + 1][jRow] = null;
	// }
	// }
	// }
	// }
	//
	// }

	/**
	 * 排序的方法
	 * 
	 * @param arObjects
	 * @param arOrders
	 */
	private void sortIntArray(int[][] arObjects, final int[] arOrders) {
		Arrays.sort(arObjects, new Comparator<Object>() {
			public int compare(Object oObjectA, Object oObjectB) {
				int[] arTempOne = (int[]) oObjectA;
				int[] arTempTwo = (int[]) oObjectB;
				for (int i = 0; i < arOrders.length; i++) {
					int k = arOrders[i];
					if (arTempOne[k] > arTempTwo[k]) {
						return -1;
					} else if (arTempOne[k] < arTempTwo[k]) {
						return 1;
					} else {
						continue;
					}
				}
				return 0;
			}
		});
	}

	// -----------------------检测是否是炸弹-----------------------
	/**
	 * 检测炸弹范围
	 */
	public void checkBoobSame(int iStar, int jStar) {
		scene.getSaveBombStar().clear();
		scene.setStarSame(0);
		int[][] indexBomb = new int[][] { { iStar + 1, jStar - 1 },
				{ iStar + 1, jStar }, { iStar + 1, jStar + 1 },
				{ iStar, jStar - 1 }, { iStar, jStar }, { iStar, jStar + 1 },
				{ iStar - 1, jStar - 1 }, { iStar - 1, jStar },
				{ iStar - 1, jStar + 1 } };

		for (int i = 0; i < indexBomb.length; i++) {
			if ((indexBomb[i][0]) >= 0 && (indexBomb[i][0]) <= 9
					&& (indexBomb[i][1]) >= 0 && (indexBomb[i][1]) <= 9) {
				if (scene.getStarList()[indexBomb[i][0]][indexBomb[i][1]] != null
						&& !scene
								.getSaveBombStar()
								.contains(
										scene.getStarList()[indexBomb[i][0]][indexBomb[i][1]])) {
					scene.getSaveBombStar()
							.add(scene.getStarList()[indexBomb[i][0]][indexBomb[i][1]]);
				}
			}
		}
		// 检测有多少个相同块
		scene.setStarSame(scene.getSaveBombStar().size());
		sortBombStar();
	}

	private void sortBombStar() {
		DataValues.getInstance().calculationClearStar(scene.getStarSame());
		int[][] sort = new int[scene.getSaveBombStar().size()][2];
		for (int i = 0; i < scene.getSaveBombStar().size(); i++) {
			sort[i][0] = (int) scene.getSaveBombStar().get(i).getI();
			sort[i][1] = (int) scene.getSaveBombStar().get(i).getJ();
		}
		sortIntArray(sort, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 });
		for (int i = 0; i < sort.length; i++) {
			destroyStar(sort[i][0], sort[i][1]);
		}

	}

}
