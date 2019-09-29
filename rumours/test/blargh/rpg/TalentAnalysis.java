package blargh.rpg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import blargh.rpg.warhammer.CareerDto;

public class TalentAnalysis {

	public static void main(String... args) {

		
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<File> fileList = Files.list(Paths.get("resources/careers"))
			.filter(path -> !path.toString().contains("trapping"))
			.map(path -> path.toFile())
			.collect(Collectors.toList());
			
			List<CareerDto> careerList = new ArrayList<>();
			
			fileList.forEach(file -> {
					try {
						careerList.add(mapper.readValue(file, CareerDto.class));
					} catch (IOException e) {
						e.printStackTrace();
					}
			});
			
			Map <String, Map<String, Integer>> analyze = new TreeMap<>();
			
			careerList.forEach(career -> {
				for(int i = 1; i < 5; i++) {
					final int level = i;
					career.getLevel().get(level - 1).get("talents").forEach(talent -> {
						Map<String, Integer> talentMap = analyze.getOrDefault(talent, new HashMap<>());
						talentMap.put(career.getCareer(), level);
						analyze.put(talent, talentMap);
					});
				}
			});
			
			analyze.forEach((talent, talentMap) -> {
				System.out.println(talent);
				System.out.println(String.join(", ", talentMap.entrySet().stream().map(entry -> String.format("%s: %d", entry.getKey(), entry.getValue())).collect(Collectors.toList())));
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


