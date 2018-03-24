package ua.yzcorp.controller;

import ua.yzcorp.model.Hero;

import java.sql.Connection;
import java.util.List;

public interface Manager<T> {
	List<T> getAllTarget();
}
