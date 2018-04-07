package ua.yzcorp.controller;

import ua.yzcorp.model.Artifacts;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class ArtifactsManager implements Manager<Artifacts> {
	@Override
	public List<Artifacts> getAllTarget() {
		List<Artifacts> artifacts = new LinkedList<>();
		Connection connection = ConnectSQL.getConnection();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM artifacts");
			while (resultSet.next()) {
				Artifacts items = new Artifacts();
				items.addNewItem(resultSet.getInt("level"), "Weapon");
				items.addNewItem(resultSet.getInt("level"), "Armor");
				items.addNewItem(resultSet.getInt("level"), "Helm");
				artifacts.add(items);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return artifacts;
	}
}