package ua.yzcorp.controller;

import ua.yzcorp.model.Enemy;
import ua.yzcorp.model.Hero;

import java.util.List;

public class Glob {
	public static int GUI;
	public static final String PIC = "/home/yaroslav/IdeaProjects/swingy/src/main/java/ua/yzcorp/pic/";
	public static String YELLOW;
	public static String BLUE;
	public static String GREEN;
	public static String GR_BOLD;
	public static String RED;
	public static String GRAY;
	public static String RESET;
	public static Hero hero;
	public static List<Enemy> mapEnemies;
	public static StringBuilder inform;

	public static void onGui() {
		YELLOW = "༺";
		BLUE = "༺";
		GREEN = "༺";
		GR_BOLD = "༺";
		RED = "༺";
		GRAY = "༺";
		RESET = "༻";
		GUI = 1;
	}

	public static void onConsole() {
		YELLOW = "\033[0;33m";
		BLUE = "\033[0;34m";
		GREEN = "\033[0;32m";
		GR_BOLD = "\033[1;32m";
		RED = "\033[0;31m";
		GRAY = "\033[0;90m";
		RESET = "\033[0m";
		GUI = 0;
	}
}