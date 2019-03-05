package blargh.rpg;

import java.util.Set;

import blargh.rpg.Character.Characteristics;
import blargh.rpg.Character.Skill;
import blargh.rpg.Character.Talents;

public interface Career {

	public Set<Skill> skillSet(int level);
	public Set<Talents> talentSet(int level);
	public Set<Characteristics> statSet(int level);
}
