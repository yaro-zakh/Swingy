package ua.yzcorp.view;

import ua.yzcorp.controller.Glob;
import ua.yzcorp.model.Hero;

import java.util.List;

public class Message {
	public static void print(String message) {
		System.out.println(message);
	}

	public static void goodBye() {
		System.out.println(Glob.YELLOW + "\n" +
				" ██████╗  ██████╗  ██████╗ ██████╗ \n" +
				"██╔════╝ ██╔═══██╗██╔═══██╗██╔══██╗\n" +
				"██║  ███╗██║   ██║██║   ██║██║  ██║\n" +
				"██║   ██║██║   ██║██║   ██║██║  ██║\n" +
				"╚██████╔╝╚██████╔╝╚██████╔╝██████╔╝\n" +
				" ╚═════╝  ╚═════╝  ╚═════╝ ╚═════╝ \n" +
				"                                   \n" +
				"    ██████╗ ██╗   ██╗███████╗      \n" +
				"    ██╔══██╗╚██╗ ██╔╝██╔════╝      \n" +
				"    ██████╔╝ ╚████╔╝ █████╗        \n" +
				"    ██╔══██╗  ╚██╔╝  ██╔══╝        \n" +
				"    ██████╔╝   ██║   ███████╗      \n" +
				"    ╚═════╝    ╚═╝   ╚══════╝      \n" +
				"                                   \n" + Glob.RESET);
		System.exit(0);
	}

	public static void youWon() {
		System.out.println(Glob.YELLOW + "\n" +
				"██╗   ██╗ ██████╗ ██╗   ██╗  \n" +
				"╚██╗ ██╔╝██╔═══██╗██║   ██║  \n" +
				" ╚████╔╝ ██║   ██║██║   ██║  \n" +
				"  ╚██╔╝  ██║   ██║██║   ██║  \n" +
				"   ██║   ╚██████╔╝╚██████╔╝  \n" +
				"   ╚═╝    ╚═════╝  ╚═════╝   \n" +
				"                             \n" +
				"██╗    ██╗ ██████╗ ███╗   ██╗\n" +
				"██║    ██║██╔═══██╗████╗  ██║\n" +
				"██║ █╗ ██║██║   ██║██╔██╗ ██║\n" +
				"██║███╗██║██║   ██║██║╚██╗██║\n" +
				"╚███╔███╔╝╚██████╔╝██║ ╚████║\n" +
				" ╚══╝╚══╝  ╚═════╝ ╚═╝  ╚═══╝\n" +
				"                             \n" + Glob.RESET);
	}

	public static void chooseClass() {
		System.out.println("Choose a hero class\n" + Glob.GREEN +
				"༒ HUMAN;\n" +
				"༒ ORC;\n" +
				"༒ ELF;\n" +
				"༒ DWARF;\n" + Glob.RESET +
				"More info about class: enter " + Glob.GREEN + "INFO" + Glob.RESET);
	}
	public static void chooseMove() {
		System.out.format("%-7s - " + Glob.BLUE + "%-10s\n" + Glob.RESET, "☢ North", "press 'W'");
		System.out.format("%-7s - " + Glob.BLUE + "%-10s\n" + Glob.RESET, "☢ West", "press 'A'");
		System.out.format("%-7s - " + Glob.BLUE + "%-10s\n" + Glob.RESET, "☢ South", "press 'S'");
		System.out.format("%-7s - " + Glob.BLUE + "%-10s\n" + Glob.RESET, "☢ East", "press 'D'");
		System.out.format("%7s ⇝ " + Glob.GREEN + "%-10s\n" + Glob.RESET, "My", "INFO");
		System.out.format("%7s ⇝ " + Glob.GREEN + "%-10s\n" + Glob.RESET, "Switch", "GUI");
		System.out.format("%7s ⇝ " + Glob.RED + "%-10s\n" + Glob.RESET, "", "EXIT");
	}

	public static void chooseOrCreate() {
		System.out.println("Choose one of the commands:\n" +
				"1. " + Glob.GREEN + "CREATE" + Glob.GRAY + " (create a new hero)" + Glob.RESET + "\n" +
				"2. " + Glob.GREEN + "CHOOSE" + Glob.GRAY + " (choose one of the already created heroes)" + Glob.RESET + "\n" +
				"3. " + Glob.GREEN + "GUI" + Glob.GRAY + " (switch to HUI)" + Glob.RESET + "\n" +
				"4. " + Glob.GREEN + "EXIT" + Glob.GRAY + " (leave the game)" + Glob.RESET + "\n");
	}

	public static void chooseHero(List<Hero> heroes) {
		Message.print("Enter the hero number from " + Glob.GREEN + "0" + Glob.RESET + " to " +
				Glob.GREEN + (heroes.size() - 1) + Glob.RESET + ", " +
				Glob.GREEN + "DELETE [nbr] " + Glob.RESET + "hero or " + Glob.GREEN + "EXIT " + Glob.RESET + "of the game");
		for (int cnt = 0; cnt < heroes.size(); cnt++) {
			Message.print(Glob.GREEN + cnt + ": " + Glob.RESET + heroes.get(cnt).getName() +
					": (" + heroes.get(cnt).getLevel() + " lvl)");
		}
	}

	public static void startGame() {
		System.out.print(Glob.GREEN + "\n" +
				"███████╗████████╗ █████╗ ██████╗ ████████╗\n" +
				"██╔════╝╚══██╔══╝██╔══██╗██╔══██╗╚══██╔══╝\n" +
				"███████╗   ██║   ███████║██████╔╝   ██║   \n" +
				"╚════██║   ██║   ██╔══██║██╔══██╗   ██║   \n" +
				"███████║   ██║   ██║  ██║██║  ██║   ██║   \n" +
				"╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝   \n" +
				"                                          \n" +
				" ██████╗  █████╗ ███╗   ███╗███████╗      \n" +
				"██╔════╝ ██╔══██╗████╗ ████║██╔════╝      \n" +
				"██║  ███╗███████║██╔████╔██║█████╗        \n" +
				"██║   ██║██╔══██║██║╚██╔╝██║██╔══╝        \n" +
				"╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗      \n" +
				" ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝      \n" +
				"                                          \n" + Glob.RESET);
	}

	public static void gameOver() {
		System.out.print(Glob.RED + "\n" +
				"  ▄████  ▄▄▄       ███▄ ▄███▓▓█████ \n" +
				" ██▒ ▀█▒▒████▄    ▓██▒▀█▀ ██▒▓█   ▀ \n" +
				"▒██░▄▄▄░▒██  ▀█▄  ▓██    ▓██░▒███   \n" +
				"░▓█  ██▓░██▄▄▄▄██ ▒██    ▒██ ▒▓█  ▄ \n" +
				"░▒▓███▀▒ ▓█   ▓██▒▒██▒   ░██▒░▒████▒\n" +
				" ░▒   ▒  ▒▒   ▓▒█░░ ▒░   ░  ░░░ ▒░ ░\n" +
				"  ░   ░   ▒   ▒▒ ░░  ░      ░ ░ ░  ░\n" +
				"░ ░   ░   ░   ▒   ░      ░      ░   \n" +
				"      ░       ░  ░       ░      ░  ░\n" +
				"                                    \n" +
				" ▒█████   ██▒   █▓▓█████  ██▀███    \n" +
				"▒██▒  ██▒▓██░   █▒▓█   ▀ ▓██ ▒ ██▒  \n" +
				"▒██░  ██▒ ▓██  █▒░▒███   ▓██ ░▄█ ▒  \n" +
				"▒██   ██░  ▒██ █░░▒▓█  ▄ ▒██▀▀█▄    \n" +
				"░ ████▓▒░   ▒▀█░  ░▒████▒░██▓ ▒██▒  \n" +
				"░ ▒░▒░▒░    ░ ▐░  ░░ ▒░ ░░ ▒▓ ░▒▓░  \n" +
				"  ░ ▒ ▒░    ░ ░░   ░ ░  ░  ░▒ ░ ▒░  \n" +
				"░ ░ ░ ▒       ░░     ░     ░░   ░   \n" +
				"    ░ ░        ░     ░  ░   ░       \n" +
				"              ░                     \n" + Glob.RESET);
	}

	public static void levelUp() {
		System.out.print(Glob.GR_BOLD + "\n" +
				"┬  ┌─┐┬  ┬┌─┐┬  \n" +
				"│  ├┤ └┐┌┘├┤ │  \n" +
				"┴─┘└─┘ └┘ └─┘┴─┘\n" +
				"    ┬ ┬┌─┐┬     \n" +
				"    │ │├─┘│     \n" +
				"    └─┘┴  o     \n" + Glob.RESET);
	}
}
