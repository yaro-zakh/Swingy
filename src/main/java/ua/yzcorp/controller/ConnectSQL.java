package ua.yzcorp.controller;

import java.sql.*;

public class ConnectSQL {
    private final static String	url = "jdbc:sqlite:swingy.db";
    private static Connection connection = null;

	public ConnectSQL() {
		try {
			connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			String	createHeroTable = "CREATE TABLE IF NOT EXISTS hero" +
					"( id INTEGER PRIMARY KEY AUTOINCREMENT ," +
					"  nickname TEXT NOT NULL," +
					"  class TEXT NOT NULL," +
					"  level INTEGER NOT NULL," +
					"  experience INTEGER NOT NULL," +
					"  attack INTEGER NOT NULL," +
					"  defense INTEGER NOT NULL," +
					"  hit_points INTEGER NOT NULL," +
					"  cc INTEGER NOT NULL" +
					")";
			String createEnemyTable = "CREATE TABLE enemy" +
					"( id INTEGER PRIMARY KEY AUTOINCREMENT ," +
					"  name TEXT NOT NULL," +
					"  attack INTEGER NOT NULL," +
					"  hit_points INTEGER NOT NULL," +
					"  level INTEGER NOT NULL, " +
					"  ascii CHARACTER NOT NULL" +
					")";
			String createArtifactTable = "CREATE TABLE artifacts" +
					"( id INTEGER PRIMARY KEY AUTOINCREMENT ," +
					"  weapon TEXT NOT NULL," +
					"  armor TEXT NOT NULL," +
					"  helm TEXT NOT NULL," +
					"  level INTEGER NOT NULL" +
					")";
			statement.executeUpdate(createHeroTable);
			statement.executeUpdate("DROP TABLE IF EXISTS enemy");
			statement.executeUpdate("DROP TABLE IF EXISTS artifacts");
			statement.executeUpdate(createEnemyTable);
			statement.executeUpdate(createArtifactTable);

			statement.executeUpdate("INSERT INTO artifacts (weapon, armor, helm, level) " +
					"VALUES ('Enhanced Shadow Slasher', 'Blessed Twilight Leather', 'Immortal Helmet', 1)");
			statement.executeUpdate("INSERT INTO artifacts (weapon, armor, helm, level) " +
					"VALUES ('Specter Slasher', 'Seraph Leather Armor', 'Seraph Helmet', 2)");
			statement.executeUpdate("INSERT INTO artifacts (weapon, armor, helm, level) " +
					"VALUES ('Dark Helios Slasher', 'Bloody Eternal Leather Armor', 'Eternal Circlet', 3)");

			statement.executeUpdate("INSERT INTO enemy (name, attack, hit_points, level, ascii) VALUES ('Ariarc', 50, 150, 1, '❶')");
			statement.executeUpdate("INSERT INTO enemy (name, attack, hit_points, level, ascii) VALUES ('Lebruum', 100, 250, 2, '❷')");
			statement.executeUpdate("INSERT INTO enemy (name, attack, hit_points, level, ascii) VALUES ('Hunchback Kwai', 200, 500, 3, '❸')");


		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static Connection getConnection() {
		return connection;
	}
}

