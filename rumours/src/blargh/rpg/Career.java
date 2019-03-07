package blargh.rpg;

import java.util.Set;

public interface Career {

	public Set<Skills> skillSet(int level);
	public Set<Talents> talentSet(int level);
	public Set<Characteristics> statSet(int level);
}
