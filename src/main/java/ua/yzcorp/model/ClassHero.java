package ua.yzcorp.model;

public enum ClassHero {
	HUMAN,
	ORC,
	ELF,
	DWARF,
	INFO,
	MISS;

	public static ClassHero getClass(String classHero) {
		if (classHero == null || classHero.isEmpty()) {
			return MISS;
		}
		for (ClassHero tmp: ClassHero.values()) {
			if (classHero.toUpperCase().equals(tmp.toString())) {
				return tmp;
			}
		}
		return MISS;
	}
}
