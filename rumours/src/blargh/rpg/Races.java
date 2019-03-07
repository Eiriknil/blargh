package blargh.rpg;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Races {
	
	HUMAN(4,20,20,20,20,20,20,20,20,20,20,0),
	DWARF(3,30,20,20,30,10,20,20,20,40,10,0),
	HALFLING(3,10,20,10,20,20,30,20,20,30,30,0),
	HIGH_ELF(5,30,30,20,20,30,40,30,30,30,20,0),
	WOOD_ELF(5,30,30,20,20,30,40,30,30,30,20,0);
	
	private Map<Characteristics, Integer> modifierMap = new ConcurrentHashMap<>();
	
	private Races(Integer... modifiers) {
		
		int index = 0;
		for(Characteristics stat:Characteristics.values()) {
			modifierMap.put(stat, modifiers[index++]);
		}
	}
	
	public int statModifier(Characteristics stat) {
		return modifierMap.get(stat);
	}
}
