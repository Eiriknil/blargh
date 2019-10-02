package blargh.rpg.warhammer;

import static blargh.rpg.warhammer.Characteristics.NONE;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.yogthos.JsonPDF;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import blargh.rpg.warhammer.Character.Skill;

public class RandomCharacterGenerator {

	public enum OutputType {
		JSON,
		TEXT;
	}
		
	
	public static String create(String career, int level, Races race, Random randomizer, OutputType outputType) {
		
		StringBuilder output = new StringBuilder("----- Start -----%n");
		
		Character randomCharacter = Character.RandomCharacter.create(career, race, level, randomizer);
		output.append(String.format("%s %s rank %d%n", Character.capitalizeAndClean(race.name()), randomCharacter.career().name(level), level));
		output.append("Stats:  ");
		Arrays.stream(Characteristics.values()).filter(stat -> stat != NONE).forEach(stat -> output.append(String.format("%4s ", stat.name())));
		output.append("%n");
		output.append("Values: ");
		Arrays.stream(Characteristics.values()).filter(stat -> stat != NONE).forEach(stat -> output.append(String.format("%4d ", randomCharacter.characteristic(stat))));
		output.append("%n");
		output.append(String.format("Wounds: %d", randomCharacter.maxWounds()));
		output.append("%n");
		
		output.append("%nSkills:%n");
		Map<Skill, Integer> allSkills = randomCharacter.allTrainedSkills();
		List<Skill> sortedSkills = new CopyOnWriteArrayList<>(allSkills.keySet());
		Collections.sort(sortedSkills);
		sortedSkills.forEach(skill -> output.append(String.format("%-40s (%2d) = %2d (%s)%n", skill.presentation(), randomCharacter.skillAdvances(skill), randomCharacter.skillValue(skill), skill.getSkillType().characteristics().name())));
		
		output.append("%nTalents:%n");
		List<String> presentationList = randomCharacter
				.talents().entrySet()
				.stream().map(entry -> entry.getKey().presentation(entry.getValue()))
				.collect(Collectors.toList());
		output.append(String.join("\n", presentationList));

		output.delete(output.length() - 2, output.length()).append("%n");
		
		output.append("%nTrappings:%n");
		randomCharacter.career().trappingList(level).forEach(trappings -> output.append(trappings).append("%n"));
		
		output.append("%n----- End -----%n");
		
		ObjectMapper mapper = new ObjectMapper();
		switch (outputType) {
		case JSON: 
			try {
				CharacterDto characterDto = randomCharacter.characterDto();
				return mapper.writeValueAsString(characterDto);
			} catch (JsonProcessingException e) {
				return output.toString();
			}
		case TEXT:
			return output.toString();
		default:
			return output.toString();
		}
	}
	
	public static void writeToPdf(String fileName, String characterPresentation) {
		
		String jsonDoc = String.join("", "[{\"pages\": true, \"orientation\":\"landscape\"}", characterPresentation.replaceAll("----- Start -----", ", \\[\"paragraph\", \"").replaceAll("----- End -----", "\"\\]\\]"));
		jsonDoc = jsonDoc.replace("Stats:     M   WS   BS    S    T   AG    I  DEX  INT   WP  FEL", 
				"\"], [\"table\",{\"header\": [\"\", \"M\", \"WS\", \"BS\", \"S\", \"T\", \"AG\", \"I\", \"DEX\", \"INT\", \"WP\", \"FEL\"]}, [");
		List<String> statValueList = Arrays.stream(characterPresentation.split("\\r?\\n")).filter(line -> line.startsWith("Values:")).collect(Collectors.toList());
		for(String statLine: statValueList) {
			String line = statLine.trim();
			while(line.indexOf("  ") > 0) {
				line = line.replaceAll("  ", " ");
			}
			String join = "" + String.join(", ", Arrays.stream(line.split(" ")).map(value -> String.join("", "\"", value, "\"")).collect(Collectors.toList())) + "]], [\"paragraph\", \"";
//			System.out.println(join);
			jsonDoc = jsonDoc.replace(statLine, join);
		}
		System.out.println(jsonDoc);
		
		JsonPDF.writeToFile(jsonDoc, fileName, null);
	}
	
	public static void main(String... args) {
//		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get("/temp/soldier.txt")))
//		{
			int level = 1;
			while(level <= 1) {
				String character = String.format(create("slayer", level, Races.DWARF, new Random(), OutputType.TEXT));
				System.out.println(character);
				System.out.println();
				writeToPdf("test.pdf", character);
//				bufferedWriter.write(character);
				level++;
			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
