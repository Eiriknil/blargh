package blargh.rpg;

import static blargh.rpg.Characteristics.AG;
import static blargh.rpg.Characteristics.BS;
import static blargh.rpg.Characteristics.DEX;
import static blargh.rpg.Characteristics.FEL;
import static blargh.rpg.Characteristics.I;
import static blargh.rpg.Characteristics.INT;
import static blargh.rpg.Characteristics.S;
import static blargh.rpg.Characteristics.T;
import static blargh.rpg.Characteristics.WP;
import static blargh.rpg.Characteristics.WS;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Skills {
	ART(DEX), ATHLETICS(AG), BRIBERY(FEL), CHARM(FEL), CHARM_ANIMAL(WP), 
	CLIMB(S), COOL(WP), CONSUME_ALCOHOL(T), DODGE(AG), DRIVE(AG), 
	ENDURANCE(T), ENTERTAIN(FEL), GAMBLE(INT), GOSSIP(FEL), HAGGLE(FEL), 
	INTIMIDATE(S), INTUITION(I), LEADERSHIP(FEL), MELEE(WS, "Basic"), 
	NAVIGATION(I), OUTDOOR_SURVIVAL(INT), PERCEPTION(I), RIDE(AG), ROW(S), STEALTH(AG),
	
	ANIMAL_CARE(INT), ANIMAL_TRAINING(INT), CHANNELLING(WP), EVALUATE(INT), HEAL(INT), LANGUAGE(INT), LORE(INT), 
	PERFORM(AG), PICK_LOCK(DEX), PLAY(DEX), PRAY(FEL), RANGED(BS), RESEARCH(INT), SAIL(AG), SECRET_SIGNS(INT), SET_TRAP(DEX), SLEIGHT_OF_HAND(DEX), SWIM(S), TRACK(I), 
	TRADE(DEX); 

	private Characteristics characteristic;
	private String defaultSpec = "";
	
	private Skills() {}
	
	private Skills(Characteristics characteristic) {
		this.characteristic = characteristic;
	}

	private Skills(Characteristics characteristic, String defaultSpec) {
		this.characteristic = characteristic;
	}
	
	public Characteristics characteristics() {
		return characteristic;
	}

	public static Set<Skills> basicSkills(){
		return new HashSet<>(Arrays.asList(
				ART, ATHLETICS, BRIBERY, CHARM, CHARM_ANIMAL, CLIMB, COOL,
				CONSUME_ALCOHOL, DODGE, DRIVE, ENDURANCE, 
				ENTERTAIN, GAMBLE, GOSSIP, HAGGLE, INTIMIDATE, INTUITION, LEADERSHIP, 
				MELEE, NAVIGATION, OUTDOOR_SURVIVAL, PERCEPTION, RIDE, ROW, STEALTH));
	}
	
	public static Set<Skills> advancedSkills(){
		return new HashSet<>(Arrays.asList(
				ANIMAL_CARE, ANIMAL_TRAINING, CHANNELLING, EVALUATE, HEAL, 
				LANGUAGE, LORE, PERFORM, PICK_LOCK, PLAY, PRAY, RANGED, 
				RESEARCH, SAIL, SECRET_SIGNS, SET_TRAP, SLEIGHT_OF_HAND, SWIM, TRACK, TRADE));
	}
	
	public String defaultSpec() {
		return defaultSpec;
	}
}
