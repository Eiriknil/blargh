package blargh.rpg;

import static blargh.rpg.warhammer.Characteristics.AG;
import static blargh.rpg.warhammer.Characteristics.BS;
import static blargh.rpg.warhammer.Characteristics.DEX;
import static blargh.rpg.warhammer.Characteristics.FEL;
import static blargh.rpg.warhammer.Characteristics.I;
import static blargh.rpg.warhammer.Characteristics.INT;
import static blargh.rpg.warhammer.Characteristics.M;
import static blargh.rpg.warhammer.Characteristics.NONE;
import static blargh.rpg.warhammer.Characteristics.S;
import static blargh.rpg.warhammer.Characteristics.T;
import static blargh.rpg.warhammer.Characteristics.WP;
import static blargh.rpg.warhammer.Characteristics.WS;
import static blargh.rpg.warhammer.HitLocation.BODY;
import static blargh.rpg.warhammer.HitLocation.LEFT_ARM;
import static blargh.rpg.warhammer.HitLocation.RIGHT_ARM;
import static blargh.rpg.warhammer.HitLocation.RIGHT_LEG;
import static blargh.rpg.warhammer.Races.HUMAN;
import static blargh.rpg.warhammer.Skills.CHARM;
import static blargh.rpg.warhammer.Skills.CLIMB;
import static blargh.rpg.warhammer.Skills.MELEE;
import static blargh.rpg.warhammer.Talents.SMALL;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import blargh.rpg.warhammer.Character;
import blargh.rpg.warhammer.Characteristics;
import blargh.rpg.warhammer.Crit;
import blargh.rpg.warhammer.HitLocation;
import blargh.rpg.warhammer.Modifier;
import blargh.rpg.warhammer.Skills;
import blargh.rpg.warhammer.Character.Skill;
import blargh.rpg.warhammer.Character.Talent;
import blargh.rpg.warhammer.Character.TrainingResult;

class CharacterTest {

	private Character character;

	@BeforeEach
	void init() {
		Character.Factory.setRandomizer(new Random(0));
		character = Character.Factory.create();
	}
	
	@Test
	void createCharacter_randomSeed0_correctSetOfStats() {
		Character.Factory.setRandomizer(new Random(0));
		character = Character.Factory.create();
		assertThat(character.characteristic(M), is(4));
		assertThat(character.characteristic(WS), is(38));
		assertThat(character.characteristicBonus(WS), is(3));
		assertThat(character.characteristic(BS), is(30));
		assertThat(character.characteristicBonus(BS), is(3));
		assertThat(character.characteristic(S), is(24));
		assertThat(character.characteristicBonus(S), is(2));
		assertThat(character.characteristic(T), is(35));
		assertThat(character.characteristicBonus(T), is(3));
		assertThat(character.characteristic(AG), is(36));
		assertThat(character.characteristicBonus(AG), is(3));
		assertThat(character.characteristic(I), is(27));
		assertThat(character.characteristicBonus(I), is(2));
		assertThat(character.characteristic(DEX), is(31));
		assertThat(character.characteristicBonus(DEX), is(3));
		assertThat(character.characteristic(INT), is(31));
		assertThat(character.characteristicBonus(INT), is(3));
		assertThat(character.characteristic(WP), is(23));
		assertThat(character.characteristicBonus(WP), is(2));
		assertThat(character.characteristic(FEL), is(33));
		assertThat(character.characteristicBonus(FEL), is(3));
		assertThat(character.maxWounds(), is(10));
		
		character.addTalent(new Talent(SMALL));
		assertThat(character.maxWounds(), is(8));
	}
	
	@Test
	void rollSkill_randomSeed0_correctSkillResults() {
		Skill climbSkill = new Skill(CLIMB);
		character.advanceSkill(climbSkill, 10);
		assertThat(character.checkSkill(climbSkill), is(-2));
		assertThat(character.checkSkill(climbSkill), is(-3));
		assertThat(character.checkSkill(climbSkill), is(3));
		assertThat(character.checkSkill(climbSkill), is(-5));
		assertThat(character.checkSkill(climbSkill), is(-6));

		assertThat(character.checkSkill(climbSkill, Modifier.VERY_EASY), is(7));
		assertThat(character.checkSkill(climbSkill, Modifier.EASY), is(3));
		assertThat(character.checkSkill(climbSkill, Modifier.AVERAGE), is(1));
		assertThat(character.checkSkill(climbSkill, Modifier.CHALLENGING), is(0));
		assertThat(character.checkSkill(climbSkill, Modifier.HARD), is(-7));
		assertThat(character.checkSkill(climbSkill, Modifier.VERY_HARD), is(0));
	}
	
	@Test
	void trainCharacteristic_randomSeed0_correctCharacteristicForXp() {
		int xpWs = 100;
		TrainingResult trainingResultWs = character.trainCharacteristic(WS, xpWs);
		assertThat(character.characteristic(WS), is(42));
		assertThat(trainingResultWs.endValue(), is(4));
		assertThat(trainingResultWs.remainingXp(), is(0));
		int xpBs = 500;
		TrainingResult trainingResultBs = character.trainCharacteristic(BS, xpBs);
		assertThat(character.characteristic(BS), is(45));
		assertThat(trainingResultBs.endValue(), is(15));
		assertThat(trainingResultBs.remainingXp(), is(25));
		int xpS = 1500;
		TrainingResult trainingResultS = character.trainCharacteristic(S, xpS);
		assertThat(character.characteristic(S), is(53));
		assertThat(trainingResultS.endValue(), is(29));
		assertThat(trainingResultS.remainingXp(), is(65));
	}
	
	@Test
	void trainSkill_randomSeed0_correctSkillForXp() {
		int xpClimb = 100;
		Skill climbSkill = new Skill(CLIMB);
		TrainingResult trainingResultClimb = character.trainSkill(climbSkill, xpClimb);
		assertThat(character.skillValue(climbSkill), is(32));
		assertThat(trainingResultClimb.endValue(), is(8));
		assertThat(trainingResultClimb.remainingXp(), is(5));
		
		xpClimb = 100;
		TrainingResult trainingResultClimb2 = character.trainSkill(climbSkill, xpClimb);
		assertThat(character.skillValue(climbSkill), is(37));
		assertThat(trainingResultClimb2.endValue(), is(13));
		assertThat(trainingResultClimb2.remainingXp(), is(10));
		
		int xpCharm = 500;
		Skill charmSkill = new Skill(CHARM);
		TrainingResult trainingResultCharm = character.trainSkill(charmSkill, xpCharm);
		assertThat(character.skillValue(charmSkill), is(56));
		assertThat(trainingResultCharm.endValue(), is(23));
		assertThat(trainingResultCharm.remainingXp(), is(5));
		
		int xpMelee = 1500;
		Skill meleeSkill = new Skill(MELEE);
		TrainingResult trainingResultMelee = character.trainSkill(meleeSkill, xpMelee);
		assertThat(character.skillValue(meleeSkill), is(75));
		assertThat(trainingResultMelee.endValue(), is(37));
		assertThat(trainingResultMelee.remainingXp(), is(5));
		
		character.advanceSkill(charmSkill, 70);
		assertThat(character.trainSkill(charmSkill, 450).remainingXp(), is(0));
		assertThat(character.trainSkill(charmSkill, 450).remainingXp(), is(0));
		
	}
	
	@Test 
	void applyWound_randomSeed0Character_correctCurrentWound() {
		assertThat(character.currentWounds(), is(character.maxWounds()));
		character.applyWounds(3);
		assertThat(character.currentWounds(), is(character.maxWounds() - 3));
	}
	
	@Test 
	void randomHitLocation_randomSeed0_correctHitLocation() {
		HitLocation.setRandomizer(new Random(0));
		HitLocation randomHitLocation = HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(BODY));
		randomHitLocation = HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(BODY));
		randomHitLocation = HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(RIGHT_ARM));
		randomHitLocation = HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(BODY));
		randomHitLocation = HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(LEFT_ARM));
		randomHitLocation = HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(BODY));
		randomHitLocation = HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(RIGHT_LEG));
		randomHitLocation = HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(BODY));
		randomHitLocation = HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(LEFT_ARM));
		randomHitLocation = HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(BODY));
	}
	
	@Test 
	void applyCrit_randomSeed0Character_correctNumberOfCrits() {
		character.applyCrit(Crit.Factory.create(1));
		character.applyCrit(Crit.Factory.create(1));
		character.applyCrit(Crit.Factory.create(1));
	}
	
	@Test
	void createRandomCharacter_testCareer_character() {
		
		{
			Character randomCharacter = Character.RandomCharacter.create("apothecary", HUMAN, 1, new Random(0));
			System.out.println(randomCharacter.toString());
			randomCharacter.allTrainedSkills().forEach((skill, value) -> System.out.printf("Skill: %s = %d%n", skill.getSkillType().name(), value));
			assertThat(randomCharacter.characteristic(DEX), is(36));
			assertThat(randomCharacter.skillValue(new Skill(Skills.TRADE, "Apothecary")), is(36));
			assertThat(randomCharacter.skillValue(new Skill(Skills.ART, "Writing")), is(36));
			assertThat(randomCharacter.characteristic(AG), is(36));
			assertThat(randomCharacter.skillValue(new Skill(Skills.ATHLETICS, "")), is(36));
			assertThat(randomCharacter.characteristic(FEL), is(33));
			assertThat(randomCharacter.skillValue(new Skill(Skills.CHARM, "")), is(33));
		}

		{
			Character randomCharacter = Character.RandomCharacter.create("apothecary", HUMAN, 2, new Random(0));
			System.out.println(randomCharacter.toString());
			randomCharacter.allTrainedSkills().forEach((skill, value) -> System.out.printf("Skill: %s = %d%n", skill.getSkillType().name(), value));
			assertThat(randomCharacter.characteristic(DEX), is(41));
			assertThat(randomCharacter.skillValue(new Skill(Skills.TRADE, "Apothecary")), is(51));
			assertThat(randomCharacter.skillValue(new Skill(Skills.ART, "Writing")), is(41));
			assertThat(randomCharacter.characteristic(AG), is(36));
			assertThat(randomCharacter.skillValue(new Skill(Skills.ATHLETICS, "")), is(36));
			assertThat(randomCharacter.characteristic(FEL), is(43));
			assertThat(randomCharacter.skillValue(new Skill(Skills.CHARM, "")), is(43));
		}
		
		{
			Character randomCharacter = Character.RandomCharacter.create("apothecary", HUMAN, 3, new Random(0));
			System.out.println(randomCharacter.toString());
			randomCharacter.allTrainedSkills().forEach((skill, value) -> System.out.printf("Skill: %s = %d%n", skill.getSkillType().name(), value));
			assertThat(randomCharacter.characteristic(DEX), is(46));
			assertThat(randomCharacter.skillValue(new Skill(Skills.TRADE, "Apothecary")), is(61));
			assertThat(randomCharacter.skillValue(new Skill(Skills.ART, "Writing")), is(46));
			assertThat(randomCharacter.characteristic(AG), is(36));
			assertThat(randomCharacter.skillValue(new Skill(Skills.ATHLETICS, "")), is(36));
			assertThat(randomCharacter.characteristic(FEL), is(48));
			assertThat(randomCharacter.skillValue(new Skill(Skills.CHARM, "")), is(48));
		}

		{
			Character randomCharacter = Character.RandomCharacter.create("apothecary", HUMAN, 4, new Random(0));
			System.out.println(randomCharacter.toString());
			randomCharacter.allTrainedSkills().forEach((skill, value) -> System.out.printf("Skill: %s = %d%n", skill.getSkillType().name(), value));
			assertThat(randomCharacter.characteristic(DEX), is(51));
			assertThat(randomCharacter.skillValue(new Skill(Skills.TRADE, "Apothecary")), is(71));
			assertThat(randomCharacter.skillValue(new Skill(Skills.ART, "Writing")), is(51));
			assertThat(randomCharacter.characteristic(AG), is(36));
			assertThat(randomCharacter.skillValue(new Skill(Skills.ATHLETICS, "")), is(36));
			assertThat(randomCharacter.characteristic(FEL), is(53));
			assertThat(randomCharacter.skillValue(new Skill(Skills.CHARM, "")), is(73));
		}
	}
	
	@Test
	public void presentCharacter() {
		Character randomCharacter = Character.RandomCharacter.create("warriorpriest", HUMAN, 4, new Random());
		System.out.println(randomCharacter.career().name(4));
		Arrays.stream(Characteristics.values()).filter(stat -> stat != NONE).forEach(stat -> System.out.printf("%4s ", stat.name()));
		System.out.println();
		Arrays.stream(Characteristics.values()).filter(stat -> stat != NONE).forEach(stat -> System.out.printf("%4d ", randomCharacter.characteristic(stat)));
		System.out.println();
		
		System.out.println("Skills:");
		Map<Skill, Integer> allSkills = randomCharacter.allTrainedSkills();
		List<Skill> sortedSkills = new CopyOnWriteArrayList<>(allSkills.keySet());
		Collections.sort(sortedSkills);
		sortedSkills.forEach(skill -> System.out.printf("%-40s (%2d) = %2d (%s)%n", skill.presentation(), randomCharacter.skillAdvances(skill), randomCharacter.skillValue(skill), skill.getSkillType().characteristics().name()));
		
		System.out.println("Talents:");
		StringBuilder talentString = new StringBuilder("");
		randomCharacter.talents().entrySet().forEach(entry -> talentString.append(talentString.length() > 0 ? ", " : "").append(Character.capitalizeAndClean(entry.getKey().presentation(entry.getValue()))));
		System.out.println(talentString.toString());
	}
}
