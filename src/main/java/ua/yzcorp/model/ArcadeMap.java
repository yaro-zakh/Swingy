package ua.yzcorp.model;

import ua.yzcorp.controller.*;
import static ua.yzcorp.controller.Glob.HERO;
import static ua.yzcorp.controller.Glob.MAPENEMIES;
import static ua.yzcorp.model.Hero.heroPos;

import java.util.*;

public class ArcadeMap {
	private static List<Enemy> defaultEnemies;
	private static List<Artifacts> artifacts;
	private static int size;
	private static String randomDrop;

	public ArcadeMap() {
		EnemyManager enemyManager = new EnemyManager();
		ArtifactsManager artifactsManager = new ArtifactsManager();
		defaultEnemies = enemyManager.getAllTarget();
		artifacts = artifactsManager.getAllTarget();
		updateMainInfo();
	}

	public void updateMainInfo() {
		size = (HERO.getLevel() - 1) * 5 + 10 - (HERO.getLevel() % 2);
		heroPos = new int[]{Math.round(size / 2), Math.round(size / 2)};
		MAPENEMIES = new LinkedList<>();
		createRandomEnemies();
	}

	private void createRandomEnemies() {
		int		howMatch = (int) (Math.pow(size, 2) / 2);
		Enemy	tmpEnemy;
		int		tmpSizeSet;
		Random	random = new Random();
		class	Pos {
			private int[] tmpPos;

			private Pos(int[] tmpPos) {
				this.tmpPos = tmpPos;
			}

			private int[] getTmpPos() {
				return tmpPos;
			}

			@Override
			public boolean equals(Object o) {
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				Pos pos = (Pos) o;
				return Arrays.equals(tmpPos, pos.tmpPos);
			}

			@Override
			public int hashCode() {
				return Arrays.hashCode(tmpPos);
			}

			@Override
			public String toString() {
				return "Pos{" +
						"tmpPos=" + tmpPos[0] + ":" + tmpPos[1] +
						'}';
			}
		}
		Set<Pos>	uniqueCoor = new HashSet<>();
		Pos			tmpPos = null;
		for (int i = 0; i < howMatch; i++) {
			tmpSizeSet = uniqueCoor.size();
			while (tmpSizeSet == uniqueCoor.size()) {
				tmpPos = new Pos(new int[] {random.nextInt(size), random.nextInt(size)});
				if (!Arrays.equals(tmpPos.getTmpPos(), heroPos)) {
					uniqueCoor.add(tmpPos);
				}
			}
			tmpEnemy = Enemy.newInstance(defaultEnemies.get(random.nextInt(defaultEnemies.size())));
			tmpEnemy.setEnemyPos(tmpPos.getTmpPos());
			MAPENEMIES.add(tmpEnemy);
		}
	}

	public List<Enemy> getDefaultEnemies() {
		return defaultEnemies;
	}

	public List<Artifacts> getArtifacts() {
		return artifacts;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		ArcadeMap.size = size;
	}

	public String getRandomDrop() {
		return randomDrop;
	}

	public void setRandomDrop(String randomDrop) {
		ArcadeMap.randomDrop = randomDrop;
	}
}