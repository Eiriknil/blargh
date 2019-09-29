package blargh.rpg.warhammer;

public class TalentDto {

	private String name;
	private String max;
	private String tests;
	private String description;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	public String getTests() {
		return tests;
	}
	public void setTests(String tests) {
		this.tests = tests;
	}
	public String getDescription() {
		return description;
	}
	@Override
	public String toString() {
		return "TalentDto [name=" + name + ", max=" + max + ", tests=" + tests + ", description=" + description + "]";
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
