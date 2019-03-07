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
	ART(DEX, true), ATHLETICS(AG), BRIBERY(FEL), CHARM(FEL), CHARM_ANIMAL(WP), 
	CLIMB(S), COOL(WP), CONSUME_ALCOHOL(T), DODGE(AG), DRIVE(AG, true), 
	ENDURANCE(T), ENTERTAIN(FEL, true), GAMBLE(INT), GOSSIP(FEL), HAGGLE(FEL), 
	INTIMIDATE(S), INTUITION(I), LEADERSHIP(FEL), MELEE(WS, true), NAVIGATION(I), 
	OUTDOOR_SURVIVAL(INT), PERCEPTION(I), RIDE(AG, true), ROW(S), STEALTH(AG),
	ANIMAL_CARE(INT), ANIMAL_TRAINING(INT), CHANNELING(WP), EVALUATE(INT), 
	HEAL(INT), LANGUAGE(INT), LORE(INT), PERFORM(AG), PICK_LOCK(DEX), 
	PLAY(DEX), PRAY(FEL), RANGED(BS), RESEARCH(INT), SAIL(AG), 
	SECRET_SIGNS(INT), SET_TRAP(DEX), SLEIGHT_OF_HAND(DEX), SWIM(S), 
	TRACK(I), TRADE(DEX);

	private Characteristics characteristic;
	private boolean grouped;
	private Skills() {}
	private Skills(Characteristics characteristic) {
		this.characteristic = characteristic;
		this.grouped = false;
	}

	private Skills(Characteristics characteristic, boolean grouped) {
		this.characteristic = characteristic;
		this.grouped = grouped;
	}
	
	public boolean isGrouped() {
		return grouped;
	}
	
	public Characteristics characteristics() {
		return characteristic;
	}
	
	public static Set<Skills> advancedSkills(){
		return new HashSet<>(Arrays.asList(ANIMAL_CARE, ANIMAL_TRAINING, CHANNELING, EVALUATE, HEAL, LANGUAGE, 
				LORE, PERFORM, PICK_LOCK, PLAY, PRAY, RANGED, RESEARCH, SAIL, SECRET_SIGNS, 
				SET_TRAP, SLEIGHT_OF_HAND, SWIM, TRACK, TRADE));
	}

	public static Set<Skills> basicSkills(){
		return new HashSet<>(Arrays.asList(ART, ATHLETICS, BRIBERY, CHARM, CHARM_ANIMAL, CLIMB, COOL,
				CONSUME_ALCOHOL, DODGE, DRIVE, ENDURANCE, ENTERTAIN, GAMBLE,
				GOSSIP, HAGGLE, INTIMIDATE, INTUITION, LEADERSHIP, MELEE,
				NAVIGATION, OUTDOOR_SURVIVAL, PERCEPTION, RIDE, ROW, STEALTH));
	}
}
