package ua.yzcorp.model;

import ua.yzcorp.controller.*;
import ua.yzcorp.view.Console;
import ua.yzcorp.view.Message;

import java.util.*;

import static ua.yzcorp.controller.Glob.hero;


public class ArcadeMap {
	private List<Enemy> enemies;
	private List<Artifacts> artifacts;
	private char [][] arcadeMap;
	private int	[] heroPos = new int[2];
	private int [] oldPos = new int[2];
	private char asciiHero = '⛹';
	private char asciiEmpty = '⛶';

	public ArcadeMap() {
		EnemyManager enemyManager = new EnemyManager();
		ArtifactsManager artifactsManager = new ArtifactsManager();
		this.enemies = enemyManager.getAllTarget();
		this.artifacts = artifactsManager.getAllTarget();
	}

	public void startGame() {
		Scanner scanner = new Scanner(System.in);
		setNewMap(hero.getLevel());
		printArcadeMap(true);
		while (hero.getHP() > 0 && scanner.hasNextLine()) {
			String move = scanner.nextLine();
			switch (move.toLowerCase()) {
				case "w":
					setHeroPos(new int[] {-1, 0});
					editMap();
					break;
				case "a":
					setHeroPos(new int[] {0, -1});
					editMap();
					break;
				case "s":
					setHeroPos(new int[] {1, 0});
					editMap();
					break;
				case "d":
					setHeroPos(new int[] {0, 1});
					editMap();
					break;
				case "info":
					Message.print(hero.toString());
					break;
				default:
					Message.print(Glob.RED + "Choose the right side" + Glob.RESET);
					break;
			}
		}
		scanner.close();
	}

	private void setNewMap(int level) {
		final Random random = new Random();
		int size = (level - 1) * 5 + 10 - (level % 2);
		heroPos = new int[]{Math.round(size / 2), Math.round(size / 2)};
		arcadeMap = new char[size][size];
		for (int i = 0; i < arcadeMap.length; i++) {
			for (int j = 0; j < arcadeMap.length; j++) {
				arcadeMap[i][j] = asciiEmpty;
			}
		}
		arcadeMap[heroPos[0]][heroPos[1]] = asciiHero;
		for (int cnt = 0; cnt < Math.pow(size, 2) / 2; cnt++) {
			int i = random.nextInt(size);
			int j = random.nextInt(size);
			if (arcadeMap[i][j] == asciiEmpty) {
				arcadeMap[i][j] = enemies.get(random.nextInt(3)).getAscii();
			}
		}
	}

	private void printArcadeMap(boolean key) {
		for (char[] row: arcadeMap) {
			for (char elem: row) {
				switch (elem) {
					case '⛹':
						System.out.format(Glob.GR_BOLD + "%2c " + Glob.RESET, elem);
						break;
					case '❶':
						System.out.format(Glob.YELLOW + "%2c " + Glob.RESET, elem);
						break;
					case '❷':
						System.out.format(Glob.BLUE + "%2c " + Glob.RESET, elem);
						break;
					case '❸':
						System.out.format(Glob.RED + "%2c " + Glob.RESET, elem);
						break;
					case '☭':
						System.out.format(Glob.RED + "%2c " + Glob.RESET, elem);
						break;
					case '☠':
						System.out.format(Glob.RED + "%2c " + Glob.RESET, elem);
						break;
					default:
						System.out.format("%2c ", elem);
				}

			}
			System.out.print("\n");
		}
		if(key) {
			Message.chooseMove();
		}
	}

	private void setHeroPos(int[] heroPos) {
		setOldPos(this.heroPos);
		this.heroPos[0] += heroPos[0];
		this.heroPos[1] += heroPos[1];
	}

	private void editMap() {
		if(findCollision()) {
			if (!startFight()) {
				Message.gameOver();
				this.arcadeMap[heroPos[0]][heroPos[1]] = '☠';
				printArcadeMap(false);
				Console.start();
			}
		}
		if (heroPos[1] == 0 || heroPos[1] == arcadeMap.length - 1 ||
				heroPos[0] == 0 || heroPos[0] == arcadeMap.length -1) {
			setNewMap(hero.getLevel());
		} else {
			this.arcadeMap[oldPos[0]][oldPos[1]] = asciiEmpty;
			this.arcadeMap[heroPos[0]][heroPos[1]] = asciiHero;
		}
		printArcadeMap(true);
	}

	private boolean startFight() {
		HeroManager heroManager = new HeroManager();
		Random random = new Random();
		int i = 0;
		for (; enemies.get(i).getAscii() != arcadeMap[heroPos[0]][heroPos[1]]; i++);
		int tmpHp = enemies.get(i).getHP();
		int tmpAttack;
		while (hero.getHP() > 0 && tmpHp > 0) {
			tmpAttack = hero.getAttack();
			int criticalChance = Math.round(100 / hero.getCC());
			if (random.nextInt(criticalChance) == 0) {
				tmpAttack *= 2;
			}
			tmpHp -= tmpAttack;
			Message.print(Glob.GREEN + hero.getName() + Glob.RESET + " inflicted " + Glob.GREEN + tmpAttack + Glob.RESET +
					" damage to " + Glob.RED + enemies.get(i).getName() + Glob.RESET);
			if (tmpHp > 0) {
				int damage = (int) calculateDamage(i);
				hero.setHP(hero.getHP() - damage);
				Message.print(Glob.RED + enemies.get(i).getName() + Glob.RESET + " inflicted " + Glob.RED +
						damage + Glob.RESET + " damage to " + Glob.GREEN + hero.getName() + Glob.RESET);
			}
		}
		if (hero.getHP() <= 0) {
			return false;
		} else {
			int mustLvl = (int)(hero.getLevel() * 1000 + Math.pow(hero.getLevel() - 1, 2) * 450);
			int exp = calculateExp(i);
			hero.setExp(hero.getExp() + exp);
			Message.print("You won and got " + Glob.BLUE + exp + Glob.RESET + " experiences");
			dropItem(i);
			if (hero.getExp() >= mustLvl) {
				hero.setLevel(hero.getLevel() + 1);
				hero = Console.getHero(hero.getClassHero(), hero.getName(), hero.getLevel(), hero.getExp(), hero, 10);
				hero.updateAllStat();
				heroManager.updateHero(hero, ConnectSQL.getConnection());
				Message.levelUp();
				Message.print(hero.toString());
				setNewMap(hero.getLevel());
			}
		}
		return true;
	}

	private double calculateDamage(int i) {
		double x = enemies.get(i).getAttack();
		double y = hero.getDef();
		return Math.round(x / y);
	}

	private int calculateExp(int i) {
		double x = (double) enemies.get(i).getLevel() / (double) hero.getLevel();
		double z = x == 1 && hero.getLevel() != 1 ? x + 1 : x;
		double y = (double) hero.getLevel() / z;
		return (int) Math.round(1000 / (y + x));
	}

	private void dropItem(int i) {
		String[] items = {"Weapon", "Armor", "Helm"};
		Random random = new Random();
		Scanner scanner = new Scanner(System.in);
		int randChoose = random.nextInt(items.length);
		Message.print(Glob.GR_BOLD + "DROP:\n" + Glob.RESET +
				artifacts.get(enemies.get(i).getLevel() - 1).toString(items[randChoose]));
		Message.print("Pick up drop: " + Glob.GREEN + "YES" + Glob.RESET + " or " + Glob.GREEN + "NO" + Glob.RESET);
		String line = scanner.nextLine();
		while (line != null || scanner.hasNextLine()) {
			switch (line.toLowerCase()) {
				case "yes":
					Item value = hero.getArtifacts().getArtifacts().get(items[randChoose].toLowerCase());
					if (value == null) {
						hero.getArtifacts().addNewItem(enemies.get(i).getLevel(), items[randChoose].toLowerCase());
						hero.updateStat(items[randChoose].toLowerCase(), artifacts.get(enemies.get(i).getLevel() - 1).getArtifacts().get(items[randChoose]));
					} else if (value.getPoint() < artifacts.get(enemies.get(i).getLevel() - 1).getArtifacts().get(items[randChoose]).getPoint()) {
						hero.getArtifacts().addNewItem(enemies.get(i).getLevel(), items[randChoose].toLowerCase());
						hero.updateStat(items[randChoose].toLowerCase(), artifacts.get(enemies.get(i).getLevel() - 1).getArtifacts().get(items[randChoose]));
					}
				case "no":
					return;
				default:
					Message.print("Select " + Glob.GREEN + "YES" + Glob.RESET +
							" or " + Glob.GREEN + "NO" + Glob.RESET + ".");
					line = scanner.nextLine();
					break;
			}
		}
	}

	private boolean findCollision() {
		Scanner scanner = new Scanner(System.in);
		Random random = new Random();
		if (arcadeMap[heroPos[0]][heroPos[1]] != asciiEmpty) {
			int i = 0;
			for (; enemies.get(i).getAscii() != arcadeMap[heroPos[0]][heroPos[1]]; i++);
			this.arcadeMap[oldPos[0]][oldPos[1]] = asciiEmpty;
			this.arcadeMap[heroPos[0]][heroPos[1]] = '☭';
			printArcadeMap(false);
			Message.print(enemies.get(i).toString());
			Message.print("Select an action: " + Glob.GR_BOLD + "RUN " + Glob.RESET +
					"or " + Glob.GR_BOLD + "FIGHT" + Glob.RESET);
			while (scanner.hasNextLine()) {
				String runOrFight = scanner.nextLine();
				switch (runOrFight.toLowerCase()) {
					case "run":
						int[] trueOrFalse = {1, 0};
						switch (trueOrFalse[random.nextInt(2)]) {
							case 1:
								Message.print(Glob.GREEN + "You managed to escape from the fight" + Glob.RESET);
								arcadeMap[heroPos[0]][heroPos[1]] = enemies.get(i).getAscii();
								heroPos[0] = oldPos[0];
								heroPos[1] = oldPos[1];
								return false;
							default:
								Message.print(Glob.RED + "You could not escape. This creature has caught up with you." + Glob.RESET);
								arcadeMap[heroPos[0]][heroPos[1]] = enemies.get(i).getAscii();
								return true;
						}
					case "fight":
						Message.print("I'll kill you hardly.");
						arcadeMap[heroPos[0]][heroPos[1]] = enemies.get(i).getAscii();
						return true;
					default:
						Message.print(Glob.RED + "Choose the right action!!!" + Glob.RESET);
				}
			}
		}
		return false;
	}

	private void setOldPos(int[] oldPos) {
		this.oldPos[0] = oldPos[0];
		this.oldPos[1] = oldPos[1];
	}
}
