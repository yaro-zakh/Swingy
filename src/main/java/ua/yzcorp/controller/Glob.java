package ua.yzcorp.controller;

import ua.yzcorp.model.Hero;

public class Glob {
	public static String PIC = "/home/yaroslav/IdeaProjects/swingy/src/main/java/ua/yzcorp/pic/";
	public static String YELLOW = Run.GUI == 0 ? "\033[0;33m" : "❲";
	public static String BLUE = Run.GUI == 0 ? "\033[0;34m" : "❲";
	public static String GREEN = Run.GUI == 0 ? "\033[0;32m" : "❲";
	public static String GR_BOLD = Run.GUI == 0 ? "\033[1;32m" : "❲";
	public static String RED = Run.GUI == 0 ? "\033[0;31m" : "❲";
	public static String RESET = Run.GUI == 0 ? "\033[0m" : "❳";
	public static Hero hero;
	public static StringBuilder inform;
}