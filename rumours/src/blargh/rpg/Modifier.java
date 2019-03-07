package blargh.rpg;

public enum Modifier {
	VERY_EASY(60),
	EASY(40),
	AVERAGE(20),
	CHALLENGING(0),
	DIFFICULT(-10),
	HARD(-20),
	VERY_HARD(-30);
	
	private int value;

	private Modifier(int value) {
		this.value = value;
	}
	
	public int value() {
		return value;
	}
	
	public String modifierPresentation() {
		if(value >= 0) {
			return name() + "(+" + value + ")";
		}
		
		return name() + "(" + value + ")";
	}
}
