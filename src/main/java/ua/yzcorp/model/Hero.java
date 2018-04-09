package ua.yzcorp.model;

import ua.yzcorp.controller.DefaultStat;
import ua.yzcorp.controller.Glob;

import java.util.Map;

public class Hero {
	private String		name;
	private String		classHero;
	private int			level;
	private int			exp;
	private int			attack;
	private int			def;
	private int			HP;
	private int			CC;
	private int			mustLevel[];
	private int			mustHP;
	public static int[]	heroPos = new int[2];
	private Artifacts	artifacts;

	public Hero(String name, String classHero, int level, int exp, int attack, int def, int HP, int CC, Artifacts artifacts) {
		this.name = name;
		this.classHero = classHero;
		this.level = level;
		this.exp = exp;
		this.attack = attack;
		this.def = def;
		this.HP = HP;
		this.CC	= CC;
		this.artifacts = artifacts == null ? new Artifacts() : artifacts;
		this.mustHP = HP;
		if (level == 1) {
			this.mustLevel = new int[] {0,
					((int) (level * 1000 + Math.pow(level - 1, 2) * 450))};
		} else {
			this.mustLevel = new int[] {((int) ((level - 1) * 1000 + Math.pow((level - 1) - 1, 2) * 450)),
					((int) (level * 1000 + Math.pow(level - 1, 2) * 450))};
		}
	}

	public void updateAllStat() {
		if (this.artifacts != null) {
			for (Map.Entry<String, Item> entry: this.artifacts.getArtifacts().entrySet()) {
				updateStat(entry.getKey().toLowerCase(), entry.getValue());
			}
		}
	}

	public int[] getMustLevel() {
		return mustLevel;
	}

	public int getMustHP() {
		return mustHP;
	}

	public void updateStat(String key, Item item) {
		DefaultStat defaultStat = new DefaultStat();
		double percent = (double) item.getLevel() / 10;
		double tmp;
		int	upper = level != 1 ? (level - 1) * 10 : 0;
		switch (key.toLowerCase()) {
			case "weapon":
				tmp = Math.round((upper + (double)defaultStat.getATK(classHero)) * percent);
				setAttack((int) ((upper + defaultStat.getATK(classHero)) + tmp));
				break;
			case "armor":
				tmp = Math.round((upper + (double) defaultStat.getDEF(classHero)) * percent);
				setDef((int) ((upper + defaultStat.getDEF(classHero)) + tmp));
				break;
			case "helm":
				tmp = (double) this.HP * percent;
				setHP((int) (this.HP + tmp));
				break;
		}
	}

	public int getCC() {
		return CC;
	}

	public void setCC(int CC) {
		this.CC = CC;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public void setHP(int HP) {
		this.HP = HP;
	}

	public void setArtifacts(Artifacts artifacts) {
		this.artifacts = artifacts;
	}

	public String getName() {
		return name;
	}

	public String getClassHero() {
		return classHero;
	}

	public int getLevel() {
		return level;
	}

	public int getExp() {
		return exp;
	}

	public int getAttack() {
		return attack;
	}

	public int getDef() {
		return def;
	}

	public int getHP() {
		return HP;
	}

	public Artifacts getArtifacts() {
		return artifacts;
	}

	public static class HeroBuilder {
		private String		name;
		private String		classHero;
		private int			level;
		private int			exp;
		private int			attack;
		private int			def;
		private int			HP;
		private int			CC;
		private Artifacts	artifacts;

		public HeroBuilder() {
		}

		public HeroBuilder setName(String name) {
			this.name = name;
			return this;
		}

		public HeroBuilder setClassHero(String classHero) {
			this.classHero = classHero;
			return this;
		}

		public HeroBuilder setLevel(int level) {
			this.level = level;
			return this;
		}

		public HeroBuilder setExp(int exp) {
			this.exp = exp;
			return this;
		}

		public HeroBuilder setAttack(int attack) {
			this.attack = attack;
			return this;
		}

		public HeroBuilder setDef(int def) {
			this.def = def;
			return this;
		}

		public HeroBuilder setHP(int HP) {
			this.HP = HP;
			return this;
		}

		public HeroBuilder setCC(int CC) {
			this.CC = CC;
			return this;
		}

		public HeroBuilder setArtifacts(Artifacts artifacts) {
			this.artifacts = artifacts;
			return this;
		}
		public Hero build() {
			return new Hero(name, classHero, level, exp, attack, def, HP, CC, artifacts);
		}
	}

	@Override
	public String toString() {
		return Glob.GR_BOLD + "Your Hero" + Glob.RESET + "\n" +
				"Nick:\t" + name + "\n" +
				"Class:\t" + classHero + "\n" +
				"Level:\t" + level + "\n" +
				"Exp:\t" + exp + "\n" +
				"Attack:\t" + attack + "\n" +
				"Def:\t" + def + "\n" +
				"HP:\t" + HP + "\n" +
				"CC:\t" + CC + "%\n" +
				Glob.GR_BOLD + "Equip:" + Glob.RESET + "\n" + artifacts;
	}
}

