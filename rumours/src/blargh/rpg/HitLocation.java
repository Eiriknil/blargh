package blargh.rpg;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum HitLocation {
	HEAD(0),
	LEFT_ARM(9),
	RIGHT_ARM(24),
	BODY(44),
	LEFT_LEG(79),
	RIGHT_LEG(89);
	
	private int range;
	private static Random randomizer = new Random();

	private HitLocation(int range) {
		this.range = range;
	}
	
	public static HitLocation randomHitLocation() {
		
		int roll = randomizer.nextInt(100);
		List<HitLocation> locations = Arrays.stream(values()).filter(location -> location.range < roll).collect(Collectors.toList());
		System.out.println("Hit location roll = " + roll + " => " + locations.get(locations.size() - 1).name());
		
		return locations.get(locations.size() -1);
	}

	public static void setRandomizer(Random randomizer) {
		HitLocation.randomizer = randomizer;
	}
}
