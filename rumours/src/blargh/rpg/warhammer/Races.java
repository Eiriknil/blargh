package blargh.rpg.warhammer;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum Races {
	
	HUMAN(4,20,20,20,20,20,20,20,20,20,20,0),
	DWARF(3,30,20,20,30,10,20,20,20,40,10,0),
	HALFLING(3,10,20,10,20,20,30,20,20,30,30,0),
	HIGH_ELF(5,30,30,20,20,30,40,30,30,30,20,0),
	WOOD_ELF(5,30,30,20,20,30,40,30,30,30,20,0);
	
	private Map<Characteristics, Integer> modifierMap = new ConcurrentHashMap<>();
	
	private static Map<String, RaceDto> raceMap = readRaceMap();
	private static Map<String, Integer> randomTalentMap = readRandomTalents();
	
	private static Map<String, RaceDto> readRaceMap() {
		Map<String, RaceDto> raceMap = new TreeMap<String, RaceDto>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<?> raceList = mapper.readValue(new File("resources/character_creation.json"), List.class);
			List<RaceDto> talentList = mapper.convertValue(raceList, new TypeReference<List<RaceDto>>() {});

			talentList.forEach(race -> {
				raceMap.put(race.getRace().toUpperCase().replaceAll(" ", "_"), race);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return raceMap;
	}
	
	private static Map<String, Integer> readRandomTalents() {
		Map<String, Integer> randomTalents = new TreeMap<>();
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<?, ?> talentList = mapper.readValue(new File("resources/random_talents.json"), Map.class);
			Map<String, Integer> talentMap = mapper.convertValue(talentList, new TypeReference<Map<String, Integer>>() {});
			randomTalents.putAll(talentMap);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return randomTalents;
	}
	
	public static String randomTalent(Random randomizer) {
		String talentName = "";

		Set<Integer> randomRange = new TreeSet<>(randomTalentMap.values());
		int randomNumber100 = randomizer.nextInt(100) + 1;
		AtomicInteger hit = new AtomicInteger(0);
		randomRange.forEach(number -> {
			if(number <= randomNumber100) {
				hit.set(number);
			}
		});
		
		talentName = randomTalentMap.entrySet().stream().filter(entry -> {return entry.getValue() == hit.get();}).findFirst().get().getKey();
		
		return talentName;
	}
	
	public String randomSkill(Random randomizer) {
		int randomSkill = randomizer.nextInt(racialSkills().size());
		String skillName = racialSkills().get(randomSkill);
		return skillName;
	}
	
	private Races(Integer... modifiers) {
		
		int index = 0;
		for(Characteristics stat:Characteristics.values()) {
			modifierMap.put(stat, modifiers[index++]);
		}
	}
	
	public int statModifier(Characteristics stat) {
		return modifierMap.get(stat);
	}
	
	public List<String> racialSkills() {
		return raceMap.get(name()).getSkills();
	}

	public Set<String> racialTalents(Random randomizer) {
		Set<String> talentSet = new HashSet<>(raceMap.get(name()).getTalents());
		
		raceMap.get(name()).getChoice().stream().map(choice -> choice.entrySet());
		
		return talentSet;
	}
}
