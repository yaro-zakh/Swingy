package ua.yzcorp.model;

import ua.yzcorp.controller.*;
import ua.yzcorp.view.Console;
import ua.yzcorp.view.Gui;
import ua.yzcorp.view.Message;

import java.util.*;

import static ua.yzcorp.controller.Glob.HERO;
import static ua.yzcorp.controller.Glob.MAPENEMIES;
import static ua.yzcorp.model.Hero.heroPos;

public class ArcadeMap {
	private static List<Enemy> defaultEnemies;
	private static List<Artifacts> artifacts;
	private static char [][] arcadeMap;
	private static int [] oldPos = new int[2];
	private static int size;
	private static char asciiHero = '⛹';
	private static char asciiEmpty = '⛶';
	private static String randomDrop;
	private static String[] levelEnemy;
	private static Scanner scanner = new Scanner(System.in);

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
		HERO.setMustLevel((int) (HERO.getLevel() * 1000 + Math.pow(HERO.getLevel() - 1, 2) * 450));
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

	/*public static boolean startGame() {
		updateMap();
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
				case "gui":
					Gui.startWithConsole();
					return true;
				default:
					Message.print(Glob.RED + "Choose the right side" + Glob.RESET);
					break;
			}
		}
		scanner.close();
		return false;
	}

	private static void updateMap() {
		arcadeMap = new char[size][size];
		for (int i = 0; i < arcadeMap.length; i++) {
			for (int j = 0; j < arcadeMap[i].length; j++) {
				levelEnemy = Gui.findEnemy(new int[]{i, j});
				if (levelEnemy != null && !Arrays.equals(heroPos, new int[]{i, j})) {
					arcadeMap[i][j] = mapEnemies.get(Integer.parseInt(levelEnemy[0])).getAscii();
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
		String line = scanner.nextLine();
		while (scanner.hasNextLine()) {
			switch (line.toLowerCase()) {
				case "try again":
					return true;
				case "exit":
					Message.goodBye();
				default:
					Message.print("Enter correct command!\n");
			}
		}
		return false;
	}

	private static void editMap() {
		if(findCollision()) {
			if (!startFight(mapEnemies.get(Integer.parseInt(levelEnemy[0])))) {
				Message.print(Glob.inform.toString());
				Message.gameOver();
				arcadeMap[heroPos[0]][heroPos[1]] = '☠';
				printArcadeMap(false);
				if (finalChoose()) {
					Console.start();
				}
			} else {
				dropItem(mapEnemies.get(Integer.parseInt(levelEnemy[0])));
			}
			if (levelUp()) {
				Message.levelUp();
				Message.print(hero.toString());
				updateMainInfo();
				updateMap();
			}
		}
		if (heroPos[1] == 0 || heroPos[1] == arcadeMap.length - 1 || heroPos[0] == 0 || heroPos[0] == arcadeMap.length -1) {
			Message.youWon();
			Console.start();
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
			Message.print(mapEnemies.get(Integer.parseInt(levelEnemy[0])).toString());
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
								arcadeMap[heroPos[0]][heroPos[1]] = mapEnemies.get(Integer.parseInt(levelEnemy[0])).getAscii();
								heroPos[0] = oldPos[0];
								heroPos[1] = oldPos[1];
								return false;
							default:
								Message.print(Glob.RED + "You could not escape. This creature has caught up with you." + Glob.RESET);
								arcadeMap[heroPos[0]][heroPos[1]] = mapEnemies.get(Integer.parseInt(levelEnemy[0])).getAscii();
								return true;
						}
					case "fight":
						Message.print("I'll kill you hardly.");
						arcadeMap[heroPos[0]][heroPos[1]] = mapEnemies.get(Integer.parseInt(levelEnemy[0])).getAscii();
						return true;
					default:
						Message.print(Glob.RED + "Choose the right action!!!" + Glob.RESET);
				}
			}
		}
		return false;
	}

	public static int calculateExp(Enemy enemy) {
		double x = (double) enemy.getLevel() / (double) hero.getLevel();
		double z = x == 1 && hero.getLevel() != 1 ? x + 1 : x;
		double y = (double) hero.getLevel() / z;
		return (int) Math.round(1000 / (y + x));
	}

	public static double calculateDamage(Enemy enemy) {
		double x = enemy.getAttack();
		double y = hero.getDef();
		return Math.round(x / y);
	}

	public static boolean startFight(Enemy enemy) {
		Random random = new Random();
		Glob.inform = new StringBuilder();
		int tmpHp = enemy.getHP();
		int tmpAttack;
		while (hero.getHP() > 0 && tmpHp > 0) {
			tmpAttack = hero.getAttack();
			int criticalChance = Math.round(100 / hero.getCC());
			if (random.nextInt(criticalChance) == 0) {
				tmpAttack *= 2;
				hero.setHP(hero.getHP() + (int) Math.round(tmpAttack * 0.02));
			}
			tmpHp -= tmpAttack;
			Glob.inform.append(Glob.GREEN).append(hero.getName()).append(Glob.RESET).append(" inflicted ").
					append(Glob.GREEN).append(tmpAttack).append(Glob.RESET).append(" damage to ").
					append(Glob.RED).append(enemy.getName()).append(Glob.RESET).append("\n");
			if (tmpHp > 0) {
				int damage = (int) calculateDamage(enemy);
				hero.setHP(hero.getHP() - damage);
				Glob.inform.append(Glob.RED).append(enemy.getName()).append(Glob.RESET).append(" inflicted ").
						append(Glob.RED).append(damage).append(Glob.RESET).append(" damage to ").
						append(Glob.GREEN).append(hero.getName()).append(Glob.RESET).append("\n");
			}
		}
		if (hero.getHP() <= 0) {
			return false;
		} else {
			int exp = calculateExp(enemy);
			hero.setExp(hero.getExp() + exp);
			Glob.inform.append("\nYou won and got ").append(Glob.BLUE).append(exp).append(Glob.RESET).append(" experiences").append("\n\n");
		}
		return true;
	}

	public static boolean levelUp() {
		HeroManager heroManager = new HeroManager();
		if (hero.getExp() >= hero.getMustLevel()) {
			hero.setLevel(hero.getLevel() + 1);
			hero = Console.getHero(hero.getClassHero(), hero.getName(), hero.getLevel(), hero.getExp(), hero, 10);
			hero.updateAllStat();
			heroManager.updateHero(hero, ConnectSQL.getConnection());
			return true;
		} else {
			return false;
		}
	}

	public static void dropItem(Enemy enemy) {
		String[] items = {"Weapon", "Armor", "Helm"};
		Random random = new Random();
		int randChoose = random.nextInt(items.length);
		randomDrop = items[randChoose];
		Glob.inform.append(Glob.GR_BOLD).append("Your DROP is:").append(Glob.RESET).append("\n").append(artifacts.get(enemy.getLevel() - 1).toString(randomDrop)).append("\n");
		if (Glob.GUI == 1) {
			Glob.inform.append("Pick up drop?");
		}
		if (Glob.GUI == 0) {
			Message.print(Glob.inform.toString());
			Message.print("Pick up drop: " + Glob.GREEN + "YES" + Glob.RESET + " or " + Glob.GREEN + "NO" + Glob.RESET);
			String line = scanner.nextLine();
			while (line != null || scanner.hasNextLine()) {
				assert line != null;
				switch (line.toLowerCase()) {
					case "yes":
						pickUpDrop(enemy, randomDrop);
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
	}

	public static void pickUpDrop(Enemy enemy, String rand) {
		Item value = hero.getArtifacts().getArtifacts().get(rand.toLowerCase());
		if (value == null) {
			hero.getArtifacts().addNewItem(enemy.getLevel(), rand.toLowerCase());
			hero.updateStat(rand.toLowerCase(), artifacts.get(enemy.getLevel() - 1).getArtifacts().get(rand));
		} else if (value.getPoint() < artifacts.get(enemy.getLevel() - 1).getArtifacts().get(rand).getPoint()) {
			hero.getArtifacts().addNewItem(enemy.getLevel(), rand.toLowerCase());
			hero.updateStat(rand.toLowerCase(), artifacts.get(enemy.getLevel() - 1).getArtifacts().get(rand));
		}
	}

	private static void setOldPos(int[] oldPos) {
		ArcadeMap.oldPos[0] = oldPos[0];
		ArcadeMap.oldPos[1] = oldPos[1];
	}*/
}