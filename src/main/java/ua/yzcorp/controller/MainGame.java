package ua.yzcorp.controller;

import ua.yzcorp.model.Enemy;
import ua.yzcorp.model.Item;
import ua.yzcorp.view.Console;
import ua.yzcorp.view.Gui;
import ua.yzcorp.view.Message;

import java.util.Arrays;
import java.util.Random;

import static ua.yzcorp.controller.Glob.HERO;
import static ua.yzcorp.controller.Glob.MAPENEMIES;
import static ua.yzcorp.controller.Glob.MAP;
import static ua.yzcorp.controller.Glob.SCANNER;
import static ua.yzcorp.model.Hero.heroPos;

public class MainGame {

	private static char [][] arcadeMap;
	private static int [] oldPos = new int[2];
	private static String[] levelEnemy;
	private static char asciiHero = '⛹';
	private static char asciiEmpty = '⛶';

	public static boolean startGame() {
		updateMap();
		printArcadeMap(true);
		while (HERO.getHP() > 0 && SCANNER.hasNextLine()) {
			String move = SCANNER.nextLine();
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
					Message.print(HERO.toString());
					break;
				case "gui":
					Gui.startWithConsole();
					return true;
				case "exit":
					Message.goodBye();
				default:
					Message.print(Glob.RED + "Choose the right side" + Glob.RESET);
					break;
			}
		}
		SCANNER.close();
		return false;
	}

	private static void updateMap() {
		arcadeMap = new char[MAP.getSize()][MAP.getSize()];
		for (int i = 0; i < arcadeMap.length; i++) {
			for (int j = 0; j < arcadeMap[i].length; j++) {
				levelEnemy = Gui.findEnemy(new int[]{i, j});
				if (levelEnemy != null && !Arrays.equals(heroPos, new int[]{i, j})) {
					arcadeMap[i][j] = MAPENEMIES.get(Integer.parseInt(levelEnemy[0])).getAscii();
				} else  if (Arrays.equals(heroPos, new int[]{i, j})) {
					arcadeMap[i][j] = asciiHero;
				} else {
					arcadeMap[i][j] = asciiEmpty;
				}
			}
		}
	}

	private static void printArcadeMap(boolean key) {
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

	private static void setHeroPos(int[] position) {
		setOldPos(heroPos);
		heroPos[0] += position[0];
		heroPos[1] += position[1];
	}

	public static boolean finalChoose() {
		Message.print("What do you want to do?\n" +
				Glob.GREEN + "1. " + Glob.RESET + "Try again\n" +
				Glob.GREEN + "2. " + Glob.RESET + "Exit\n");
		while (SCANNER.hasNextLine()) {
			String line = SCANNER.nextLine();
			switch (line.toLowerCase()) {
				case "1":
					return true;
				case "2":
					Message.goodBye();
				default:
					Message.print(Glob.RED + "Enter correct command!" + Glob.RESET + "\n");
			}
		}
		return false;
	}

	private static void editMap() {
		if(findCollision()) {
			if (!startFight(MAPENEMIES.get(Integer.parseInt(levelEnemy[0])))) {
				Message.print(Glob.INFORM.toString());
				Message.gameOver();
				arcadeMap[heroPos[0]][heroPos[1]] = '☠';
				printArcadeMap(false);
				HERO = null;
				if (finalChoose()) {
					Console.start();
				}
			} else {
				dropItem(MAPENEMIES.get(Integer.parseInt(levelEnemy[0])));
				MAPENEMIES.remove(Integer.parseInt(levelEnemy[0]));
			}
			if (levelUp()) {
				Message.levelUp();
				Message.print(HERO.toString());
				MAP.updateMainInfo();
				updateMap();
			}
		}
		if (heroPos[1] == 0 || heroPos[1] == arcadeMap.length - 1 || heroPos[0] == 0 || heroPos[0] == arcadeMap.length -1) {
			Message.youWon();
			HERO = null;
			if (finalChoose()) {
				Console.start();
			}
		} else {
			arcadeMap[oldPos[0]][oldPos[1]] = asciiEmpty;
			arcadeMap[heroPos[0]][heroPos[1]] = asciiHero;
		}
		printArcadeMap(true);
	}

	private static boolean findCollision() {
		Random random = new Random();
		if (arcadeMap[heroPos[0]][heroPos[1]] != asciiEmpty) {
			levelEnemy = Gui.findEnemy(new int[]{heroPos[0], heroPos[1]});
			arcadeMap[oldPos[0]][oldPos[1]] = asciiEmpty;
			arcadeMap[heroPos[0]][heroPos[1]] = '☭';
			printArcadeMap(false);
			Message.print(MAPENEMIES.get(Integer.parseInt(levelEnemy[0])).toString());
			Message.print("Select an action: " + Glob.GR_BOLD + "RUN " + Glob.RESET +
					"or " + Glob.GR_BOLD + "FIGHT" + Glob.RESET);
			while (SCANNER.hasNextLine()) {
				String runOrFight = SCANNER.nextLine();
				switch (runOrFight.toLowerCase()) {
					case "run":
						int[] trueOrFalse = {1, 0};
						switch (trueOrFalse[random.nextInt(2)]) {
							case 1:
								Message.print(Glob.GREEN + "You managed to escape from the fight" + Glob.RESET);
								arcadeMap[heroPos[0]][heroPos[1]] = MAPENEMIES.get(Integer.parseInt(levelEnemy[0])).getAscii();
								heroPos[0] = oldPos[0];
								heroPos[1] = oldPos[1];
								return false;
							default:
								Message.print(Glob.RED + "You could not escape. This creature has caught up with you." + Glob.RESET);
								arcadeMap[heroPos[0]][heroPos[1]] = MAPENEMIES.get(Integer.parseInt(levelEnemy[0])).getAscii();
								return true;
						}
					case "fight":
						Message.print("I'll kill you hardly.");
						arcadeMap[heroPos[0]][heroPos[1]] = MAPENEMIES.get(Integer.parseInt(levelEnemy[0])).getAscii();
						return true;
					default:
						Message.print(Glob.RED + "Choose the right action!!!" + Glob.RESET);
				}
			}
		}
		return false;
	}

	public static int calculateExp(Enemy enemy) {
		double x = (double) enemy.getLevel() / (double) HERO.getLevel();
		double z = x == 1 && HERO.getLevel() != 1 ? x + 1 : x;
		double y = (double) HERO.getLevel() / z;
		return (int) Math.round(1000 / (y + x));
	}

	public static double calculateDamage(Enemy enemy) {
		double x = enemy.getAttack();
		double y = HERO.getDef();
		return Math.round(x / y);
	}

	public static boolean startFight(Enemy enemy) {
		Random random = new Random();
		Glob.INFORM = new StringBuilder();
		int tmpHp = enemy.getHP();
		int tmpAttack;
		while (HERO.getHP() > 0 && tmpHp > 0) {
			tmpAttack = HERO.getAttack();
			int criticalChance = Math.round(100 / HERO.getCC());
			if (random.nextInt(criticalChance) == 0) {
				tmpAttack *= 2;
				HERO.setHP(HERO.getHP() + (int) Math.round(tmpAttack * 0.02));
			}
			tmpHp -= tmpAttack;
			Glob.INFORM.append(Glob.GREEN).append(HERO.getName()).append(Glob.RESET).append(" inflicted ").
					append(Glob.GREEN).append(tmpAttack).append(Glob.RESET).append(" damage to ").
					append(Glob.RED).append(enemy.getName()).append(Glob.RESET).append("\n");
			if (tmpHp > 0) {
				int damage = (int) calculateDamage(enemy);
				HERO.setHP(HERO.getHP() - damage);
				Glob.INFORM.append(Glob.RED).append(enemy.getName()).append(Glob.RESET).append(" inflicted ").
						append(Glob.RED).append(damage).append(Glob.RESET).append(" damage to ").
						append(Glob.GREEN).append(HERO.getName()).append(Glob.RESET).append("\n");
			}
		}
		if (HERO.getHP() <= 0) {
			return false;
		} else {
			int exp = calculateExp(enemy);
			HERO.setExp(HERO.getExp() + exp);
			Glob.INFORM.append("\nYou won and got ").append(Glob.BLUE).append(exp).append(Glob.RESET).append(" experiences").append("\n\n");
		}
		return true;
	}

	public static boolean levelUp() {
		HeroManager heroManager = new HeroManager();
		if (HERO.getExp() >= HERO.getMustLevel()[1]) {
			HERO.setLevel(HERO.getLevel() + 1);
			HERO = Console.getHero(HERO.getClassHero(), HERO.getName(), HERO.getLevel(), HERO.getExp(), HERO, 10);
			HERO.updateAllStat();
			heroManager.updateHero(HERO, ConnectSQL.getConnection());
			return true;
		} else {
			return false;
		}
	}

	public static void dropItem(Enemy enemy) {
		String[] items = {"Weapon", "Armor", "Helm"};
		Random random = new Random();
		int randChoose = random.nextInt(items.length);
		MAP.setRandomDrop(items[randChoose]);
		Glob.INFORM.append(Glob.GR_BOLD).append("Your DROP is:").append(Glob.RESET).append("\n").append(MAP.getArtifacts().get(enemy.getLevel() - 1).toString(MAP.getRandomDrop())).append("\n");
		if (Glob.GUI == 1) {
			Glob.INFORM.append("Pick up drop?");
		}
		if (Glob.GUI == 0) {
			Message.print(Glob.INFORM.toString());
			Message.print("Pick up drop: " + Glob.GREEN + "YES" + Glob.RESET + " or " + Glob.GREEN + "NO" + Glob.RESET);
			String line = SCANNER.nextLine();
			while (line != null || SCANNER.hasNextLine()) {
				assert line != null;
				switch (line.toLowerCase()) {
					case "yes":
						pickUpDrop(enemy, MAP.getRandomDrop());
					case "no":
						return;
					default:
						Message.print("Select " + Glob.GREEN + "YES" + Glob.RESET +
								" or " + Glob.GREEN + "NO" + Glob.RESET + ".");
						line = SCANNER.nextLine();
						break;
				}
			}
		}
	}

	public static void pickUpDrop(Enemy enemy, String rand) {
		Item value = HERO.getArtifacts().getArtifacts().get(rand.toLowerCase());
		if (value == null) {
			HERO.getArtifacts().addNewItem(enemy.getLevel(), rand.toLowerCase());
			HERO.updateStat(rand.toLowerCase(), MAP.getArtifacts().get(enemy.getLevel() - 1).getArtifacts().get(rand));
		} else if (value.getPoint() < MAP.getArtifacts().get(enemy.getLevel() - 1).getArtifacts().get(rand).getPoint()) {
			HERO.getArtifacts().addNewItem(enemy.getLevel(), rand.toLowerCase());
			HERO.updateStat(rand.toLowerCase(), MAP.getArtifacts().get(enemy.getLevel() - 1).getArtifacts().get(rand));
		}
	}

	private static void setOldPos(int[] Pos) {
		oldPos[0] = Pos[0];
		oldPos[1] = Pos[1];
	}
}
