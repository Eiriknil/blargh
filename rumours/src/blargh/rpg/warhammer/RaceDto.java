package blargh.rpg.warhammer;

import java.util.List;
import java.util.Map;

public class RaceDto {

	private String race;
	private List<String> skills;
	private List<String> talents;
	private int random;
	private List<Map<String, String>> choice;
	
	public String getRace() {
		return race;
	}
	
	public void setRace(String race) {
		this.race = race;
	}
	
	public List<String> getSkills() {
		return skills;
	}
	
	public void setSkills(List<String> skills) {
		this.skills = skills;
	}
	
	public List<String> getTalents() {
		return talents;
	}
	
	public void setTalents(List<String> talents) {
		this.talents = talents;
	}
	
	public int getRandom() {
		return random;
	}
	
	public void setRandom(int random) {
		this.random = random;
	}

	public List<Map<String, String>> getChoice() {
		return choice;
	}

	public void setChoice(List<Map<String, String>> choice) {
		this.choice = choice;
	}

	@Override
	public String toString() {
		return "RaceDto [race=" + race + ", skills=" + skills + ", talents=" + talents + ", random=" + random
				+ ", choice=" + choice + "]";
	}

	
}
