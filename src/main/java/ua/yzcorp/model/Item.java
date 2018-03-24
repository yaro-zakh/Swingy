package ua.yzcorp.model;

import ua.yzcorp.controller.ConnectSQL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Item {
	private String	attribute;
	private int		point;
	private int		level;
	private String	name;

	public Item(int level, String type) {
		Connection connection = ConnectSQL.getConnection();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT " + type + " FROM artifacts WHERE level = " + level);
			this.name = resultSet.getString(type);
			this.level = level;
			this.point = level * 10;
			switch (type.toLowerCase()) {
				case "weapon":
					this.attribute = "ATK";
					break;
				case "armor":
					this.attribute = "DEF";
					break;
				case "helm":
					this.attribute = "HP";
					break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getArtibute() {
		return attribute;
	}

	public int getPoint() {
		return point;
	}

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public String toString(String type) {
		return "\t" + type + ": " + name + " (+" + point + "% " + attribute + ")\n";
	}
}
