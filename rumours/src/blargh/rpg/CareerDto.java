package blargh.rpg;

import java.util.List;
import java.util.Map;

public class CareerDto {

	private String career;
	private List<Map<String, List<String>>> level;
	
	public String getCareer() {
		return career;
	}
	
	public void setCareer(String career) {
		this.career = career;
	}
	
	public List<Map<String, List<String>>> getLevel() {
		return level;
	}
	
	public void setLevel(List<Map<String, List<String>>> level) {
		this.level = level;
	}
}
