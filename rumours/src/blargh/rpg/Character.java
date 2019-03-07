package blargh.rpg;

import static blargh.rpg.Characteristics.M;
import static blargh.rpg.Characteristics.S;
import static blargh.rpg.Characteristics.T;
import static blargh.rpg.Characteristics.WP;
import static blargh.rpg.Modifier.CHALLENGING;
import static blargh.rpg.Races.HUMAN;
import static blargh.rpg.Talents.SMALL;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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

	public int maxWounds();
	
	public int currentWounds();
	public List<Crit> crits();
	public void applyWounds(int wound);
	public void applyCrit(Crit crit);

	public int characteristic(Characteristics characteristics);
	public int characteristicBonus(Characteristics characteristics);
	public int charAdvances(Characteristics characteristic);
	public TrainingResult trainCharacteristic(Characteristics characteristic, int xp);

	public int skillValue(Skills skill);
	public int skillAdvances(Skills skill);
	public int advanceSkill(Skills skill, int value);
	public int checkSkill(Skills skill);
	public int checkSkill(Skills skill, Modifier modifier);
	public TrainingResult trainSkill(Skills skill, int xp);

	public void addTalent(Talents talent);
	
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
			private Set<Talents> talentSet = new HashSet<>();
			private Map<Skills, Integer> skillMap = new ConcurrentHashMap<>();
			private int woundsTaken = 0;
			private List<Crit> critList = new CopyOnWriteArrayList<>();
			private Races race;
			private static final int[] skillCosts = {10, 15, 20, 30, 40, 60, 80, 110, 140, 180, 220, 270, 320, 380, 450};

			public CharacterImpl(Map<Characteristics, Characteristic> charMap) {
				this.charMap = new ConcurrentHashMap<>(charMap);
			}

			public CharacterImpl() {
				race = HUMAN;
				Arrays.stream(Characteristics.values()).forEach(stat -> charMap.put(stat, new Characteristic(stat, race.statModifier(stat) + 2 + random.nextInt(10) + random.nextInt(10))));
				charMap.put(M, new Characteristic(M, race.statModifier(M)));
				Skills.basicSkills().forEach(skill -> skillMap.put(skill, 0));
			}

			@Override
			public int skillAdvances(Skills skill) {

				return skillMap.getOrDefault(skill, 0);
			}

			@Override
			public int maxWounds() {

				if(talentSet.contains(SMALL)) {
					return characteristicBonus(T)*2 + characteristicBonus(WP);
				}
				return characteristicBonus(T)*2 + characteristicBonus(WP) + characteristicBonus(S);
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
			public int checkSkill(Skills skill) {
				return checkSkill(skill, CHALLENGING);
			}

			@Override
			public int checkSkill(Skills skill, Modifier modifier) {
				int roll = random.nextInt(100) + 1;
				int successLevel = (int)((skillValue(skill) + modifier.value())/10) - (int)(roll/10);
				
				System.out.println("Skill: " + skillValue(skill) + " Modifier: " + modifier.modifierPresentation() + " = " + (skillValue(skill) + modifier.value()) 
						+ " Roll: " + roll + " => SL = " + successLevel );
				
				return successLevel;
			}
			
			@Override
			public int skillValue(Skills skill) {
				return skillAdvances(skill) + characteristic(skill.characteristics());
			}

			@Override
			public int advanceSkill(Skills skill, int value) {
				
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
			public TrainingResult trainSkill(Skills skill, int xp) {

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
				if(critList.size() > characteristicBonus(T)) {
					throw new RuntimeException("Character is dead!");
				}
			}

			@Override
			public void addTalent(Talents talent) {
				talentSet.add(talent);
			}

		}
		
		private static class Characteristic {
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

			public int getInitialValue() {
				return initialValue;
			}
		}
	}

	public static HitLocation randomHitLocation() {
		
		return null;
	}
	
	public static class RandomCharacter {
		
		public static Character create(String career, Races race, int careerRank) {
			Character character = Factory.create();
			
			return randomizeCharacter(character);
		}
		
		
		public static Character create(String career, Races race, int careerRank, Map<Characteristics, blargh.rpg.Character.Factory.Characteristic> stats) {
			Character character = Factory.create(stats);
			
			return randomizeCharacter(character);
		}

		private static Character randomizeCharacter(Character character) {
			return character;
		}
	}
}
