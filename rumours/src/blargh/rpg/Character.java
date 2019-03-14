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
import java.util.Map.Entry;
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

	public int maxWounds();
	
	public int currentWounds();
	public List<Crit> crits();
	public void applyWounds(int wound);
	public void applyCrit(Crit crit);

	public int characteristic(Characteristics characteristics);
	public int characteristicBonus(Characteristics characteristics);
	public int charAdvances(Characteristics characteristic);
	public TrainingResult trainCharacteristic(Characteristics characteristic, int xp);
	public void advanceCharacteristic(Characteristics stat, int advance);

	public int skillValue(Skills skill);
	public int skillAdvances(Skills skill);
	public int advanceSkill(Skills skill, int value);
	public int checkSkill(Skills skill);
	public int checkSkill(Skills skill, Modifier modifier);
	public TrainingResult trainSkill(Skills skill, int xp);
	public Map<Skills, Integer> allSkills();
	public Map<Skills, Integer> allTrainedSkills();

	public void addTalent(Talents talent);
	public Set<Talents> talents();
	
	public Career career();
	public void changeCareer(Career career);

	public static class Factory {

		private static Random random = new Random();

		private Factory() {
		}
		
		public static void setRandomizer(Random random) {
			Factory.random = random;
		}
		
		public static Character create() {

			return new CharacterImpl();
		}
		
		public static Character create(Races race) {
			
			return new CharacterImpl(race);
		}
		
		public static Character create(Map<Characteristics, Characteristic> statMap) {
			return new CharacterImpl(statMap);
		}

		public static Character create(Map<Characteristics, Characteristic> statMap, Races race) {
			return new CharacterImpl(statMap, race);
		}
		
		private static class CharacterImpl implements Character {
			
			private Map<Characteristics, Characteristic> charMap = new ConcurrentHashMap<>();
			private Set<Talents> talentSet = new HashSet<>();
			private Map<Skills, Integer> skillMap = new ConcurrentHashMap<>();
			private int woundsTaken = 0;
			private List<Crit> critList = new CopyOnWriteArrayList<>();
			private Races race;
			private Career career;
			private static final int[] skillCosts = {10, 15, 20, 30, 40, 60, 80, 110, 140, 180, 220, 270, 320, 380, 450};

			public CharacterImpl(Map<Characteristics, Characteristic> charMap) {
				this(charMap, HUMAN);
			}
			
			public CharacterImpl(Map<Characteristics, Characteristic> charMap, Races race) {
				
				this.race = race;
				this.charMap = new ConcurrentHashMap<>(charMap);
			}

			public CharacterImpl() {
				this(HUMAN);
			}
			
			public CharacterImpl(Races characterRace) {
				this.race = characterRace;
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
					throw new CharacterIsDeadException();
				}
			}

			@Override
			public void addTalent(Talents talent) {
				talentSet.add(talent);
			}

			@Override
			public Career career() {
				return career;
			}

			@Override
			public void changeCareer(Career career) {
				this.career = career;
			}

			@Override
			public String toString() {
				return String.format(
						"Character [charMap=%s, talentSet=%s, skillMap=%s, woundsTaken=%s, critList=%s, race=%s, career=%s]",
						charMap, talentSet, skillMap, woundsTaken, critList, race, career);
			}

			@Override
			public Map<Skills, Integer> allSkills() {
				return new ConcurrentHashMap<>(skillMap);
			}

			@Override
			public Map<Skills, Integer> allTrainedSkills() {
				
				return skillMap.entrySet().stream()
						.filter(entry -> entry.getValue() > 0)
						.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
			}

			@Override
			public void advanceCharacteristic(Characteristics stat, int advance) {
				charMap.get(stat).advance(advance);
			}

			@Override
			public Set<Talents> talents() {
				return new HashSet<>(talentSet);
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

			@Override
			public String toString() {
				return String.format("Characteristic [type=%s, advances=%s, initialValue=%s]", type, advances,
						initialValue);
			}
		}
	}

	public static class RandomCharacter {
		
		private RandomCharacter() {
		}
		
		public static Character create(String career, Races race, int careerRank, Random randomizer) {
			
			Character.Factory.setRandomizer(randomizer);
			Character character = Factory.create(race);
			character.changeCareer(Career.Factory.create(career));
			
			return randomizeCharacter(character, careerRank, randomizer);
		}
		
		
		public static Character create(String career, Races race, int careerRank, Map<Characteristics, blargh.rpg.Character.Factory.Characteristic> stats, Random randomizer) {

			Character.Factory.setRandomizer(randomizer);
			Character character = Factory.create(stats);
			character.changeCareer(Career.Factory.create(career));
			
			return randomizeCharacter(character, careerRank, randomizer);
		}

		private static Character randomizeCharacter(Character character, int rank, Random randomizer) {
			
			Skills primarySkill =  character.career().allSkills(1).get(0);
			character.advanceSkill(primarySkill, rank*5);

			for(int i = 1;i <= rank;i++) {

				improveStats(character, i);
				improveSkills(character, randomizer, i);
				character.addTalent(character.career().talentList(i).get(randomizer.nextInt(4)));
			}
			
			return character;
		}

		private static void improveStats(Character character, int i) {
			final int maxAdvances = i*5;
			character.career().allStats(i).forEach(stat -> {
				while(character.charAdvances(stat) < maxAdvances) { 
					character.advanceCharacteristic(stat, 5);
					}
				});
		}

		private static void improveSkills(Character character, Random randomizer, int i) {
			List<Skills> allSkills = character.career().allSkills(i);
			for(int count = 0; count < 7; count++) {
				Skills randomSkill = allSkills.remove(randomizer.nextInt(allSkills.size() - 1) + 1);
				 int advances = character.skillAdvances(randomSkill);
				 character.advanceSkill(randomSkill, (i*5)-advances);
			}
		}
	}



}
