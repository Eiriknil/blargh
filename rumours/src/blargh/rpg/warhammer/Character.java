package blargh.rpg.warhammer;

import static blargh.rpg.warhammer.Characteristics.M;
import static blargh.rpg.warhammer.Characteristics.S;
import static blargh.rpg.warhammer.Characteristics.T;
import static blargh.rpg.warhammer.Characteristics.WP;
import static blargh.rpg.warhammer.Modifier.CHALLENGING;
import static blargh.rpg.warhammer.Races.HUMAN;
import static blargh.rpg.warhammer.Talents.SMALL;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import blargh.rpg.exception.CharacterIsDeadException;

public interface Character {

	public static class Skill implements Comparable<Skill>{
		private Skills skillType;
		private String specialisation;
		
		public Skill(String skillName) {
			String skillTypeString = skillName;
			specialisation = "";
			if(skillName.contains("(")) {
				String[] splitSkillName = skillName.split("\\(");
				skillTypeString = splitSkillName[0];
				specialisation = splitSkillName[1].replace("(", "").replace(")", "");
			}
			skillType = Skills.valueOf(skillTypeString);
		}
		
		public Skill(Skills skillType, String specialisation) {
			this.skillType = skillType;
			this.specialisation = specialisation;
		}
		
		public Skill(Skills skillType) {
			this.skillType = skillType;
			this.specialisation = "";
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(skillType, specialisation);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof Skill)) {
				return false;
			}
			Skill other = (Skill) obj;
			return skillType == other.skillType && Objects.equals(specialisation, other.specialisation);
		}

		public Skills getSkillType() {
			return skillType;
		}

		public String getSpesialisation() {
			return specialisation;
		}

		@Override
		public int compareTo(Skill o) {
			return skillType.compareTo(o.getSkillType());
		}

		public String presentation() {
			
			String presentation = capitilizeAndClean(skillType.name());
			if(!Objects.isNull(specialisation)&&!specialisation.isEmpty()) {
				presentation = String.format("%s(%s)", capitilizeAndClean(presentation), capitilizeAndClean(specialisation));
			}
			
			return presentation;
		}
	}
	
	public static class Talent implements Comparable<Talent> {

		private Talents talentType;
		private String specialisation;

		public Talent(Talents talentType) {
			this.talentType = talentType;
			specialisation = "";
		}
		
		public Talent(String talentName) {
			String talentTypeString = talentName;
			specialisation = "";
			if(talentName.contains("(")) {
				String[] splitTalentName = talentName.split("\\(");
				talentTypeString = splitTalentName[0];
				specialisation = splitTalentName[1].replace("(", "").replace(")", "");
			}
			talentType = Talents.valueOf(talentTypeString);
		}
		
		public Talent(Talents talentType, String specialisation) {
			this.talentType = talentType;
			this.specialisation = specialisation;
		}
		
		@Override
		public int compareTo(Talent o) {
			
			return talentType.compareTo(o.getTalentType());
		}

		public Talents getTalentType() {
			return talentType;
		}
		
		public String getSpecialisation() {
			return specialisation;
		}

		@Override
		public int hashCode() {
			return Objects.hash(specialisation, talentType);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof Talent)) {
				return false;
			}
			Talent other = (Talent) obj;
			return Objects.equals(specialisation, other.specialisation) && talentType == other.talentType;
		}

		public String presentation() {
			
			StringBuilder presentation = new StringBuilder(capitilizeAndClean(talentType.name()));
			
			if(!specialisation.isEmpty()) {
				presentation.append(String.format("(%s)", capitilizeAndClean(specialisation)));
			}
			
			return presentation.toString();
		}
	}

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

	public int skillValue(Skill skill);
	public int skillAdvances(Skill skill);
	public int advanceSkill(Skill skill, int value);
	public int checkSkill(Skill skill);
	public int checkSkill(Skill skill, Modifier modifier);
	public TrainingResult trainSkill(Skill skill, int xp);
	public Map<Skill, Integer> allSkills();
	public Map<Skill, Integer> allTrainedSkills();

	public void addTalent(Talent talent);
	public Set<Talent> talents();
	
	public Career career();
	public void changeCareer(Career career);

	public static String capitilizeAndClean(String input) {
		return String.format("%S%s", input.substring(0, 1).toUpperCase(), input.substring(1).toLowerCase()).replace("_", " ");
	}
	
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
			private Set<Talent> talentSet = new HashSet<>();
			private Map<Skill, Integer> skillMap = new ConcurrentHashMap<>();
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
				Skills.basicSkills().forEach(skill -> skillMap.put(new Skill(skill, skill.defaultSpec()), 0));
			}

			@Override
			public int skillAdvances(Skill skill) {

				return skillMap.getOrDefault(skill, 0);
			}

			@Override
			public int maxWounds() {

				if(talentSet.contains(new Talent(SMALL))) {
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
			public int checkSkill(Skill skill) {
				return checkSkill(skill, CHALLENGING);
			}

			@Override
			public int checkSkill(Skill skill, Modifier modifier) {
				int roll = random.nextInt(100) + 1;
				int successLevel = ((skillValue(skill) + modifier.value())/10) - (roll/10);
				
				System.out.println("Skill: " + skillValue(skill) + " Modifier: " + modifier.modifierPresentation() + " = " + (skillValue(skill) + modifier.value()) 
						+ " Roll: " + roll + " => SL = " + successLevel );
				
				return successLevel;
			}
			
			@Override
			public int skillValue(Skill skill) {
				return skillAdvances(skill) + characteristic(skill.getSkillType().characteristics());
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
				
				System.out.println(skill.getSkillType().name() + " advanced " + boughtAdvances + " to " + skillMap.get(skill) + " using " + usedXp + " of " + xp + " xp");
				
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
			public void addTalent(Talent talent) {
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
			public Map<Skill, Integer> allSkills() {
				return new ConcurrentHashMap<>(skillMap);
			}

			@Override
			public Map<Skill, Integer> allTrainedSkills() {
				
				return skillMap.entrySet().stream()
						.filter(entry -> entry.getValue() > 0)
						.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
			}

			@Override
			public void advanceCharacteristic(Characteristics stat, int advance) {
				charMap.get(stat).advance(advance);
			}

			@Override
			public Set<Talent> talents() {
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
				return String.format("Characteristic [type=%s, advances=%s, initialValue=%s]", type, advances, initialValue);
			}
		}
	}

	public static class RandomCharacter {
		
		private RandomCharacter() {
		}
		
		public static Character create(String careerName, Races race, int careerRank, Random randomizer) {
			
			Character.Factory.setRandomizer(randomizer);
			Character character = Factory.create(race);
			Career career = Career.Factory.create(careerName);
			character.changeCareer(career);
			
			return randomizeCharacter(character, careerRank, randomizer);
		}
		
		
		public static Character create(String career, Races race, int careerRank, Map<Characteristics, blargh.rpg.warhammer.Character.Factory.Characteristic> stats, Random randomizer) {

			Character.Factory.setRandomizer(randomizer);
			Character character = Factory.create(stats);
			character.changeCareer(Career.Factory.create(career));
			
			return randomizeCharacter(character, careerRank, randomizer);
		}

		private static Character randomizeCharacter(Character character, int rank, Random randomizer) {
			
			Skill primarySkill =  character.career().allSkills(1).get(0);
			character.advanceSkill(primarySkill, rank*5);

			for(int i = 1;i <= rank;i++) {

				improveStats(character, i);
				improveSkills(character, randomizer, i, i == rank);
				character.addTalent(character.career().talentList(i).get(randomizer.nextInt(4)));
			}
			
			return character;
		}

		private static void improveStats(Character character, int rank) {
			final int maxAdvances = rank*5;
			int lastRank = rank - 1;
			if(lastRank == 0) {
				lastRank = 1;
			}
			character.career().allStats(lastRank).forEach(stat -> {
				while(character.charAdvances(stat) < maxAdvances) { 
					character.advanceCharacteristic(stat, 5);
					}
				});
		}

		private static void improveSkills(Character character, Random randomizer, int rank, boolean halfRank) {
			List<Skill> allSkills = character.career().allSkills(rank);
			int max = 7;
			if(halfRank) {
				max = randomizer.nextInt(7);
			}
			for(int count = 0; count < max; count++) {
				int random = randomizer.nextInt(allSkills.size() - 1) + 1;
				Skill randomSkill = allSkills.remove(random);
				int advances = character.skillAdvances(randomSkill);
				character.advanceSkill(randomSkill, (rank*5)-advances);
			}
		}
	}



}
