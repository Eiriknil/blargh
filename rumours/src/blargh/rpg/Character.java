package blargh.rpg;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public interface Character {

	public class TrainingResult {

		private int usedXp;
		private int endValue;

		public TrainingResult(int usedXp, int endValue) {
			this.usedXp = usedXp;
			this.endValue = endValue;
		}

		public int remainingXp() {
			return usedXp;
		}

		public int endValue() {
			return endValue;
		}
	}

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

	public enum Characteristics {
		M, WS, BS, S, T, I, AG, DEX, INT, WP, FEL;
	}

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

	public enum Skill {
		ART(Characteristics.DEX), ATHLETICS(Characteristics.AG), BRIBERY(Characteristics.FEL), CHARM(Characteristics.FEL), CHARM_ANIMAL(Characteristics.WP), 
		CLIMB(Characteristics.S), COOL(Characteristics.WP), CONSUME_ALCOHOL(Characteristics.T), DODGE(Characteristics.AG), DRIVE(Characteristics.AG), 
		ENDURANCE(Characteristics.T), ENTERTAIN(Characteristics.FEL), GAMBLE(Characteristics.INT), GOSSIP(Characteristics.FEL), HAGGLE(Characteristics.FEL), 
		INTIMIDATE(Characteristics.S), INTUITION(Characteristics.I), LEADERSHIP(Characteristics.FEL), MELEE(Characteristics.WS), NAVIGATION(Characteristics.I), 
		OUTDOOR_SURVIVAL(Characteristics.INT), PERCEPTION(Characteristics.I), RIDE(Characteristics.AG), ROW(Characteristics.S), STEALTH(Characteristics.AG),
		ANIMAL_CARE(Characteristics.INT), ANIMAL_TRAINING(Characteristics.FEL), CHANNELING(Characteristics.WP), EVALUATE(Characteristics.INT), 
		HEAL(Characteristics.INT), LANGUAGE(Characteristics.INT), LORE(Characteristics.INT), PERFORM(Characteristics.FEL), PICK_LOCK(Characteristics.DEX), 
		PLAY(Characteristics.FEL), PRAY(Characteristics.WP), RANGED(Characteristics.BS), RESEARCH(Characteristics.INT), SAIL(Characteristics.AG), 
		SECRET_SIGNS(Characteristics.INT), SET_TRAP(Characteristics.DEX), SLEIGHT_OF_HAND(Characteristics.DEX), SWIM(Characteristics.S), 
		TRACK(Characteristics.I), TRADE(Characteristics.FEL);

		private Characteristics characteristic;
		private Skill() {}
		private Skill(Characteristics characteristic) {
			this.characteristic = characteristic;
		}
	}

	public enum Talents {
		ACCURATE_SHOT, ACUTE_SENSE, AETHYRIC_ATTUNEMENT, ALLEY_CAT, AMBIDEXTROUS, ANIMAL_AFFINITY, ARCANE_MAGIC_LORE, ARGUMENTATIVE, ARTISTIC, ATTRACTIVE,
		AVERAGE_SIZE, BATTLE_RAGE, BEAT_BLADE, BENEATH_NOTICE, BERSERK_CHARGE, BLATHER, BLESS, BOOKISH, BREAK_AND_ENTER, BRIBER, CARDSHARP, CAREFUL_STRIKE,
		CAROUSER, CATFALL, CAT_TONGUED, CHAOS_MAGIC_LORE, COMBAT_AWARE, COMBAT_MASTER, COMBAT_REFLEXES, COMMANDING_PRESENCE, CONCOCT, CONTORTIONIST, 
		COOLHEADED, CRACK_THE_WHIP, CRAFTSMAN_TRADE, CRIMINAL, DEADEYE_SHOT, DEALMAKER, DETECT_ARTEFACT, DICEMAN, DIRTY_FIGHTING, DISARM, DISTRACT, DOOMED, 
		DRILLED, DUAL_WIELDER, EMBEZZLE, ENCLOSED_FIGHTER, ETIQUETTE_SOCIAL_GROUP, FAST_HANDS, FAST_SHOT, FEARLESS_ANY, FEINT, FIELD_DRESSING, FISHERMAN, 
		FLAGELLANT, FLEE, FLEET_FOOTED, FRENZY, FRIGHTENING, FURIOUS_ASSAULT, GREGARIOUS, GUNNER, HARDY, HATRED_GROUP, HOLY_HATRED, HOLY_VISIONS, HUNTERS_EYE, 
		IMPASSIONED_ZEAL, IMPLACABLE, IN_FIGHTER, INSPIRING, INSTINCTIVE_DICTION, INVOKE_ANY, IRON_JAW, IRON_WILL, JUMP_UP, KINGPIN, LIGHTNING_REFLEXES, 
		LINGUISTICS, LIP_READING, LUCK, MAGICAL_SENSE, MAGIC_RESISTANCE, MAGNUM_OPUS, MARKSMAN, MASTER_OF_DISGUISE, MASTER_ORATOR, MASTER_TRADESMAN_TRADE, 
		MENACING, MIMIC, NIGHT_VISION, NIMBLE_FINGERED, NOBLE_BLOOD, NOSE_FOR_TROUBLE, NUMISMATICS, OLD_SALT, ORIENTATION, PANHANDLE, PERFECT_PITCH, PETTY_MAGIC, 
		PHARMACIST, PILOT, PUBLIC_SPEAKING, PURE_SOUL, RAPID_RELOAD, REACTION_STRIKE, READ_WRITE, RELENTLESS, RESISTANCE_THREAT, RESOLUTE, REVERSAL, RIPOSTE, 
		RIVER_GUIDE, ROBUST, ROUGHRIDER, ROVER, SAVANT_LORE, SAVVY, SCALE_SHEER_SURFACE, SCHEMER, SEA_LEGS, SEASONED_TRAVELLER, SECOND_SIGHT, SECRET_IDENTITY, 
		SHADOW, SHARP, SHARPSHOOTER, SHIELDSMAN, SIXTH_SENSE, SLAYER, SMALL, SNIPER, SPEEDREADER, SPRINTER, STEP_ASIDE, STONE_SOUP, STOUT_HEARTED, 
		STRIDER_TERRAIN, STRIKE_MIGHTY_BLOW, STRIKE_TO_INJURE, STRIKE_TO_STUN, STRONG_BACK, STRONG_LEGS, STRONG_MINDED, STRONG_SWIMMER, STURDY, SUAVE, 
		SUPER_NUMERATE, SUPPORTIVE, SURE_SHOT, SURGERY, TENACIOUS, TINKER, TOWER_OF_MEMORIES, TRAPPER, TRICK_RIDING, TUNNEL_RAT, UNSHAKEABLE, VERY_RESILIENT, 
		VERY_STRONG, WAR_LEADER, WAR_WIZARD, WARRIOR_BORN, WATERMAN, WEALTHY, WELL_PREPARED, WITCH
	}

	public int maxWounds();
	
	public int currentWounds();
	public List<Crit> crits();
	public void applyWounds(int wound);
	public void applyCrit(Crit crit);

	public int characteristic(Characteristics characteristics);
	public int characteristicBonus(Characteristics characteristics);
	public int charAdvances(Characteristics characteristic);
	public TrainingResult trainCharacteristic(Characteristics characteristic, int xp);

	public int skillValue(Skill skill);
	public int skillAdvances(Skill skill);
	public int advanceSkill(Skill skill, int value);
	public int checkSkill(Skill skill);
	public int checkSkill(Skill skill, Modifier modifier);
	public TrainingResult trainSkill(Skill skill, int xp);

	
	public static Set<Skill> advancedSkills(){
		return new HashSet<>(Arrays.asList(Skill.ANIMAL_CARE, Skill.ANIMAL_TRAINING, Skill.CHANNELING, Skill.EVALUATE, Skill.HEAL, Skill.LANGUAGE, 
				Skill.LORE, Skill.PERFORM, Skill.PICK_LOCK, Skill.PLAY, Skill.PRAY, Skill.RANGED, Skill.RESEARCH, Skill.SAIL, Skill.SECRET_SIGNS, 
				Skill.SET_TRAP, Skill.SLEIGHT_OF_HAND, Skill.SWIM, Skill.TRACK, Skill.TRADE));
	}

	public static Set<Skill> basicSkills(){
		return new HashSet<>(Arrays.asList(Skill.ART, Skill.ATHLETICS, Skill.BRIBERY, Skill.CHARM, Skill.CHARM_ANIMAL, Skill.CLIMB, Skill.COOL,
				Skill.CONSUME_ALCOHOL, Skill.DODGE, Skill.DRIVE, Skill.ENDURANCE, Skill.ENTERTAIN, Skill.GAMBLE,
				Skill.GOSSIP, Skill.HAGGLE, Skill.INTIMIDATE, Skill.INTUITION, Skill.LEADERSHIP, Skill.MELEE,
				Skill.NAVIGATION, Skill.OUTDOOR_SURVIVAL, Skill.PERCEPTION, Skill.RIDE, Skill.ROW, Skill.STEALTH));
	}

	public static class Factory {

		private static Random random = new Random();

		public static void setRandomizer(Random random) {
			Factory.random = random;
		}
		
		public static Character create() {

			return new CharacterImpl();
		}
		
		public static Character create(Map<Characteristics, Characteristic> statMap) {
			return new CharacterImpl(statMap);
		}

		private static class CharacterImpl implements Character {
			
			private Map<Characteristics, Characteristic> charMap = new ConcurrentHashMap<>();
			private List<Talents> talentList = new CopyOnWriteArrayList<>();
			private Map<Skill, Integer> skillMap = new ConcurrentHashMap<>();
			private int woundsTaken = 0;
			private List<Crit> critList = new CopyOnWriteArrayList<>();
			private static final int[] skillCosts = {10, 15, 20, 30, 40, 60, 80, 110, 140, 180, 220, 270, 320, 380};

			public CharacterImpl(Map<Characteristics, Characteristic> charMap) {
				this.charMap = new ConcurrentHashMap<>(charMap);
			}

			public CharacterImpl() {
				Arrays.stream(Characteristics.values()).forEach(stat -> charMap.put(stat, new Characteristic(stat, 20 + 2 + random.nextInt(10) + random.nextInt(10))));
				charMap.put(Characteristics.M, new Characteristic(Characteristics.M, 4));
				basicSkills().forEach(skill -> skillMap.put(skill, 0));
			}

			@Override
			public int skillAdvances(Skill skill) {

				return skillMap.getOrDefault(skill, 0);
			}

			@Override
			public int maxWounds() {

				if(talentList.contains(Talents.SMALL)) {
					return characteristicBonus(Characteristics.T)*2 + characteristicBonus(Characteristics.WP);
				}
				return characteristicBonus(Characteristics.T)*2 + characteristicBonus(Characteristics.WP) + characteristicBonus(Characteristics.S);
			}

			@Override
			public int currentWounds() {
				return maxWounds() - woundsTaken;
			}

			@Override
			public List<Crit> crits() {
				return new CopyOnWriteArrayList<>(critList);
			}

			@Override
			public int characteristicBonus(Characteristics characteristics) {

				return (int)(characteristic(characteristics))/10;
			}

			@Override
			public int characteristic(Characteristics characteristic) {

				return charMap.get(characteristic).value();
			}

			@Override
			public int checkSkill(Skill skill) {
				return checkSkill(skill, Modifier.CHALLENGING);
			}

			@Override
			public int checkSkill(Skill skill, Modifier modifier) {
				int roll = random.nextInt(100) + 1;
				int successLevel = (int)((skillValue(skill) + modifier.value())/10) - (int)(roll/10);
				
				System.out.println("Skill: " + skillValue(skill) + " Modifier: " + modifier.modifierPresentation() + " = " + (skillValue(skill) + modifier.value()) 
						+ " Roll: " + roll + " => SL = " + successLevel );
				
				return successLevel;
			}
			
			@Override
			public int skillValue(Skill skill) {
				return skillAdvances(skill) + characteristic(skill.characteristic);
			}

			@Override
			public int advanceSkill(Skill skill, int value) {
				
				Integer skillValue = skillAdvances(skill);
				skillMap.put(skill, skillValue + value);
				return 0;
			}

			@Override
			public TrainingResult trainCharacteristic(Characteristics characteristic, int xp) {

				int usedXp = 0;
				int charAdvances = charAdvances(characteristic);
				int boughtAdvances = 0;
				int cost = Characteristic.cost(charAdvances + boughtAdvances);
				while(cost <= xp - usedXp) {
					boughtAdvances++;
					usedXp += cost;
					cost = Characteristic.cost(charAdvances + boughtAdvances);
				}
				
				charMap.get(characteristic).advance(boughtAdvances);
				
				System.out.println(characteristic.name() + " advanced " + boughtAdvances + " to " + charMap.get(characteristic).value() + " using " + usedXp + " of " + xp + " xp");
				
				return new TrainingResult(xp - usedXp, charMap.get(characteristic).getAdvances());
			}

			@Override
			public TrainingResult trainSkill(Skill skill, int xp) {

				int usedXp = 0;
				int skillAdvances = skillMap.get(skill);
				int boughtAdvances = 0;
				int cost = skillCost(skillAdvances + boughtAdvances);
				while(cost <= xp - usedXp) {
					boughtAdvances++;
					usedXp += cost;
					cost = skillCost(skillAdvances + boughtAdvances);
				}
				
				advanceSkill(skill, boughtAdvances);
				
				System.out.println(skill.name() + " advanced " + boughtAdvances + " to " + skillMap.get(skill) + " using " + usedXp + " of " + xp + " xp");
				
				return new TrainingResult(xp - usedXp, skillMap.get(skill));
			}

			private int skillCost(int currentAdvances) {
				
				int costIndex = (int)(currentAdvances/5);
				if(costIndex >= skillCosts.length) {
					costIndex = skillCosts.length - 1;
				}
				
				return skillCosts[costIndex];
			}

			@Override
			public int charAdvances(Characteristics characteristic) {
				
				return charMap.get(characteristic).getAdvances();
			}

			@Override
			public void applyWounds(int wounds) {
				woundsTaken += wounds;
				if(woundsTaken > maxWounds()) {
					Crit newCrit = Crit.Factory.create(woundsTaken - maxWounds());
					applyCrit(newCrit);
					woundsTaken = maxWounds();
				}
			}

			@Override
			public void applyCrit(Crit crit) {
				critList.add(crit);
				if(critList.size() > characteristicBonus(Characteristics.T)) {
					throw new RuntimeException("Character is dead!");
				}
			}

		}
		
		private static class Characteristic {
			private Characteristics type;
			private int advances;
			private int initialValue;

			private static int[] costs = {25, 30, 40, 50, 70, 90, 120, 150, 190, 230, 280, 330, 390, 450};

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

			public int getInitialValue() {
				return initialValue;
			}
		}
	}

	public static HitLocation randomHitLocation() {
		
		return null;
	}
	
}
