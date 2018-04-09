package ua.yzcorp.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Artifacts {
	private Map<String, Item> artifacts = new LinkedHashMap<>();

	public Artifacts() {
	}

	public Map<String, Item> getArtifacts() {
		return artifacts;
	}

	public void addNewItem(int level, String type) {
		artifacts.put(type, new Item(level, type));
	}

	@Override
	public String toString() {
		StringBuilder returnValue = new StringBuilder();
		if (artifacts.isEmpty()) {
			return "\tWeapon:\tmissing;\n" +
					"\tArmor:\tmissing;\n" +
					"\tHelm:\tmissing;\n";
		}
		else {
			for (Map.Entry<String, Item> entry: artifacts.entrySet()){
				returnValue.append("\t").append(entry.getKey()).append(":\t").append(entry.getValue().getName()).append(" (+").
						append(entry.getValue().getPoint()).append("% ").append(entry.getValue().getArtibute()).append(")\n");
			}
		}
		return returnValue.toString();
	}

	public String toString(String type) {
		return artifacts.get(type).toString(type);
	}
}