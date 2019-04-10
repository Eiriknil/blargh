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
		sortedSkills.forEach(skill -> output.append(String.format("%-40s (%2d) = %2d%n", skill.presentation(), randomCharacter.skillAdvances(skill), randomCharacter.skillValue(skill))));
		
		output.append("Talents:%n");
		randomCharacter.talents().forEach(talent -> output.append(String.format("%s, ", Character.capitilizeAndClean(talent.getTalentType().name()))));

		return output.substring(0, output.length() - 2).toString() + "%n";
	}
	
	public static void main(String... args) {
		Random randomizer = new Random();
		try {
			try(BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get("/temp/warhammer/chargen"))){
				bufferedWriter.write(String.format(create("scout", 1, Races.HUMAN, randomizer)));
				bufferedWriter.write("\n");
				bufferedWriter.write(String.format(create("scout", 1, Races.HUMAN, randomizer)));
				bufferedWriter.write("\n");
				bufferedWriter.write(String.format(create("scout", 2, Races.HUMAN, randomizer)));
				bufferedWriter.write("\n");
				bufferedWriter.write(String.format(create("scout", 2, Races.HUMAN, randomizer)));
				bufferedWriter.write("\n");
				bufferedWriter.write(String.format(create("scout", 3, Races.HUMAN, randomizer)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
