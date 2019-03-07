package blargh.rpg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class RunnerTest {

	@Test
	void test() {

		try {
			Files.lines(Paths.get("resources/weapons.json")).map(line -> line.split(" "))
			.collect(Collectors.toList())
			.forEach(line -> {
				List<String> lineList = new ArrayList<>(Arrays.asList(line));
				for (int i = 0; i < 8; i++) {
					lineList.remove(0);
				}
				StringBuilder qualitiesFlaws = new StringBuilder("");
				lineList.forEach(item -> qualitiesFlaws.append(item).append(" "));
				
				System.out.println(Weapon.Factory.create(line[0], line[1], line[2], line[3], line[4], line[5], line[6], qualitiesFlaws.toString().trim()).toJson());
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
