package blargh.rpg.warhammer;

public class Characteristic {
	private Characteristics type;
	private int advances;
	private int initialValue;

	private static int[] costs = {25, 30, 40, 50, 70, 90, 120, 150, 190, 230, 280, 330, 390, 450, 520};

	Characteristic(Characteristics type, int initialValue){
		this.type = type;
		this.initialValue = initialValue;
		this.advances = 0;
	}

	public static int cost(int currentAdvances) {

		int costIndex = (int)(currentAdvances/5);
		if(costIndex >= costs.length) {
			costIndex = costs.length - 1;
		}

		return costs[costIndex];
	}

	public int value() {
		return initialValue + advances;
	}

	public void advance(int advances) {
		this.advances += advances; 
	}

	public Characteristics getType() {
		return type;
	}

	public int getAdvances() {
		return advances;
	}

	@Override
	public String toString() {
		return String.format("Characteristic [type=%s, advances=%s, initialValue=%s]", type, advances, initialValue);
	}
}
