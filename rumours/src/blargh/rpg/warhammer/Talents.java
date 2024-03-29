package blargh.rpg.warhammer;

import static blargh.rpg.warhammer.Characteristics.DEX;
import static blargh.rpg.warhammer.Characteristics.FEL;
import static blargh.rpg.warhammer.Characteristics.I;
import static blargh.rpg.warhammer.Characteristics.INT;
import static blargh.rpg.warhammer.Characteristics.NONE;
import static blargh.rpg.warhammer.Characteristics.S;
import static blargh.rpg.warhammer.Characteristics.T;
import static blargh.rpg.warhammer.Characteristics.WP;
import static blargh.rpg.warhammer.Characteristics.WS;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum Talents {
	ACCURATE_SHOT, 
	ACUTE_SENSE, 
	AETHYRIC_ATTUNEMENT, 
	ALLEY_CAT, 
	AMBIDEXTROUS, 
	ANIMAL_AFFINITY, 
	ARCANE_MAGIC, 
	ARGUMENTATIVE, 
	ARTISTIC, 
	ATTRACTIVE, 
	AVERAGE_SIZE, 
	BATTLE_RAGE, 
	BEAT_BLADE, 
	BENEATH_NOTICE, 
	BERSERK_CHARGE, 
	BLATHER, 
	BLESS, 
	BOOKISH, 
	BREAK_AND_ENTER, 
	BRIBER, 
	CARDSHARP, 
	CAREFUL_STRIKE, 
	CAROUSER, 
	CATFALL, 
	CAT_TONGUED, 
	CHAOS_MAGIC_LORE, 
	COMBAT_AWARE, 
	COMBAT_MASTER, 
	COMBAT_REFLEXES, 
	COMMANDING_PRESENCE, 
	CONCOCT, 
	CONTORTIONIST, 
	COOLHEADED, 
	CRACK_THE_WHIP, 
	CRAFTSMAN, 
	CRIMINAL, 
	DEADEYE_SHOT, 
	DEALMAKER, 
	DETECT_ARTEFACT, 
	DICEMAN, 
	DIRTY_FIGHTING, 
	DISARM, 
	DISTRACT, 
	DOOMED, 
	DRILLED, 
	DUAL_WIELDER, 
	EMBEZZLE, 
	ENCLOSED_FIGHTER, 
	ETIQUETTE, 
	FAST_HANDS, 
	FAST_SHOT, 
	FEARLESS, 
	FEINT, 
	FIELD_DRESSING, 
	FISHERMAN,
	FLAGELLANT,
	FLEE,
	FLEET_FOOTED,
	FRENZY,
	FRIGHTENING, 
	FURIOUS_ASSAULT, 
	GREGARIOUS, 
	GUNNER, 
	HARDY, 
	HATRED, 
	HOLY_HATRED, 
	HOLY_VISIONS, 
	HUNTERS_EYE, 
	IMPASSIONED_ZEAL, 
	IMPLACABLE, 
	IN_FIGHTER, 
	INSPIRING, 
	INSTINCTIVE_DICTION, 
	INVOKE, 
	IRON_JAW, 
	IRON_WILL, 
	JUMP_UP, 
	KINGPIN, 
	LIGHTNING_REFLEXES, 
	LINGUISTICS, 
	LIP_READING, 
	LUCK, 
	MAGICAL_SENSE, 
	MAGIC_RESISTANCE, 
	MAGNUM_OPUS, 
	MARKSMAN, 
	MASTER_OF_DISGUISE, 
	MASTER_ORATOR, 
	MASTER_TRADESMAN, 
	MASTER_CRAFTSMAN, 
	MENACING, 
	MIMIC, 
	NIGHT_VISION, 
	NIMBLE_FINGERED(DEX, 5), 
	NOBLE_BLOOD, 
	NOSE_FOR_TROUBLE, 
	NUMISMATICS,
	OLD_SALT,
	ORIENTATION,
	PANHANDLE,
	PERFECT_PITCH,
	PETTY_MAGIC,
	PHARMACIST,
	PILOT,
	PUBLIC_SPEAKER,
	PURE_SOUL,
	RAPID_RELOAD, 
	REACTION_STRIKE, 
	READ_WRITE,
	RELENTLESS,
	RESISTANCE,
	RESOLUTE,
	REVERSAL,
	RIPOSTE,
	RIVER_GUIDE,
	ROBUST,
	ROUGHRIDER,
	ROVER,
	SAVANT_APOTHECARY,
	SAVANT_THEOLOGY,
	SAVANT_ENGINEERING,
	SAVANT,
	SAVVY(INT, 5),
	SCALE_SHEER_SURFACE,
	SCHEMER,
	SEA_LEGS,
	SEASONED_TRAVELLER,
	SECOND_SIGHT,
	SECRET_IDENTITY,
	SHADOW,
	SHARP(I, 5), 
	SHARPSHOOTER, 
	SHIELDSMAN, 
	SIXTH_SENSE, 
	SLAYER, 
	SMALL, 
	SNIPER, 
	SPEEDREADER, 
	SPRINTER, 
	STEP_ASIDE, 
	STONE_SOUP, 
	STOUT_HEARTED, 
	STRIDER, 
	STRIKE_MIGHTY_BLOW, 
	STRIKE_TO_INJURE, 
	STRIKE_TO_STUN, 
	STRONG_BACK, 
	STRONG_LEGS, 
	STRONG_MINDED(WP, 5), 
	STRONG_SWIMMER, 
	STURDY, 
	SUAVE(FEL, 5), 
	SUPER_NUMERATE, 
	SUPPORTIVE, 
	SURE_SHOT, 
	SURGERY, 
	TENACIOUS, 
	TINKER, 
	TOWER_OF_MEMORIES, 
	TRAPPER, 
	TRICK_RIDING, 
	TUNNEL_RAT, 
	UNSHAKEABLE, 
	VERY_RESILIENT(T, 5), 
	VERY_STRONG(S, 5), 
	WARLEADER, 
	WAR_WIZARD, 
	WARRIOR_BORN(WS, 5), 
	WATERMAN, 
	WEALTHY, 
	WELL_PREPARED, 
	WITCH;
	
	private Characteristics stat = NONE;
	private int bonus = 0;

	private static Map<String, TalentDto> talents = createTalentsMap();
	
	private static Map<String, TalentDto> createTalentsMap()
	{
		Map<String, TalentDto> talentsMap = new TreeMap<String, TalentDto>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<TalentDto> talentList = mapper.convertValue(mapper.readValue(new File("resources/talents.json"), List.class), new TypeReference<List<TalentDto>>() {});
			
			talentList.forEach(talent -> {
				talentsMap.put(talent.getName().toUpperCase().replaceAll(" ", "_").replaceAll("-", "_").replaceAll("/", "_"), talent);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return talentsMap;
	}
	
	private Talents() {
	}
	
	private Talents(Characteristics stat, int bonus) {
		this.stat = stat;
		this.bonus = bonus;
	}
	
	public Characteristics hasBonus() {
		return stat;
	}
	
	public int bonus() {
		return bonus;
	}
	
	public String max() {
		return talents.get(name()).getMax();
	}
	
	public String tests() {
		return talents.get(name()).getTests();
	}
	
	public String description() {
		return talents.get(name()).getDescription();
	}
}
