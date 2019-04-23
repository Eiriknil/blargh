package blargh.rpg;

import static blargh.rpg.Characteristics.NONE;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import blargh.rpg.Character.Skill;

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
		Random randomizer = new Random();
		try {
			Files.list(Paths.get("resources/careers")).forEach(path -> System.out.printf(create(path.getFileName().toString().replace(".json", ""), 4, Races.HUMAN, randomizer)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
