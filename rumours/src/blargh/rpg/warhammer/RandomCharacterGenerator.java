package blargh.rpg.warhammer;

import static blargh.rpg.warhammer.Characteristics.NONE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.yogthos.JsonPDF;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import blargh.rpg.warhammer.Character.Skill;

public class RandomCharacterGenerator {

	private static final String STAT_WIDTHS = "\"widths\": [2,1,1,1,1,1,1,1,1,1,1,1],";
	private static final String CELL_ALIGN_CENTER = "[\"cell\", {\"align\": \"center\"}, ";
	private static final String CELL_ALIGN_LEFT = "[\"cell\", {\"align\": \"left\"}, ";

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
		
		String jsonDoc = String.join("", "[{\"pages\": true, \"orientation\":\"landscape\"}", characterPresentation.replaceAll("----- Start -----", ", \\[\"paragraph\", \"")
				.replaceAll("----- End -----", "\"\\]\\]"));
//		jsonDoc = createStatPdf(characterPresentation, jsonDoc);
		
		AtomicBoolean collect = new AtomicBoolean(false);
		List<String> skillList = new ArrayList<>();
		Arrays.stream(characterPresentation.split("\\r?\\n")).forEach(line -> {
			if(line.startsWith("Talents:")) {
				collect.set(false);
			}
			if(collect.get() && !line.trim().isEmpty()) {
				skillList.add(line);
			}
			if(line.startsWith("Skills:")){
				collect.set(true);
			}
		});

		jsonDoc = jsonDoc.replace("Skills:", "\"], [\"heading\", \"Skills:\"],[\"table\", {\"widths\": [2, 1, 1, 1]},");
		jsonDoc = jsonDoc.replace("Talents:", "], [\"heading\", \"Talents:\"],[\"paragraph\", \"");
		
		List<String> skillPdfList = skillList.stream().map(line -> {
			String newLine = line;
			
			while(newLine.indexOf("  ") > 0) {
				newLine = newLine.replaceAll("  ", " ");
			}
			newLine = newLine.replace(" = ", " ");
			newLine = newLine.replace("( ", ":(");
			
			String[] split = newLine.split(":");
			String skillName = split[0].trim();
			
			newLine = String.join(",", Arrays.stream(split[1].split(" ")).map(value -> String.format("%s\"%s\"]", CELL_ALIGN_LEFT, value)).collect(Collectors.toList()));
			newLine = String.join("", "[", String.format("%s\"%s\"], ", CELL_ALIGN_CENTER, skillName),  newLine, "]");
			
			return newLine;
		}).collect(Collectors.toList());
		skillPdfList.set(skillPdfList.size() - 1, String.join("", skillPdfList.get(skillPdfList.size() - 1), "]"));

		for(int i=0;i<skillList.size();i++) {
			jsonDoc = jsonDoc.replace(skillList.get(i), i+1 < skillList.size() ? String.format("%s,", skillPdfList.get(i)) : skillPdfList.get(i));
		}
		
		System.out.println(jsonDoc);
		
		JsonPDF.writeToFile(jsonDoc, fileName, null);
	}

	private static String createStatPdf(String characterPresentation, String jsonDoc) {
		jsonDoc = jsonDoc.replace("Stats:     M   WS   BS    S    T   AG    I  DEX  INT   WP  FEL", 
				String.join("", "\"], [\"table\",{", 
						STAT_WIDTHS,
						"\"header\": [\"\", ",
						CELL_ALIGN_CENTER, "\"M\"], ",
						CELL_ALIGN_CENTER, "\"WS\"], ",
						CELL_ALIGN_CENTER, "\"BS\"], ",
						CELL_ALIGN_CENTER, "\"S\"], ",
						CELL_ALIGN_CENTER, "\"T\"], ",
						CELL_ALIGN_CENTER, "\"AG\"], ", 
						CELL_ALIGN_CENTER, "\"I\"], ",
						CELL_ALIGN_CENTER, "\"DEX\"], ",
						CELL_ALIGN_CENTER, "\"INT\"], ",
						CELL_ALIGN_CENTER, "\"WP\"], ",
						CELL_ALIGN_CENTER, "\"FEL\"]],",
						"\"width\": 50}, ["));
		List<String> statValueList = Arrays.stream(characterPresentation.split("\\r?\\n"))
				.filter(line -> line.startsWith("Values:"))
				.collect(Collectors.toList());
		for(String statLine: statValueList) {
			String line = statLine.trim();
			
			while(line.indexOf("  ") > 0) {
				line = line.replaceAll("  ", " ");
			}
			String join = "" + String.join(", ", Arrays.stream(line.split(" "))
					.map(value -> String.join("", "[\"cell\", {\"align\": \"center\", \"background-color\": [200, 200, 200]}, \"", value, "\"]"))
					.collect(Collectors.toList())) + "]], [\"paragraph\", \"";
			jsonDoc = jsonDoc.replace(statLine, join);
		}
		return jsonDoc;
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
