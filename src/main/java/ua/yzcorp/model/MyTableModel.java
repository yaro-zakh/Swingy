package ua.yzcorp.model;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MyTableModel extends AbstractTableModel {

	private List<Hero> heroes;

	public MyTableModel(List<Hero> heroes) {
		super();
		this.heroes = heroes;
	}

	public void setHeroes(List<Hero> heroes) {
		this.heroes = heroes;
	}

	@Override
	public int getRowCount() {
		return heroes.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
			case 0: return "Number";
			case 1: return "Nickname";
			case 2: return "Class";
			case 3: return "Level";
		}
		return super.getColumnName(column);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0:
				return rowIndex;
			case 1:
				return heroes.get(rowIndex).getName();
			case 2:
				return heroes.get(rowIndex).getClassHero();
			case 3:
				return heroes.get(rowIndex).getLevel();
			default:
				return "";
		}
	}
}