package blargh.rpg;

import java.util.Random;

import blargh.rpg.warhammer.Races;

public class TestRaceGeneration {

	public static void main(String... args) {
		
		System.out.println(Races.HUMAN.randomSkill(new Random()));
		System.out.println(Races.randomTalent(new Random()));
	}
	
}
