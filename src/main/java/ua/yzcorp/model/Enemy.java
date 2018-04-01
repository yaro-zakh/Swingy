package ua.yzcorp.model;

import ua.yzcorp.controller.Glob;

import java.io.Serializable;

public class Enemy implements Serializable {
	private String	name;
	private int		attack;
	private int		HP;
	private int		level;
	private char	ascii;
	private int[]	enemyPos = new int[2];

	public Enemy(String name, int attack, int HP, int level, char ascii) {
		this.name = name;
		this.attack = attack;
		this.HP = HP;
		this.level = level;
		this.ascii = ascii;
	}

	public static Enemy newInstance(Enemy copyEnemy) {
		return new Enemy(copyEnemy.getName(),
						copyEnemy.getAttack(),
						copyEnemy.getHP(),
						copyEnemy.getLevel(),
						copyEnemy.getAscii());
	}

	public int[] getEnemyPos() {
		return enemyPos;
	}

	public void setEnemyPos(int[] enemyPos) {
		this.enemyPos[0] = enemyPos[0];
		this.enemyPos[1] = enemyPos[1];
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public void setHP(int HP) {
		this.HP = HP;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setAscii(char ascii) {
		this.ascii = ascii;
	}

	public String getName() {
		return name;
	}
	public int getAttack() {
		return attack;
	}
	public int getHP() {
		return HP;
	}
	public int getLevel() {
		return level;
	}
	public char getAscii() {
		return ascii;
	}
	public static class EnemyBuilder {
		private String	name;
		private int		attack;
		private int		HP;
		private int		level;
		private char	ascii;

		public EnemyBuilder() {
		}

		public EnemyBuilder setName(String name) {
			this.name = name;
			return this;
		}

		public EnemyBuilder setAttack(int attack) {
			this.attack = attack;
			return this;
		}

		public EnemyBuilder setHP(int HP) {
			this.HP = HP;
			return this;
		}

		public EnemyBuilder setLevel(int level) {
			this.level = level;
			return this;
		}

		public EnemyBuilder setAscii(char ascii) {
			this.ascii = ascii;
			return this;
		}

		public Enemy build() {
			return new Enemy(name, attack, HP, level, ascii);
		}
	}

	@Override
	public String toString() {
		return "My name is " + Glob.RED + name + Glob.RESET + ". Tremble in front of me!\n" +
				""+ Glob.GREEN + "Level: " + Glob.RESET + "" + level + "\n" +
				"" + Glob.GREEN + "ATK  : " + Glob.RESET + "" + attack + "\n" +
				"" + Glob.GREEN + "HP   : " + Glob.RESET + "" + HP;
	}
}
