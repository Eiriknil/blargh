package blargh.rpg;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import blargh.rpg.Character.Characteristics;
import blargh.rpg.Character.HitLocation;
import blargh.rpg.Character.Modifier;
import blargh.rpg.Character.Skill;
import blargh.rpg.Character.TrainingResult;

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
		assertThat(character.characteristic(Characteristics.M), is(4));
		assertThat(character.characteristic(Characteristics.WS), is(38));
		assertThat(character.characteristicBonus(Characteristics.WS), is(3));
		assertThat(character.characteristic(Characteristics.BS), is(30));
		assertThat(character.characteristicBonus(Characteristics.BS), is(3));
		assertThat(character.characteristic(Characteristics.S), is(24));
		assertThat(character.characteristicBonus(Characteristics.S), is(2));
		assertThat(character.characteristic(Characteristics.T), is(35));
		assertThat(character.characteristicBonus(Characteristics.T), is(3));
		assertThat(character.characteristic(Characteristics.AG), is(36));
		assertThat(character.characteristicBonus(Characteristics.AG), is(3));
		assertThat(character.characteristic(Characteristics.I), is(27));
		assertThat(character.characteristicBonus(Characteristics.I), is(2));
		assertThat(character.characteristic(Characteristics.DEX), is(31));
		assertThat(character.characteristicBonus(Characteristics.DEX), is(3));
		assertThat(character.characteristic(Characteristics.INT), is(31));
		assertThat(character.characteristicBonus(Characteristics.INT), is(3));
		assertThat(character.characteristic(Characteristics.WP), is(23));
		assertThat(character.characteristicBonus(Characteristics.WP), is(2));
		assertThat(character.characteristic(Characteristics.FEL), is(33));
		assertThat(character.characteristicBonus(Characteristics.FEL), is(3));
		assertThat(character.maxWounds(), is(10));
	}
	
	@Test
	void rollSkill_randomSeed0_correctSkillResults() {
		character.advanceSkill(Skill.CLIMB, 10);
		assertThat(character.checkSkill(Skill.CLIMB), is(-2));
		assertThat(character.checkSkill(Skill.CLIMB), is(-3));
		assertThat(character.checkSkill(Skill.CLIMB), is(3));
		assertThat(character.checkSkill(Skill.CLIMB), is(-5));
		assertThat(character.checkSkill(Skill.CLIMB), is(-6));

		assertThat(character.checkSkill(Skill.CLIMB, Modifier.VERY_EASY), is(7));
		assertThat(character.checkSkill(Skill.CLIMB, Modifier.EASY), is(3));
		assertThat(character.checkSkill(Skill.CLIMB, Modifier.AVERAGE), is(1));
		assertThat(character.checkSkill(Skill.CLIMB, Modifier.CHALLENGING), is(0));
		assertThat(character.checkSkill(Skill.CLIMB, Modifier.HARD), is(-7));
		assertThat(character.checkSkill(Skill.CLIMB, Modifier.VERY_HARD), is(0));
	}
	
	@Test
	void trainCharacteristic_randomSeed0_correctCharacteristicForXp() {
		int xpWs = 100;
		TrainingResult trainingResultWs = character.trainCharacteristic(Characteristics.WS, xpWs);
		assertThat(character.characteristic(Characteristics.WS), is(42));
		assertThat(trainingResultWs.endValue(), is(4));
		assertThat(trainingResultWs.remainingXp(), is(0));
		int xpBs = 500;
		TrainingResult trainingResultBs = character.trainCharacteristic(Characteristics.BS, xpBs);
		assertThat(character.characteristic(Characteristics.BS), is(45));
		assertThat(trainingResultBs.endValue(), is(15));
		assertThat(trainingResultBs.remainingXp(), is(25));
		int xpS = 1500;
		TrainingResult trainingResultS = character.trainCharacteristic(Characteristics.S, xpS);
		assertThat(character.characteristic(Characteristics.S), is(53));
		assertThat(trainingResultS.endValue(), is(29));
		assertThat(trainingResultS.remainingXp(), is(65));
	}
	
	@Test
	void trainSkill_randomSeed0_correctSkillForXp() {
		int xpClimb = 100;
		TrainingResult trainingResultClimb = character.trainSkill(Skill.CLIMB, xpClimb);
		assertThat(character.skillValue(Skill.CLIMB), is(32));
		assertThat(trainingResultClimb.endValue(), is(8));
		assertThat(trainingResultClimb.remainingXp(), is(5));
		
		xpClimb = 100;
		TrainingResult trainingResultClimb2 = character.trainSkill(Skill.CLIMB, xpClimb);
		assertThat(character.skillValue(Skill.CLIMB), is(37));
		assertThat(trainingResultClimb2.endValue(), is(13));
		assertThat(trainingResultClimb2.remainingXp(), is(10));
		
		int xpCharm = 500;
		TrainingResult trainingResultCharm = character.trainSkill(Skill.CHARM, xpCharm);
		assertThat(character.skillValue(Skill.CHARM), is(56));
		assertThat(trainingResultCharm.endValue(), is(23));
		assertThat(trainingResultCharm.remainingXp(), is(5));
		
		int xpMelee = 1500;
		TrainingResult trainingResultMelee = character.trainSkill(Skill.MELEE, xpMelee);
		assertThat(character.skillValue(Skill.MELEE), is(75));
		assertThat(trainingResultMelee.endValue(), is(37));
		assertThat(trainingResultMelee.remainingXp(), is(5));
		
	}
	
	@Test 
	void applyWound_randomSeed0Character_correctCurrentWound() {
		assertThat(character.currentWounds(), is(character.maxWounds()));
		character.applyWounds(3);
		assertThat(character.currentWounds(), is(character.maxWounds() - 3));
	}
	
	@Test 
	void randomHitLocation_randomSeed0_correctHitLocation() {
		Character.HitLocation.setRandomizer(new Random(0));
		HitLocation randomHitLocation = Character.HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(HitLocation.BODY));
		randomHitLocation = Character.HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(HitLocation.BODY));
		randomHitLocation = Character.HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(HitLocation.RIGHT_ARM));
		randomHitLocation = Character.HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(HitLocation.BODY));
		randomHitLocation = Character.HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(HitLocation.LEFT_ARM));
		randomHitLocation = Character.HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(HitLocation.BODY));
		randomHitLocation = Character.HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(HitLocation.RIGHT_LEG));
		randomHitLocation = Character.HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(HitLocation.BODY));
		randomHitLocation = Character.HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(HitLocation.LEFT_ARM));
		randomHitLocation = Character.HitLocation.randomHitLocation();
		assertThat(randomHitLocation, is(HitLocation.BODY));
	}
	
	@Test 
	void applyCrit_randomSeed0Character_correctNumberOfCrits() {
		character.applyCrit(Crit.Factory.create(1));
		character.applyCrit(Crit.Factory.create(1));
		character.applyCrit(Crit.Factory.create(1));
	}
}
