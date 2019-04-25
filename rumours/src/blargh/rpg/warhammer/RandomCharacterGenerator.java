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

import blargh.rpg.warhammer.Character.Skill;

public class RandomCharacterGenerator {

	public static String create(String career, int level, Races race, Random randomizer) {
		
		StringBuilder output = new StringBuilder();
		
		Character randomCharacter = Character.RandomCharacter.create(career, race, level, randomizer);
		output.append(String.format("%s %s rank %d%n", Character.capitilizeAndClean(race.name()), randomCharacter.career().name(), level));
		Arrays.stream(Characteristics.values()).filter(stat -> stat != NONE).forEach(stat -> output.append(String.format("%4s ", stat.name())));
		output.append("%n");
		Arrays.stream(Characteristics.values()).filter(stat -> stat != NONE).forEach(stat -> output.append(String.format("%4d ", randomCharacter.characteristic(stat))));
		output.append("%n");
		
		output.append("Skills:%n");
		Map<Skill, Integer> allSkills = randomCharacter.allTrainedSkills();
		List<Skill> sortedSkills = new CopyOnWriteArrayList<>(allSkills.keySet());
		Collections.sort(sortedSkills);
		sortedSkills.forEach(skill -> output.append(String.format("%-40s (%2d) = %2d (%s)%n", skill.presentation(), randomCharacter.skillAdvances(skill), randomCharacter.skillValue(skill), skill.getSkillType().characteristics().name())));
		
		output.append("Talents:%n");
		randomCharacter.talents().forEach(talent -> output.append(String.format("%s, ", Character.capitilizeAndClean(talent.presentation()))));

		return output.substring(0, output.length() - 2).toString() + "%n";
	}
	
	public static void main(String... args) {
//		Random randomizer = new Random();
//		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get("/temp/soldier.txt")))
//		{
//			bufferedWriter.write(String.format(create("soldier", 3, HUMAN, randomizer)));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		try {
			List<String> tempList = new ArrayList<>();
			Files.lines(Paths.get("resources/trappings.txt")).forEach(line -> {
//				System.out.println(line);
				tempList.add(line);
				if(tempList.size() == 8) {
					String careerName = tempList.get(2).split(" - ")[0];
					if("Giant Slayer".contentEquals(careerName)) {
						careerName = "Slayer";
					}
					StringBuilder output = new StringBuilder("{%n")
							.append("  career: \"").append(careerName).append("\",%n")
							.append("  level: [%n");
					for(int i=0;i<4;i++) {
						if(i > 0) {
							output.append(",%n");
						}
						String title = tempList.get(i*2);
						String trappings = tempList.get(i*2+1).replace("Trappings: ", "");
						String status = title.split(" - ")[1];
						title = title.split(" - ")[0];
						output.append(String.format("    {%n      name: \"%s\",%n      status: \"%s\",%n      trappings: \"%s\"%n    }", title, status, trappings));
					}
					output.append("%n  ]%n")
					.append("}%n");
					tempList.clear();
					String fileName = careerName.replace(" ", "").toLowerCase() + "_trapping.json";
					try(BufferedWriter bw = Files.newBufferedWriter(Paths.get("resources/careers/" + fileName))) {
						bw.write(String.format(output.toString()));
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.printf(output.toString());
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
