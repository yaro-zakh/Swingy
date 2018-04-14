package ua.yzcorp.controller;

import ua.yzcorp.model.ArcadeMap;
import ua.yzcorp.model.Enemy;
import ua.yzcorp.model.Hero;

import java.util.List;
import java.util.Scanner;

public class Glob {
	public static int GUI;
	public static final String PIC = "src/main/java/ua/yzcorp/pic/";
	public static String YELLOW;
	public static String BLUE;
	public static String GREEN;
	public static String GR_BOLD;
	public static String RED;
	public static String GRAY;
	public static String RESET;
	public static Hero HERO;
	public static List<Enemy> MAPENEMIES;
	public static StringBuilder INFORM;
	public static ArcadeMap MAP;
	public static Scanner SCANNER = new Scanner(System.in);

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