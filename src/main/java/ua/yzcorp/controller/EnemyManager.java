package ua.yzcorp.controller;

import ua.yzcorp.model.Enemy;
import ua.yzcorp.model.Enemy.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class EnemyManager implements Manager<Enemy> {

	@Override
	public List<Enemy> getAllTarget() {
		List<Enemy> enemies = new LinkedList<>();
		Connection connection = ConnectSQL.getConnection();
		EnemyBuilder enemy = new EnemyBuilder();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM enemy");
			while (resultSet.next()) {
				enemy.setName(resultSet.getString("name"));
				enemy.setAttack(resultSet.getInt("attack"));
				enemy.setHP(resultSet.getInt("hit_points"));
				enemy.setLevel(resultSet.getInt("level"));
				enemy.setAscii(resultSet.getString("ascii").charAt(0));
				enemies.add(enemy.build());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return enemies;
	}
}
