package ua.yzcorp.view;

import ua.yzcorp.controller.ConnectSQL;
import ua.yzcorp.controller.DefaultStat;
import ua.yzcorp.controller.Glob;
import ua.yzcorp.controller.HeroManager;
import ua.yzcorp.model.ArcadeMap;
import ua.yzcorp.model.ClassHero;
import ua.yzcorp.model.Hero;
import ua.yzcorp.model.Hero.HeroBuilder;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console {
	public static HeroManager heroManager = new HeroManager();
	public static void start() {
		Hero hero;

		Scanner scanner = new Scanner(System.in);
		ArcadeMap arcadeMap;

		Message.chooseOrCreate();
		while (scanner.hasNextLine()) {
			String tmp = scanner.nextLine();
			heroManager.startListener(tmp);
			hero = heroManager.getHero();
			if (hero != null) {
				Message.print(hero.toString());		//print full info about Hero
				Message.startGame();
				arcadeMap = new ArcadeMap(hero);
				arcadeMap.startGame();
			}
		}
		scanner.close();
	}

	public static Hero chooseHero() {
		Scanner scanner = new Scanner(System.in);
		HeroManager heroManager = new HeroManager();
		List<Hero> heroes = heroManager.getAllTarget();
		if (heroes.isEmpty()) {
			Message.print(Glob.RED + "Your heroes are empty. " + Glob.GREEN + "CREATE" + Glob.RESET + " a new hero" + Glob.RESET);
			return null;
		}
		Pattern delete = Pattern.compile("^delete \\[\\d+]$", Pattern.CASE_INSENSITIVE);
		Pattern nbr = Pattern.compile("(^\\d+$)");
		Message.chooseHero(heroes);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Matcher matchDel = delete.matcher(line);
			Matcher matchNbr = nbr.matcher(line);
			if (!matchDel.matches() && !matchNbr.matches()) {
				Message.print(Glob.RED + "Enter correct command" + Glob.RESET);
			} else  {
				line = line.replaceAll("\\D+", "");
				try {
					int number = Integer.parseInt(line);
					if (number > heroes.size() - 1 || number < 0) {
						Message.print(Glob.RED + "Enter correct number" + Glob.RESET);
					} else {
						if (matchDel.matches()) {
							heroManager.deleteHero(heroes.get(number).getName(), ConnectSQL.getConnection());
							Message.print("Deletion was successful");
							heroes = heroManager.getAllTarget();
							if (heroes.isEmpty()) {
								Message.print(Glob.RED + "Your heroes are empty. " + Glob.GREEN + "CREATE" + Glob.RESET + " a new hero" + Glob.RESET);
								return null;
							}
							Message.chooseHero(heroes);
						} else {
							return heroes.get(number);
						}
					}
				} catch (NumberFormatException e) {
					Message.print(Glob.RED + "Enter correct number or command" + Glob.RESET);
				}
			}
		}
		scanner.close();
		return null;
	}

	public static Hero createHero() {
		String classHero = chooseClass();		//choose class Hero and getting information about each class
		Message.print("Enter the name of your hero");		//create name Hero
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String nameHero = scanner.nextLine();
			if (nameHero == null || nameHero.isEmpty()) {
				Message.print(heroManager.emptyName());
			} else if (!HeroManager.checkName(nameHero)) {
				Message.print(heroManager.existsName());
			} else {
				return (getHero(classHero, nameHero, 1, 0, null, 0));		//method getHero create Hero with help Builder pattern
			}
		}
		scanner.close();
		return null;
	}

	public static Hero getHero(String classHero, String nameHero, int level, int exp, Hero heroOld, int upper) {
		HeroBuilder hero = new HeroBuilder();
		DefaultStat defaultStat = new DefaultStat();
		hero.setName(nameHero).setClassHero(classHero).setLevel(level).setExp(exp);
		hero.setAttack(defaultStat.getATK(classHero) + upper);
		hero.setDef(defaultStat.getDEF(classHero) + upper);
		hero.setHP(defaultStat.getHP(classHero) + upper);
		hero.setCC(defaultStat.getCC(classHero) + upper);
		if (heroOld != null) {
			hero.setArtifacts(heroOld.getArtifacts());
		} else {
			hero.setArtifacts(null);
		}
		return hero.build();
	}

	private static String chooseClass() {
		Message.chooseClass();
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			ClassHero classHero = ClassHero.getClass(scanner.nextLine());
			switch (classHero.toString().toLowerCase()) {
				case "human":
					return "Human";
				case "orc":
					return "Orc";
				case "elf":
					return "Elf";
				case "dwarf":
					return "Dwarf";
				case "info":
					while (showInfo());
					break;
				default:
					Message.print(Glob.RED + "Choose the right hero class" + Glob.RESET);
					break;
			}
		}
		scanner.close();
		return null;
	}

	private static boolean showInfo() {
		Message.print("About which class you want to know the information: "
				+ Glob.GREEN + "HUMAN, ORC, ELF " + Glob.RESET + "or " +
				Glob.GREEN + "DWARF" + Glob.RESET);
		Scanner scanner = new Scanner(System.in);
		DefaultStat defaultStat = new DefaultStat();
		Message.print(defaultStat.toString(scanner.nextLine())); //print info about class
		Message.print("Continue? [" + Glob.GREEN + "YES" + Glob.RESET +
				" | " + Glob.GREEN + "NO" + Glob.RESET + "]");
		String tmp = scanner.nextLine();
		while (tmp != null || scanner.hasNextLine()) {
			switch (tmp.toLowerCase()) {
				case "yes":
					return true;
				case "no":
					Message.chooseClass();
					return false;
				default:
					Message.print("Select " + Glob.GREEN + "YES" + Glob.RESET +
							" or " + Glob.GREEN + "NO" + Glob.RESET + ".");
					tmp = scanner.nextLine();
					break;
			}
		}
		scanner.close();
		return false;
	}
}

