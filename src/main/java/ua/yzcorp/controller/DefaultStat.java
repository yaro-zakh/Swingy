package ua.yzcorp.controller;

import ua.yzcorp.view.Message;

import java.util.*;

public class DefaultStat {
	private Map<String, Integer> human = new LinkedHashMap<>();
	private Map<String, Integer> orc = new LinkedHashMap<>();
	private Map<String, Integer> elf = new LinkedHashMap<>();
	private Map<String, Integer> dwarf = new LinkedHashMap<>();

	public DefaultStat() {
		this.human.put("ATK", 25);
		this.human.put("DEF", 25);
		this.human.put("HP", 25);
		this.human.put("CC", 25);

		this.orc.put("ATK", 20);
		this.orc.put("DEF", 20);
		this.orc.put("HP", 40);
		this.orc.put("CC", 20);

		this.elf.put("ATK", 20);
		this.elf.put("DEF", 20);
		this.elf.put("HP", 20);
		this.elf.put("CC", 40);

		this.dwarf.put("ATK", 20);
		this.dwarf.put("DEF", 30);
		this.dwarf.put("HP", 30);
		this.dwarf.put("CC", 20);
	}



	public int getATK(String classHero) {
		switch (classHero.toLowerCase()) {
			case "human":
				return human.get("ATK");
			case "orc":
				return orc.get("ATK");
			case "elf":
				return elf.get("ATK");
			case "dwarf":
				return dwarf.get("ATK");
		}
		return 0;
	}

	public int getDEF(String classHero) {
		switch (classHero.toLowerCase()) {
			case "human":
				return human.get("DEF");
			case "orc":
				return orc.get("DEF");
			case "elf":
				return elf.get("DEF");
			case "dwarf":
				return dwarf.get("DEF");
		}
		return 0;
	}

	public int getHP(String classHero) {
		switch (classHero.toLowerCase()) {
			case "human":
				return human.get("HP");
			case "orc":
				return orc.get("HP");
			case "elf":
				return elf.get("HP");
			case "dwarf":
				return dwarf.get("HP");
		}
		return 0;
	}

	public int getCC(String classHero) {
		switch (classHero.toLowerCase()) {
			case "human":
				return human.get("CC");
			case "orc":
				return orc.get("CC");
			case "elf":
				return elf.get("CC");
			case "dwarf":
				return dwarf.get("CC");
		}
		return 0;
	}

	public String toString(String classHero) {
		StringBuilder defStat = new StringBuilder();
		switch (classHero.toLowerCase()) {
			case "human":
				defStat.append(Glob.GREEN).append("Human default characteristics").append(Glob.RESET);
				defStat.append("\nHuman created by Gran Cain, have \n" +
								"balanced abilities. Worship the \n" +
								"Goddess of Light Einhasad.\n");
				for (Map.Entry<String, Integer> entry: human.entrySet()) {
					defStat.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
				}
				break;
			case "orc":
				defStat.append(Glob.GREEN).append("Orc default characteristics").append(Glob.RESET);
				defStat.append("\nThe orcs created by Einhasad with \n" +
								"the help of the Spirit of Fire are \n" +
								"born warriors, as they have tremendous \n" +
								"power and immense vitality. They worship \n" +
								"the God of Fire of Pa'agrio.\n");
				for (Map.Entry<String, Integer> entry: orc.entrySet()) {
					defStat.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
				}
				break;
			case "elf":
				defStat.append(Glob.GREEN).append("Elf default characteristics").append(Glob.RESET);
				defStat.append("\nThe elves created by Einhasad with the \n" +
								"help of the Tree of Life are distinguished \n" +
								"by the lightness of flesh and dexterity. \n" +
								"They worship the Goddess of Water Eve.\n");
				for (Map.Entry<String, Integer> entry: elf.entrySet()) {
					defStat.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
				}
				break;
			case "dwarf":
				defStat.append(Glob.GREEN).append("Dwarf default characteristics").append(Glob.RESET);
				defStat.append("\nDwarves created by Einhasad with the help \n" +
								"of the Spirit of the Earth have powerful \n" +
								"physical characteristics and cancellation \n" +
								"of the ability to apply to the arts. They \n" +
								"worship the God of the Earth.\n");
				for (Map.Entry<String, Integer> entry: dwarf.entrySet()) {
					defStat.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
				}
				break;
			default:
				Message.print(Glob.RED + "Choose the right hero class" + Glob.RESET);
				break;
		}
		return defStat.toString();
	}
}
