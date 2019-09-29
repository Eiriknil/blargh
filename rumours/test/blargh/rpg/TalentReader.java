package blargh.rpg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import blargh.rpg.warhammer.TalentDto;

public class TalentReader {

	public static void main(String... args) {
		
		List<TalentDto> talents = new ArrayList<>();
		try {
			Files.readAllLines(Paths.get("resources", "Warhammer.csv")).forEach(line -> {
				TalentDto talentDto = new TalentDto();
				String[] split = line.split(",");
				talentDto.setName(split[0]);
				talentDto.setMax(split[1].toUpperCase());
				talentDto.setTests(split[2]);
				talentDto.setDescription(String.join(",", Arrays.copyOfRange(split, 3, split.length)).trim());
				talents.add(talentDto);
			});
			
			ObjectMapper mapper = new ObjectMapper();
			List<TalentDto> readList = mapper.readValue(new File("resources/talents.json"), List.class);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
