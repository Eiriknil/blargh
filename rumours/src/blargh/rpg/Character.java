package blargh.rpg;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Character {

	public enum Characteristics {
		M,
		WS,
		BS,
		S,
		T,
		I,
		AG,
		DEX,
		INT,
		WP,
		FEL
	}

	public enum HitLocation {
		HEAD,
		RIGHT_ARM,
		LEFT_ARM,
		BODY,
		RIGHT_LEG,
		LEFT_LEG
	}

	public enum Skill {
		ART(Characteristics.DEX), ATHLETICS(Characteristics.AG), BRIBERY(Characteristics.FEL), CHARM(Characteristics.FEL), CHARM_ANIMAL(Characteristics.WP), 
		CLIMB(Characteristics.S), COOL(Characteristics.WP), CONSUME_ALCOHOL(Characteristics.T), DODGE(Characteristics.AG), DRIVE(Characteristics.AG), 
		ENDURANCE(Characteristics.T), ENTERTAIN(Characteristics.FEL), GAMBLE(Characteristics.INT), GOSSIP(Characteristics.FEL), HAGGLE(Characteristics.FEL), 
		INTIMIDATE(Characteristics.S), INTUITION(Characteristics.I), LEADERSHIP(Characteristics.FEL), MELEE(Characteristics.WS), NAVIGATION(Characteristics.I), 
		OUTDOOR_SURVIVAL(Characteristics.INT), PERCEPTION(Characteristics.I), RIDE(Characteristics.AG), ROW(Characteristics.S), STEALTH(Characteristics.AG),
		ANIMAL_CARE, ANIMAL_TRAINING, CHANNELING, EVALUATE, HEAL,
		LANGUAGE, LORE, PERFORM, PICK_LOCK, PLAY, PRAY, RANGED, RESEARCH,
		SAIL, SECRET_SIGNS, SET_TRAP, SLEIGHT_OF_HAND, SWIM, TRACK, TRADE;

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
	public int characteristic(Characteristics characteristics);
	public int characteristicBonus(Characteristics characteristics);
	public int skillValue(Skill skill);

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

		public Character create() {

			return new CharacterImpl();
		}
		
		public Character create(Map<Characteristics, Integer> statMap) {
			return new CharacterImpl(statMap);
		}

		private static class CharacterImpl implements Character {

			private Map<Characteristics, Integer> charMap = new ConcurrentHashMap<>();
			private List<Talents> talentList = new CopyOnWriteArrayList<>();
			private Map<Skill, Integer> skillMap = new ConcurrentHashMap<>();
			private int woundsTaken = 0;
			private List<Crit> critList = new CopyOnWriteArrayList<>();

			public CharacterImpl(Map<Characteristics, Integer> charMap) {
				this.charMap = new ConcurrentHashMap<>(charMap);
			}

			public CharacterImpl() {
				Arrays.stream(Characteristics.values()).forEach(stat -> charMap.put(stat, 30));
				charMap.put(Characteristics.M, 4);
			}

			@Override
			public int skillValue(Skill skill) {

				return skillMap.get(skill);
			}

			@Override
			public int maxWounds() {
				return calculateMaxWounds();
			}

			private int calculateMaxWounds() {

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

				return (int)(characteristic(characteristics) - 0.5)/10;
			}

			@Override
			public int characteristic(Characteristics characteristic) {

				return charMap.get(characteristic);
			}
		}
	}
}
