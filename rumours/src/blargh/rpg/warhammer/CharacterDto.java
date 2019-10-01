package blargh.rpg.warhammer;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import blargh.rpg.warhammer.Character.Skill;
import blargh.rpg.warhammer.Character.Talent;

public class CharacterDto {

	private Map<Characteristics, Characteristic> characteristics = new ConcurrentHashMap<>();
	private Map<Talent, Integer> talents = new TreeMap<>();
	private Map<Skill, Integer> skills = new ConcurrentHashMap<>();
	private Races race;
//	private CareerDto career;
	
	public Map<Characteristics, Characteristic> getCharacteristics() {
		return characteristics;
	}
	
	public void setCharacteristics(Map<Characteristics, Characteristic> characteristics) {
		this.characteristics = characteristics;
	}
	
	public Map<Talent, Integer> getTalents() {
		return talents;
	}
	
	public void setTalents(Map<Talent, Integer> talents) {
		this.talents = talents;
	}
	
	public Map<Skill, Integer> getSkills() {
		return skills;
	}
	
	public void setSkills(Map<Skill, Integer> skills) {
		this.skills = skills;
	}
	
	public Races getRace() {
		return race;
	}
	
	public void setRace(Races race) {
		this.race = race;
	}
	
//	public CareerDto getCareer() {
//		return career;
//	}
//	public void setCareer(CareerDto career) {
//		this.career = career;
//	}

	
}
