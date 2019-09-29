package blargh.rpg;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import blargh.rpg.warhammer.RaceDto;

public class RaceReader {

	public static void main(String... args) {
		
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
		
		System.out.println(raceMap);
	}
	
}
