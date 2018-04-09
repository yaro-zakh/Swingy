package ua.yzcorp.controller;

import ua.yzcorp.model.Hero;
import ua.yzcorp.model.Hero.*;
import ua.yzcorp.view.Console;
import ua.yzcorp.view.Gui;
import ua.yzcorp.view.Message;
import static ua.yzcorp.controller.Glob.HERO;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class HeroManager implements Manager<Hero> {

	public HeroManager() {
	}

	public boolean startListener(String text) {
		switch (text.toLowerCase()) {
			case "create":
				HERO = Console.createHero();
				save(HERO, ConnectSQL.getConnection());
				Message.print("You have successfully created a new hero");
				break;
			case "choose":
				HERO = Console.chooseHero();
				break;
			case "gui":
				Gui.start();
				return true;
			case "exit":
				Message.goodBye();
				break;
			default:
				Message.print(Glob.RED + "Please enter correct command" + Glob.RESET);
		}
		return false;
	}

	public String emptyName() {
		return Glob.RED + "Enter correct name.\n" +
				"You can use only English letters, numbers, symbols.\n" +
				"The length of the field can be from 1 to 30 characters." + Glob.RESET;
	}

	public String existsName() {
		return Glob.RED + "This name already exists. Enter a different name" + Glob.RESET;
	}

	public void deleteHero(String name, Connection connection) {
		try {
			String delete = "DELETE FROM hero WHERE nickname = ?";
			PreparedStatement statement = connection.prepareStatement(delete);
			statement.setString(1, name);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateHero(Hero target, Connection connection) {
		try {
			String update = "UPDATE hero SET level = ?, experience = ?, attack = ?, defense = ?, hit_points = ? WHERE nickname = ?";
			PreparedStatement statement = connection.prepareStatement(update);
			statement.setInt(1, target.getLevel());
			statement.setInt(2, target.getExp());
			statement.setInt(3, target.getAttack());
			statement.setInt(4, target.getDef());
			statement.setInt(5, target.getHP());
			statement.setString(6, target.getName());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void save(Hero target, Connection connection) {
		try {
			String insertInto = "INSERT INTO hero (nickname, class, level, experience, attack, defense, hit_points, cc) " +
					"VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(insertInto);
			statement.setString(1, target.getName());
			statement.setString(2, target.getClassHero());
			statement.setInt(3, target.getLevel());
			statement.setInt(4, target.getExp());
			statement.setInt(5, target.getAttack());
			statement.setInt(6, target.getDef());
			statement.setInt(7, target.getHP());
			statement.setInt(8, target.getCC());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Hero> getAllTarget() {
		List<Hero> heroes = new LinkedList<>();
		Connection connection = ConnectSQL.getConnection();
		HeroBuilder hero = new HeroBuilder();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM hero");
			while (resultSet.next()) {
				hero.setName(resultSet.getString("nickname"));
				hero.setClassHero(resultSet.getString("class"));
				hero.setLevel(resultSet.getInt("level"));
				hero.setExp(resultSet.getInt("experience"));
				hero.setCC(resultSet.getInt("CC"));
				hero.setAttack(resultSet.getInt("attack"));
				hero.setDef(resultSet.getInt("defense"));
				hero.setHP(resultSet.getInt("hit_points"));
				heroes.add(hero.build());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return heroes;
	}

	public static boolean checkName(String name) {
		Connection connection = ConnectSQL.getConnection();
		try {
			Statement statement = connection.createStatement();
			String getAllName = "SELECT nickname FROM hero WHERE  lower(nickname) = '" + name.toLowerCase() + "'";
			ResultSet resultSet = statement.executeQuery(getAllName);
			if (resultSet.next()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}
